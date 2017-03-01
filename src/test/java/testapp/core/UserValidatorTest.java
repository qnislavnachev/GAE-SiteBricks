package testapp.core;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testapp.adapter.listeners.UserLogListener;
import testapp.adapter.repository.PersistentUserRepository;
import testapp.modules.AppModule;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static testapp.matchers.CustomMatchers.containsUser;
import static testapp.matchers.CustomMatchers.sameAs;

public class UserValidatorTest {
    private Injector injector = Guice.createInjector(new AppModule());
    private UserRepository repository= injector.getInstance(UserRepository.class);

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
    public void registerUsersWithOnlyAlphabeticalCharacters() throws Exception {
        User ianis = new User(1, "Ianis");
        User maggie = new User(2, "Maggie");

        repository.register(ianis);
        repository.register(maggie);
        List<User> userList = repository.findUsers();

        assertThat(userList.size(), is(2));
        assertThat(userList, containsUser(ianis));
    }

    @Test(expected = UserException.class)
    public void registerUsersWithNumericSymbols() throws Exception {
        User ianis = new User(1, "Ianis123");

        repository.register(ianis);
    }

    @Test(expected = UserException.class)
    public void registerUserWithSpecialCharacters() throws Exception {
        User ianis = new User(1, "Ianis!@#$%^&*()");

        repository.register(ianis);
    }

    @Test(expected = UserException.class)
    public void registerUserWithIdThatAlreadyExist() throws Exception {
        User ianis1 = new User(1, "Ianis");
        User ianis2 = new User(1, "Ianischo");

        repository.register(ianis1);
        repository.register(ianis2);
    }

    @Test
    public void findUserById() throws Exception {
        User ianis = new User(1, "Ianis");
        User maggie = new User(2, "Maggie");

        repository.register(ianis);
        repository.register(maggie);

        User user1 = repository.findById(1);
        User user2 = repository.findById(2);

        assertThat(user1, sameAs(ianis));
        assertThat(user2, sameAs(maggie));
    }

    @Test(expected = UserException.class)
    public void findUserByIdThatNotExist() throws Exception {
        UserValidator userValidator = new UserValidator(new PersistentUserRepository(new UserLogListener()));
        userValidator.findById(1);
    }

    @Test
    public void deleteUser() throws Exception {
        UserValidator userValidator = new UserValidator(new PersistentUserRepository(new UserLogListener()));
        User ianis = new User(1, "Ianis");
        User maggie = new User(2, "Maggie");

        userValidator.register(ianis);
        userValidator.register(maggie);

        userValidator.delete(1);
        userValidator.delete(2);

        List<User> userList = userValidator.findUsers();

        assertThat(userList.size(), is(0));
    }

    @Test(expected = UserException.class)
    public void deleteUserThatDoesNotExist() throws Exception {
        repository.delete(1);
    }
}
