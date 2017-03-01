package testapp.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import testapp.core.User;

import java.util.List;

public class CustomMatchers {

    public static Matcher<List<User>> containsUser(final User user) {
        return new TypeSafeMatcher<List<User>>() {
            @Override
            protected boolean matchesSafely(List<User> item) {
                for (User each : item) {
                    if (each.getUcn() == user.getUcn()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            protected void describeMismatchSafely(List<User> item, Description mismatchDescription) {
                mismatchDescription.appendText("value statusIs ");
                mismatchDescription.appendValue(null);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("contains value ");
                description.appendValue(user);
            }
        };
    }

    public static Matcher<User> sameAs(final User user) {
        return new TypeSafeMatcher<User>() {
            @Override
            protected boolean matchesSafely(User item) {
                return user.getUcn() == item.getUcn();
            }

            @Override
            protected void describeMismatchSafely(User item, Description mismatchDescription) {
                mismatchDescription.appendText("but was ");
                mismatchDescription.appendValue(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("expect to be ");
                description.appendValue(user);
            }
        };
    }
}