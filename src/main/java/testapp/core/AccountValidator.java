package testapp.core;

import com.google.appengine.api.datastore.Key;

import java.util.List;

public class AccountValidator implements AccountRepository {
    private AccountRepository origin;

    public AccountValidator(AccountRepository origin) {
        this.origin = origin;
    }

    @Override
    public void registerAccount(UserAccount account) {
        origin.registerAccount(account);
    }

    public void depositToAccount(UserAccount account, int value) {
        origin.depositToAccount(account, value);
    }

    @Override
    public void withdrawFromAccount(UserAccount account, int value) {
        origin.withdrawFromAccount(account, value);
    }

    @Override
    public void deleteAccount(Key userKey) {
        if (isAccountExist(userKey)) {
            origin.deleteAccount(userKey);
            return;
        }
        throw new AccountException("Account does not exist");
    }

    @Override
    public UserAccount findUserAccount(Key userKey) {
        UserAccount account = origin.findUserAccount(userKey);
        if (account != null) {
            return account;
        }
        throw new AccountException("Account does not exist");
    }

    @Override
    public List<UserAccount> findAllAccounts() {
        return origin.findAllAccounts();
    }

    /**
     * -= Validation methods start here =-
     */

    private boolean isAccountExist(Key userKey) {
        if (origin.findUserAccount(userKey) != null) {
            return true;
        }
        return false;
    }
}
