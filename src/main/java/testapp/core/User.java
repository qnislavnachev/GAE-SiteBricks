package testapp.core;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    private UserAccount account = new UserAccount();

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserAccount getAccount() {
        return account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
