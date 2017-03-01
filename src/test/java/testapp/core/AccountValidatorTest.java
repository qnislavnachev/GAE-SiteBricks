package testapp.core;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testapp.modules.AppModule;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static testapp.core.FakeKey.aKey;

public class AccountValidatorTest {
    private Injector injector = Guice.createInjector(new AppModule());
    private AccountRepository accountRepository = injector.getInstance(AccountRepository.class);

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
        Key key = aKey("accounts", 1);
        accountRepository.registerAccount(new UserAccount(key));

        UserAccount account = accountRepository.findUserAccount(key);

        assertNotNull(account);
    }

    @Test
    public void updateAccount() throws Exception {
        Key key = aKey("accounts", 1);
        accountRepository.registerAccount(new UserAccount(key));
        accountRepository.depositToAccount(new UserAccount(key), 20);

        UserAccount account = accountRepository.findUserAccount(key);

        assertThat(account.getTokenBalance(), is(20));
    }

    @Test
    public void findAccountSuccessful() throws Exception {
        Key key = aKey("accounts", 1);
        accountRepository.registerAccount(new UserAccount(key));

        UserAccount account = accountRepository.findUserAccount(key);

        assertNotNull(account);
    }

    @Test(expected = AccountException.class)
    public void findAccountThatNotExist() throws Exception {
        Key key = aKey("accounts", 1);
        accountRepository.findUserAccount(key);
    }

    @Test
    public void deleteAccountThatExist() throws Exception {
        Key key1 = aKey("accounts", 1);
        Key key2 = aKey("accounts", 2);
        Key key3 = aKey("accounts", 3);
        accountRepository.registerAccount(new UserAccount(key1));
        accountRepository.registerAccount(new UserAccount(key2));
        accountRepository.registerAccount(new UserAccount(key3));

        accountRepository.deleteAccount(key1);
        accountRepository.deleteAccount(key3);

        List<UserAccount> accounts = accountRepository.findAllAccounts();
        UserAccount account = accountRepository.findUserAccount(key2);

        assertThat(accounts.size(), is(1));
        assertNotNull(account);
    }

    @Test(expected = AccountException.class)
    public void deleteAccountThatNotExist() throws Exception {
        Key key = aKey("accounts", 1);
        accountRepository.deleteAccount(key);
    }
}
