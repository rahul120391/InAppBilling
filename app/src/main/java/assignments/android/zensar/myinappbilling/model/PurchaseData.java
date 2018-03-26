package assignments.android.zensar.myinappbilling.model;

/**
 * Created by RK51670 on 28-02-2018.
 */

public class PurchaseData {

    private String Sku,PurchaseToken;

    public String getSku() {
        return Sku;
    }

    public void setSku(String sku) {
        Sku = sku;
    }

    public String getPurchaseToken() {
        return PurchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        PurchaseToken = purchaseToken;
    }
}
