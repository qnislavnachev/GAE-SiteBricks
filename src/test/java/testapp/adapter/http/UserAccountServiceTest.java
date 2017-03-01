package testapp.adapter.http;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testapp.core.*;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertThat;
import static testapp.core.FakeKey.aKey;
import static testapp.matchers.SitebricksMatchers.containsJson;
import static testapp.matchers.SitebricksMatchers.containsValue;
import static testapp.matchers.SitebricksMatchers.statusIs;

public class UserAccountServiceTest {
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig(), new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() throws Exception {
        helper.setUp();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void userGetBalance() throws Exception {
        final AccountRepository accountRepository = context.mock(AccountRepository.class);
        final UserRepository userRepository = context.mock(UserRepository.class);

        final Key key = aKey("users", 1);
        UserAccountService accountService = new UserAccountService(userRepository, accountRepository);

        context.checking(new Expectations() {{
            oneOf(userRepository).getEntityKey(1);
            will(returnValue(key));

            oneOf(accountRepository).findUserAccount(key);
            will(returnValue(new UserAccount()));
        }});

        Reply<?> response = accountService.getUserBalance(1);

        assertThat(response, containsValue(0));
    }

    @Test
    public void getBalanceOfNonExistingUser() throws Exception {
        final AccountRepository accountRepository = context.mock(AccountRepository.class);
        final UserRepository userRepository = context.mock(UserRepository.class);

        final Key key = aKey("users", 1);
        UserAccountService accountService = new UserAccountService(userRepository, accountRepository);

        context.checking(new Expectations() {{
            oneOf(userRepository).getEntityKey(1);
            will(returnValue(key));

            oneOf(accountRepository).findUserAccount(key);
            will(throwException(new AccountException("")));
        }});

        Reply<?> response = accountService.getUserBalance(1);

        assertThat(response, statusIs(HttpURLConnection.HTTP_BAD_REQUEST));
    }

    @Test
    public void depositToAccount() throws Exception {
        final AccountRepository accountRepository = context.mock(AccountRepository.class);
        final UserRepository userRepository = context.mock(UserRepository.class);
        final Request request = context.mock(Request.class);

        final Key key = aKey("users", 1);
        final UserAccount account = new UserAccount(key);
        UserAccountService accountService = new UserAccountService(userRepository, accountRepository);

        context.checking(new Expectations() {{
            oneOf(userRepository).getEntityKey(1);
            will(returnValue(key));

            oneOf(accountRepository).findUserAccount(key);
            will(returnValue(account));

            oneOf(request).param("amount");
            will(returnValue("20"));

            oneOf(accountRepository).depositToAccount(account, 20);
        }});

        Reply<?> response = accountService.deposit(1, request);

        assertThat(response, statusIs(HttpURLConnection.HTTP_OK));
    }

    @Test
    public void depositToUnexistingAccount() throws Exception {
        final AccountRepository accountRepository = context.mock(AccountRepository.class);
        final UserRepository userRepository = context.mock(UserRepository.class);
        final Request request = context.mock(Request.class);

        final Key key = aKey("users", 1);
        UserAccountService accountService = new UserAccountService(userRepository, accountRepository);

        context.checking(new Expectations() {{
            oneOf(userRepository).getEntityKey(1);
            will(returnValue(key));

            oneOf(request).param("amount");
            will(returnValue("20"));

            oneOf(accountRepository).findUserAccount(key);
            will(throwException(new AccountException("")));
        }});

        Reply<?> response = accountService.deposit(1, request);

        assertThat(response, statusIs(HttpURLConnection.HTTP_BAD_REQUEST));
    }

    @Test
    public void withdrawFromAccount() throws Exception {
        final AccountRepository accountRepository = context.mock(AccountRepository.class);
        final UserRepository userRepository = context.mock(UserRepository.class);
        final Request request = context.mock(Request.class);

        final Key key = aKey("users", 1);
        final UserAccount account = new UserAccount(key);
        UserAccountService accountService = new UserAccountService(userRepository, accountRepository);
        account.deposit(20);

        context.checking(new Expectations() {{
            oneOf(userRepository).getEntityKey(1);
            will(returnValue(key));

            oneOf(accountRepository).findUserAccount(key);
            will(returnValue(account));

            oneOf(request).param("amount");
            will(returnValue("15"));

            oneOf(accountRepository).withdrawFromAccount(account, 15);
        }});

        Reply<?> response = accountService.withdraw(1, request);

        assertThat(response, statusIs(HttpURLConnection.HTTP_OK));
    }

    @Test
    public void withdrawFromUnexistingAccount() throws Exception {
        final AccountRepository accountRepository = context.mock(AccountRepository.class);
        final Request request = context.mock(Request.class);
        final UserRepository userRepository = context.mock(UserRepository.class);

        final Key key = aKey("users", 1);
        UserAccountService accountService = new UserAccountService(userRepository, accountRepository);

        context.checking(new Expectations() {{
            oneOf(userRepository).getEntityKey(1);
            will(returnValue(key));

            oneOf(request).param("amount");
            will(returnValue("15"));

            oneOf(accountRepository).findUserAccount(key);
            will(throwException(new AccountException("")));
        }});

        Reply<?> response = accountService.withdraw(1, request);

        assertThat(response, statusIs(HttpURLConnection.HTTP_BAD_REQUEST));
    }
}
