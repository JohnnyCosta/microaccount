package org.micro.controller;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.micro.scenario.FindAccount;
import org.micro.scenario.UpdateAccount;
import org.micro.utils.JsonCollectors;
import org.micro.vm.CreateAccountVM;
import org.micro.scenario.CreateAccount;
import io.vertx.ext.web.RoutingContext;
import org.micro.vm.UpdateAccountVM;

import static java.util.Objects.isNull;

@Slf4j
public class AccountController {

  private final CreateAccount createAccount;
  private final FindAccount findAccount;
  private final UpdateAccount updateAccount;

  public AccountController(CreateAccount createAccount, FindAccount findAccount, UpdateAccount updateAccount) {
    this.createAccount = createAccount;
    this.findAccount = findAccount;
    this.updateAccount = updateAccount;
  }

  public void createAccount(final RoutingContext routingContext) {
    log.info("Request to create account");
    var response = routingContext.response();
    var body = routingContext.getBody();
    if (isNull(body)) {
      sendError(400, response);
    } else {
      var createAccountVM = body.toJsonObject().mapTo(CreateAccountVM.class);
      var account = createAccount.create(createAccountVM.getName(), createAccountVM.getUsd_balance());
      var result = JsonObject.mapFrom(account);
      sendSuccess(result, response);
    }
  }

  public void findAccount (final RoutingContext routingContext) {
    log.info("Request to find account");
    var response = routingContext.response();
    var id = routingContext.request().getParam("id");
    if (isNull(id)) {
      sendError(400, response);
    } else {
      var account = findAccount.fetchAccountDetails(id);
      if (account.isPresent()) {
        var result = JsonObject.mapFrom(account.get());
        sendSuccess(result, response);
      } else {
        sendError(404, response);
      }
    }
  }

  public void findAllAccounts(final RoutingContext routingContext) {
    log.info("Request to find all accounts");
    var response = routingContext.response();
    var accounts = findAccount.findAllAccounts();
    var result = accounts.stream()
      .map(JsonObject::mapFrom)
      .collect(JsonCollectors.toJsonArray());
    response
      .putHeader("content-type", "application/json")
      .end(result.encodePrettily());
  }

  public void updateAccount (final RoutingContext routingContext) {
    log.info("Request to update account");
    var response = routingContext.response();
    var body = routingContext.getBody();
    if (isNull(body)) {
      sendError(400, response);
    } else {
      var updateAccountVM = body.toJsonObject().mapTo(UpdateAccountVM.class);
      var account = updateAccount.update(updateAccountVM.getAccountId(), updateAccountVM.getBtcPrice());
      if (account.isEmpty()){
        sendError(400, response);
      } else {
        var result = JsonObject.mapFrom(account.get());
        sendSuccess(result, response);
      }
    }
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
