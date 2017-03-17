package testapp.adapter.repository;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import testapp.core.*;

import java.util.List;

public class PersistentUserRepository implements UserRepository {
    private DatastoreService datastore;
    private MemcacheService cache;
    private UserListener listener;

    public PersistentUserRepository(UserListener listener) {
        this.listener = listener;
        this.datastore = DatastoreServiceFactory.getDatastoreService();
        this.cache = MemcacheServiceFactory.getMemcacheService();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> findUsers() {
        if (cache.contains("users")) {
            return (List) cache.get("users");
        }
        return getAllUsers();
    }

    @Override
    public User findById(long ucn) {
        Query findUser = Queries.findSingleEntity("users", "ucn", ucn);
        Entity userEntity = datastore.prepare(findUser).asSingleEntity();

        if (userEntity == null) {
            return null;
        }

        return User.fromEntity(userEntity);
    }

    @Override
    public Key getEntityKey(long ucn) {
        Query findUser = Queries.findSingleEntity("users", "ucn", ucn);
        return datastore.prepare(findUser).asSingleEntity().getKey();
    }

    @Override
    public Entity register(User user) {
        Entity entity = user.toEntity("users");
        datastore.put(entity);
        listener.onRegistered(user);
        listener.onRegistered(getAllUsers());
        return entity;
    }

    @Override
    public Entity delete(long ucn) {
        Query findUser = Queries.findSingleEntity("users", "ucn", ucn);
        Entity userEntity = datastore.prepare(findUser).asSingleEntity();

        if (userEntity == null) {
            return null;
        }

        datastore.delete(userEntity.getKey());
        listener.onDeleted(getAllUsers());
        return userEntity;
    }

    private List<User> getAllUsers() {
        List<User> userList = Lists.newArrayList();
        Iterable<Entity> entities =
                datastore.prepare(Queries.getAllEntitiesInKind("users")).asIterable();

        for (Entity entity : entities) {
            userList.add(User.fromEntity(entity));
        }
        return userList;
    }
}
