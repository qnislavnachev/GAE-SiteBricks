package testapp.adapter.http;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.sitebricks.At;
import com.google.sitebricks.headless.Request;
import com.google.sitebricks.http.Get;
import testapp.core.SendGrid;

@At("/sendMail")
public class MailService {

    @Get
    public void sendMail(Request request) {
        try {

            new SendGrid("qnislav.nachev", "qwerty123123")
                    .setTo("qnislav.nachev@gmail.com")
                    .setFrom("iani_ty2@abv.bg")
                    .setSubject("User was registered")
                    .setText("New User was registered to your App !")
                    .send();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
