package nativespeak.app.ui.message

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_message.*
import nativespeak.app.R
import nativespeak.app.base.view.BaseActivity
import nativespeak.app.data.UserData
import nativespeak.app.data.response.MessageListResponse
import nativespeak.app.databinding.ActivityMessageBinding
import nativespeak.app.ui.message.adapter.MessageAdapter
import nativespeak.app.util.TtsUtil


class MessageActivity : BaseActivity<MessageViewModel, ActivityMessageBinding>(MessageViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.activity_message

    lateinit var mTTS: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var intentSpeech: Intent

    lateinit var adapter: MessageAdapter

    private val messageReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val message = intent?.getParcelableExtra<MessageListResponse>("msg")

            message?.let {
                adapter.add(message,adapter.itemCount)
                recyclerViewMessages.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun init() {
        super.init()
        binding.vmUsername = viewModel

        intent.extras?.let {
            //viewModel.userData = it.getParcelable(EXTRA_USER)
            // viewModel.hisUsername.value = viewModel.userData?.username
            viewModel.setHisUserData(it.getParcelable(EXTRA_USER))
        }

        viewModel.getMessages()

        recyclerViewMessages.setHasFixedSize(true)

        viewModel.liveData.observe(this, Observer {
            when (it) {
                is MessageViewModel.State.OnMessageListResponse -> onMessageListResponse()
                is MessageViewModel.State.OnSendMessageSuccess -> onSendMessageSuccess(it.message)
            }
        })


        binding.imgSend.setOnClickListener { viewModel.translateText(binding.etText.text.toString()) }

        binding.imgVoice.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                showSpeechDialog()
            else
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 2)
        }


    }

    private fun showSpeechDialog() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        try {
            startActivityForResult(intent, 1)
        } catch (a: ActivityNotFoundException) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            binding.etText.setText(result?.get(0))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 2 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            showSpeechDialog()
    }

    private fun onSendMessageSuccess(message: MessageListResponse?) {
        adapter.add(message, adapter.itemCount)
        recyclerViewMessages.smoothScrollToPosition(adapter.itemCount - 1)
        binding.etText.setText("")
    }

    private fun onMessageListResponse() {
        binding.recyclerViewMessages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = MessageAdapter(viewModel.messageList ?: arrayListOf(), prefUtil.getId() ?: "")
        binding.recyclerViewMessages.adapter = adapter
        if(adapter.itemCount != 0)
        recyclerViewMessages.smoothScrollToPosition(adapter.itemCount - 1)

        adapter.onMessageListenClick = ::onMessageListenClick
        adapter.onTranslateListenClick = ::onTranslateListenClick
    }

    private fun onMessageListenClick(messageListResponse: MessageListResponse) {
        TtsUtil(this@MessageActivity, messageListResponse.message_text.toString())
        Log.i("MessageResponseTest", messageListResponse.message_text.toString())
    }

    private fun onTranslateListenClick(messageListResponse: MessageListResponse) {
        TtsUtil(this@MessageActivity, messageListResponse.translate_text.toString())
        Log.i("MessageResponseTest", messageListResponse.translate_text.toString())

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(messageReceiver, IntentFilter("UpdateChatActivity"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(messageReceiver)
    }


    companion object {
        const val EXTRA_USER = "nativespeak.app.ui.message.EXTRA_USER"
        fun start(activity: Activity, userData: UserData?) {
            // let, apply, run
            /* val intent = Intent(activity,MessageActivity::class.java)
             intent.putExtra(EXTRA_USER,userData)*/
            activity.startActivity(Intent(activity, MessageActivity::class.java).apply {
                putExtra(EXTRA_USER, userData)
            })
        }
    }
}
