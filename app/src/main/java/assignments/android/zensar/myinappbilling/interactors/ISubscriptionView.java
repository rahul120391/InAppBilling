package assignments.android.zensar.myinappbilling.interactors;

/**
 * Created by RK51670 on 28-02-2018.
 */

public interface ISubscriptionView {
    void setUpClient();
    void initViews();
    boolean checkIfAlreadyBought(String skuid);
}
