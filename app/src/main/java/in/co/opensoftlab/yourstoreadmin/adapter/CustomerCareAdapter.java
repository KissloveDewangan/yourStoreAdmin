package in.co.opensoftlab.yourstoreadmin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.co.opensoftlab.yourstoreadmin.R;
import in.co.opensoftlab.yourstoreadmin.model.CareModel;

/**
 * Created by dewangankisslove on 22-03-2017.
 */

public class CustomerCareAdapter extends RecyclerView
        .Adapter<CustomerCareAdapter
        .DataObjectHolder> {
    private List<CareModel> filterProducts = new ArrayList<>();
    private static Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView email, type, msg;

        public DataObjectHolder(View itemView) {
            super(itemView);
            email = (TextView) itemView.findViewById(R.id.email);
            type = (TextView) itemView.findViewById(R.id.type);
            msg = (TextView) itemView.findViewById(R.id.msg);
        }
    }

    public CustomerCareAdapter(Context context, List<CareModel> filterProducts) {
        this.context = context;
        this.filterProducts = filterProducts;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.care_row_item, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.email.setText(filterProducts.get(position).getEmail());
        holder.type.setText(filterProducts.get(position).getType());
        holder.msg.setText(filterProducts.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return filterProducts.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}
