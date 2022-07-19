package com.lux.zena.Activity

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.loader.content.CursorLoader
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.lux.zena.Activity.databinding.ActivityMainBinding
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import java.io.InputStream


class MainActivity : AppCompatActivity() {

    private val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var mlText:String

    fun getRealPathFromUri(uri:Uri) : String{
        val proj= arrayOf(MediaStore.Images.Media.DATA)
        val loader: CursorLoader =CursorLoader(this,uri,proj,null,null,null)
        val cursor: Cursor? =loader.loadInBackground()
        val column_index:Int= cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result:String=cursor.getString(column_index)
        cursor.close()
        return result
    }

    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        binding.btnSelectPhoto.setOnClickListener {
            TedImagePicker.with(this)
                .mediaType(MediaType.IMAGE)
                .start {
                    val image:InputImage = InputImage.fromFilePath(this,it)
                    recognizer.process(image)
                        .addOnSuccessListener { result->
                            Log.e("SUCCESS","$result")
                            mlText= result.toString()
                        }
                        .addOnFailureListener { e->
                            Log.e("FAIL","error : $e")
                        }
                }
        }

        binding.btnRecogText.setOnClickListener {
            binding.tv.text = mlText
        }


    }


}