package cmput.app.catch_me_if_you_scan;

import android.app.Application;
import android.graphics.Typeface;

/**
 * This class is used for setting the font in the application
 */
public class SetFont
        extends Application {

    /**
     * creates the font
     */
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), String.valueOf(Typeface.DEFAULT), "res/font/alata.xml"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }
}

