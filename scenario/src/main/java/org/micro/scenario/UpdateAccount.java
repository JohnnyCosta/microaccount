package org.micro.scenario;

import org.micro.domain.entity.Account;
import org.micro.domain.entity.Order;
import org.micro.domain.port.AccountRepository;
import org.micro.domain.port.OrderRepository;

import java.util.Optional;

public final class UpdateAccount {

  private final AccountRepository repository;

  public UpdateAccount(final AccountRepository repository) {
    this.repository = repository;
  }

  public Optional<Account> update (String accountId, Float btcPrice) {
   return repository.update(accountId, btcPrice);
  }

}
