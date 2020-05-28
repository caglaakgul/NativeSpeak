package nativespeak.app.ui.login

import android.content.Intent
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.google.firebase.iid.FirebaseInstanceId
import nativespeak.app.R
import nativespeak.app.base.view.BaseActivity
import nativespeak.app.data.UserData
import nativespeak.app.databinding.ActivityLoginBinding
import nativespeak.app.ui.discover.DiscoverActivity
import nativespeak.app.ui.register.RegisterActivity

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(LoginViewModel::class.java){
    val PREFS_FILENAME = "java.nativespeak.app"



    override fun getLayoutRes(): Int = R.layout.activity_login

    override fun init() {
        super.init()

        viewModel.liveData.observe(this, Observer {
            when (it) {
                is LoginViewModel.State.OnLoginResponse -> onLoginResponse(it.success,it.data)
            }
        })

        binding.btnLogin.setOnClickListener {
            showProgress()
            onLogin(binding.edtLgnUsername.text.toString().trim(), binding.edtLgnPassword.text.toString().trim())
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

    }

    private fun onLogin(username: String, password: String) {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val token = instanceIdResult.token
            viewModel.login(username, password, token)
        }
    }

    private fun onLoginResponse(success: Boolean, data: UserData?){
        dismissProgress()
        if(success){
            prefUtil.login(data)
            DiscoverActivity.start(this@LoginActivity)
            supportFinishAfterTransition()
        }else{
            MaterialDialog(this).show {
                title(text = "Login")
                message(text = "Incorrect entry!")
                positiveButton(text = "OK"){}
            }
        }
    }

}