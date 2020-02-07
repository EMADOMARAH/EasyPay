package com.olympics.easypay.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olympics.easypay.R;
import com.olympics.easypay.models.ChargeHistoryModel;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {

    private Context context;
    private List<ChargeHistoryModel> chargeHistoryModelList;
    private ChargeHistoryListener listener;

    public HistoryAdapter(List<ChargeHistoryModel> chargeHistoryModelList, ChargeHistoryListener listener) {
        this.chargeHistoryModelList = chargeHistoryModelList;
        this.listener = listener;
    }

    public List<ChargeHistoryModel> getChargeHistoryModelList() {
        return chargeHistoryModelList;
    }

    public void setChargeHistoryModelList(List<ChargeHistoryModel> chargeHistoryModelList) {
        this.chargeHistoryModelList = chargeHistoryModelList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new VH(LayoutInflater.from(context).inflate(R.layout.item_payment_history, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ChargeHistoryModel chargeHistoryModel = chargeHistoryModelList.get(position);

        holder.amount.setText("Charge: " + chargeHistoryModel.getChargeAmount() + " EGP");
        holder.date.setText("Charge date : " + chargeHistoryModel.getChargeDate());
        holder.method.setText("Payment method : " + chargeHistoryModel.getChargeMethod());
    }

    @Override
    public int getItemCount() {
        return chargeHistoryModelList.size();
    }

    interface ChargeHistoryListener {
        void onItemSelected(int position);
    }

    class VH extends RecyclerView.ViewHolder {
        TextView amount, date, method;

        VH(@NonNull View itemView, final ChargeHistoryListener listener) {
            super(itemView);

            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            method = itemView.findViewById(R.id.method);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemSelected(getAdapterPosition());
                }
            });
        }
    }
}
