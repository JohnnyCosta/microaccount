package org.micro.app.account.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;
import org.micro.config.AppConfig;
import org.micro.controller.AccountController;

@Slf4j
public class AccountVerticle extends AbstractVerticle {

  private final AccountController accountController = new AppConfig().getAccountController();
  private final String configPath;

  public AccountVerticle(String configPath) {
    this.configPath = configPath;
  }


  @Override
  public void start() throws Exception {

    var server = vertx.createHttpServer();

    var router = Router.router(vertx);
    router
      .route()
      .handler(BodyHandler.create());
    router
      .post("/account")
      .handler(accountController::createAccount)
      .produces("application/json");
    router
      .get("/account/:id")
      .handler(accountController::findAccount)
      .produces("application/json");
    router
      .get("/accounts")
      .handler(accountController::findAllAccounts)
      .produces("application/json");
    router
      .post("/account/update")
      .handler(accountController::updateAccount)
      .produces("application/json");

    ConfigStoreOptions file = new ConfigStoreOptions()
      .setType("file")
      .setConfig(new JsonObject().put("path", configPath));
    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(file));

    retriever.getConfig(conf -> {
      server
        .requestHandler(router)
        .listen(conf
          .result()
          .getInteger("port"));
      JsonObject json = new JsonObject()
        .put("ID", conf
          .result()
          .getString("name"))
        .put("Name", "account-service")
        .put("Address", conf
          .result()
          .getString("host"))
        .put("Port", conf
          .result()
          .getInteger("port"))
        .put("Tags", new JsonArray().add("http-endpoint"));

      WebClient client = WebClient.create(vertx);
      JsonObject discoveryConfig = conf
        .result()
        .getJsonObject("discovery");
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
