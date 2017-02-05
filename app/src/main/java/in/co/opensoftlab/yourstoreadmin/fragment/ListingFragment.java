package in.co.opensoftlab.yourstoreadmin.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    RelativeLayout listings;
//    RelativeLayout search;
    RecyclerView recyclerView;
//    ImageView productSearch;
//    ImageView cancelSearch;
//    EditText searchName;
//    TextView searchHeader;

    private SwipeRefreshLayout swipeContainer;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    List<ListingItem> listingItems = new ArrayList<>();
    List<ProductModel> productModels = new ArrayList<>();
    ListingAdapter listingAdapter;
    LinearLayoutManager linearLayoutManager;
    ValueEventListener valueEventListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("products");
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
            if (mDatabase != null)
                mDatabase.removeEventListener(valueEventListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", "onResume");
        listings.setVisibility(View.VISIBLE);
//        search.setVisibility(View.GONE);
//        searchHeader.setVisibility(View.GONE);

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
                        ListingItem listingItem = new ListingItem(postSnapshot.getKey(), products.getProductUrls().split("::")[0], products.getProductName(), products.getSellingPrice(), products.getQty());
                        listingItems.add(listingItem);
                        productModels.add(products);
                    }
                    listingAdapter = new ListingAdapter(getActivity().getApplicationContext(), listingItems);
                    recyclerView.setAdapter(listingAdapter);
                    //listingAdapter.notifyDataSetChanged();
                    listingAdapter.setOnItemClickListener(new ListingAdapter.MyClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            if (v.getId() == R.id.iv_more) {
                                showPopup(v, position);
                            } else {

                            }
                        }
                    });
                } else {
                    userInfo.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("dbError", databaseError.getMessage());
                swipeContainer.setRefreshing(false);
            }
        };
        if (isNetworkConnected())
            mDatabase.child("inactive").addListenerForSingleValueEvent(valueEventListener);
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
//        productSearch = (ImageView) view.findViewById(R.id.iv_search);
        listings = (RelativeLayout) view.findViewById(R.id.rl_listings);
//        search = (RelativeLayout) view.findViewById(R.id.rl_search);
//        searchName = (EditText) view.findViewById(R.id.et_search);
//        cancelSearch = (ImageView) view.findViewById(R.id.iv_close);
//        searchHeader = (TextView) view.findViewById(R.id.tv_search);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

//        productSearch.setOnClickListener(this);
//        cancelSearch.setOnClickListener(this);
//
//        searchName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                String watchable = editable.toString();
//                listingAdapter.filter(watchable);
//
//            }
//        });
//
//        searchName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
//                    InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm2.hideSoftInputFromWindow(searchName.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
//                    searchHeader.setVisibility(View.VISIBLE);
//                    searchName.setVisibility(View.GONE);
//                    return true;
//                }
//                return false;
//            }
//        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                mDatabase.child("inactive").addListenerForSingleValueEvent(valueEventListener);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.iv_search:
//                if (isNetworkConnected()) {
//                    listings.setVisibility(View.GONE);
//                    search.setVisibility(View.VISIBLE);
//                    searchName.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(searchName, InputMethodManager.SHOW_IMPLICIT);
//                }
//                break;
//            case R.id.iv_close:
//                searchHeader.setVisibility(View.GONE);
//                searchName.setVisibility(View.VISIBLE);
//                searchName.setText("");
//                search.setVisibility(View.GONE);
//                listings.setVisibility(View.VISIBLE);
//                InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm2.hideSoftInputFromWindow(searchName.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
//                break;
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
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.view_as_user) {
                    lookProduct(listingItems.get(position).getId());
                } else if (item.getItemId() == R.id.approve) {
                    ProductModel model = productModels.get(position);
                    mDatabase.child("live").child(listingItems.get(position).getId()).setValue(model);
                    mDatabase.child("inactive").child(listingItems.get(position).getId()).setValue(null);
                    listingItems.remove(position);
                    listingAdapter = new ListingAdapter(getActivity().getApplicationContext(), listingItems);
                    recyclerView.setAdapter(listingAdapter);
                }
                popup.dismiss();
                return true;
            }
        });
        popup.show();
    }

    private void lookProduct(String productId) {
        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);
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
