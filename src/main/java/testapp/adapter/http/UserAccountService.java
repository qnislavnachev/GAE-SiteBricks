package testapp.adapter.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import testapp.core.User;
import testapp.core.UserAccount;
import testapp.core.UserException;
import testapp.core.UserRepository;

import java.util.logging.Logger;

@At("/accounts")
public class UserAccountService {

    @Inject
    private UserRepository repository;
    private Logger log = Logger.getLogger(UserAccountService.class.getName());
    public UserAccountService() {
    }

    @Get
    @At("/:userId/balance")
    public Reply<?> getUserBalance(@Named("userId") int userId) {
        User user = repository.findById(userId);
        if (user == null) {
            return Reply.saying().badRequest();
        }
        log.info("User " + user.getName() + " check his balance");
        return Reply.with(user.getAccount().getBalance()).as(Json.class);
    }

    @Post
    @At("/:userId/deposit/:value")
    public Reply<?> deposit(@Named("userId") int userId, @Named("value") int value) {
        User user = repository.findById(userId);
        if (user != null) {
            UserAccount acc = user.getAccount();
            acc.deposit(value);
            log.info("User");
            return Reply.saying().ok();
        }
        return Reply.saying().badRequest();
    }

    @Post
    @At("/:userId/withdraw/:value")
    public Reply<?> withdraw(@Named("userId") int userId, @Named("value") int value) {
        User user = repository.findById(userId);
        if (user != null) {
            withdraw(user.getAccount(), value);
        }
        return Reply.saying().badRequest();
    }

    private Reply<?> withdraw(UserAccount acc, int value) {
        try {
            acc.withdraw(value);
        } catch (UserException e) {
            return Reply.saying().badRequest();
        }
        return Reply.saying().ok();
    }
}
