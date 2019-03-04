package org.micro.scenario;

import org.micro.domain.entity.Order;
import org.micro.domain.port.IdGenerator;
import org.micro.domain.port.OrderRepository;

import java.util.List;
import java.util.Optional;

public final class UpdateOrder {

  private final OrderRepository repository;

  public UpdateOrder(final OrderRepository repository) {
    this.repository = repository;
  }

  public Optional<Order> finish (String order_id, Float btcPrice) {
   return repository.finish(order_id, btcPrice);
  }

}
