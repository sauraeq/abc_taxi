package com.rehablab.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class UtillyClass {

    companion object {

        public fun compressImageFile(
            path: String,
            shouldOverride: Boolean = true,
            uri: Uri
        ): String {
        //  var scaledBitmap: Bitmap? = null

            try {
                var file = File(path)
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                o.inSampleSize = 6
        // factor of downsizing the image
                var inputStream = FileInputStream(file)
                //Bitmap selectedBitmap = null;
                BitmapFactory.decodeStream(inputStream, null, o)
                inputStream.close()
                // The new size we want to scale to
                val REQUIRED_SIZE = 75
// Find the correct scale value. It should be the power of 2.
                var scale = 1
                while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE
                ) {
                    scale *= 2
                }
                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                inputStream = FileInputStream(file)
                val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
                inputStream.close()


                file.createNewFile()
                val outputStream = FileOutputStream(file)
                selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

                return file.path
            } catch (e: java.lang.Exception) {
                return ""
            }

        }

        fun isNetworkAvailable(context: Context?): Boolean {
            if (context == null) return false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            } else {
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
            return false
        }

        fun getBitmap(path: String?, context: Context): Bitmap? {
            val uri = Uri.fromFile(File(path))
            var `in`: InputStream? = null
            return try {
                val IMAGE_MAX_SIZE = 1200000 // 1.2MP
                `in` = context.getContentResolver().openInputStream(uri)

                // Decode image size
                var o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                BitmapFactory.decodeStream(`in`, null, o)
                if (`in` != null) {
                    `in`.close()
                }
                var scale = 1
                while (o.outWidth * o.outHeight * (1 / Math.pow(scale.toDouble(), 2.0)) >
                    IMAGE_MAX_SIZE
                ) {
                    scale++
                }
                var bitmap: Bitmap? = null
                `in` = context.getContentResolver().openInputStream(uri)
                if (scale > 1) {
                    scale--
                    // scale to max possible inSampleSize that still yields an image
                    // larger than target
                    o = BitmapFactory.Options()
                    o.inSampleSize = scale
                    bitmap = BitmapFactory.decodeStream(`in`, null, o)

                    // resize to desired dimensions
                    val height = bitmap!!.height
                    val width = bitmap.width
                    val y = Math.sqrt(
                        IMAGE_MAX_SIZE
                                / (width.toDouble() / height)
                    )
                    val x = y / height * width
                    val scaledBitmap = Bitmap.createScaledBitmap(
                        bitmap, x.toInt(),
                        y.toInt(), true
                    )
                    bitmap.recycle()
                    bitmap = scaledBitmap
                    System.gc()
                } else {
                    bitmap = BitmapFactory.decodeStream(`in`)
                }
                if (`in` != null) {
                    `in`.close()
                }
                bitmap
            } catch (e: IOException) {
                null
            }
        }

        @Throws(IOException::class)
        fun Context.getBitmapFromUri(uri: Uri, options: BitmapFactory.Options? = null): Bitmap? {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            val image: Bitmap? = if (options != null)
                BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
            else
                BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()
            return image
        }

        public fun getTimestampString(): String {
            val date = Calendar.getInstance()
            return SimpleDateFormat("yyyy MM dd hh mm ss", Locale.US).format(date.time)
                .replace("", "")
        }

        fun encodeTobase64(image: Bitmap): String {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val b = baos.toByteArray()
            val imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)
            Log.e("LOOK", imageEncoded)
            return imageEncoded
        }
       fun base64ToBitmap(base64String :String): Bitmap?{
           val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
           val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
          return decodedImage!!
       }

        fun getTimeStampToTime(time:Long) : String{
            val currentTime = System.currentTimeMillis()
            var defe = currentTime - time
            val time_in: Long

            if (time!= 0L) {
                time_in = time
            } else {
                time_in = currentTime
                defe = 0
            }

            val s = defe.toInt() / 1000
            val m = defe.toInt() / (1000 * 60)
            val h = defe.toInt() / (1000 * 60 * 60)
            val d = defe.toInt() / (1000 * 60 * 60 * 24)
            val w = defe.toInt() / (1000 * 60 * 60 * 24 * 7)

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = time_in
            val date = calendar.time
            @SuppressLint("SimpleDateFormat") val formattedDate =
                SimpleDateFormat("hh:mm a").format(date)
            @SuppressLint("SimpleDateFormat") val formattedYear = SimpleDateFormat("MMM d, ''yy")
                .format(date)
            @SuppressLint("SimpleDateFormat") val formattedm =
                SimpleDateFormat("MMM d").format(date)

             if (d > 365) {
              return  formattedYear
            } else if (s > 172000) {
               return formattedm
            } else if (s > 86400) {
               return "Yesterday"
            } else {
              return  formattedDate
            }
        }





    }


}