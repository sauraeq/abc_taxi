package com.example.taxibookinguserapplication.Payment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.taxibookinguserapplication.Api.APIUtils
import com.example.taxibookinguserapplication.Main.TipInformation
import com.example.taxibookinguserapplication.R
import com.example.taxibookinguserapplication.Responses.BookingStatusResponse
import com.example.taxibookinguserapplication.Responses.PaymentResponse
import com.example.taxibookinguserapplication.util.SharedPreferenceUtils
import com.rehablab.util.ConstantUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_tip_information.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Payment_method : AppCompatActivity() {
    var amount=""
    var driver_id=""
    var booking_idd=""
    var user_id=""
    lateinit var webview_paymentt: WebView
    lateinit var customprogress: Dialog
    private var requestQueue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loaderrrr_layout)


        user_id= SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.USER_ID,"").toString()
        // booking_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils
        // .Booking_id,"").toString()
        driver_id=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Driver_id,"")
            .toString()

        booking_idd=SharedPreferenceUtils.getInstance(this)!!.getStringValue(ConstantUtils.Booking_id,"")
            .toString()

        webview_paymentt=findViewById(R.id.webview_payement)
        webview_paymentt.getSettings().setJavaScriptEnabled(true)
        webview_paymentt.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        payment()
    }

    fun payment()
    {
        customprogress.show()
        val request = HashMap<String, String>()
        request.put("user_id",user_id)
        request.put("payment_method","PayPal")
        request.put("booking_id",booking_idd)
        request.put("amount","100")
        request.put("driver_id",driver_id)



        var payment: Call<PaymentResponse> = APIUtils.getServiceAPI()!!.payment(request)

        payment.enqueue(object : Callback<PaymentResponse> {
            override fun onResponse(call: Call<PaymentResponse>, response: Response<PaymentResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                        var payment_link_url=response.body()!!.data.payment_url
                        //webview_paymentt.loadUrl(payment_link_url)
                        startWebView(payment_link_url)


                        Toast.makeText(this@Payment_method,response.body()!!.msg, Toast.LENGTH_LONG).show()




                        customprogress.hide()


                    } else {

                        Toast.makeText(this@Payment_method,response.body()!!.msg, Toast
                            .LENGTH_LONG).show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@Payment_method,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<PaymentResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@Payment_method,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                customprogress.hide()
            }

        })
    }

    private fun startWebView(url: String) {
        webview_paymentt.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }


            override fun onLoadResource(view: WebView, url: String) {
                /* Toast.makeText(this@Payment_method,"payment under process",Toast.LENGTH_LONG).show()*/

            }
            override fun onPageFinished(view: WebView, url: String) {
                try {

                    Log.d("sasa",url)

                    jsonParse(url)


                } catch (exception: java.lang.Exception) {
                    exception.printStackTrace()
                }
            }
        })


        webview_paymentt.loadUrl(url)
    }

     fun jsonParse(url: String) {
        val Url = url
        val request = JsonObjectRequest(Request.Method.GET, Url, null, { response
            ->try {
            Log.d("res",response.toString())

            val jsonobject: JSONObject = JSONObject(response.toString())
            var user:String=jsonobject.getString("success")
            //Toast.makeText(this,user,Toast.LENGTH_LONG).show()
            if (user.equals("true"))
            {
                showDialog()
            }
            else
            {
                val intent= Intent(this,Payment_method::class.java)
                startActivity(intent)
                finish()
            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        }, { error -> error.printStackTrace() })
        requestQueue?.add(request)



    }

    private fun showDialog() {
        val dialog = Dialog(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.successalert)
        lateinit var button: LinearLayout


        button = dialog.findViewById(R.id.payment_success)!!

        button.setOnClickListener {
            dialog.dismiss()
            val intent=Intent(this,TipInformation::class.java)
            startActivity(intent)
            finish()
        }




        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        //dialog.window?.setLayout(700,750)

    }
/*    fun start_staus()
    {

        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)

        var driver_vec_details: Call<BookingStatusResponse> = APIUtils.getServiceAPI()!!.booking_status(request)

        driver_vec_details.enqueue(object : Callback<BookingStatusResponse> {
            override fun onResponse(call: Call<BookingStatusResponse>, response: Response<BookingStatusResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {




                    }
                    else {

                        Toast.makeText(this@TipInformation,response.body()!!.msg, Toast.LENGTH_LONG)
                            .show()
                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(this@TipInformation,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<BookingStatusResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(this@TipInformation,"Weak Internet Connection", Toast.LENGTH_LONG).show()
                customprogress.hide()

            }

        })
    }*/

}