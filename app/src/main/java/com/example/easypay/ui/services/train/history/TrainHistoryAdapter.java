package com.example.easypay.ui.services.train.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easypay.R;
import com.example.easypay.models.TrainHistoryModel;

import java.util.List;

public class TrainHistoryAdapter extends RecyclerView.Adapter<TrainHistoryAdapter.VH> {

    private Context context;
    private List<TrainHistoryModel> trainHistoryModelList;
    private TrainHistoryListener listener;

    public TrainHistoryAdapter(List<TrainHistoryModel> trainHistoryModelList, TrainHistoryListener listener) {
        this.trainHistoryModelList = trainHistoryModelList;
        this.listener = listener;
    }

    public List<TrainHistoryModel> getTrainHistoryModelList() {
        return trainHistoryModelList;
    }

    public void setTrainHistoryModelList(List<TrainHistoryModel> busHistoryModelList) {
        this.trainHistoryModelList = busHistoryModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new VH(LayoutInflater.from(context).inflate(R.layout.item_train_history, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        TrainHistoryModel trainHistoryModel = trainHistoryModelList.get(position);

        holder.number.setText(trainHistoryModel.getStartStation() + " > " + trainHistoryModel.getEndStation());
        holder.date.setText(trainHistoryModel.getReserveTime());
    }

    @Override
    public int getItemCount() {
        return trainHistoryModelList.size();
    }

    interface TrainHistoryListener {
        void onItemSelected(int position);
    }

    class VH extends RecyclerView.ViewHolder {
        TextView number, date;

        VH(@NonNull View itemView, final TrainHistoryListener listener) {
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
