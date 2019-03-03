package org.micro.scenario;

import org.micro.domain.entity.Order;
import org.micro.domain.exception.AccountIdInvalidException;
import org.micro.domain.exception.OrderAlreadyExistsException;
import org.micro.domain.port.AccountRepository;
import org.micro.domain.port.IdGenerator;
import org.micro.domain.port.OrderRepository;
import org.micro.scenario.validator.OrderValidator;

public final class CreateOrder {

  private final OrderRepository orderRepository;
  private final IdGenerator idGenerator;

  public CreateOrder(final OrderRepository orderRepository, final IdGenerator idGenerator) {
    this.orderRepository = orderRepository;
    this.idGenerator = idGenerator;
  }

  public Order createLimitOrder(String account_id, Long priceLimit) {

    String id = idGenerator.generate();
    Order order = Order
      .builder()
      .orderId(id)
      .accountId(account_id)
      .priceLimit(priceLimit)
      .finished(false)
      .build();

    OrderValidator.validateCreateOrder(order);

    if (orderRepository
      .findById(id).isPresent()) {
      throw new OrderAlreadyExistsException(String.format("Order with id '%s' already exists",id));
    }

    return orderRepository.create(order);
  }


}
