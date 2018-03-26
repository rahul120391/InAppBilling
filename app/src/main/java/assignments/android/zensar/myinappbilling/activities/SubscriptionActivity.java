package assignments.android.zensar.myinappbilling.activities;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.ArrayList;
import java.util.List;

import assignments.android.zensar.myinappbilling.R;
import assignments.android.zensar.myinappbilling.interactors.ISubscriptionView;
import assignments.android.zensar.myinappbilling.model.PurchaseData;

public class SubscriptionActivity extends AppCompatActivity implements
        ISubscriptionView,
        BillingClientStateListener,
        PurchasesUpdatedListener, View.OnClickListener {

    private BillingClient mBillingClient;
    private List<PurchaseData> purchaseData = new ArrayList<>();
    private List<String> oldSkuList = new ArrayList<>();
    String purchaseToken = "";
    String skuid = "";
    int position = 0;
    RelativeLayout rlWeek, rlOneMonth, rlThreeMonth, rlSixMonth, rlOneYear;
    String ids[] = new String[]{"week1234", "onemonth1234", "threemonth1234", "sixmonth1234", "oneyear1234"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        initViews();
        setUpClient();
    }

    @Override
    public void setUpClient() {
        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        mBillingClient.startConnection(this);
    }


    @Override
    public void initViews() {
        rlWeek = findViewById(R.id.rlWeek);
        rlOneMonth = findViewById(R.id.rlOneMonth);
        rlThreeMonth = findViewById(R.id.rlThreeMonth);
        rlSixMonth = findViewById(R.id.rlSixMonth);
        rlOneYear = findViewById(R.id.rlOneYear);
        rlWeek.setOnClickListener(this);
        rlOneMonth.setOnClickListener(this);
        rlThreeMonth.setOnClickListener(this);
        rlSixMonth.setOnClickListener(this);
        rlOneYear.setOnClickListener(this);
    }

    @Override
    public void onBillingSetupFinished(int responseCode) {
        if (responseCode == BillingClient.BillingResponse.OK) {
            if (mBillingClient != null && mBillingClient.isReady()) {
                Purchase.PurchasesResult purchaseResult = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
                if (purchaseResult.getResponseCode() == BillingClient.BillingResponse.OK && purchaseResult != null && purchaseResult.getPurchasesList() != null && purchaseResult.getPurchasesList().size() > 0) {
                    purchaseData.clear();
                    oldSkuList.clear();
                    for (Purchase purchase : purchaseResult.getPurchasesList()) {
                        PurchaseData data = new PurchaseData();
                        data.setPurchaseToken(purchase.getPurchaseToken());
                        data.setSku(purchase.getSku());
                        oldSkuList.add(purchase.getSku());
                        purchaseData.add(data);
                    }
                }
            }
        }
    }

    @Override
    public void onBillingServiceDisconnected() {

    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        switch (responseCode) {
            case BillingClient.BillingResponse.USER_CANCELED:
                break;
            case BillingClient.BillingResponse.OK:
                break;
            case BillingClient.BillingResponse.SERVICE_UNAVAILABLE:
                break;
            case BillingClient.BillingResponse.DEVELOPER_ERROR:
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

    private void startPurchaseFlow(String skuId) {
        if (mBillingClient != null) {
            skuid = skuId;
            if (checkIfAlreadyBought(skuId)) {
                Toast.makeText(SubscriptionActivity.this, "you have already purchased this plan", Toast.LENGTH_SHORT).show();
            } else {
                BillingFlowParams.Builder params = BillingFlowParams.newBuilder();
                params.setSku(skuId);
                params.setType(BillingClient.SkuType.SUBS);
                if (oldSkuList.size() > 0) {
                    params.setOldSkus((ArrayList<String>) oldSkuList);
                }
                mBillingClient.launchBillingFlow(this, params.build());
            }
        }
    }

    @Override
    public boolean checkIfAlreadyBought(String skuid) {
        boolean flag = false;
        for (PurchaseData data : purchaseData) {
            if (data.getSku().equalsIgnoreCase(skuid)) {
                flag = true;
                purchaseToken = data.getPurchaseToken();
                System.out.println("purchase token");
                break;
            }
        }
        return flag;
    }

    @Override
    protected void onDestroy() {
        if (mBillingClient != null) {
            mBillingClient.endConnection();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlWeek:
                startPurchaseFlow(ids[0]);
                break;
            case R.id.rlOneMonth:
                startPurchaseFlow(ids[1]);
                break;
            case R.id.rlThreeMonth:
                startPurchaseFlow(ids[2]);
                break;
            case R.id.rlSixMonth:
                startPurchaseFlow(ids[3]);
                break;
            case R.id.rlOneYear:
                startPurchaseFlow(ids[4]);
                break;
        }
    }
}
