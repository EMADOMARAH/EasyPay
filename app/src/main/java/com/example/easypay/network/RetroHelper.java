package com.example.easypay.network;

import com.example.easypay.models.AvailableTrainsModel;
import com.example.easypay.models.BalanceModel;
import com.example.easypay.models.BusHistoryModel;
import com.example.easypay.models.BusTicketModel;
import com.example.easypay.models.CreditCheckModel;
import com.example.easypay.models.GetSettingModel;
import com.example.easypay.models.LineModel;
import com.example.easypay.models.MetroHistoryModel;
import com.example.easypay.models.MetroTicketModel;
import com.example.easypay.models.SetSettingModel;
import com.example.easypay.models.StationModel;
import com.example.easypay.models.TokenModel;
import com.example.easypay.models.TrainHistoryModel;
import com.example.easypay.models.TrainTicketModel;
import com.example.easypay.models.WalletModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetroHelper {

    @POST("signup.php")
    Call<List<TokenModel>> signup(
            @Query("php_name") String name,
            @Query("php_email") String email,
            @Query("php_password") String pass
    );

    @POST("login.php")
    Call<List<TokenModel>> login(
            @Query("php_email") String email,
            @Query("php_password") String pass
    );

    @POST("balance.php")
    Call<List<BalanceModel>> getBalance(
            @Query("php_id") int id
    );

    @POST("credit_check.php")
    Call<List<CreditCheckModel>> checkCredits(
            @Query("php_id") int id
    );

    @POST("credits.php")
    Call<Void> setCredits(
            @Query("php_id") int id,
            @Query("php_card_number") int cardNumber,
            @Query("php_cvv") int cvvKey,
            @Query("php_month") int month,
            @Query("php_year") int year,
            @Query("php_holder_name") String holderName,
            @Query("php_amount") int amount,
            @Query("php_method") String method
    );

    @POST("settingin.php")
    Call<List<GetSettingModel>> getSetting(
            @Query("php_id") int id
    );

    @POST("settingout.php")
    Call<List<SetSettingModel>> setSetting(
            @Query("php_id") int id,
            @Query("php_password") String password,
            @Query("php_name") String name
    );

    @POST("show.php")
    Call<Void> show(
            @Query("php_id") int id
    );

    @POST("wallet.php")
    Call<List<WalletModel>> getWallet(
            @Query("php_id") int id
    );

    @POST("bus_history.php")
    Call<List<BusHistoryModel>> getBusHistory(
            @Query("php_id") int id
    );

    @POST("bus_ticket.php")
    Call<List<BusTicketModel>> getBusTicket(
            @Query("php_id") int id
    );

    @POST("metro_history.php")
    Call<List<MetroHistoryModel>> getMetroHistory(
            @Query("php_id") int id
    );

    @POST("metro_ticket.php")
    Call<List<MetroTicketModel>> getMetroTicket(
            @Query("php_id") int id
    );

    @POST("train_history.php")
    Call<List<TrainHistoryModel>> getTrainHistory(
            @Query("php_id") int id
    );

    @POST("train_ticket.php")
    Call<List<TrainTicketModel>> getTrainTicket(
            @Query("php_id") int id
    );

    @POST("select_line.php")
    Call<List<LineModel>> getLines();

    @POST("select_station.php")
    Call<List<StationModel>> getStations(
            @Query("php_line") String lineName
    );

    @POST("avaliable_trains.php")
    Call<List<AvailableTrainsModel>> getAvailableTrains(
            @Query("php_start") String from,
            @Query("php_end") String to
    );

    @POST("saveTrainTicket.php")
    Call<Void> saveTrainTicket(
            @Query("php_id") int id,
            @Query("php_reserve_from") String from,
            @Query("php_reserve_to") String to,
            @Query("php_ticket_time") String time,
            @Query("php_quantity") int quantity
    );
}
