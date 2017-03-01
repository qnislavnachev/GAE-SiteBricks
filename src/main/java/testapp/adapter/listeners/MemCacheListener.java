package testapp.adapter.listeners;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import testapp.core.User;
import testapp.core.UserListener;

import java.util.List;

public class MemCacheListener implements UserListener {
    private MemcacheService cache = MemcacheServiceFactory.getMemcacheService();

    @Override
    public void onRegistered(User user) {

    }

    @Override
    public void onRegistered(List<User> userList) {
        cache.put("users", userList);
    }

    @Override
    public void onDeleted(List<User> userList) {
        cache.put("users", userList);
    }
}
