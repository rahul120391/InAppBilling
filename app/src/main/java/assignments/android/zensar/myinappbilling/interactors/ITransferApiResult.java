package assignments.android.zensar.myinappbilling.interactors;

public interface ITransferApiResult {
    void success(Object object);
    void failure(String message);

}