package nativespeak.app.ui.discover

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.afollestad.materialdialogs.MaterialDialog
import nativespeak.app.R
import nativespeak.app.base.view.BaseActivity
import nativespeak.app.data.UserData
import nativespeak.app.databinding.ActivityDiscoverBinding
import nativespeak.app.ui.login.LoginActivity
import nativespeak.app.ui.message.MessageActivity
import nativespeak.app.ui.settings.SettingsActivity

class DiscoverActivity : BaseActivity<DiscoverViewModel, ActivityDiscoverBinding>(DiscoverViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.activity_discover

    override fun init() {
        super.init()
        binding.vm = viewModel


        viewModel.liveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is DiscoverViewModel.State.OnDiscoverResponse -> onDiscoverResponse(it.success, it.data)
            }
        })

        binding.btnSettings.setOnClickListener {
            SettingsActivity.startAct(this@DiscoverActivity)
        }

        binding.btnFind.setOnClickListener {
            showProgress()
            viewModel.findUser(binding.edtFindUser.text.toString().trim())
        }

        /* binding.btnLogout.setOnClickListener {
              val sharedPreferences: SharedPreferences = getSharedPreferences("java.nativespeak.app", Context.MODE_PRIVATE)
              var editor : SharedPreferences.Editor = sharedPreferences.edit()
             editor.clear()
             editor.apply()
             finish()
         }*/
    }

    private fun onDiscoverResponse(success: Boolean?, data: UserData?) {
        dismissProgress()
        if (success == true) MessageActivity.start(this, data)
        else {
            MaterialDialog(this).show {
                title(text = "Uyarı")
                message(text = "Kullanıcı bulunamadı!")
                positiveButton(text = "OK") {}
            }

        }

        /*Handler().postDelayed({
        },3000)*/
    }

    override fun onResume() {
        super.onResume()
        viewModel.name.value = prefUtil.getUsername()
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, DiscoverActivity::class.java))
        }
    }

}