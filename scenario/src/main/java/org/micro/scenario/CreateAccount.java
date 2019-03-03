package org.micro.scenario;

import org.micro.domain.entity.Account;
import org.micro.domain.port.IdGenerator;
import org.micro.domain.exception.AccountAlreadyExistsException;
import org.micro.domain.port.AccountRepository;
import org.micro.scenario.validator.AccountValidator;

public final class CreateAccount {

  private final AccountRepository repository;
  private final IdGenerator idGenerator;

  public CreateAccount(final AccountRepository repository, final IdGenerator idGenerator) {
    this.repository = repository;
    this.idGenerator = idGenerator;
  }

  public Account create(String name,Long usd_balance) {
    String id = idGenerator.generate();
    Account account = Account
      .builder()
      .id(id)
      .name(name)
      .usd(usd_balance)
      .btc(0L)
      .build();

    AccountValidator.validateCreateUser(account);

    if (repository.findByName(name).isPresent()) {
      throw new AccountAlreadyExistsException(String.format("Account with name '%s' already exists",name));
    }

    if (repository.findById(id).isPresent()) {
      throw new AccountAlreadyExistsException(String.format("Account with id '%s' already exists",id));
    }

    return repository.create(account);
  }


}
