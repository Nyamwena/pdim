package com.example.pdim

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject


class ProductCatelogue : AppCompatActivity() {
    private var listView: ListView? = null
    private var productList: MutableList<Products>? = null
    private var textView : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_catelogue)
        setSupportActionBar(findViewById(R.id.toolbar))

textView = findViewById(R.id.message)
        listView = findViewById(R.id.listViewProducts) as ListView
        productList = mutableListOf<Products>()
        loadProducts()
    }
    private fun loadProducts() {
        val stringRequest = StringRequest(Request.Method.GET,
                Constants.GET_CATELOGUE,
                Response.Listener<String> { s ->
                    try {
                      //  val obj = JSONObject(s)
                        val strResp = s.toString()
                        val obj: JSONObject = JSONObject(strResp)
                       // Log.d("APi", strResp)
                     //   if (!obj.getBoolean("error")) {
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
                                productList!!.add(products)
                                val adapter = ProductList(this@ProductCatelogue, productList!!)
                                listView!!.adapter = adapter
                            }
 //                       }
//                        else {
////                            Toast.makeText(applicationContext, obj.getString("message"), Toast.LENGTH_LONG).show()
//                            textView?.text = obj.getString("message")
//                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, Response.ErrorListener { volleyError -> textView?.text = volleyError.message })
//            Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() })

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add<String>(stringRequest)
    }
}