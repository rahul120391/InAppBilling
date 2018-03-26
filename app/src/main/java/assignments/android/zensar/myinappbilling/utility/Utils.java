package assignments.android.zensar.myinappbilling.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by RK51670 on 28-02-2018.
 */

public class Utils {
    private Utils(){

    }

    public static ProgressDialog progressdialog;
    /**
     * Show play services error
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    /**
     * Load Images
     */
    public static void loadImages(Context context, ImageView imageView, String url) {
        Picasso.with(context).load(url).into(imageView);
    }

    /**
     * Start activity intent function
     */
    public static void startAcivity(Context context, Class classname) {
        Intent startActivity = new Intent(context, classname);
        startActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(startActivity);
    }

    /**
     * Start activity for result intent function
     */
    public static void startAcivityForResult(Context context, Class classname, int requestCode) {
        Intent startActivity = new Intent(context, classname);
        startActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ((Activity) context).startActivityForResult(startActivity, requestCode);
    }

    /**
     * Start activity intent function
     */
    public static void startAcivityForResultWithData(Context context, Class classname, Bundle bundle, int requestCode) {
        Intent startActivity = new Intent(context, classname);
        startActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ((Activity) context).startActivityForResult(startActivity, requestCode);
    }

    /**
     * Start activity intent function
     */
    public static void startAcivityWithData(Context context, Class classname, Bundle bundle) {
        Intent startActivity = new Intent(context, classname);
        startActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ((Activity) context).startActivity(startActivity);
    }

    /**
     * Check network connectivity
     */
    public static Boolean checkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static String getDeviceId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void hideKeyboard(Activity context){
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }





    public static void ShowDialog(Context context) {

        progressdialog = new ProgressDialog(context, AlertDialog.THEME_HOLO_LIGHT);

        DismissDialog();

        if (!progressdialog.isShowing()) {

            progressdialog.setMessage("Downloading...");

            progressdialog.setIndeterminate(true);

            progressdialog.setCancelable(false);

            progressdialog.show();

        }

    }



    public static void DismissDialog() {

        if (progressdialog!=null && progressdialog.isShowing()) {

            progressdialog.dismiss();

        }

    }
}
