package org.micro.repository;


import org.micro.domain.entity.Account;
import org.micro.domain.entity.Order;
import org.micro.domain.port.AccountRepository;
import org.micro.domain.port.OrderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryOrderRepository implements OrderRepository {

  private final Map<String, Order> inMemoryDb = new HashMap<>();

  @Override
  public Order create(Order order) {
    inMemoryDb.put(order.getOrderId(), order);
    return order;
  }


  @Override
  public Optional<Order> findById(String id) {
    return inMemoryDb.values().stream()
      .filter(order -> order.getOrderId().equals(id))
      .findAny();
  }

  @Override
  public Optional<Order> findByAccountId(String accountId) {
    return inMemoryDb.values().stream()
      .filter(order -> order.getAccountId().equals(accountId))
      .findAny();
  }

  @Override
  public Optional<Order> finish(String id) {
    return findById(id)
      .map(order -> {
        order.setFinished(true);
        return order;
      });
  }

  @Override
  public List<Order> findAll() {
    return new ArrayList<>(inMemoryDb.values());
  }
}
