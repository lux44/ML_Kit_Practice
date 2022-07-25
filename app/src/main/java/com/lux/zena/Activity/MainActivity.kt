package com.lux.zena.Activity

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeechService
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.loader.content.CursorLoader
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.lux.zena.Activity.databinding.ActivityMainBinding
import gun0912.tedimagepicker.builder.TedImagePicker
import gun0912.tedimagepicker.builder.type.MediaType
import java.io.InputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    private val binding:ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var mlText:String
    lateinit var image: InputImage

    lateinit var tts:TextToSpeech


    val recognizerKor = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())


    fun recognizeText(image:InputImage){
        val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

        val result = recognizer.process(image)
            .addOnSuccessListener {
                Log.e("TAG","SUCCESS")
                mlText = it.text
                binding.tv.text = mlText
            }
            .addOnFailureListener {
                Log.e("TAG","FAIL : $it")
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        binding.tv.movementMethod = ScrollingMovementMethod()

        binding.btnSelectPhoto.setOnClickListener {
            TedImagePicker.with(this)
                .mediaType(MediaType.IMAGE)
                .start {
                    //setImage(it)
                    binding.iv.setImageURI(it)
                    image= InputImage.fromFilePath(this,it)
                }
        }

        binding.btnRecogText.setOnClickListener {

            recognizeText(image)
//            if (result.isComplete) {
//                Log.e("RESULT","${result.result}")
//                binding.tv.text=result.result.toString()
//            }else Log.e("TASK","is not yet complete")
        }

        tts = TextToSpeech(this, TextToSpeech.OnInitListener { status->
            if (status == TextToSpeech.SUCCESS){
                tts.language = Locale.UK
            }
        })

        binding.btnTts1.setOnClickListener {
            tts.setPitch(1.0f)
            tts.setSpeechRate(1.0f)
            tts.speak(mlText,TextToSpeech.QUEUE_FLUSH, null)
        }

        binding.btnTts2.setOnClickListener {
            tts.setPitch(2.0f)
            tts.setSpeechRate(1.0f)
            tts.speak(mlText,TextToSpeech.QUEUE_FLUSH,null)
        }










    }






}