package org.micro.app.account;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.micro.app.account.verticle.AccountVerticle;

import java.io.IOException;
import java.util.Arrays;


@Slf4j
public class AccountApp {


  public static void main(String[] args) throws IOException {
    log.debug("Arguments: {}", Arrays.toString(args));
//    if (args.length != 1) {
//      log.error("Invalid number of arguments: <CONFIG>");
//      printHelp();
//      System.exit(1);
//    } else {
//
//      Vertx vertx = Vertx.vertx();
//      vertx.deployVerticle(new AccountVerticle(args[0]));
//    }

    Vertx vertx = Vertx.vertx();
      vertx.deployVerticle(new AccountVerticle("account-app-1.json"));

  }

  private static void printHelp() {
    log.info("Usage: java app.jar <CONFIG> ");
  }

}
