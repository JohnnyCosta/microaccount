package org.micro.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceReference;
import lombok.extern.slf4j.Slf4j;
import org.micro.domain.entity.Account;

@Slf4j
public class AccountClient {
  private ServiceDiscovery discovery;

  public AccountClient(ServiceDiscovery discovery) {
    this.discovery = discovery;
  }

  public AccountClient findAccount(String accountId, Handler<AsyncResult<Account>> resultHandler) {
    log.info("Client request to find account id: {}", accountId);
    discovery.getRecord(r -> r
      .getName()
      .equals("account-service"), res -> {
      log.info("Result: {}", res
        .result()
        .getType());
      ServiceReference ref = discovery.getReference(res.result());
      WebClient client = ref.getAs(WebClient.class);
      client
        .get("/account/" + accountId)
        .send(handler -> {
          if (handler.succeeded()) {
            log.info("Handler succeeded with body: {}", handler
              .result()
              .bodyAsString());

            var account = handler
              .result()
              .bodyAsJsonObject()
              .mapTo(Account.class);

            resultHandler.handle(Future.succeededFuture(account));
          } else {
            log.error("Handler failed", handler.cause());
            resultHandler.handle(Future.failedFuture(handler.cause()));
          }

        });
    });
    return this;
  }
}
