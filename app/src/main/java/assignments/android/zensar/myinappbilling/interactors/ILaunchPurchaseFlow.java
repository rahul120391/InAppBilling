package assignments.android.zensar.myinappbilling.interactors;

/**
 * Created by RK51670 on 28-02-2018.
 */

public interface ILaunchPurchaseFlow {
    void startPurchaseFlow(String skuId,int position);
    void startDownload(int position);
}
