package org.micro.app.order;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.micro.app.order.verticle.OrderVerticle;

import java.io.IOException;


@Slf4j
public class OrderApp {


  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      log.error("Invalid number of arguments: <CONFIG>");
      printHelp();
      System.exit(1);
    } else {

      Vertx vertx = Vertx.vertx();
      vertx.deployVerticle(new OrderVerticle(args[0]));
    }

//    Vertx vertx = Vertx.vertx();
//      vertx.deployVerticle(new OrderVerticle("order-app-1.json"));

  }

  private static void printHelp() {
    log.info("Usage: java app.jar <CONFIG> ");
  }

}
