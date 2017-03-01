package testapp.core;

import java.io.Serializable;

public class UserAccount implements Serializable {
    private int balance = 0;

    public UserAccount() {
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int balance) {
        this.balance += balance;
    }

    public void withdraw(int balance) {
        if (this.balance < balance) {
            throw new UserException("No enough amount in your account");
        }
        this.balance -= balance;
    }
}
