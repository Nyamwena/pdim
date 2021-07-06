package com.example.pdim

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class ProductResults : AppCompatActivity() {
    private lateinit var textView: TextView
    private  lateinit var errorTxt: TextView
    private  lateinit var productDscTxt: TextView
    private  lateinit var priceTxt: TextView
    private  lateinit var siTxt: TextView
    private  lateinit var expTxt: TextView
    private  lateinit var manuTxt: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_results)
        textView = findViewById(R.id.items_results)
        productDscTxt = findViewById(R.id.productDsc)
        priceTxt = findViewById(R.id.price)
        siTxt = findViewById(R.id.siUnit)
        expTxt = findViewById(R.id.exDate)
        manuTxt = findViewById(R.id.manuDate)

        val message = intent.getStringExtra("barcode")

        val messageTextView: TextView = findViewById(R.id.txtResults)
        messageTextView.text = message
        getProduct(message.toString())
    }

    private fun getProduct(barcode: String) {
        val URL_BARCODE: String =  Constants.GET_PRODUCT + barcode
        val stringRequest = StringRequest(Request.Method.GET,
           URL_BARCODE,
            Response.Listener<String> { s ->
                try {
                    //  val obj = JSONObject(s)
                    val strResp = s.toString()
                    val obj: JSONObject = JSONObject(strResp)
                    Log.d("APi", strResp)
//                if (!obj.getBoolean("error")) {
                    val array = obj.getJSONArray("products")

                    for (i in 0 until array.length()) {
                        val objectArtist = array.getJSONObject(i)
                        val products= Products(
                            objectArtist.getString("productName"),
                            objectArtist.getString("productDescription"),
                            objectArtist.getString("price"),
                            objectArtist.getString("siUnit"),
                            objectArtist.getString("expiryDate"),
                            objectArtist.getString("manufactureDate")

                        )
                        Log.d("Product", objectArtist.getString("productName"))
                        textView.text = "Product Name: " + objectArtist.getString("productName")
                        productDscTxt.text = "Product Description: " + objectArtist.getString("productDescription")
                        priceTxt.text = "The Price is $" + objectArtist.getString("price")
                        siTxt.text =  "" + objectArtist.getString("siUnit")
                        expTxt.text = "The expiry date is: " + objectArtist.getString("expiryDate")
                        manuTxt.text = "The manufacture date is: " + objectArtist.getString("manufactureDate")

                    }
          //      }
//                        else {
//                     // Toast.makeText(applicationContext, obj.getString("success"), Toast.LENGTH_LONG).show()
//                   errorTxt.text = obj.getString("success")
//
//                        }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { volleyError -> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() })

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add<String>(stringRequest)
    }
}