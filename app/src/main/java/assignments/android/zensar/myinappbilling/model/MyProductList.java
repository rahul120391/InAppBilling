package assignments.android.zensar.myinappbilling.model;

/**
 * Created by RK51670 on 28-02-2018.
 */

public class MyProductList {
    private String productTitle,productDescription,productPrice,productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isProductPurchased() {
        return productPurchased;
    }

    public void setProductPurchased(boolean productPurchased) {
        this.productPurchased = productPurchased;
    }

    private boolean productPurchased;
}
