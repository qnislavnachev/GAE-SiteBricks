package testapp.adapter.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import testapp.core.User;
import testapp.core.UserAlreadyExistsException;
import testapp.core.UserRepository;

import java.net.HttpURLConnection;
import java.util.List;

@At("/users")
public class UserService {

    @Inject
    private UserRepository repository;

    @Get
    public Reply<?> getUsers() {
        List<User> userList = repository.findUsers();
        return Reply.with(userList).as(Json.class);
    }

    @Post
    public Reply<?> registerUser(Request request) {
        User user = request.read(User.class).as(Json.class);
        try {
            repository.register(user);
        } catch (UserAlreadyExistsException e) {
            Reply.saying().badRequest();
        }
        return Reply.saying().status(HttpURLConnection.HTTP_CREATED);
    }

    @Delete
    @At("/:userId")
    public Reply<?> deleteUserById (@Named("userId") int userId) {
        repository.delete(userId);
        return Reply.saying().ok();
    }
}
