package in.co.opensoftlab.yourstoreadmin;

import android.app.Application;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by dewangankisslove on 06-12-2016.
 */

public class YourStoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Lato-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
