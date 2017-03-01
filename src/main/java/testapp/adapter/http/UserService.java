package testapp.adapter.http;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.sitebricks.At;
import com.google.sitebricks.client.transport.Json;
import com.google.sitebricks.headless.Reply;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.http.Delete;
import com.google.sitebricks.http.Get;
import com.google.sitebricks.http.Post;
import testapp.core.Tasks;
import testapp.core.User;
import testapp.core.UserException;
import testapp.core.UserRepository;

import java.net.HttpURLConnection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@At("/users")
public class UserService {
    private Logger log;
    private Queue queue;
    private UserRepository userRepository;

    @Inject
    public UserService(UserRepository repository) {
        this.userRepository = repository;
        this.queue = QueueFactory.getDefaultQueue();
        this.log = Logger.getLogger(UserService.class.getName());
    }

    @Get
    public Reply<?> getUsers() {
        List<User> userList = userRepository.findUsers();
        log.info("User list was fetched at " + new Date());
        return Reply.with(userList).as(Json.class);
    }
    @Get
    @At("/:userId")
    public Reply<?> findUserById(@Named("userId") int userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            log.info("User by Id was fetched");
            return Reply.with(user).as(Json.class);
        }
        return Reply.saying().badRequest();
    }

    @Post
    public Reply<?> registerUser(Request request) {
        User user = request.read(User.class).as(Json.class);
        try {
            userRepository.register(user);
            queue.add(new Tasks().sendEmailToAdminTask());
            log.info("User was registered at " + new Date());
        } catch (UserException e) {
            return Reply.saying().badRequest();
        }
        return Reply.saying().status(HttpURLConnection.HTTP_CREATED);
    }

    @Delete
    @At("/:userId")
    public Reply<?> deleteUserById (@Named("userId") int userId) {
        try {
            userRepository.delete(userId);
            log.info("User was deleted at " + new Date());
        } catch (UserException e) {
            return Reply.saying().badRequest();
        }
        return Reply.saying().ok();
    }
}
