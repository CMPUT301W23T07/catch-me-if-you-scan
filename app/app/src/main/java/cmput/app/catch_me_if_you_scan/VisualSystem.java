package cmput.app.catch_me_if_you_scan;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class VisualSystem {
    int height;
    int width;
    Bitmap bmp;
    Canvas canvas;

    public VisualSystem(int h, int w){
        this.height =h;
        this.width = w;
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        this.bmp = Bitmap.createBitmap(w, h, conf);
        this.canvas = new Canvas(bmp);
        this.canvas.drawRGB(255,255,255);
    }


}
