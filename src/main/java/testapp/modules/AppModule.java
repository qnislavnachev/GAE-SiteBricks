package testapp.modules;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import com.google.sitebricks.SitebricksModule;
import testapp.adapter.http.MailService;
import testapp.adapter.http.UserAccountService;
import testapp.adapter.http.UserService;
import testapp.adapter.listeners.LogUserListener;
import testapp.adapter.listeners.NotificationUserListener;
import testapp.adapter.memory.InMemoryUserRepository;
import testapp.core.User;
import testapp.core.UserListener;
import testapp.core.UserRepository;
import testapp.core.UserValidator;

import java.util.Set;

public class AppModule extends SitebricksModule {

    @Override
    protected void configureSitebricks() {
        Multibinder<UserListener> listeners = Multibinder.newSetBinder(binder(), UserListener.class);
        listeners.addBinding().to(LogUserListener.class);
        listeners.addBinding().to(NotificationUserListener.class);

        at("/users").serve(UserService.class);
        at("/sendMail").serve(MailService.class);
        at("/accounts").serve(UserAccountService.class);

//        bind(UserRepository.class).to(InMemoryUserRepository.class).in(Scopes.SINGLETON);
    }

    @Provides
    public UserRepository get(UserListener listener) {
        return new UserValidator(Lists.<User>newArrayList(), new InMemoryUserRepository(listener));
    }

    @Provides
    public UserListener getListener(final Set<UserListener> listeners) {
        return new UserListener() {
            @Override
            public void onRegister(User user) {
                for (UserListener listener : listeners) {
                    listener.onRegister(user);
                }
            }
        };
    }
}
