package cmput.app.catch_me_if_you_scan;

import android.provider.Settings;
import android.content.Context;
import android.provider.Settings;
import android.content.ContentResolver;

public class TestDetails {

    private String testUser = "TestAccount912384";
    private String testEmail = "AccountEmail@example.com";
    private String deviceId;
    private Context context;
    private static TestDetails testDetails;

    private TestDetails() {
    }

    public static synchronized TestDetails getInstance(Context context) {
        if (testDetails == null) {
            testDetails = new TestDetails();
            testDetails.init(context);
        }
        return testDetails;
    }

    private void init(Context context) {
        this.context = context;
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    // getters and setters for testUser, testEmail, and deviceId
    public String getDeviceId(){
        return deviceId;
    }
    public String getTestUser(){
        return testUser;
    }
    public String getTestEmail(){
        return testEmail;
    }
}
