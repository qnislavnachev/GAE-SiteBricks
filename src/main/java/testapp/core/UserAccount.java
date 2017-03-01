package testapp.core;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

import java.io.Serializable;

public class UserAccount implements Serializable {
    private Key userKey;
    private int tokenBalance;

    public UserAccount() {
    }

    public UserAccount(Key userKey) {
        this.userKey = userKey;
        this.tokenBalance = 0;
    }

    public UserAccount(Key userKey, int tokenBalance) {
        this.userKey = userKey;
        this.tokenBalance = tokenBalance;
    }

    public int getTokenBalance() {
        return tokenBalance;
    }

    public Key getUserKey() {
        return userKey;
    }

    public void deposit(int tokens) {
        this.tokenBalance += tokens;
    }

    public void withdraw(int tokens) {
        if (this.tokenBalance < tokens) {
            throw new UserException("No enough amount in your account");
        }
        this.tokenBalance -= tokens;
    }

    public Entity toEntity(String kind) {
        Entity acc = new Entity(kind);
        acc.setProperty("ucn", this.userKey);
        acc.setProperty("tokenBalance", this.tokenBalance);
        return acc;
    }

    public static UserAccount fromEntity(Entity entity) {
        Key userId = (Key) entity.getProperty("ucn");
        Long balance = (Long) entity.getProperty("tokenBalance");
        return new UserAccount(userId, balance.intValue());
    }
}
