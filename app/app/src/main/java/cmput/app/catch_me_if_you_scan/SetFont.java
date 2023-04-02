package cmput.app.catch_me_if_you_scan;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;

public class SetFont
        extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), String.valueOf(Typeface.DEFAULT), "res/font/alata.xml"); // font from assets: "assets/fonts/Roboto-Regular.ttf
    }
}

