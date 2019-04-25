package org.micro.app.account.verticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;
import org.micro.config.AppConfig;
import org.micro.controller.AccountController;

import java.util.Objects;

@Slf4j
public class AccountVerticle extends AbstractVerticle {

  private final AccountController accountController = new AppConfig().getAccountController();


  @Override
  public void start(Future<Void> future) {
    startServer(future, vertx.createHttpServer(), configureRouter());
  }

  private Router configureRouter() {
    var router = Router.router(vertx);

    HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);

    router
      .get("/health")
      .handler(healthCheckHandler);

    healthCheckHandler.register("basic-check", f -> {
      f.complete(Status.OK());
    });

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
    return router;
  }

  private void startServer(Future<Void> future, HttpServer server, Router router) {
    JsonObject configuration = config();
    server
      .requestHandler(router)
      .listen(configuration.getInteger("port"), res -> {
        if (res.succeeded()) {
          register(future);
        } else {
          failedEvent(future, "Error to bind port");
        }
      });
  }

  private void register(Future<Void> future) {
    JsonObject configuration = config();
    JsonObject healthConfig = configuration
      .getJsonObject("health");

    JsonObject json = new JsonObject()
      .put("ID", configuration.getString("name"))
      .put("Name", "account-service")
      .put("Address", configuration.getString("host"))
      .put("Port", configuration.getInteger("port"))
      .put("Tags", new JsonArray().add("http-endpoint"))
      .put("Check",
        new JsonObject()
          .put("DeregisterCriticalServiceAfter", healthConfig.getString("DeregisterCriticalServiceAfter"))
          .put("Method", "GET")
          .put("HTTP", healthConfig.getString("url"))
          .put("Interval", healthConfig.getString("Interval"))
      );

    JsonObject discoveryConfig = configuration
      .getJsonObject("discovery");
    RegisterConsul(discoveryConfig.getString("host"), discoveryConfig.getInteger("port"), future, json);
  }

  private void RegisterConsul(String host, Integer port, Future<Void> future, JsonObject json) {
    WebClient client = WebClient.create(vertx);
    client
      .put(port, host, "/v1/agent/service/register")
      .sendJsonObject(json, asyncResult -> {
        handleConsulResponse(future, asyncResult);
      });
  }

  private void handleConsulResponse(Future<Void> future, AsyncResult<HttpResponse<Buffer>> asyncResult) {
    if (asyncResult.succeeded()){
      final HttpResponse<Buffer> result = asyncResult.result();
      log.info("Consul registration status: {}", result
        .statusCode());
      if (200 == result
        .statusCode()) {
        succeededEvent(future);
      } else {
        failedEvent(future, "Error to register, return code not expected");
      }
    } else {
      failedEvent(future, "Error to register: " + asyncResult.cause());
    }
  }

  private void succeededEvent(Future<Void> future) {
    log.info("Finished to start account service");
    future.complete();
  }

  private void failedEvent(Future<Void> future, String error) {
    log.error("Error to start account service: {}", error);
    future.fail(error);
    vertx.close();
  }

}
