package assignments.android.zensar.myinappbilling.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDatum {

    @SerializedName("productId")
    @Expose
    private String productId;
    public String getPurchaseToken() {
        return purchaseToken;
    }
    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }
    @SerializedName("purchaseToken")
    @Expose
    private String purchaseToken;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}