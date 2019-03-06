package org.micro.config;

import org.micro.controller.AccountController;
import org.micro.controller.OrderController;
import org.micro.domain.port.AccountRepository;
import org.micro.domain.port.IdGenerator;
import org.micro.domain.port.OrderRepository;
import org.micro.idgenerator.UUIDGenerator;
import org.micro.repository.InMemoryAccountRepository;
import org.micro.repository.InMemoryOrderRepository;
import org.micro.scenario.CreateAccount;
import org.micro.scenario.CreateOrder;
import org.micro.scenario.FindAccount;
import org.micro.scenario.FindOrder;
import org.micro.scenario.UpdateAccount;
import org.micro.scenario.UpdateOrder;

public class AppConfig {

  private final AccountRepository accountRepository = new InMemoryAccountRepository();
  private final IdGenerator idGenerator = new UUIDGenerator();
  private final CreateAccount createAccount = new CreateAccount(accountRepository, idGenerator);
  private FindAccount findAccount = new FindAccount(accountRepository);
  private UpdateAccount updateAccount = new UpdateAccount(accountRepository);
  private final AccountController accountController = new AccountController(createAccount, findAccount, updateAccount);
  private OrderRepository orderRepository = new InMemoryOrderRepository();
  private CreateOrder createOrder = new CreateOrder(orderRepository, idGenerator);
  private FindOrder findOrder = new FindOrder(orderRepository);
  private UpdateOrder updateOrder = new UpdateOrder(orderRepository);
  private final OrderController orderController = new OrderController(createOrder, findOrder, updateOrder);

  public AccountController getAccountController() {
    return accountController;
  }

  public OrderController getOrderController() {
    return orderController;
  }
}
