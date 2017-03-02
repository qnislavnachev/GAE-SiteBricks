package testapp.adapter.memory;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import testapp.core.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserRepository implements UserRepository {
    private List<User> userList;
    private UserValidator validator;
    private MemcacheService cache;
    private UserListener listener;

    public InMemoryUserRepository(UserListener listener) {
        this.listener = listener;
        this.userList = new ArrayList<>();
        this.validator = new UserValidator(userList, this);
        this.cache = MemcacheServiceFactory.getMemcacheService();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUsers() {
        if (cache.contains("users")) {
            return (List) cache.get("users");
        }
        return userList;
    }

    @Override
    public User findById(int id) {
        for (User each : userList) {
            if (each.getId() == id) {
                return each;
            }
        }
        return null;
    }

    @Override
    public void register(User user) {
        String userName = user.getName();
        String regex = "[a-zA-z]";

        if (!validator.isExist(user) || validator.validate(userName, regex)) {
            userList.add(user);
            cache.put("users", userList);
            listener.onRegister(user);
            return;
        }
        throw new UserException("User Already Exist");
    }

    @Override
    public void delete(int userId) {
        User user = findById(userId);
        if (user != null) {
            userList.remove(user);
            return;
        }
        throw new UserException("User does not exist");
    }
}
