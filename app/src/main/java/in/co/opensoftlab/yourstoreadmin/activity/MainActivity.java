package in.co.opensoftlab.yourstoreadmin.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import in.co.opensoftlab.yourstoreadmin.R;
import in.co.opensoftlab.yourstoreadmin.fragment.ListingFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    FrameLayout landingLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        openUnApprovedProducts();
    }

    private void initUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        landingLayout = (FrameLayout) findViewById(R.id.landing_layout);
    }

    private void openUnApprovedProducts() {
        Fragment listingFrag = new ListingFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.landing_layout, listingFrag);
        fragmentTransaction.commit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
}
