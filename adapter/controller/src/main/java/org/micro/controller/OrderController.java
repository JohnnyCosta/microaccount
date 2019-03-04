package org.micro.controller;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.micro.client.AccountClient;
import org.micro.client.BtcPriceClient;
import org.micro.domain.entity.Account;
import org.micro.domain.entity.Order;
import org.micro.scenario.CreateOrder;
import org.micro.scenario.FindOrder;
import org.micro.scenario.UpdateOrder;
import org.micro.utils.JsonCollectors;
import org.micro.vm.CreateOrderVM;

import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
public class OrderController {

  private final CreateOrder createOrder;
  private final FindOrder findOrder;
  private final UpdateOrder updateOrder;

  public OrderController(CreateOrder createOrder, FindOrder findOrder, UpdateOrder updateOrder) {
    this.createOrder = createOrder;
    this.findOrder = findOrder;
    this.updateOrder = updateOrder;
  }

  private Handler<Long> createPeriodicAction(Order order, BtcPriceClient btcPriceClient, Vertx vertx) {
    return id -> {

      log.info("Starting periodic action");

      btcPriceClient.getPrice(handler -> {
        if (handler.succeeded()) {
          var btcPrice = handler.result();
          if (btcPrice.getPrice() > order.getPriceLimit()) {
            log.info("Handler detected that passed price limit, stop periodic action");
            Optional<Order> finish = updateOrder.finish(order.getOrderId());
            if (!finish.isPresent() || !finish.get().getFinished())
              log.error("Error to finish order: {}", order.getOrderId());
            vertx.cancelTimer(id);
          }
        } else {
          log.error("Handler failed, aborting periodic action", handler.cause());
          vertx.cancelTimer(id);
        }
      });
    };
  }

  public void createOrder(final RoutingContext routingContext, AccountClient accountClient,
                          BtcPriceClient btcPriceClient, Vertx vertx
  ) {
    log.info("Request to create order");
    var response = routingContext.response();
    var body = routingContext.getBody();
    if (isNull(body)) {
      sendError(400, response);
    } else {
      var createOrderVM = body
        .toJsonObject()
        .mapTo(CreateOrderVM.class);
      accountClient.findAccount(createOrderVM.getAccount_id(), resultHandler -> {
        if (resultHandler.succeeded()) {
          Account account = resultHandler.result();
          if (createOrderVM
            .getAccount_id()
            .equals(account.getId())) {
            var order = createOrder.createLimitOrder(createOrderVM.getAccount_id(), createOrderVM.getPrice_limit());
            var result = JsonObject.mapFrom(order);

            vertx.setPeriodic(1000, createPeriodicAction(order, btcPriceClient, vertx));

            sendSuccess(result, response);
          } else {
            log.error("Account id '{}' do not match response", createOrderVM.getAccount_id());
            sendError(400, response);
          }
        } else {
          sendError(400, response);
        }
      });
    }
  }

  public void findOrder(final RoutingContext routingContext) {
    log.info("Request to find order");
    var response = routingContext.response();
    var id = routingContext
      .request()
      .getParam("id");
    if (isNull(id)) {
      sendError(400, response);
    } else {
      var order = findOrder.fetchOrderDetails(id);
      if (order.isPresent()) {
        var result = JsonObject.mapFrom(order.get());
        sendSuccess(result, response);
      } else {
        sendError(404, response);
      }
    }
  }

  public void findAllOrders(final RoutingContext routingContext) {
    log.info("Request to find all orders");
    var response = routingContext.response();
    var orders = findOrder.findAllOrders();
    var result = orders
      .stream()
      .map(order -> JsonObject.mapFrom(order))
      .collect(JsonCollectors.toJsonArray());
    response
      .putHeader("content-type", "application/json")
      .end(result.encodePrettily());
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response
      .putHeader("content-type", "application/json")
      .setStatusCode(statusCode)
      .end();
  }

  private void sendSuccess(JsonObject body, HttpServerResponse response) {
    response
      .putHeader("content-type", "application/json")
      .end(body.encodePrettily());
  }
}
