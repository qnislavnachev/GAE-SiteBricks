package testapp.matchers;

import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.sitebricks.headless.Reply;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import testapp.core.JsonBuilder;

import java.lang.reflect.Field;

/**
 * @author Borislav Gadjev <gadjevb@gmail.com>
 */
public class SitebricksMatchers {

    public static Matcher<Reply<?>> containsJson(final JsonBuilder content) {
        return new TypeSafeMatcher<Reply<?>>() {
            @Override
            protected boolean matchesSafely(Reply<?> item) {
                Gson gson = new Gson();
                Object value = property("entity", item);
                String jsonContent = gson.toJson(value);

                return jsonContent.equalsIgnoreCase(content.build());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(content.build());
            }

            @Override
            protected void describeMismatchSafely(Reply<?> item, Description mismatchDescription) {
                String jsonContent = asJsonContent(item);
                mismatchDescription.appendText("was ");
                mismatchDescription.appendText(jsonContent);
            }

            private String asJsonContent(Reply<?> reply) {
                Gson gson = new Gson();
                Object value = property("entity", reply);
                String jsonContent = gson.toJson(value);

                return jsonContent;
            }
        };
    }

    public static Matcher<Reply<?>> statusIs(final int status) {
        return new TypeSafeMatcher<Reply<?>>() {
            @Override
            protected boolean matchesSafely(Reply<?> item) {
                Reply<?> reply = Reply.saying().status(status);
                return item.equals(reply);
            }

            @Override
            protected void describeMismatchSafely(Reply<?> item, Description mismatchDescription) {
                mismatchDescription.appendText("statusIs ");
                mismatchDescription.appendValue(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("expect to be ");
                description.appendValue(status);
            }
        };
    }

    public static Matcher<Reply<?>> containsValue(final int value) {
        return new TypeSafeMatcher<Reply<?>>() {
            @Override
            protected boolean matchesSafely(Reply<?> item) {
                int replyValue = (int) property("entity", item);
                return replyValue == value;
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    private static Object property(String fieldName, Reply<?> reply) {
        try {
            Field field = reply.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(reply);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Reply has no entity information");
        }
    }
}
