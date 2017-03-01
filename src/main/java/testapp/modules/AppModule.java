package testapp.modules;

import com.google.inject.Scopes;
import com.google.sitebricks.SitebricksModule;
import testapp.adapter.http.MailService;
import testapp.adapter.http.UserAccountService;
import testapp.adapter.http.UserService;
import testapp.adapter.memory.InMemoryUserRepository;
import testapp.core.UserRepository;

public class AppModule extends SitebricksModule {

    @Override
    protected void configureSitebricks() {
        at("/users").serve(UserService.class);
        at("/sendMail").serve(MailService.class);
        at("/accounts").serve(UserAccountService.class);

        bind(UserRepository.class).to(InMemoryUserRepository.class).in(Scopes.SINGLETON);
    }
}
