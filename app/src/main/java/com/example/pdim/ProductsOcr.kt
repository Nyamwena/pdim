package com.example.pdim

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.android.synthetic.main.activity_products_ocr.*
import java.io.FileDescriptor
import java.io.IOException


class ProductsOcr : AppCompatActivity() {
    private var bitmap: Bitmap? = null
    private val TAG = "Productocr"
    lateinit var detect_btn: Button
    lateinit var path_select_view: Button
    lateinit var  path_et : EditText
    private var previewImage: ImageView? = null
    private var imageFilePath: String? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_ocr)
//        path_select_view.setOnClickListener {
//            if (!checkStoragePermission()) {
//                requestPermissions(listOf(storage_permission).toTypedArray(), 100)
//            } else {
//                pickImage()
//            }
//        }
        val bundle = intent.extras
        imageFilePath = bundle!!.getString("IMAGE_URI")
        Log.d(TAG, "Image File Path:\t$imageFilePath")
        previewImage = findViewById(R.id.text_image)
        detect_btn = findViewById(R.id.detect_btn)

         bitmap = BitmapFactory.decodeFile(imageFilePath)
        if (bitmap != null){
            previewImage!!.setImageBitmap(bitmap)
        }

        val recognizer = TextRecognition.getClient()

        detect_btn.setOnClickListener() {
            bitmap?.let {
                val image = InputImage.fromBitmap(it, 0)
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        // textBlocks -> will return list of block of detected text
                        // lines -> will return list of detected lines
                        // elements -> will return list of detected words
                        // boundingBox -> will return rectangle box area in bitmap
                        Toast.makeText(this, visionText.text, Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_SHORT).show()
                    }
            }
            if (bitmap == null) Toast.makeText(this, "Please select image!", Toast.LENGTH_SHORT)
                .show()

        }
    }

    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkStoragePermission(): Boolean {
        return checkSelfPermission(storage_permission) == PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if (isAllPermissionGranted(permissions)) {
                    pickImage()
                } else {
                    Toast.makeText(this, "Please grant storage permission!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_IMAGE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        data?.data?.let {
                            path_et.setText(it.path)
                            Log.e(TAG, "Uri: $it")
                            bitmap = null
                            bitmap = getBitmapFromUri(it);
                            Glide.with(text_image)
                                .load(bitmap)
                                .into(text_image)
                        }

                    }
                    RESULT_CANCELED -> {
                        bitmap = null
                        Toast.makeText(this, "Please select valid image!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isAllPermissionGranted(permissions: Array<out String>): Boolean {
        permissions.forEach {
            if (checkSelfPermission(it) != PERMISSION_GRANTED) return false
        }
        return true
    }

    companion object {
        const val storage_permission = Manifest.permission.READ_EXTERNAL_STORAGE
        const val PICK_IMAGE = 101
    }

    /**
     * credits: https://stackoverflow.com/a/21517011
     */
    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }
}