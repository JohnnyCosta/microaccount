package org.micro.client;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.ServiceReference;
import lombok.extern.slf4j.Slf4j;
import org.micro.domain.entity.Account;
import org.micro.vm.BtcPriceVM;

@Slf4j
public class BtcPriceClient {

  private final HttpRequest<Buffer> request;

  public BtcPriceClient(String host, String query, int port, Vertx vertx) {
    this.request = WebClient
      .create(vertx)
      .get(port, host, query);
  }

  public BtcPriceClient getPrice(Handler<AsyncResult<BtcPriceVM>> resultHandler) {
    log.info("Client request to get Btc price");

    request
      .send(handler -> {
          if (handler.succeeded()){
            log.info("Handler succeeded with body: {}", handler
              .result()
              .bodyAsString());

            var btcPriceVM = handler
              .result()
              .bodyAsJsonObject()
              .mapTo(BtcPriceVM.class);

            resultHandler.handle(Future.succeededFuture(btcPriceVM));
          } else {
            log.error("Handler failed", handler.cause());
            resultHandler.handle(Future.failedFuture(handler.cause()));
          }

        });

    return this;
  }
}
