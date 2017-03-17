package testapp.modules;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.sitebricks.SitebricksModule;
import testapp.adapter.http.UserAccountService;
import testapp.adapter.http.UserService;
import testapp.adapter.listeners.AdminNotificationListener;
import testapp.adapter.listeners.UserLogListener;
import testapp.adapter.repository.PersistentAccountRepository;
import testapp.adapter.repository.PersistentUserRepository;
import testapp.core.*;

import java.util.List;
import java.util.Set;

public class AppModule extends SitebricksModule {

    @Override
    protected void configureSitebricks() {
        Multibinder<UserListener> listeners = getListeners();
        at("/v1/users").serve(UserService.class).in(Singleton.class);
        at("/v1/accounts").serve(UserAccountService.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public AccountRepository getAccounts() {
        return new AccountValidator(new PersistentAccountRepository());
    }

    @Provides
    @Singleton
    public UserRepository get(UserListener listener) {
        return new UserValidator(new PersistentUserRepository(listener));
    }

    @Provides
    public UserListener getListener(final Set<UserListener> listeners) {
        return new UserListener() {
            @Override
            public void onRegistered(User user) {
                for (UserListener listener : listeners) {
                    listener.onRegistered(user);
                }
            }

            @Override
            public void onRegistered(List<User> userList) {
                for (UserListener listener : listeners) {
                    listener.onRegistered(userList);
                }
            }

            @Override
            public void onDeleted(List<User> userList) {
                for (UserListener listener : listeners) {
                    listener.onDeleted(userList);
                }
            }
        };
    }

    private Multibinder<UserListener> getListeners() {
        Multibinder<UserListener> listeners = Multibinder.newSetBinder(binder(), UserListener.class);
        listeners.addBinding().to(UserLogListener.class).in(Singleton.class);
        listeners.addBinding().to(AdminNotificationListener.class).in(Singleton.class);
        return listeners;
    }
}
