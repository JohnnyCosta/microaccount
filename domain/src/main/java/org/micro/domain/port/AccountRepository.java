package org.micro.domain.port;

import org.micro.domain.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

	Account create(Account account);

	Optional<Account> findByName(String name);

  Optional<Account> findById(String id);

  List<Account> findAll();

}
