package org.micro.domain.port;

import org.micro.domain.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

  Order create(Order order);

  Optional<Order> findById(String orderId);

  Optional<Order> finish(String orderId, Float btcPrice);

  Optional<Order> findByAccountId(String accountId);

  List<Order> findAll();

}
