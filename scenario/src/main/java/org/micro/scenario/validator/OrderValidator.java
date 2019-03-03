package org.micro.scenario.validator;

import org.micro.domain.entity.Order;
import org.micro.domain.exception.OrderValidationException;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class OrderValidator {

  public static void validateCreateOrder(final Order order) {
    if (order == null) throw new OrderValidationException("Order should not be null");
    if (isBlank(order.getAccountId())) throw new OrderValidationException("Account id should not be null");
    if (isBlank(order.getOrderId())) throw new OrderValidationException("Order id should not be null");
    if (Objects.isNull(order.getFinished())) throw new OrderValidationException("Order finished status should not be null");
    if (Objects.isNull(order.getPriceLimit())) throw new OrderValidationException("Order price limit should not be null");
  }

  private OrderValidator() {

  }
}
