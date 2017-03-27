package in.co.opensoftlab.yourstoreadmin.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import in.co.opensoftlab.yourstoreadmin.R;
import in.co.opensoftlab.yourstoreadmin.activity.ProductDescription;
import in.co.opensoftlab.yourstoreadmin.adapter.ListingAdapter;
import in.co.opensoftlab.yourstoreadmin.model.ListingItem;
import in.co.opensoftlab.yourstoreadmin.model.ProductModel;

/**
 * Created by dewangankisslove on 19-12-2016.
 */

public class ListingFragment extends Fragment implements View.OnClickListener {
    RelativeLayout userInfo;
    RecyclerView recyclerView;

    Query query;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mGeo;
    private GeoFire geoFire;
    private GeoFire geoFireAll;

    List<ListingItem> listingItems = new ArrayList<>();
    List<ProductModel> productModels = new ArrayList<>();
    ListingAdapter listingAdapter;
    LinearLayoutManager linearLayoutManager;
    ValueEventListener valueEventListener;
    AVLoadingIndicatorView loadingIndicatorView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("products").child("all");
        mGeo = FirebaseDatabase.getInstance().getReference("products").child("geoData");
        query = mDatabase.child("live").orderByChild("assuredProduct").equalTo(0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listings, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isNetworkConnected()) {
            if (query != null)
                query.removeEventListener(valueEventListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", "onResume");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userInfo.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    listingItems.clear();
                    productModels.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        ProductModel products = postSnapshot.getValue(ProductModel.class);
                        if (products.getProductType().contentEquals("Car")) {
                            ListingItem listingItem = new ListingItem(products.getProductType(), postSnapshot.getKey(),
                                    products.getProductUrls().split("::")[0], products.getProductName(), products.getPrice(),
                                    products.getFuelType() + " - " + products.getDrivenDistance() + " kms - " + products.getModelYear());
                            listingItems.add(listingItem);
                        } else {
                            ListingItem listingItem = new ListingItem(products.getProductType(), postSnapshot.getKey(),
                                    products.getProductUrls().split("::")[0], products.getProductName(), products.getPrice(),
                                    products.getDrivenDistance() + " kms - " + products.getModelYear());
                            listingItems.add(listingItem);
                        }
                        productModels.add(products);
                    }
                    listingAdapter.notifyDataSetChanged();
                } else {
                    userInfo.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
//                swipeContainer.setRefreshing(false);
                loadingIndicatorView.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("dbError", databaseError.getMessage());
//                swipeContainer.setRefreshing(false);
                loadingIndicatorView.hide();
            }
        };
        if (isNetworkConnected())
            query.addListenerForSingleValueEvent(valueEventListener);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initUI(View view) {
        userInfo = (RelativeLayout) view.findViewById(R.id.rl_info);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        loadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.avi);

        loadingIndicatorView.smoothToShow();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        listingAdapter = new ListingAdapter(getActivity().getApplicationContext(), listingItems);
        recyclerView.setAdapter(listingAdapter);
        listingAdapter.setOnItemClickListener(new ListingAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (v.getId() == R.id.iv_more) {
                    showPopup(v, position);
                } else {

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                return;
        }
    }

    private void showPopup(View view, final int position) {
        Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.PopupMenu);
        final PopupMenu popup = new PopupMenu(wrapper, view);
        popup.getMenuInflater().inflate(R.menu.listing_popup, popup.getMenu());
        popup.getMenu().findItem(R.id.view_as_user).setTitle(Html.fromHtml("<font color='#000000'>View as Buyer"));
        popup.getMenu().findItem(R.id.approve).setTitle(Html.fromHtml("<font color='#000000'>Approve"));
        popup.getMenu().findItem(R.id.reject).setTitle(Html.fromHtml("<font color='#000000'>Reject"));
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.view_as_user) {
                    lookProduct(listingItems.get(position).getId(), listingItems.get(position).getProductType());
                } else if (item.getItemId() == R.id.reject) {
                    final AlertDialog.Builder alertDiallogBuilder = new AlertDialog.Builder(getContext());
                    alertDiallogBuilder.setTitle(Html.fromHtml("<font color='#212121'><b>Alert"));
                    alertDiallogBuilder.setMessage(Html.fromHtml("<font color=\"#424242\">Are you sure want to reject this item?</font>"));
                    alertDiallogBuilder.setPositiveButton(Html.fromHtml("<font color=\"#212121\"><b>Yes</b></font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            geoFire = new GeoFire(mGeo.child("live").child("All"));
                            geoFireAll = new GeoFire(mGeo.child("live").child(listingItems.get(position).getProductType()));

                            ProductModel model = productModels.get(position);
                            mDatabase.child("inactive").child(listingItems.get(position).getId()).setValue(model);
                            mDatabase.child("live").child(listingItems.get(position).getId()).setValue(null);

//                            double placeLat = Double.parseDouble(productModels.get(position).getGeoLocation().split("::")[0]);
//                            double placeLng = Double.parseDouble(productModels.get(position).getGeoLocation().split("::")[1]);

//                            geoFire.setLocation(listingItems.get(position).getId(),
//                                    new GeoLocation(placeLat, placeLng));
//                            geoFireAll.setLocation(listingItems.get(position).getId(),
//                                    new GeoLocation(placeLat, placeLng));

                            geoFire.removeLocation(listingItems.get(position).getId());
                            geoFireAll.removeLocation(listingItems.get(position).getId());

                            listingItems.remove(position);
                            listingAdapter.notifyDataSetChanged();

                            dialog.dismiss();
                        }
                    });
                    alertDiallogBuilder.setNegativeButton(Html.fromHtml("<font color=\"#212121\"><b>No</b></font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final AlertDialog alertDialog = alertDiallogBuilder.create();
                    alertDialog.show();
                } else if (item.getItemId() == R.id.approve) {
                    final AlertDialog.Builder alertDiallogBuilder = new AlertDialog.Builder(getContext());
                    alertDiallogBuilder.setTitle(Html.fromHtml("<font color='#212121'><b>Alert"));
                    alertDiallogBuilder.setMessage(Html.fromHtml("<font color=\"#424242\">Are you sure want to approve this item?</font>"));
                    alertDiallogBuilder.setPositiveButton(Html.fromHtml("<font color=\"#212121\"><b>Yes</b></font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            mDatabase.child("live").child(listingItems.get(position).getId()).runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {

                                    ProductModel p = mutableData.getValue(ProductModel.class);
                                    if (p == null) {
                                        return Transaction.success(mutableData);
                                    }

                                    p.setAssuredProduct(1);

                                    mutableData.setValue(p);
                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                                }
                            });

                            listingItems.remove(position);
                            listingAdapter.notifyDataSetChanged();

                            dialog.dismiss();
                        }
                    });
                    alertDiallogBuilder.setNegativeButton(Html.fromHtml("<font color=\"#212121\"><b>No</b></font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final AlertDialog alertDialog = alertDiallogBuilder.create();
                    alertDialog.show();
                }
                popup.dismiss();
                return true;
            }
        });
        popup.show();
    }

    private void lookProduct(String productId, String productType) {
        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);
        bundle.putString("productType", productType);
        Intent intent = new Intent(getActivity().getApplicationContext(), ProductDescription.class);
        intent.putExtra("lookProduct", bundle);
        startActivity(intent);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
