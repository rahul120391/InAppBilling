package assignments.android.zensar.myinappbilling.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.ArrayList;
import java.util.List;

import assignments.android.zensar.myinappbilling.R;
import assignments.android.zensar.myinappbilling.interactors.ILaunchPurchaseFlow;
import assignments.android.zensar.myinappbilling.model.Datum;
import assignments.android.zensar.myinappbilling.model.MyProductList;
import assignments.android.zensar.myinappbilling.model.ProductList;
import assignments.android.zensar.myinappbilling.utility.Utils;

/**
 * Created by RK51670 on 28-02-2018.
 */

public class MyInAppProductListAdapter extends RecyclerView.Adapter<MyInAppProductListAdapter.ViewHolder> {


    List<Datum> details = new ArrayList<>();
    ILaunchPurchaseFlow mPurchaseFlow;
    Context context;
    public MyInAppProductListAdapter(ILaunchPurchaseFlow mPurchaseFlow, Context context) {
        this.mPurchaseFlow = mPurchaseFlow;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvName.setText(details.get(position).getProductName());
        holder.tvDescription.setText(details.get(position).getProductDescription());
        Utils.loadImages(context,holder.imgProduct,details.get(position).getImageUrl());
        holder.mBtnBuy.setTag(position);
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button mBtnBuy;
        AppCompatTextView tvDescription;
        AppCompatTextView tvName;
        AppCompatImageView imgProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProduct=itemView.findViewById(R.id.imgProduct);
            mBtnBuy = itemView.findViewById(R.id.btnBuy);
            tvDescription = itemView.findViewById(R.id.txtProductDescription);
            tvName = itemView.findViewById(R.id.txtProductName);
            mBtnBuy.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
             int position = (int) v.getTag();
             mPurchaseFlow.startPurchaseFlow(details.get(position).getProductId(),position);
        }
    }

    public void notifyData(List<Datum> list) {
        details = list;
        notifyDataSetChanged();
    }
}
