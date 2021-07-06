package com.example.pdim

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : AppCompatActivity() {
    lateinit var cardVBarcode: CardView
    lateinit var textView: TextView
    lateinit var cardVProductCata : CardView
    lateinit var cardVProductScan : CardView
    lateinit var cardVOcr : CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//        startActivityForResult(intent, 0)

        cardVBarcode = findViewById(R.id.scanItem)
        textView = findViewById(R.id.contentReader)
        cardVBarcode.setOnClickListener {
            val intentIntegrator = IntentIntegrator(this@MainActivity)
            intentIntegrator.setBeepEnabled(true)
            intentIntegrator.setCameraId(0)
            intentIntegrator.setPrompt("SCAN")
            intentIntegrator.setBarcodeImageEnabled(false)
            intentIntegrator.initiateScan();
        }


        cardVProductCata =   findViewById(R.id.productCatalogue);
        cardVProductCata.setOnClickListener{
            val intent = Intent(applicationContext, ProductCatelogue::class.java)
            startActivity(intent)
        }
        cardVProductScan = findViewById(R.id.clothesScanCard)

        cardVProductScan.setOnClickListener{
            val intent1 = Intent(applicationContext, ScanClothes::class.java)
            startActivity(intent1)
        }
        cardVOcr = findViewById(R.id.btnOcr)
        cardVOcr.setOnClickListener{
            val intent2 = Intent(applicationContext, TakePicture::class.java)
            startActivity(intent2)
        }
    }

    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("MainActivity", "Scanned")
//                Toast.makeText(this, "Scanned -> " + result.contents, Toast.LENGTH_SHORT)
//                        .show()
                val intent = Intent(applicationContext, ProductResults::class.java)
                intent.putExtra("barcode", result.contents )
                startActivity(intent)
                finish()
              //  textView.text = result.contents;
              //  textView.text = String.format("Scanned Result: %s", result)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}