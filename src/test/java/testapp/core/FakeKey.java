package testapp.core;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class FakeKey {

    public static Key aKey(String kind, long id) {
        return KeyFactory.createKey(kind, id);
    }
}
