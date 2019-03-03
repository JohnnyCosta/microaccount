package org.micro.controller;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.micro.client.AccountClient;
import org.micro.domain.entity.Account;
import org.micro.scenario.CreateOrder;
import org.micro.scenario.FindOrder;
import org.micro.utils.JsonCollectors;
import org.micro.vm.CreateOrderVM;

import static java.util.Objects.isNull;

@Slf4j
public class OrderController {

  private final CreateOrder createOrder;
  private final FindOrder findOrder;

  public OrderController(CreateOrder createOrder, FindOrder findOrder) {
    this.createOrder = createOrder;
    this.findOrder = findOrder;
  }

  public void createOrder(final RoutingContext routingContext, ServiceDiscovery serviceDiscovery) {
    log.info("Request to create order");
    var response = routingContext.response();
    var body = routingContext.getBody();
    if (isNull(body)) {
      sendError(400, response);
    } else {
      var createOrderVM = body.toJsonObject().mapTo(CreateOrderVM.class);
      new AccountClient(serviceDiscovery).findAccount(createOrderVM.getAccount_id(), resultHandler->{
        if (resultHandler.succeeded()){
          Account account = resultHandler.result();
          if (createOrderVM.getAccount_id().equals(account.getId())){
            var order = createOrder.createLimitOrder(createOrderVM.getAccount_id(), createOrderVM.getPrice_limit());
            var result = JsonObject.mapFrom(order);
            sendSuccess(result, response);
          } else {
            log.error("Account id '{}' do not match response",createOrderVM.getAccount_id());
            sendError(400, response);
          }
        } else {
          sendError(400, response);
        }
      });
    }
  }

  public void findOrder (final RoutingContext routingContext) {
    log.info("Request to find order");
    var response = routingContext.response();
    var id = routingContext.request().getParam("id");
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

  public void findAllOrders (final RoutingContext routingContext) {
    log.info("Request to find all orders");
    var response = routingContext.response();
    var orders = findOrder.findAllOrders();
    var result = orders.stream()
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
