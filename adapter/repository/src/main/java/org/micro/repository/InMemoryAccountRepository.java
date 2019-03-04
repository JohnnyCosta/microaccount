package org.micro.repository;


import org.micro.domain.entity.Account;
import org.micro.domain.entity.Order;
import org.micro.domain.port.AccountRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {

  private final Map<String, Account> inMemoryDb = new HashMap<>();

  @Override
  public Account create(Account account) {
    inMemoryDb.put(account.getId(), account);
    return account;
  }

  @Override
  public Optional<Account> findByName(String name) {
    return inMemoryDb.values().stream()
      .filter(account -> account.getName().equals(name))
      .findAny();
  }

  @Override
  public Optional<Account> findById(String id) {
    return inMemoryDb.values().stream()
      .filter(user -> user.getId().equals(id))
      .findAny();
  }

  @Override
  public List<Account> findAll() {
    return new ArrayList<>(inMemoryDb.values());
  }

  @Override
  public Optional<Account> update( String accountId, Float btcPrice) {
    return findById(accountId)
      .map(account -> {
        var usd = account.getUsd();
        if (usd>0){
          var btc = account.getUsd() / btcPrice;
          account.setBtc(account.getBtc() + btc);
          account.setUsd(account.getUsd() - usd);
        }
        return account;
      });

  }
}
