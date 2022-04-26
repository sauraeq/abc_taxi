package com.example.taxibookinguserapplication.util.Camerautils;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Utility {
    static ProgressBar mProgressBar;
    static boolean isCallingBannerApiFirstTime = true;
    public static boolean shouldOpenReportList = false;

    public static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null)
            return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            // Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    public static final String REGEX = "[0-9]+([,.][0-9]{1,2})?";

    public static String getCommaSeparatedAmount(String amount) {
        if (!TextUtils.isEmpty(amount)) {

            String strWithoutComma = amount.replace(",", "");

            if (strWithoutComma.matches(REGEX)) {

                BigDecimal bd = new BigDecimal(amount.replace(",", ""));
                return IndianFormat(bd);
            }
        }
        return amount;
    }

    // Formats a number in the Indian style
    public static String IndianFormat(BigDecimal n) {

        // DecimalFormat formatter = new DecimalFormat("#,###.00");
        DecimalFormat formatter = new DecimalFormat("#,###");
        // we never reach double digit grouping so return
        if (n.doubleValue() < 100000) {
            return formatter.format(n.setScale(2, 1).doubleValue());
        }
        StringBuffer returnValue = new StringBuffer();
        // Spliting integer part and decimal part
        String value = n.setScale(2, 1).toString();
        String intpart = value.substring(0, value.indexOf("."));
        // String decimalpart = value
        // .substring(value.indexOf("."), value.length());
        // switch to double digit grouping
        formatter.applyPattern("#,##");
        returnValue.append(
                formatter.format(Math.floor(new BigDecimal(intpart)
                        .doubleValue() / 1000))).append(",");
        // appending last 3 digits and decimal part
        // returnValue.append(intpart.substring(intpart.length() - 3,
        // intpart.length())).append(decimalpart);
        returnValue.append(intpart.substring(intpart.length() - 3,
                intpart.length()));
        // returning complete string
        return returnValue.toString();
    }

    public static String removeIndianFormat(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ',') {
                continue;
            }
            str = str + s.charAt(i);
        }
        return str;
    }

    public static String convertFormat(double value) {
        String str = "" + value;
        double val = value / 100000;
        String newstr = "" + val;
        String[] str_array = newstr.split("\\.");
        if (str_array.length > 1) {
            str = str_array[0] + "." + str_array[1].substring(0, 1) + " L";
        }
        return str;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }


    /***
     * return reverse date
     *
     * @param date
     * @return
     */
    public static String reverseDate(String date) {
        StringTokenizer st = new StringTokenizer(date, "-");
        String day = st.nextToken();
        String month = st.nextToken();
        String year = st.nextToken();
        String reverse_date = year + "-" + month + "-" + day;
        return reverse_date;
    }

    /***
     * return reverse date formate 1(Y-m-d)
     *
     * @param date
     * @return
     */
    public static String reverseDateFormate1(String date) {
        StringTokenizer st = new StringTokenizer(date, "-");
        String day = st.nextToken();
        String month = st.nextToken();
        String year = st.nextToken();
        year = year.substring(2, 4);
        String reverse_date = year + "-" + month + "-" + day;
        return reverse_date;
    }

    public static boolean isDateValid(String date, int validation) {
        Calendar mCurrentDate = Calendar.getInstance();
        int currentYear = mCurrentDate.get(Calendar.YEAR);
        StringTokenizer st = new StringTokenizer(date, "-");
        String day = st.nextToken();
        String month = st.nextToken();
        int year = Integer.parseInt(st.nextToken());
        if (currentYear - year > validation && currentYear - year <= 0)
            return false;
        return true;
    }

    /***
     * show error toast message
     *
     * @param context
     * @param toast_id
     */
    public static void showErrorToast(Context context, int toast_id) {
        Toast.makeText(context, toast_id, Toast.LENGTH_SHORT).show();
    }

    /***
     * show comman toast message
     *
     * @param context
     * @param toast_id
     */
    public static void showCommanToast(Context context, int toast_id) {
        Toast.makeText(context, toast_id, Toast.LENGTH_SHORT).show();
    }

    /**
     * Always verify the host - dont check for certificate
     */
    public final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - don't check for any certificate
     */
    public static void trustAllHosts() {

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a trust manager that does not validate certificate chains
     */
    public static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] chain, String authType) {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] chain, String authType) {
            // TODO Auto-generated method stub
        }
    }};

    // Host name verifier for SOAP calls
    public static class NullHostNameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            // Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(
                        context.getContentResolver(),
                        Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    }

    public static double distanceKm(double lat1, double lon1, double lat2,
                                    double lon2) {
        int EARTH_RADIUS_KM = 6371;
        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLonRad = Math.toRadians(lon2 - lon1);

        return Math
                .acos(Math.sin(lat1Rad) * Math.sin(lat2Rad) + Math.cos(lat1Rad)
                        * Math.cos(lat2Rad) * Math.cos(deltaLonRad))
                * EARTH_RADIUS_KM;
    }

    public static boolean cardExpiry(String date) {
        boolean expired = false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
        simpleDateFormat.setLenient(false);
        Date expiry = null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int current_year = calendar.get(Calendar.YEAR) / 100;
        date = date.replace(" ", "");
        String[] datevalue = date.split("/");
        if (datevalue.length > 1) {
            String year = current_year + datevalue[1];
            int mm = Integer.parseInt(datevalue[0]);
            if (mm < 10) {
                date = "0" + mm + "/" + year;
            } else {
                date = mm + "/" + year;
            }
        } else {
            return true;
        }

        try {
            expiry = simpleDateFormat.parse(date);
            String nowDateString = simpleDateFormat.format(new Date());
            if (date.equalsIgnoreCase(nowDateString)) {
                expired = false;
            } else {
                expired = expiry.before(new Date());
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("My date", "ParseException : " + e);
        }

        Log.e("My date", "expired : " + expired);
        return expired;
    }

    boolean validateCardExpiryDate(String expiryDate) {
        return expiryDate.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
    }

    //open dialer
    public static void callCustomerCare(Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:18001027718"));
        context.startActivity(intent);
    }

    public static String toCamelCase(String s) {
        String[] parts = s.split(" ");
        String camelCaseString = "";
        for (String part : parts) {
            if (part != null && part.trim().length() > 0)
                camelCaseString = camelCaseString + toProperCase(part);
            else
                camelCaseString = camelCaseString + part + " ";
        }
        return camelCaseString;
    }

    public static String toProperCase(String s) {
        String temp = s.trim();
        String spaces = "";
        if (temp.length() != s.length()) {
            int startCharIndex = s.charAt(temp.indexOf(0));
            spaces = s.substring(0, startCharIndex);
        }
        temp = temp.substring(0, 1).toUpperCase() +
                spaces + temp.substring(1).toLowerCase() + " ";
        return temp;

    }

    //get App Version
    public static String getAppVersion(Context context) {
        // App version name
        PackageManager manager =
                context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context
                    .getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String version;
        if (info != null) {

            version = info.versionName + "";

        } else {

            version = "default";
        }

        return version;
    }

    public static String getUniqId() {
        Random rand = new Random();
        String rndm = Integer.toString(rand.nextInt())
                + (System.currentTimeMillis() / 1000L);
        String txnid = hashCal("SHA-256", rndm).substring(0, 20);
        return txnid;
    }

    public static String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hexString.toString();

    }


    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for (int idx = 0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    public static String loadFileAsString(String filename) throws java.io.IOException {
        final int BUFLEN = 1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try {
                is.close();
            } catch (Exception ex) {
            }
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}

