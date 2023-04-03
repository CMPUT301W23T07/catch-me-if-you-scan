package cmput.app.catch_me_if_you_scan;

import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class MyTestRunListener extends RunListener {
    @Override
    public void testRunFinished(Result result) throws Exception {
        // code to run after all test classes have completed
    }
}