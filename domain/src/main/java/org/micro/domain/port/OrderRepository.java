package org.micro.domain.port;

import org.micro.domain.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

  Order create(Order order);

  Optional<Order> findById(String orderId);

  List<Order> findAll();

}
