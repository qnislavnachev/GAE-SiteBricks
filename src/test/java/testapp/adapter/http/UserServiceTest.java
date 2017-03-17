package testapp.adapter.http;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
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
import java.util.List;

import static org.junit.Assert.assertThat;
import static testapp.core.JsonBuilder.aNewJson;
import static testapp.core.JsonBuilder.aNewJsonArray;
import static testapp.matchers.SitebricksMatchers.containsJson;
import static testapp.matchers.SitebricksMatchers.statusIs;

public class UserServiceTest {

    public JUnitRuleMockery context = new JUnitRuleMockery();
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
    public void registerUser() throws Exception {
        final Request request = context.mock(Request.class);
        final UserRepository userRepository = context.mock(UserRepository.class);
        final AccountRepository accountRepository = context.mock(AccountRepository.class);

        final UserDto user = new UserDto(1, "ianis");
        final Entity entity = new Entity("users");

        final UserService userService = new UserService(userRepository, accountRepository);

        context.checking(new Expectations() {{
            oneOf(request).read(UserDto.class);
            will(returnValue(new FakeRequestRead(user)));

            oneOf(userRepository).register(with(any(User.class)));
            will(returnValue(entity));

            oneOf(accountRepository).registerAccount(with(any(UserAccount.class)));
        }});

        Reply<?> response = userService.registerUser(request);

        assertThat(response, statusIs(HttpURLConnection.HTTP_CREATED));
    }

    @Test
    public void registerIncorrectUser() throws Exception {
        final Request request = context.mock(Request.class);
        final UserRepository userRepository = context.mock(UserRepository.class);
        final AccountRepository accountRepository = context.mock(AccountRepository.class);

        final UserService userService = new UserService(userRepository, accountRepository);
        final UserDto user = new UserDto(1, "ianis123");

        context.checking(new Expectations() {{
            oneOf(request).read(UserDto.class);
            will(returnValue(new FakeRequestRead(user)));

            oneOf(userRepository).register(with(any(User.class)));
            will(throwException(new UserException("")));
        }});

        Reply<?> response = userService.registerUser(request);

        assertThat(response, statusIs(HttpURLConnection.HTTP_BAD_REQUEST));
    }

    @Test
    public void deleteUser() throws Exception {
        final UserRepository userRepository = context.mock(UserRepository.class);
        final AccountRepository accountRepository = context.mock(AccountRepository.class);

        final Entity entity = new Entity("users");
        final UserService userService = new UserService(userRepository, accountRepository);

        context.checking(new Expectations(){{
            oneOf(userRepository).delete(1);
            will(returnValue(entity));

            oneOf(accountRepository).deleteAccount(with(any(Key.class)));
        }});

        Reply<?> response = userService.deleteUserById(1);

        assertThat(response, statusIs(HttpURLConnection.HTTP_OK));
    }

    @Test
    public void deleteUserThatNotExist() throws Exception {
        final UserRepository userRepository = context.mock(UserRepository.class);
        final AccountRepository accountRepository = context.mock(AccountRepository.class);

        final UserService userService = new UserService(userRepository, accountRepository);

        context.checking(new Expectations(){{
            oneOf(userRepository).delete(1);
            will(throwException(new UserException("")));
        }});

        Reply<?> response = userService.deleteUserById(1);

        assertThat(response, statusIs(HttpURLConnection.HTTP_BAD_REQUEST));
    }

    @Test
    public void findUserById() throws Exception {
        final UserRepository userRepository = context.mock(UserRepository.class);
        final AccountRepository accountRepository = context.mock(AccountRepository.class);

        final User user = new User(1, "ianis");
        final UserService userService = new UserService(userRepository, accountRepository);

        context.checking(new Expectations(){{
            oneOf(userRepository).findById(1);
            will(returnValue(user));
        }});

        Reply<?> response = userService.findUserById(1);

        assertThat(response, containsJson(
                            aNewJson()
                                .add("ucn", user.getUcn())
                                .add("name", user.getName())
                                ));
    }

    @Test
    public void findUserByIdThatNotExist() throws Exception {
        final UserRepository userRepository = context.mock(UserRepository.class);
        final AccountRepository accountRepository = context.mock(AccountRepository.class);

        final UserService userService = new UserService(userRepository, accountRepository);

        context.checking(new Expectations(){{
            oneOf(userRepository).findById(1);
            will(throwException(new UserException("")));
        }});

        Reply<?> response = userService.findUserById(1);

        assertThat(response, statusIs(HttpURLConnection.HTTP_BAD_REQUEST));
    }

    @Test
    public void findAllUsers() throws Exception {
        final UserRepository userRepository = context.mock(UserRepository.class);
        final AccountRepository accountRepository = context.mock(AccountRepository.class);

        final User ianis = new User(1, "ianis");
        final UserService userService = new UserService(userRepository, accountRepository);

        context.checking(new Expectations(){{
            oneOf(userRepository).findUsers();
            will(returnValue(Lists.newArrayList(ianis)));
        }});

        Reply<?> response = userService.getUsers();

        assertThat(response, containsJson(
                                aNewJsonArray().withElements(
                                    aNewJson()
                                        .add("ucn", ianis.getUcn())
                                        .add("name", ianis.getName()))));
    }
}
