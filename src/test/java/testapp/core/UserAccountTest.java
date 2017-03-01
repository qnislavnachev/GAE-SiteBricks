package testapp.core;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UserAccountTest {

    @Test
    public void accountBalance() throws Exception {
        UserAccount account = new UserAccount();

        assertThat(account.getTokenBalance(), is(0));
    }

    @Test
    public void depositToAccount() throws Exception {
        UserAccount account = new UserAccount();
        account.deposit(20);

        assertThat(account.getTokenBalance(), is(20));
    }

    @Test
    public void withdrawFromAccount() throws Exception {
        UserAccount account = new UserAccount();

        account.deposit(20);
        account.withdraw(15);

        assertThat(account.getTokenBalance(), is(5));
    }

    @Test(expected = UserException.class)
    public void withdrawFromAccountThatHaveNoEnoughBalance() throws Exception {
        UserAccount account = new UserAccount();

        account.withdraw(20);
    }
}
