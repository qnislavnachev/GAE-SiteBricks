package testapp.adapter.repository;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testapp.core.User;
import testapp.core.UserListener;
import testapp.core.UserRepository;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static testapp.matchers.CustomMatchers.containsUser;
import static testapp.matchers.CustomMatchers.sameAs;

public class PersistentUserRepositoryTest {
    private LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalMemcacheServiceTestConfig(), new LocalDatastoreServiceTestConfig());
    private UserListener listener = new UserListener() {
        @Override
        public void onRegistered(User user) {

        }

        @Override
        public void onRegistered(List<User> userList) {

        }

        @Override
        public void onDeleted(List<User> userList) {

        }
    };

    @Before
    public void setUp() throws Exception {
        helper.setUp();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public void userRegistration() throws Exception {
        final UserRepository repository = new PersistentUserRepository(listener);
        final User ianis = new User(1, "ianis");
        final User maggie = new User(2, "maggie");

        repository.register(ianis);
        repository.register(maggie);
        List<User> userList = repository.findUsers();

        assertThat(userList.size(), is(2));
        assertThat(userList, containsUser(ianis));
    }

    @Test
    public void deleteUser() throws Exception {
        UserRepository repository = new PersistentUserRepository(listener);
        User ianis = new User(1, "ianis");
        User maggie = new User(2, "maggie");

        repository.register(ianis);
        repository.register(maggie);

        repository.delete(1);
        List<User> userList = repository.findUsers();

        assertThat(userList.size(), is(1));
        assertThat(userList, containsUser(maggie));
    }

    @Test
    public void findUserById() throws Exception {
        UserRepository repository = new PersistentUserRepository(listener);
        User ianis = new User(1, "ianis");
        User vasko = new User(2, "vasko");
        User maggie = new User(3, "maggie");

        repository.register(ianis);
        repository.register(vasko);
        repository.register(maggie);

        User possibleUser = repository.findById(3);

        assertThat(possibleUser, sameAs(maggie));
    }

    @Test
    public void findUserByIdThatDoesNotExist() throws Exception {
        UserRepository repository = new PersistentUserRepository(listener);
        User possibleUser = repository.findById(3);

        assertThat(possibleUser, is(nullValue()));
    }
}
