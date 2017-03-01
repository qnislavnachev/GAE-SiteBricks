package testapp.core;

import com.google.sitebricks.client.Transport;
import com.google.sitebricks.headless.Request;

public class FakeRequestRead implements Request.RequestRead<UserDto> {
    private UserDto user;

    public FakeRequestRead(UserDto user) {
        this.user = user;

    }

    @Override
    public UserDto as(Class<? extends Transport> transport) {
        return user;
    }
}
