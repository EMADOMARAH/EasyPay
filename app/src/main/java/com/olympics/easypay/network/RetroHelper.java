package com.olympics.easypay.network;

import com.olympics.easypay.models.AvailableTrainsModel;
import com.olympics.easypay.models.BalanceModel;
import com.olympics.easypay.models.BusHistoryModel;
import com.olympics.easypay.models.BusTicketModel;
import com.olympics.easypay.models.CardNumberModel;
import com.olympics.easypay.models.ChargeHistoryModel;
import com.olympics.easypay.models.CreditCheckModel;
import com.olympics.easypay.models.EmailCheckModel;
import com.olympics.easypay.models.GetSettingModel;
import com.olympics.easypay.models.LineModel;
import com.olympics.easypay.models.MetroHistoryModel;
import com.olympics.easypay.models.MetroTicketModel;
import com.olympics.easypay.models.PasswordCheckModel;
import com.olympics.easypay.models.PhoneCheckModel;
import com.olympics.easypay.models.RetrievePasswordModel;
import com.olympics.easypay.models.SetSettingModel;
import com.olympics.easypay.models.StationModel;
import com.olympics.easypay.models.TokenModel;
import com.olympics.easypay.models.TrainCostModel;
import com.olympics.easypay.models.TrainHistoryModel;
import com.olympics.easypay.models.TrainTicketModel;
import com.olympics.easypay.models.WalletModel;

import java.math.BigInteger;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.olympics.easypay.utils.Constants.BASE_PERSONAL;
import static com.olympics.easypay.utils.Constants.BASE_QR;

public interface RetroHelper {

    @POST("signup.php")
    Call<List<TokenModel>> signup(
            @Query("php_name") String name,
            @Query("php_email") String email,
            @Query("php_password") String pass,
            @Query("php_phone_number") String phone
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
            @Query("php_card_number") BigInteger cardNumber,
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
            @Query("php_name") String name,
            @Query("php_phone_number") String phone
    );

    @POST("show.php")
    Call<Void> show(
            @Query("php_id") int id
    );

    @POST("wallet.php")
    Call<List<WalletModel>> getWallet(
            @Query("php_id") int id
    );

    @POST("charges.php")
    Call<List<ChargeHistoryModel>> getPaymentHistory(
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
            @Query("php_quantity") int quantity,
            @Query("php_avilable_train") int availableTrain,
            @Query("php_total_cost") int totalCost
    );

    @POST("show_credits.php")
    Call<List<CardNumberModel>> getMyCardNumbers(
            @Query("php_id") int id
    );

    @POST("delete_creidet_card.php")
    Call<Void> deleteCard(
            @Query("php_id") int id,
            @Query("php_card_number") BigInteger cardNo
    );

    @POST("add_credit.php")
    Call<Void> addCredit(
            @Query("php_id") int id,
            @Query("php_card_number") BigInteger cardNo,
            @Query("php_cvv") int cvvKey,
            @Query("php_month") int month,
            @Query("php_year") int year,
            @Query("php_holder_name") String holderName
    );

    @POST(BASE_QR + "bus_qr.php")
    Call<ResponseBody> getBusQR(
            @Query("php_id") int id
    );

    @POST(BASE_QR + "metro_qr.php")
    Call<ResponseBody> getMetroQR(
            @Query("php_id") int id
    );

    @POST(BASE_QR + "train_qr.php")
    Call<ResponseBody> getTrainQR(
            @Query("php_id") int id
    );

    @POST(BASE_PERSONAL + "personal_qr.php")
    Call<ResponseBody> getMyQR(
            @Query("php_id") int id
    );

    @POST("login_check_email.php")
    Call<List<EmailCheckModel>> checkEmailForLogin(
            @Query("php_email") String email
    );

    @POST("sign_check_email.php")
    Call<List<EmailCheckModel>> checkEmailForSignUp(
            @Query("php_email") String email
    );

    @POST("login_check_email_password.php")
    Call<List<PasswordCheckModel>> checkPassword(
            @Query("php_email") String email,
            @Query("php_password") String pass
    );

    @POST("sign_check_phone.php")
    Call<List<PhoneCheckModel>> checkPhone(
            @Query("php_phone_number") String phone
    );

    @POST("charge_with_amount.php")
    Call<Void> charge(
            @Query("php_id") int id,
            @Query("php_card_number") BigInteger cardNo,
            @Query("php_amount") int amount
    );

    @POST("metro_pending.php")
    Call<ResponseBody> isMetroPending(
            @Query("php_id") int id
    );

    @POST("forget_pass_signin.php")
    Call<List<RetrievePasswordModel>> retrievePassword(
            @Query("php_email") String email,
            @Query("php_phone_number") String phone
    );

    @POST("forget_pass_editpass.php")
    Call<ResponseBody> changePassword(
            @Query("php_id") int id,
            @Query("php_password") String pass,
            @Query("php_password_retype") String rePass
    );

    @POST("bus_reservation_by_qr.php")
    Call<ResponseBody> reserveBus(
            @Query("php_id") int id,
            @Query("php_bus_number") int line
    );

    @POST("metro_reservation_buy_qr.php")
    Call<ResponseBody> reserveMetro(
            @Query("php_id") int id,
            @Query("php_metro_station") int stationNumber
    );

    @POST("train_cost.php")
    Call<List<TrainCostModel>> getTrainCost(
            @Query("php_id") int id,
            @Query("php_train_number") int trainNumber
    );

    @POST("check")
    Call<ResponseBody> checkValidEmail(
            @Query("access_key") String id,
            @Query("email") String email,
            @Query("smtp") int smtp,
            @Query("format") int format
    );

    @POST("pass_ticket_train.php")
    Call<List<TrainTicketModel>> getTrainTicketByNumber(
            @Query("php_pass_ticket") int ticketNumber
    );

    @POST("pass_ticket_metro.php")
    Call<List<MetroTicketModel>> getMetroTicketByNumber(
            @Query("php_pass_ticket") int ticketNumber
    );

    @POST("pass_ticket_bus.php")
    Call<List<BusTicketModel>> getBusTicketByNumber(
            @Query("php_pass_ticket") int ticketNumber
    );

    @POST("bus_history.php")
    Call<ResponseBody> getLastBusTrip(
            @Query("php_id") int id
    );

    @POST("metro_history.php")
    Call<ResponseBody> getLastMetroTrip(
            @Query("php_id") int id
    );

    @POST("train_history.php")
    Call<ResponseBody> getLastTrainTrip(
            @Query("php_id") int id
    );

    @POST("transfer_money.php")
    Call<ResponseBody> transferCredits(
            @Query("Easypay_Account_Name") String name,
            @Query("Account_Email") String email,
            @Query("Transfer_Amount") int amount,
            @Query("php_id") int id
    );
}
