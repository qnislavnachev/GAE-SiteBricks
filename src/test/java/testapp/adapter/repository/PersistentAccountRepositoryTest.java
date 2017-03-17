package testapp.adapter.repository;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testapp.core.AccountRepository;
import testapp.core.UserAccount;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static testapp.core.FakeKey.aKey;

public class PersistentAccountRepositoryTest {
    private AccountRepository accountRepository = new PersistentAccountRepository();

    private LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalMemcacheServiceTestConfig(), new LocalDatastoreServiceTestConfig());


    @Before
    public void setUp() throws Exception {
        helper.setUp();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void registerAccount() throws Exception {
        Key key1 = aKey("accounts", 1);
        Key key2 = aKey("accounts", 2);
        accountRepository.registerAccount(new UserAccount(key1));
        accountRepository.registerAccount(new UserAccount(key2));

        UserAccount account1 = accountRepository.findUserAccount(key1);
        UserAccount account2 = accountRepository.findUserAccount(key2);

        assertNotNull(account1);
        assertNotNull(account2);
    }

    @Test
    public void updateAccount() throws Exception {
        Key key = aKey("accounts", 1);
        accountRepository.registerAccount(new UserAccount(key));
        accountRepository.depositToAccount(new UserAccount(key), 20);

        UserAccount account = accountRepository.findUserAccount(key);

        assertNotNull(account);
        assertThat(account.getTokenBalance(), is(20));
    }

    @Test
    public void deleteAccount() throws Exception {
        Key key1 = aKey("accounts", 1);
        Key key2 = aKey("accounts", 2);

        accountRepository.registerAccount(new UserAccount(key1));
        accountRepository.registerAccount(new UserAccount(key2));

        accountRepository.deleteAccount(key1);

        List<UserAccount> accounts = accountRepository.findAllAccounts();
        UserAccount account = accountRepository.findUserAccount(key2);

        assertThat(accounts.size(), is(1));
        assertNotNull(account);
    }
}
