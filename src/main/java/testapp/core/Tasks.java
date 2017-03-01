package testapp.core;

import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;

public class Tasks {

    public TaskOptions sendEmailToAdminTask() {
        return TaskOptions.Builder
                .withUrl("/sendMail")
                .method(TaskOptions.Method.GET)
                .retryOptions(oneTry());
    }

    private RetryOptions oneTry() {
        return RetryOptions.Builder.withDefaults().taskRetryLimit(0);
    }
}
