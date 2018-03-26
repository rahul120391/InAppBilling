package assignments.android.zensar.myinappbilling.network;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import assignments.android.zensar.myinappbilling.interactors.ITransferApiResult;
import assignments.android.zensar.myinappbilling.model.AddPaymentSubscriptionResponse;
import assignments.android.zensar.myinappbilling.model.ProductList;
import assignments.android.zensar.myinappbilling.model.UserDetailResponse;
import assignments.android.zensar.myinappbilling.model.ZenVerseLoginResponse;
import assignments.android.zensar.myinappbilling.utility.Constants;
import assignments.android.zensar.myinappbilling.utility.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by RK51670 on 07-02-2018.
 */
public class ApiClass {


    ITransferApiResult mTransferResult;
    RetrofitService retrofitService = null;


    public ApiClass(ITransferApiResult mITransferApiResult) {
        retrofitService = RetrofitAdapter.create();
        mTransferResult = mITransferApiResult;
    }


    public void login(HashMap<String, String> body) {
        Call<ZenVerseLoginResponse> response = retrofitService.loginUser(Constants.AUTHENTICATE_USER, body);

        response.enqueue(new Callback<ZenVerseLoginResponse>() {
            @Override
            public void onResponse(Call<ZenVerseLoginResponse> call, Response<ZenVerseLoginResponse> response) {
                mTransferResult.success(response.body());
            }

            @Override
            public void onFailure(Call<ZenVerseLoginResponse> call, Throwable t) {
                String error = Constants.ERROR;
                if (t != null && t.getMessage() != null) {
                    error = t.getMessage();
                }
                mTransferResult.failure(error);
            }
        });
    }


    public void saveUserDetails(HashMap<String, String> body) {
        Call<UserDetailResponse> response = retrofitService.saveUserDeatils(body);
        response.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(Call<UserDetailResponse> call, Response<UserDetailResponse> response) {
                mTransferResult.success(response.body());
            }

            @Override
            public void onFailure(Call<UserDetailResponse> call, Throwable t) {
                String error = Constants.ERROR;
                if (t != null && t.getMessage() != null) {
                    error = t.getMessage();
                }
                mTransferResult.failure(error);
            }
        });
    }

    public void savePaymentDetails(HashMap<String,String> params){
        Call<AddPaymentSubscriptionResponse> response=retrofitService.savePaymentDetails(params);
        response.enqueue(new Callback<AddPaymentSubscriptionResponse>() {
            @Override
            public void onResponse(Call<AddPaymentSubscriptionResponse> call, Response<AddPaymentSubscriptionResponse> response) {
                   if(response!=null){
                       if(response.isSuccessful()){
                           mTransferResult.success(response.body());
                       }
                       else{
                           mTransferResult.failure(Constants.ERROR);
                       }
                   }
            }

            @Override
            public void onFailure(Call<AddPaymentSubscriptionResponse> call, Throwable t) {
                String error = Constants.ERROR;
                if (t != null && t.getMessage() != null) {
                    error = t.getMessage();
                }
                mTransferResult.failure(error);
            }
        });

    }

    public void getUserProducts(String userId) {

        Call<ProductList> response = retrofitService.getProducts(userId);
        response.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(Call<ProductList> call, Response<ProductList> response) {
                mTransferResult.success(response.body());
            }

            @Override
            public void onFailure(Call<ProductList> call, Throwable t) {
                String error = Constants.ERROR;
                if (t != null && t.getMessage() != null) {
                    error = t.getMessage();
                }
                mTransferResult.failure(error);
            }
        });
    }

    public void downloadFile(final Context context, String downloadLink, final String title, final String extension) {
        Call<ResponseBody> body = retrofitService.downloadFile(downloadLink);
        body.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (writeFileToDisk(response.body(), title, extension)) {
                           // Utils.showToast(context, context.getString(R.string.success_complete));
                            Utils.DismissDialog();
                        } else {
                            mTransferResult.failure(Constants.DOWNLOAD_ERROR);
                            Utils.DismissDialog();
                        }
                    } else {
                        String error = "";
                        try {
                            error = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            error = e.getMessage();
                        }
                        mTransferResult.failure(error);
                    }
                } else {
                    mTransferResult.failure(Constants.DOWNLOAD_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String error = Constants.DOWNLOAD_ERROR;
                if (t != null && t.getMessage() != null) {
                    error = t.getMessage();
                }
                mTransferResult.failure(error);
            }
        });
    }

    private boolean writeFileToDisk(ResponseBody body, String title, String extension) {
        File downloadFile = new File(Environment.DIRECTORY_DOWNLOADS, title + extension);
        System.out.println("path"+downloadFile.getAbsolutePath());
        FileOutputStream outputStream = null;
        try{
            if(downloadFile.canWrite()){
                downloadFile.createNewFile();
                outputStream = new FileOutputStream(downloadFile);
                outputStream.write(body.bytes());
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return false;
        }
    }

    /**
     * Cancel running repoRequest
     */
    public void cancelRequest() {
        RetrofitAdapter.cancelRetrofitRequest();
    }


}