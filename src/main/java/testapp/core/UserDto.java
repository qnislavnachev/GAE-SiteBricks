package testapp.core;

import java.util.ArrayList;
import java.util.List;

public class UserDto {
    private long ucn;
    private String name;

    public UserDto() {
    }

    public UserDto(long ucn, String name) {
        this.ucn = ucn;
        this.name = name;
    }

    public long getUcn() {
        return ucn;
    }

    public String getName() {
        return name;
    }

    public User toUser() {
        return new User(ucn, name);
    }

    public static UserDto fromUser(User user) {
        return new UserDto(user.getUcn(), user.getName());
    }

    public static List<UserDto> fromUsers(List<User> userList) {
        List<UserDto> userDtos = new ArrayList<>();
        for(User each : userList) {
            userDtos.add(fromUser(each));
        }
        return userDtos;
    }
}
