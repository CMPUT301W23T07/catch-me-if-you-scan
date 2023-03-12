package cmput.app.catch_me_if_you_scan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScanningManager {
    private Button scan_button;
    private String message;
    private ComponentActivity activity;

    private ActivityResultLauncher<ScanOptions> barLauncher;

    public ScanningManager(ComponentActivity activity) {
        this.activity = activity;

        this.barLauncher = activity.registerForActivityResult(new ScanContract(), result ->
        {
            // If the capture was successful. We take them to the Submission Activity
            if ((result.getContents() != null))
            {
                message = result.getContents();

                Intent intent = new Intent(activity, SubmissionActivity.class);
                intent.putExtra("Code name", message);

                activity.startActivity(intent);
            }
        });
    }
    public void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
}
