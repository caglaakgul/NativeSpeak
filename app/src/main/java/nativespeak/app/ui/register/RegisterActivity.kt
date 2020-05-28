package nativespeak.app.ui.register

import android.content.Intent
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.google.firebase.iid.FirebaseInstanceId
import nativespeak.app.R
import nativespeak.app.base.view.BaseActivity
import nativespeak.app.databinding.ActivityRegisterBinding
import nativespeak.app.ui.login.LoginActivity

class RegisterActivity : BaseActivity<RegisterViewModel, ActivityRegisterBinding>(RegisterViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.activity_register

    override fun init() {
        super.init()

        viewModel.getCountries()

        viewModel.liveData.observe(this, Observer {
            when (it) {
                is RegisterViewModel.State.OnCountryListResponse -> onCountryListResponse()
                is RegisterViewModel.State.OnRegisterResponse -> onRegisterResponse()
            }
        })

        binding.btnRegister.setOnClickListener {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {instanceIdResult->
                val token = instanceIdResult.token
                viewModel.register(binding.edtUsername.text.toString().trim(), binding.edtPassword.text.toString().trim(),token)
            }

        }

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

    }


    private fun onRegisterResponse() {
        startActivity(Intent(this,LoginActivity::class.java))
    }

    private fun onCountryListResponse() {
        binding.language.setOnClickListener {
            MaterialDialog(this).show {
                title(text = "Select Language")
                listItems(items = viewModel.countryList.map { it.name ?: "" }) { dialog, index, text ->
                    binding.language.text = text
                    viewModel.selectedCountryId = index + 1
                }
            }
        }
    }

}