package com.olympics.easypay.ui.services.bus.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olympics.easypay.R;
import com.olympics.easypay.models.BusHistoryModel;

import java.util.List;

public class BusHistoryAdapter extends RecyclerView.Adapter<BusHistoryAdapter.VH> {

    private Context context;
    private List<BusHistoryModel> busHistoryModelList;
    private BusHistoryListener listener;

    public BusHistoryAdapter(List<BusHistoryModel> busHistoryModelList, BusHistoryListener listener) {
        this.busHistoryModelList = busHistoryModelList;
        this.listener = listener;
    }

    public List<BusHistoryModel> getBusHistoryModelList() {
        return busHistoryModelList;
    }

    public void setBusHistoryModelList(List<BusHistoryModel> busHistoryModelList) {
        this.busHistoryModelList = busHistoryModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new VH(LayoutInflater.from(context).inflate(R.layout.item_bus_history, parent, false), listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        BusHistoryModel busHistoryModel = busHistoryModelList.get(position);

        holder.number.setText(busHistoryModel.getTicketNumber() + "");
        holder.date.setText(busHistoryModel.getTicketDate() + "");
    }

    @Override
    public int getItemCount() {
        return busHistoryModelList.size();
    }

    interface BusHistoryListener {
        void onItemSelected(int position);
    }

    class VH extends RecyclerView.ViewHolder {
        TextView number, date;

        VH(@NonNull View itemView, final BusHistoryListener listener) {
            super(itemView);

            number = itemView.findViewById(R.id.lineNumber);
            date = itemView.findViewById(R.id.payDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemSelected(getAdapterPosition());
                }
            });
        }
    }
}
