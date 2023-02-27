package cmput.app.catch_me_if_you_scan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ScanningManager {
    Button scan_button;
    private String message;
    private Context context;
    private ActivityResultLauncher<ScanOptions> barLauncher;

//    public ScanningManager(Context context) {
//        this.context = context;
//        this.barLauncher = registerForActivityResult(new ScanContract(), result ->
//        {
//            // If the capture was successfull. We take them to the Submission Activity
//            if ((result.getContents() != null))
//            {
//                message = result.getContents();
//
//                Intent intent = new Intent((Activity) context, SubmissionActivity.class);
//                intent.putExtra("Code name", message);
//
//                context.startActivity(intent);
//            }
//        });
//    }

    public void scanCode() {

        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
}
