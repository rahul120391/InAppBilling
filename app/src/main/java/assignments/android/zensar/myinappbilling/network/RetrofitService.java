package assignments.android.zensar.myinappbilling.network;

import java.util.HashMap;

import assignments.android.zensar.myinappbilling.model.AddPaymentSubscriptionResponse;
import assignments.android.zensar.myinappbilling.model.ProductList;
import assignments.android.zensar.myinappbilling.model.UserDetailResponse;
import assignments.android.zensar.myinappbilling.model.ZenVerseLoginResponse;
import assignments.android.zensar.myinappbilling.utility.Constants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * Created by RK51670 on 05-02-2018.
 */
public interface RetrofitService {

    @POST
    Call<ZenVerseLoginResponse> loginUser(@Url String url, @HeaderMap HashMap<String,String> map);

    @FormUrlEncoded
    @POST(Constants.SAVE_USER_DETAILS)
    Call<UserDetailResponse> saveUserDeatils(@FieldMap HashMap<String,String> body);

    @GET(Constants.GET_USER_PRODUCTS)
    Call<ProductList> getProducts(@Query("userId") String userId);

    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    @FormUrlEncoded
    @POST(Constants.SUBSCRIBE_PRODUCT)
    Call<AddPaymentSubscriptionResponse> savePaymentDetails(@FieldMap HashMap<String,String> body);

}