package assignments.android.zensar.myinappbilling.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductList {

@SerializedName("isSuccess")
@Expose
private String isSuccess;
@SerializedName("message")
@Expose
private String message;
@SerializedName("userData")
@Expose
private List<UserDatum> userData = null;
@SerializedName("data")
@Expose
private List<Datum> data = null;

public String getIsSuccess() {
return isSuccess;
}

public void setIsSuccess(String isSuccess) {
this.isSuccess = isSuccess;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public List<UserDatum> getUserData() {
return userData;
}

public void setUserData(List<UserDatum> userData) {
this.userData = userData;
}

public List<Datum> getData() {
return data;
}

public void setData(List<Datum> data) {
this.data = data;
}

}