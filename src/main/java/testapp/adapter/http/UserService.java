package testapp.adapter.http;

import com.google.appengine.api.datastore.Entity;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import testapp.core.*;

import java.net.HttpURLConnection;
import java.util.List;

@At("/v1/users")
public class UserService {
    private UserRepository userRepository;
    private AccountRepository accRepository;

    @Inject
    public UserService(UserRepository userRepository, AccountRepository accRepository) {
        this.userRepository = userRepository;
        this.accRepository = accRepository;
    }

    @Get
    public Reply<?> getUsers() {
        List<User> userList = userRepository.findUsers();
        return Reply.with(UserDto.fromUsers(userList)).as(Json.class);
    }

    @Get
    @At("/:userId")
    public Reply<?> findUserById(@Named("userId") long ucn) {
        User user;
        try {
            user = userRepository.findById(ucn);
        } catch (UserException e) {
            return Reply.saying().badRequest();
        }
        return Reply.with(UserDto.fromUser(user)).as(Json.class);
    }

    @Post
    public Reply<?> registerUser(Request request) {
        UserDto userDto = request.read(UserDto.class).as(Json.class);
        try {
            Entity user = userRepository.register(userDto.toUser());
            accRepository.registerAccount(new UserAccount(user.getKey()));
        } catch (UserException | AccountException e) {
            return Reply.saying().badRequest();
        }
        return Reply.saying().status(HttpURLConnection.HTTP_CREATED);
    }

    @Delete
    @At("/:userId")
    public Reply<?> deleteUserById(@Named("userId") long ucn) {
        try {
            Entity user = userRepository.delete(ucn);
            accRepository.deleteAccount(user.getKey());
        } catch (UserException | AccountException e) {
            return Reply.saying().badRequest();
        }
        return Reply.saying().ok();
    }
}
