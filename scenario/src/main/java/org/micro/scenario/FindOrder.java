package org.micro.scenario;

import org.micro.domain.entity.Order;
import org.micro.domain.port.IdGenerator;
import org.micro.domain.port.OrderRepository;

import java.util.List;
import java.util.Optional;

public final class FindOrder {

  private final OrderRepository repository;

  public FindOrder(final OrderRepository repository, final IdGenerator idGenerator) {
    this.repository = repository;
  }

  public Optional<Order> fetchOrderDetails(String order_id) {
    return repository.findById(order_id);
  }

  public List<Order> findAllOrders() {
    return repository.findAll();
  }

}
