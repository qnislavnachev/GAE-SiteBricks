package testapp.adapter.http;

import com.google.appengine.api.datastore.Key;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import testapp.core.*;

@At("/v1/accounts")
public class UserAccountService {
    private AccountRepository accRepository;
    private UserRepository userRepository;

    @Inject
    public UserAccountService(UserRepository userRepository, AccountRepository accRepository) {
        this.accRepository = accRepository;
        this.userRepository = userRepository;
    }

    @Get
    @At("/:userId/balance")
    public Reply<?> getUserBalance(@Named("userId") long ucn) {
        int balance;
        Key userKey;
        UserAccount account;
        try {
            userKey = userRepository.getEntityKey(ucn);
            account = accRepository.findUserAccount(userKey);
            balance = account.getTokenBalance();
        } catch (AccountException | NullPointerException e) {
            return Reply.saying().badRequest();
        }
        return Reply.with(balance).as(Json.class);
    }

    @Post
    @At("/:userId/deposit")
    public Reply<?> deposit(@Named("userId") long ucn, Request request) {
        Key userKey;
        UserAccount account;
        String amount = request.param("amount");
        int value = Integer.valueOf(amount);
        try {
            userKey = userRepository.getEntityKey(ucn);
            account = accRepository.findUserAccount(userKey);
            accRepository.depositToAccount(account, value);
        } catch (AccountException | NullPointerException e) {
            return Reply.saying().badRequest();
        }
        return Reply.saying().ok();
    }

    @Post
    @At("/:userId/withdraw")
    public Reply<?> withdraw(@Named("userId") long ucn, Request request) {
        Key userKey;
        UserAccount account;
        String amount = request.param("amount");
        int value = Integer.valueOf(amount);
        try {
            userKey = userRepository.getEntityKey(ucn);
            account = accRepository.findUserAccount(userKey);
            accRepository.withdrawFromAccount(account, value);
        } catch (AccountException | NullPointerException e) {
            return Reply.saying().badRequest();
        }
        return Reply.saying().ok();
    }
}
