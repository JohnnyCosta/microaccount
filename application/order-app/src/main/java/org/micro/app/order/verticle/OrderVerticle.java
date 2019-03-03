package org.micro.app.order.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.consul.ConsulServiceImporter;
import lombok.extern.slf4j.Slf4j;
import org.micro.config.AppConfig;
import org.micro.controller.OrderController;

@Slf4j
public class OrderVerticle extends AbstractVerticle {

  private final OrderController orderController = new AppConfig().getOrderController();
  private final String configPath;

  public OrderVerticle(String configPath) {
    this.configPath = configPath;
  }


  @Override
  public void start() throws Exception {

    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);

    var server = vertx.createHttpServer();

    var router = Router.router(vertx);
    router
      .route()
      .handler(BodyHandler.create());
    router
      .post("/order")
      .handler(event -> {
        orderController.createOrder(event, discovery);
      })
      .produces("application/json");
    router
      .get("/order/:id")
      .handler(orderController::findOrder)
      .produces("application/json");
    router
      .get("/orders")
      .handler(orderController::findAllOrders)
      .produces("application/json");

    ConfigStoreOptions file = new ConfigStoreOptions()
      .setType("file")
      .setConfig(new JsonObject().put("path", configPath));
    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(file));

    retriever.getConfig(conf -> {
      var confResult = conf
        .result();

      var discoveryConfig = confResult
        .getJsonObject("discovery");

      discovery.registerServiceImporter(new ConsulServiceImporter(), new JsonObject()
        .put("host", discoveryConfig.getString("host"))
        .put("port", discoveryConfig.getInteger("port"))
        .put("scan-period", 2000));

      server
        .requestHandler(router)
        .listen(confResult
          .getInteger("port"));
      JsonObject json = new JsonObject()
        .put("ID", confResult
          .getString("name"))
        .put("Name", "order-service")
        .put("Address", confResult
          .getString("host"))
        .put("Port", confResult
          .getInteger("port"))
        .put("Tags", new JsonArray().add("http-endpoint"));
      WebClient client = WebClient.create(vertx);


      client
        .put(discoveryConfig.getInteger("port"), discoveryConfig.getString("host"), "/v1/agent/service/register")
        .sendJsonObject(json, res -> {
          log.info("Consul registration status: {}", res
            .result()
            .statusCode());
        });
    });

    log.info("Finished to start account service");

  }

}
