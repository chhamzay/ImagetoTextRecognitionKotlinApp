package com.example.imagetotextrecognitionapps

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var result:EditText
    private lateinit var camerabtn:ImageView
    private lateinit var erasebtn:ImageView
    private lateinit var copybtn:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        result = findViewById(R.id.result)
        camerabtn= findViewById(R.id.camera)
        erasebtn= findViewById(R.id.cancel)
        copybtn= findViewById(R.id.copy)


        camerabtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(packageManager)!=null){
                startActivityForResult(intent,123)
            }
            else{
                Toast.makeText(this,"Oops Something Wrong !!!",Toast.LENGTH_SHORT).show()
            }
        }

        erasebtn.setOnClickListener {
            clearAllText(result)
        }

        copybtn.setOnClickListener {
            copyTexttoClipBoard(this,result.text.toString())
        }

    }


    fun clearAllText(text: EditText){
        text.setText("")
    }

    fun copyTexttoClipBoard(context: Context,text:String){
        val clipboad = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label",text)
        clipboad.setPrimaryClip(clip)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123 && resultCode == RESULT_OK){
            if(data!=null){
                val extras = data.extras
                val bitmap = extras?.get("data") as Bitmap
                detectTextUsingML(bitmap)
            }

        }
    }

    private fun detectTextUsingML(bitmap: Bitmap) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = InputImage.fromBitmap(bitmap, 0)

        val result = recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Task completed successfully
                Log.d("Text",visionText.text.toString());
                result.setText(visionText.text.toString())
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                Toast.makeText(this,"Opps Something is Wrong to Convert Data in Text Format",Toast.LENGTH_SHORT).show()
            }

    }

}