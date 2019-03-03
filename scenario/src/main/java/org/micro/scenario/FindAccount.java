package org.micro.scenario;

import org.micro.domain.entity.Account;
import org.micro.domain.port.AccountRepository;

import java.util.List;
import java.util.Optional;

public final class FindAccount {

  private final AccountRepository repository;

  public FindAccount(final AccountRepository repository) {
    this.repository = repository;
  }

  public Optional<Account> fetchAccountDetails(String account_id) {
    return repository.findById(account_id);
  }

  public List<Account> findAllAccounts() {
    return repository.findAll();
  }


}
