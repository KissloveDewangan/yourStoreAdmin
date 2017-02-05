package in.co.opensoftlab.yourstoreadmin.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.opensoftlab.yourstoreadmin.CirclePageIndicator;
import in.co.opensoftlab.yourstoreadmin.PageIndicator;
import in.co.opensoftlab.yourstoreadmin.R;
import in.co.opensoftlab.yourstoreadmin.adapter.FeaturesAdapter;
import in.co.opensoftlab.yourstoreadmin.model.ProductModel;
import in.co.opensoftlab.yourstoreadmin.model.SpecificationItem;
import in.co.opensoftlab.yourstoreadmin.adapter.ImageSlideAdapter;
import in.co.opensoftlab.yourstoreadmin.adapter.SpecificationListAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by dewangankisslove on 08-12-2016.
 */

public class ProductDescription extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private Circle circle;

    private static final long ANIM_VIEWPAGER_DELAY = 2000;
    private static final long ANIM_VIEWPAGER_DELAY_USER_VIEW = 3000;

    boolean stopSliding = false;
    private Runnable animateViewPager;
    private Handler handler;

    //ImageView productImg;
    ViewPager viewPager;
    PageIndicator pageIndicator;
    ImageView back;
    ImageView share;
    ImageView favourite;
    TextView productName;
    TextView productPrice;
    TextView mrp;
    TextView discount;
    CircleImageView sellerImg;
    TextView sellerName;
    TextView storeName;
    RecyclerView features;
    RecyclerView recyclerView;
    Button contactSeller;

    List<String> productUrls = new ArrayList<>();
    ImageSlideAdapter imageSlideAdapter;

    LinearLayoutManager linearLayoutManager;
    List<SpecificationItem> featureItems = new ArrayList<>();
    SpecificationListAdapter specificationListAdapter;

    LinearLayoutManager layoutManager;
    List<String> listFeatures = new ArrayList<>();
    FeaturesAdapter featuresAdapter;

    Bundle bundle = null;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
        initUI();

        mAuth = FirebaseAuth.getInstance();

        bundle = getIntent().getExtras().getBundle("lookProduct");
        if (bundle != null) {
            Log.d("readData", bundle.getString("productId"));
            mDatabase = FirebaseDatabase.getInstance().getReference("products").child("inactive");

            valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("readData", dataSnapshot.toString());
                    if (dataSnapshot.exists()) {
                        ProductModel products = dataSnapshot.getValue(ProductModel.class);

                        productName.setText(products.getProductName());
                        productPrice.setText("" + products.getSellingPrice());
                        mrp.setText("MRP-" + products.getMrp());
                        discount.setText("" + products.getDiscount()+"%");
                        sellerName.setText(products.getSellerName());
                        Picasso.with(getApplicationContext())
                                .load(products.getSellerUrl())
                                .into(sellerImg);
                        storeName.setText(products.getStoreName());

                        for (int i = 0; i < products.getProductUrls().split("::").length; i++) {
                            String url = products.getProductUrls().split("::")[i];
                            productUrls.add(url);
                        }
                        imageSlideAdapter.notifyDataSetChanged();
                        pageIndicator.notifyDataSetChanged();

                        if (productUrls != null && productUrls.size() != 0) {
                            runnable(productUrls.size());
                            handler.postDelayed(animateViewPager,
                                    ANIM_VIEWPAGER_DELAY);
                        }

                        for (int i = 0; i < products.getProductFeatures().split("::").length; i++) {
                            String s = products.getProductFeatures().split("::")[i];
                            listFeatures.add(s);
                        }
                        featuresAdapter.notifyDataSetChanged();

                        Log.d("specificationLength", String.valueOf(products.getProductSpecifications().split(";;").length));
                        for (int j = 0; j < products.getProductSpecifications().split(";;").length; j++) {
                            SpecificationItem featureItem = new SpecificationItem(products.getProductSpecifications().split(";;")[j].split("::")[0], products.getProductSpecifications().split(";;")[j].split("::")[1]);
                            featureItems.add(featureItem);
                        }
                        specificationListAdapter.notifyDataSetChanged();

                        float lat = Float.parseFloat(products.getGeoLocation().split("::")[0]);
                        float lon = Float.parseFloat(products.getGeoLocation().split("::")[1]);

                        circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(lat, lon))
                                .radius(250)
                                .strokeWidth(0)
                                .fillColor(Color.argb(40, 255, 0, 0))
                                .clickable(true));

                        // Add a marker in Sydney, Australia, and move the camera.
                        LatLng place = new LatLng(lat, lon);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);
                        mMap.setMinZoomPreference(12);
                        Marker melbourne = mMap.addMarker(new MarkerOptions()
                                .position(place)
                                .title(products.getAddress()));
                        melbourne.showInfoWindow();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mDatabase.child(bundle.getString("productId")).addListenerForSingleValueEvent(valueEventListener);
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initUI() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        pageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        //productImg = (ImageView) findViewById(R.id.product_img);
        back = (ImageView) findViewById(R.id.action_back);
        share = (ImageView) findViewById(R.id.action_share);
        favourite = (ImageView) findViewById(R.id.action_favorite);
        productName = (TextView) findViewById(R.id.tv_product_name);
        productPrice = (TextView) findViewById(R.id.tv_product_price);
        mrp = (TextView) findViewById(R.id.tv_mrp);
        discount = (TextView) findViewById(R.id.tv_discount);
        mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        sellerImg = (CircleImageView) findViewById(R.id.seller_image);
        sellerName = (TextView) findViewById(R.id.tv_seller_name);
        storeName = (TextView) findViewById(R.id.tv_store_name);
        features = (RecyclerView) findViewById(R.id.recyclerFeatures);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        contactSeller = (Button) findViewById(R.id.b_contact);

        imageSlideAdapter = new ImageSlideAdapter(getApplicationContext(), productUrls);
        viewPager.setAdapter(imageSlideAdapter);
        pageIndicator.setViewPager(viewPager);
        viewPager.setOnPageChangeListener(new PageChangeListener());
        pageIndicator.setOnPageChangeListener(new PageChangeListener());

        viewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction()) {

                    case MotionEvent.ACTION_CANCEL:
                        break;

                    case MotionEvent.ACTION_UP:
                        // calls when touch release on ViewPager
                        if (productUrls != null && productUrls.size() != 0) {
                            stopSliding = false;
                            runnable(productUrls.size());
                            handler.postDelayed(animateViewPager,
                                    ANIM_VIEWPAGER_DELAY_USER_VIEW);
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        // calls when ViewPager touch
                        if (handler != null && stopSliding == false) {
                            stopSliding = true;
                            handler.removeCallbacks(animateViewPager);
                        }
                        break;
                }
                return false;
            }
        });

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(ProductDescription.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        specificationListAdapter = new SpecificationListAdapter(ProductDescription.this, featureItems);
        recyclerView.setAdapter(specificationListAdapter);

        features.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ProductDescription.this, LinearLayoutManager.VERTICAL, false);
        features.setLayoutManager(layoutManager);
        featuresAdapter = new FeaturesAdapter(ProductDescription.this, listFeatures);
        features.setAdapter(featuresAdapter);

        contactSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                pageIndicator.setCurrentItem(viewPager.getCurrentItem());
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
        }
    }

    public void runnable(final int size) {
        handler = new Handler();
        animateViewPager = new Runnable() {
            public void run() {
                if (!stopSliding) {
                    if (viewPager.getCurrentItem() == size - 1) {
                        viewPager.setCurrentItem(0);
                        pageIndicator.setCurrentItem(0);
                    } else {
                        int temp = viewPager.getCurrentItem() + 1;
                        viewPager.setCurrentItem(temp, true);
                        pageIndicator.setCurrentItem(temp);
                    }
                    handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.child(bundle.getString("productId")).removeEventListener(valueEventListener);
        if (handler != null) {
            //Remove callback
            handler.removeCallbacks(animateViewPager);
        }
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.product_description_menu, menu);
//        return true;
//    }
//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                return true;

            case R.id.action_favorite:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {

            @Override
            public void onCircleClick(Circle circle) {
                // Flip the r, g and b components of the circle's
                // stroke color.
                int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                circle.setStrokeColor(strokeColor);
            }
        });

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
}
