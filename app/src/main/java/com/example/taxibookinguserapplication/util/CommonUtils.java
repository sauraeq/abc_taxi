
package com.example.taxibookinguserapplication.util;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.webkit.MimeTypeMap;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public final class CommonUtils {

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isValidMobile(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }
    public static String loadJSONFromAsset(Context context, String jsonFileName) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }


    /**
     * this method is to check mobile number all 0
     *
     * @param number mobile number enterd by the user
     * @return false if zero else true
     */
    public static boolean isMobileZero(String number) {
        long zip = 0;
        try {
            zip = Long.parseLong(number);
            Log.i("DEBUG", zip + "code value");
        } catch (NumberFormatException e) {
            return true;
        }
        if (zip == 0) {
            return false;
        }
        return true;
    }

    public static String getMimeType(Uri uri, Context context) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                //  handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                if (id != null) {
                    if (id.startsWith("raw:")) {
                        return id.substring(4);
                    }
                    try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String stingFormatStringToDate(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.i(TAG, "ParseException - dateFormat");
        }
        return outputDate;

    }

    public static String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }


    public static String onChangeTimeFormat(String inputFormat, String outputFormat, String inputTime) {
        String output = null;
        try {
            SimpleDateFormat time = new SimpleDateFormat(inputFormat, Locale.getDefault());
            SimpleDateFormat formatter = new SimpleDateFormat(outputFormat, Locale.getDefault());

            output = formatter.format(time.parse(inputTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * this method invoke to show dialog to choose camera or gallery
     */

    public static void showPermissionAlertDialog(final Activity context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setMessage("Please go to setting and gave the permissions");
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);

            }
        });

        alertDialog.setNegativeButton("No", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
                dialog.dismiss();

            }
        }));
        alertDialog.show();
    }

    public static Bitmap getBitmapValue(String path, Context context) {

        Uri uri = Uri.fromFile(new File(path));

        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = context.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }

            Bitmap bitmap = null;
            in = context.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                bitmap = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) x,
                        (int) y, true);
                bitmap.recycle();
                bitmap = scaledBitmap;

                System.gc();
            } else {
                bitmap = BitmapFactory.decodeStream(in);
            }
            in.close();


            return bitmap;
        } catch (IOException e) {
            return null;
        }
    }

    public static void showBackSpaceAlertDialog(Context context, String message) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("No", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                dialog.dismiss();
            }
        }));
        alertDialog.show();
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }


    public static boolean checkGPSStatus(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }


    public static String getFileSize(File file) {
        String modifiedFileSize = null;
        double fileSize = 0.0;
        if (file.isFile()) {
            fileSize = (double) file.length();//in Bytes

            if (fileSize < 1024) {
                modifiedFileSize = String.valueOf(fileSize).concat("B");
            } else if (fileSize > 1024 && fileSize < (1024 * 1024)) {
                modifiedFileSize = String.valueOf(Math.round((fileSize / 1024 * 100.0)) / 100.0).concat("KB");
            } else {
                modifiedFileSize = String.valueOf(Math.round((fileSize / (1024 * 1204) * 100.0)) / 100.0).concat("MB");
            }
        } else {
            modifiedFileSize = "Unknown";
        }

        return modifiedFileSize;
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else return "";
    }

    private static MediaRecorder recorder;
    public static void startRecording(String outputPath)
    {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(outputPath);
        try
        {
            recorder.prepare();
            recorder.start();
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void stopRecording() {
        if (null != recorder) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        }
    }

    private static String getFilename()
    {
        String filepath = Environment.getExternalStorageDirectory().getPath()+"/Gogokull";
        File file = new File(filepath);
        if(!file.exists()){
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3");
    }

    private static String _audioBase64;
    public static String encodeAudio(String selectedPath) {

        byte[] audioBytes;
        try {
            File audioFile = new File(selectedPath);
            long fileSize = audioFile.length();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(selectedPath));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf))) baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();

            _audioBase64 = Base64.encodeToString(audioBytes, Base64.DEFAULT);

        } catch (Exception e) {
           e.printStackTrace();
        }
        return _audioBase64;
    }
    private static File file2;
    public static File base64ToAudio(String encoded){
        String filepath = Environment.getExternalStorageDirectory().getPath() + "/Gogokull";
        byte[] decoded = Base64.decode(encoded, 0);
        try
        {
            file2 = new File(filepath + "/" + System.currentTimeMillis() +".mp3");
            FileOutputStream os = new FileOutputStream(file2, true);
            os.write(decoded);
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    return file2;
    }

    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        //Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
//        if (hours == 0) {
//            finalTimerString = "";
//        }

        // Pre appending 0 to seconds if it is one digit
        if (seconds == 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public static String parseDate(String givenDateString) {
        if (givenDateString.equalsIgnoreCase("")) {
            return "";
        }

        long timeInMilliseconds=0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        try {

            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String result = "now";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        String todayDate = formatter.format(new Date());
        Calendar calendar = Calendar.getInstance();

        long dayagolong =  timeInMilliseconds;
        calendar.setTimeInMillis(dayagolong);
        String agoformater = formatter.format(calendar.getTime());

        Date CurrentDate = null;
        Date CreateDate = null;

        try {
            CurrentDate = formatter.parse(todayDate);
            CreateDate = formatter.parse(agoformater);

            long different = Math.abs(CurrentDate.getTime() - CreateDate.getTime());

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            different = different % secondsInMilli;
            if (elapsedDays == 0) {
                if (elapsedHours == 0) {
                    if (elapsedMinutes == 0) {
                        if (elapsedSeconds < 0) {
                            return "0" + " s";
                        } else {
                            if (elapsedDays > 0 && elapsedSeconds < 59) {
                                return "now";
                            }
                        }
                    } else {
                        return String.valueOf(elapsedMinutes) + "mins ago";
                    }
                } else {
                    return String.valueOf(elapsedHours) + "hr ago";
                }

            } else {
                if (elapsedDays <= 29) {
                    return String.valueOf(elapsedDays) + "d ago";

                }
                else if (elapsedDays > 29 && elapsedDays <= 58) {
                    return "1Mth ago";
                }
                if (elapsedDays > 58 && elapsedDays <= 87) {
                    return "2Mth ago";
                }
                if (elapsedDays > 87 && elapsedDays <= 116) {
                    return "3Mth ago";
                }
                if (elapsedDays > 116 && elapsedDays <= 145) {
                    return "4Mth ago";
                }
                if (elapsedDays > 145 && elapsedDays <= 174) {
                    return "5Mth ago";
                }
                if (elapsedDays > 174 && elapsedDays <= 203) {
                    return "6Mth ago";
                }
                if (elapsedDays > 203 && elapsedDays <= 232) {
                    return "7Mth ago";
                }
                if (elapsedDays > 232 && elapsedDays <= 261) {
                    return "8Mth ago";
                }
                if (elapsedDays > 261 && elapsedDays <= 290) {
                    return "9Mth ago";
                }
                if (elapsedDays > 290 && elapsedDays <= 319) {
                    return "10Mth ago";
                }
                if (elapsedDays > 319 && elapsedDays <= 348) {
                    return "11Mth ago";
                }
                if (elapsedDays > 348 && elapsedDays <= 360) {
                    return "12Mth ago";
                }

                if (elapsedDays > 360 && elapsedDays <= 720) {
                    return "1 year ago";
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String getDateTime(String timeStamp){
        String dateTime ="";
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf.parse(timeStamp);

            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss ");
            sdf2.setTimeZone(TimeZone.getTimeZone("IST"));
            dateTime = sdf2.format(date);
            System.out.println("ssss"+dateTime);
            return dateTime;
        }catch (ParseException e){
            e.printStackTrace();
        }
        System.out.println("ssss"+dateTime);

        return dateTime;
    }


    public static String getTime(String dateTime){
        String times ="";
        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date d = f.parse(dateTime);

            DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
            DateFormat time = new SimpleDateFormat("hh:mm:ss a");
            System.out.println("Date: " + date.format(d));
            System.out.println("Time: " + time.format(d));//09:00:00 am

            String []word= time.format(d).split(":");
            times=times+word[0]+":"+word[1]+" "+word[2].split(" ")[1];

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return times;
    }

    public static String getDate(String dateTime){
        String dateStr="";
        try {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            Date d = f.parse(dateTime);

            DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
//            System.out.println("Date: " + date.format(d));
            dateStr = date.format(d);


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static Bitmap getVideoThumbnail(String videoPath)throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath,new HashMap<String,String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)"+ e.getMessage());
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

}

