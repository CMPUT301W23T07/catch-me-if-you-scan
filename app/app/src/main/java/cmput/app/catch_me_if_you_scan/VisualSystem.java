package cmput.app.catch_me_if_you_scan;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.nio.charset.StandardCharsets;

public class VisualSystem {

    private byte[] hash;

    public VisualSystem(String hash){
        this.hash = hash.getBytes(StandardCharsets.US_ASCII);
    }
    public Bitmap genVisual(int size){
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(size, size, conf);
        Canvas canvas = new Canvas(bmp);
        canvas.drawRGB(255,255,255);
        return bmp;
    }

}
