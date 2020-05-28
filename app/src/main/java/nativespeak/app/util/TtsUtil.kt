package nativespeak.app.util

import android.app.Activity
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.*

class TtsUtil (private val activity: Activity, private val message: String) : TextToSpeech.OnInitListener {

    private val tts : TextToSpeech = TextToSpeech(activity,this)

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            //val localeTR = Locale("tr","TR")
            val localeUK = Locale.ENGLISH

            val result : Int
            result = tts.setLanguage(localeUK)

            if(result == TextToSpeech.LANG_NOT_SUPPORTED)
                Toast.makeText(activity,"This Language is not supported", Toast.LENGTH_LONG).show()
            else
                speakOut(message)
        } else
            Toast.makeText(activity,"Initilization Failed!",Toast.LENGTH_LONG).show()

    }

    private fun speakOut(message: String){
        tts.speak(message, TextToSpeech.QUEUE_FLUSH,null,null)
    }

}