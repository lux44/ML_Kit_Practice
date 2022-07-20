package com.lux.zena.Activity

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.BitmapFactory
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
    lateinit var image: InputImage

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

    fun setImage (uri:Uri) {
        var cr  = contentResolver.openInputStream(uri)!!
        val bitmap = BitmapFactory.decodeStream(cr)
        binding.iv.setImageBitmap(bitmap)

        image = InputImage.fromBitmap(bitmap,0)
        Log.e("setImage","Bitmap")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        binding.btnSelectPhoto.setOnClickListener {
            TedImagePicker.with(this)
                .mediaType(MediaType.IMAGE)
                .start {
                    setImage(it)
                }
        }

        binding.btnRecogText.setOnClickListener {
                val result=recognizer.process(image)
                        .addOnSuccessListener {     result->
                            Log.e("SUCCESS","${result.text}")
                            mlText= result.text
                            binding.tv.text = mlText
                        }
                        .addOnFailureListener {     e->
                            Log.e("FAIL","error : $e")
                        }
                if (result.isComplete) {
                    Log.e("RESULT","${result.result}")
                    binding.tv.text=result.result.toString()
                }else Log.e("TASK","is not yet complete")




        }


    }


}