package com.doubtnut.doubtnuttest.view

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import app.co.uk.tensorflow.util.Keys.INPUT_SIZE
import com.doubtnut.doubtnuttest.R
import com.doubtnut.doubtnuttest.util.ImageClassifier
import io.reactivex.rxkotlin.subscribeBy
import java.io.FileNotFoundException


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAKE_PICTURE = 1
    private val OPEN_GALLERY = 2
    private var imageUri: Uri? = null
    private lateinit var photoImage: Bitmap
    private lateinit var classifier: ImageClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        classifier = ImageClassifier(assets)

        val camera = findViewById<Button>(R.id.camera_btn)
        val gallery = findViewById<Button>(R.id.gallery_btn)

        camera.setOnClickListener(this)
        gallery.setOnClickListener(this)
    }


    private fun openGallery() {
        try {
            val intent = Intent()
            intent.type = "image/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            }
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), OPEN_GALLERY)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) run {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            } else {
                try {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.TITLE, "image.jpg")
                    imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                    val intentPicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(intentPicture, TAKE_PICTURE)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            super.onActivityResult(requestCode, resultCode, data)
            when (requestCode) {
                TAKE_PICTURE -> if (resultCode == Activity.RESULT_OK) {
                    val selectedImage = imageUri
                    ProcessImage(selectedImage!!)
                }
                OPEN_GALLERY -> if (resultCode == Activity.RESULT_OK) {
                    if (data?.data != null) {
                        ProcessImage(data.data)
                    } else {
                        Toast.makeText(this, "Select a image to process", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Select a image to process", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Log.e("e", e.toString())
            Toast.makeText(this, "Opps, Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    private fun ProcessImage(data: Uri) {
        try {
            val stream = contentResolver!!.openInputStream(data)
            if (::photoImage.isInitialized) photoImage.recycle()
            photoImage = BitmapFactory.decodeStream(stream)
            photoImage = Bitmap.createScaledBitmap(photoImage, INPUT_SIZE, INPUT_SIZE, false)
            classifier.recognizeImage(photoImage).subscribeBy(
                    onSuccess = {
                        Log.e("text", it.toString())
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra("data", it.toString())
                        startActivity(intent)
                    }
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
                    openCamera()
                } else {
                    //Permission Denied :
                    Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_LONG).show()
                }
            }
            1 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission granted
                    openCamera()
                } else {
                    //Permission Denied :
                    Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.camera_btn -> {
                openCamera()
            }
            R.id.gallery_btn -> {
                openGallery()
            }
        }
    }
}
