package assignments.android.zensar.myinappbilling.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ZenVerseLoginResponse {

    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("encryptedToken")
    @Expose
    private String encryptedToken;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("employeeId")
    @Expose
    private String employeeId;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("moduleId")
    @Expose
    private Integer moduleId;
    @SerializedName("isQAAdmin")
    @Expose
    private String isQAAdmin;
    @SerializedName("isAdmin")
    @Expose
    private String isAdmin;
    @SerializedName("unreadNotificatinCount")
    @Expose
    private Object unreadNotificatinCount;
    @SerializedName("userDetails")
    @Expose
    private Object userDetails;
    @SerializedName("departmetSLAs")
    @Expose
    private Object departmetSLAs;
    @SerializedName("showVideo")
    @Expose
    private Object showVideo;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getEncryptedToken() {
        return encryptedToken;
    }

    public void setEncryptedToken(String encryptedToken) {
        this.encryptedToken = encryptedToken;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getIsQAAdmin() {
        return isQAAdmin;
    }

    public void setIsQAAdmin(String isQAAdmin) {
        this.isQAAdmin = isQAAdmin;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Object getUnreadNotificatinCount() {
        return unreadNotificatinCount;
    }

    public void setUnreadNotificatinCount(Object unreadNotificatinCount) {
        this.unreadNotificatinCount = unreadNotificatinCount;
    }

    public Object getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(Object userDetails) {
        this.userDetails = userDetails;
    }

    public Object getDepartmetSLAs() {
        return departmetSLAs;
    }

    public void setDepartmetSLAs(Object departmetSLAs) {
        this.departmetSLAs = departmetSLAs;
    }

    public Object getShowVideo() {
        return showVideo;
    }

    public void setShowVideo(Object showVideo) {
        this.showVideo = showVideo;
    }

}
