package testapp.adapter.repository;

import com.google.appengine.api.datastore.*;
import testapp.core.AccountRepository;
import testapp.core.Queries;
import testapp.core.UserAccount;

import java.util.ArrayList;
import java.util.List;

public class PersistentAccountRepository implements AccountRepository {
    private DatastoreService datastore;

    public PersistentAccountRepository() {
        this.datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void registerAccount(UserAccount account) {
        datastore.put(account.toEntity("accounts"));
    }

    @Override
    public void depositToAccount(UserAccount account, int value) {
        account.deposit(value);
        Entity entity;
        try {
            entity = datastore.get(account.getUserKey());
            entity.setProperty("ucn", account.getUserKey());
            entity.setProperty("tokenBalance", account.getTokenBalance());
        } catch (EntityNotFoundException e) {
            return;
        }
        datastore.put(entity);
    }

    @Override
    public void withdrawFromAccount(UserAccount account, int value) {
        account.deposit(value);
        UserAccount updatedAccount = new UserAccount(account.getUserKey(), account.getTokenBalance());
        datastore.put(updatedAccount.toEntity("accounts"));
    }

    @Override
    public void deleteAccount(Key userKey) {
        datastore.delete(userKey);
    }

    @Override
    public UserAccount findUserAccount(Key userKey) {
        Entity entity;
        try {
            entity = datastore.get(userKey);
        } catch (EntityNotFoundException e) {
            return null;
        }
        return UserAccount.fromEntity(entity);
    }

    @Override
    public List<UserAccount> findAllAccounts() {
        List<UserAccount> accountList = new ArrayList<>();
        Query findAllAccounts = Queries.getAllEntitiesInKind("accounts");
        Iterable<Entity> accounts = datastore.prepare(findAllAccounts).asIterable();

        for (Entity each : accounts) {
            accountList.add(UserAccount.fromEntity(each));
        }

        return accountList;
    }
}
