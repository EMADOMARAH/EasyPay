package com.example.easypay.ui.services.metro.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easypay.R;
import com.example.easypay.models.MetroHistoryModel;

import java.util.List;

public class MetroHistoryAdapter extends RecyclerView.Adapter<MetroHistoryAdapter.VH> {

    private Context context;
    private List<MetroHistoryModel> metroHistoryModelList;
    private MetroHistoryListener listener;

    public MetroHistoryAdapter(List<MetroHistoryModel> metroHistoryModelList, MetroHistoryListener listener) {
        this.metroHistoryModelList = metroHistoryModelList;
        this.listener = listener;
    }

    public List<MetroHistoryModel> getMetroHistoryModelList() {
        return metroHistoryModelList;
    }

    public void setMetroHistoryModelList(List<MetroHistoryModel> busHistoryModelList) {
        this.metroHistoryModelList = busHistoryModelList;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new VH(LayoutInflater.from(context).inflate(R.layout.item_metro_history, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MetroHistoryModel metroHistoryModel = metroHistoryModelList.get(position);

        holder.number.setText(metroHistoryModel.getStartStation() + " > " + metroHistoryModel.getEndStation());
        holder.date.setText(metroHistoryModel.getTicketDate());
    }

    @Override
    public int getItemCount() {
        return metroHistoryModelList.size();
    }

    interface MetroHistoryListener {
        void onItemSelected(int position);
    }

    class VH extends RecyclerView.ViewHolder {
        TextView number, date;

        VH(@NonNull View itemView, final MetroHistoryListener listener) {
            super(itemView);

            number = itemView.findViewById(R.id.line);
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
