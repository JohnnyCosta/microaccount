package org.micro.app.account;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.micro.app.account.verticle.AccountVerticle;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;


@Slf4j
public class AccountApp {


  public static void main(String[] args) throws IOException {
    log.debug("Arguments: {}", Arrays.toString(args));

    String configPath;
//    if (args.length != 1) {
//      log.error("Invalid number of arguments: <CONFIG>");
//      printHelp();
//      System.exit(1);
//    } else {
//      configPath = args[0];
//    }
    configPath = "account-app-1.json";

    Vertx vertx = Vertx.vertx();

    ConfigStoreOptions file = new ConfigStoreOptions()
      .setType("file")
      .setConfig(new JsonObject().put("path", configPath));
    ConfigRetriever retriever = ConfigRetriever.create(vertx, new ConfigRetrieverOptions().addStore(file));

    retriever.getConfig(conf -> {
      final JsonObject configuration = conf.result();
      if (Objects.isNull(configuration)) {
        log.error("Invalid argument: <CONFIG>");
        printHelp();
        vertx.close();
        System.exit(1);
      } else {
        DeploymentOptions options = new DeploymentOptions().setConfig(configuration);
        vertx.deployVerticle(new AccountVerticle(),options);
      }
    });
  }

  private static void printHelp() {
    log.info("Usage: java app.jar <CONFIG> ");
  }

}
