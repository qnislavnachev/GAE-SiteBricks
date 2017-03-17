package testapp.core;

import com.google.appengine.api.datastore.Entity;

import java.io.Serializable;

public class User implements Serializable {
    private long ucn;
    private String name;

    public User() {
    }

    public User(long ucn, String name) {
        this.ucn = ucn;
        this.name = name;
    }

    public long getUcn() {
        return ucn;
    }

    public String getName() {
        return name;
    }

    public Entity toEntity(String kind) {
        Entity entity = new Entity(kind);
        entity.setProperty("ucn", this.ucn);
        entity.setProperty("name", this.name);
        return entity;
    }

    public static User fromEntity(Entity entity) {
        Long id = (Long) entity.getProperty("ucn");
        String name = (String) entity.getProperty("name");

        return new User(id.intValue(), name);
    }
}
