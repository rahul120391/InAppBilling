package assignments.android.zensar.myinappbilling.interactors;

/**
 * Created by RK51670 on 26-02-2018.
 */

public interface IInAppView {

    void setUpClient();
    void showLoading();
    void hideLoading();
    void initViews();
    boolean checkIfAlreadyBought(String skuid);
}
