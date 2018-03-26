package assignments.android.zensar.myinappbilling.activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import assignments.android.zensar.myinappbilling.adapters.MyInAppProductListAdapter;
import assignments.android.zensar.myinappbilling.interactors.ITransferApiResult;
import assignments.android.zensar.myinappbilling.model.AddPaymentSubscriptionResponse;
import assignments.android.zensar.myinappbilling.model.Datum;
import assignments.android.zensar.myinappbilling.model.ProductList;
import assignments.android.zensar.myinappbilling.model.PurchaseData;
import assignments.android.zensar.myinappbilling.R;
import assignments.android.zensar.myinappbilling.interactors.IInAppView;
import assignments.android.zensar.myinappbilling.interactors.ILaunchPurchaseFlow;
import assignments.android.zensar.myinappbilling.model.UserDatum;
import assignments.android.zensar.myinappbilling.network.ApiClass;
import assignments.android.zensar.myinappbilling.utility.Constants;
import assignments.android.zensar.myinappbilling.utility.Utils;

public class InAppProductsActivity extends AppCompatActivity
        implements IInAppView, PurchasesUpdatedListener,
        BillingClientStateListener,
        ILaunchPurchaseFlow, ConsumeResponseListener, ITransferApiResult {

    private BillingClient mBillingClient;
    private RecyclerView mRcView;
    private ProgressBar mPbBar;
    private MyInAppProductListAdapter adapter;
    private List<Datum> productList = new ArrayList<>();
    private List<UserDatum> userProductList = new ArrayList<>();
    String purchaseToken = "";
    String productId = "";
    int position = 0;
    ApiClass mApiClass;
    int apihit = 0;
    private Toolbar mToolbar;
    List<Purchase> result;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inappproduct);
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        initViews();
        setUpClient();
    }

    @Override
    public void setUpClient() {
        mBillingClient = BillingClient.newBuilder(this)
                .setListener(this).build();
        mBillingClient.startConnection(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void showLoading() {
        mPbBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mPbBar.setVisibility(View.GONE);
    }

    @Override
    public void initViews() {
        mToolbar = findViewById(R.id.toolBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        adapter = new MyInAppProductListAdapter(this, this);
        mPbBar = findViewById(R.id.pbBar);
        mRcView = findViewById(R.id.recyclerView);
        mRcView.setLayoutManager(new LinearLayoutManager(this));
        mRcView.setHasFixedSize(false);
        mRcView.setItemAnimator(new DefaultItemAnimator());
        mRcView.setAdapter(adapter);
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public boolean checkIfAlreadyBought(String skuid) {
        boolean flag = false;
        for (UserDatum data : userProductList) {
            if (data.getProductId().equalsIgnoreCase(skuid)) {
                flag = true;
                purchaseToken = data.getPurchaseToken();
                break;
            }
        }
        return flag;
    }

    @Override
    public void onBillingSetupFinished(int responseCode) {
        if (responseCode == BillingClient.BillingResponse.OK) {
            Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
            result = purchasesResult.getPurchasesList();
            if (Utils.checkConnectivity(InAppProductsActivity.this)) {
                if (mApiClass == null) {
                    mApiClass = new ApiClass(this);
                }
                apihit = 1;
                mApiClass.getUserProducts(InAppProductsActivity.this.getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE).getString(Constants.USER_ID, ""));
            } else {
                hideLoading();

            }
        } else {
            hideLoading();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inappmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            SharedPreferences prefs = getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE);
            prefs.edit().clear().commit();
            finish();
            Utils.startAcivity(this, LoginActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBillingServiceDisconnected() {

    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        System.out.println("codee" + responseCode);
        switch (responseCode) {
            case BillingClient.BillingResponse.USER_CANCELED:
                Toast.makeText(this, getString(R.string.payment_cancelled), Toast.LENGTH_SHORT).show();
                break;
            case BillingClient.BillingResponse.OK:
                List<String> productIdList = new ArrayList<>();

                for (UserDatum datum : userProductList) {
                    productIdList.add(datum.getProductId());
                }

                for (Purchase purchase : purchases) {
                    if (productIdList.contains(productId)) {
                        int pos = productIdList.indexOf(productId);
                        userProductList.get(pos).setPurchaseToken(purchase.getPurchaseToken());
                    } else {
                        UserDatum datum = new UserDatum();
                        datum.setProductId(purchase.getSku());
                        datum.setPurchaseToken(purchase.getPurchaseToken());
                        userProductList.add(datum);
                    }
                }

                for (UserDatum datum : userProductList) {
                    if (datum.getProductId().equalsIgnoreCase(productId)) {
                        purchaseToken = datum.getPurchaseToken();
                    }
                }
                showLoading();
                apihit = 2;
                HashMap<String, String> params = new HashMap<>();
                params.put(Constants.USER_ID, InAppProductsActivity.this.getSharedPreferences(Constants.USER_DATA, Context.MODE_PRIVATE).getString(Constants.USER_ID, ""));
                params.put(Constants.PRODUCT_ID, productId);
                params.put(Constants.PURCHASE_TOKEN, purchaseToken);
                mApiClass.savePaymentDetails(params);
                break;
            case BillingClient.BillingResponse.SERVICE_UNAVAILABLE:
                break;
            case BillingClient.BillingResponse.DEVELOPER_ERROR:
                System.out.println("developer error");
                break;
            case BillingClient.BillingResponse.ERROR:
                break;
            case BillingClient.BillingResponse.BILLING_UNAVAILABLE:
                break;
            case BillingClient.BillingResponse.ITEM_NOT_OWNED:
                break;
            case BillingClient.BillingResponse.ITEM_UNAVAILABLE:
                break;
            case BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED:
                break;
            case BillingClient.BillingResponse.ITEM_ALREADY_OWNED:
                Toast.makeText(this, getString(R.string.item_already_purchased), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void startPurchaseFlow(final String skuId, int position) {
        if (mBillingClient != null) {
            productId = skuId;
            this.position = position;
            if (checkIfAlreadyBought(productId)) {
                showLoading();
                mBillingClient.consumeAsync(purchaseToken, this);
            } else {
                for (Purchase purchase : result) {
                    if (purchase.getSku().equalsIgnoreCase(productId)) {
                        showLoading();
                        purchaseToken = purchase.getPurchaseToken();
                        mBillingClient.consumeAsync(purchaseToken, this);
                        return;
                    }
                }
                BillingFlowParams params = BillingFlowParams.newBuilder()
                        .setSku(skuId)
                        .setType(BillingClient.SkuType.INAPP).build();
                mBillingClient.launchBillingFlow(this, params);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mBillingClient != null) {
            mBillingClient.endConnection();
        }
        if (mApiClass != null) {
            mApiClass.cancelRequest();
        }
        unregisterReceiver(onComplete);
        super.onDestroy();
    }

    @Override
    public void onConsumeResponse(int responseCode, String purchaseToken) {
        hideLoading();
        if (responseCode == BillingClient.BillingResponse.OK || responseCode == BillingClient.BillingResponse.DEVELOPER_ERROR || responseCode == BillingClient.BillingResponse.ITEM_NOT_OWNED
                || responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            purchaseFlow();
        }
    }

    @Override
    public void success(Object object) {
        hideLoading();
        if (object != null) {
            if (apihit == 1) {
                if (((ProductList) object) != null) {
                    ProductList productList = (ProductList) object;
                    if (productList.getIsSuccess().equalsIgnoreCase("true")) {

                        if (productList.getUserData() != null && productList.getUserData().size() > 0) {
                            for (UserDatum userDatum : productList.getUserData()) {
                                for (Datum datum : productList.getData()) {
                                    if (userDatum.getProductId().equalsIgnoreCase(datum.getProductId())) {
                                        datum.setPurchased(true);
                                    }
                                    System.out.println("download url" + datum.getDownloadUrl());
                                }
                            }
                        }

                        userProductList = productList.getUserData();
                        this.productList = productList.getData();
                        adapter.notifyData(this.productList);

                    } else if (productList.getIsSuccess().equalsIgnoreCase("false")) {
                        Utils.showToast(InAppProductsActivity.this, productList.getMessage());
                    }
                }
            } else if (apihit == 2) {
                hideLoading();
                if (((AddPaymentSubscriptionResponse) object) != null) {
                    productList.get(position).setPurchased(true);
                    adapter.notifyData(productList);
                    startDownload(position);
                    Utils.showToast(InAppProductsActivity.this, getString(R.string.download_started));
                }
            }
        }
    }

    @Override
    public void failure(String message) {
        hideLoading();
        Utils.DismissDialog();
        Utils.showToast(InAppProductsActivity.this, message);
    }

    @Override
    public void startDownload(int position) {
        Uri Download_Uri = Uri.parse(productList.get(position).getDownloadUrl());
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        String downloadUrl = productList.get(position).getDownloadUrl();
        String extension = downloadUrl.substring(downloadUrl.lastIndexOf("."));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(productList.get(position).getProductName() + extension);
        request.setDescription(productList.get(position).getProductName() + extension);
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(View.VISIBLE);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, productList.get(position).getProductName() + extension);
        downloadManager.enqueue(request);
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {
            Utils.showToast(ctxt, getString(R.string.download_complete));
        }

    };

    private void purchaseFlow() {
        BillingFlowParams params = BillingFlowParams.newBuilder()
                .setSku(productId)
                .setType(BillingClient.SkuType.INAPP).build();
        mBillingClient.launchBillingFlow(InAppProductsActivity.this, params);
    }

}
