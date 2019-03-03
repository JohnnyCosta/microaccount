package org.micro.scenario.validator;

import org.micro.domain.entity.Account;
import org.micro.domain.exception.AccountValidationException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class AccountValidator {

	public static void validateCreateUser(final Account account) {
		if (account == null) throw new AccountValidationException("Account should not be null");
		if (isBlank(account.getName())) throw new AccountValidationException("Account name should not be null");
    if (isBlank(account.getId())) throw new AccountValidationException("Account id should not be null");
	}

	private AccountValidator() {

	}
}
