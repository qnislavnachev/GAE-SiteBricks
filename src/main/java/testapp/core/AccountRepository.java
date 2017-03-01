package testapp.core;

import com.google.appengine.api.datastore.Key;

import java.util.List;

public interface AccountRepository {

    void registerAccount(UserAccount account);

    void depositToAccount(UserAccount account, int value);

    void withdrawFromAccount(UserAccount account, int value);

    void deleteAccount(Key userKey);

    UserAccount findUserAccount(Key userKey);

    List<UserAccount> findAllAccounts();
}
