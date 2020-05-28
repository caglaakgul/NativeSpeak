package nativespeak.app.ui.settings

import android.app.Activity
import androidx.lifecycle.Observer
import nativespeak.app.R
import nativespeak.app.base.view.BaseActivity
import android.content.Intent
import nativespeak.app.databinding.ActivitySettingsBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems


class SettingsActivity : BaseActivity<SettingsViewModel, ActivitySettingsBinding>(SettingsViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.activity_settings

    override fun init() {
        setupClickListeners()
        binding.etUsername.setText(prefUtil.getUsername().toString())

        viewModel.getCountry()
        showProgress()

        viewModel.liveData.observe(this, Observer {
            when (it) {
                is SettingsViewModel.State.OnUpdateCountryResponse -> onUpdateCountryResponse()
                is SettingsViewModel.State.OnSaveUpdate -> onSaveUpdate(it.success)
            }
        })

        binding.btnSave.setOnClickListener {
            viewModel.updateData(binding.etUsername.text.toString(), binding.etPassword.text.toString())
        }
    }

    private fun onSaveUpdate(success: Boolean) {
        val message = if (success) {
            prefUtil.update(binding.etUsername.text.toString(), viewModel.selectedCountry.code ?: "")
            "Successfully updated!"
        } else "Incorrect entry!"

        MaterialDialog(this).show {
            title(text = "Update")
            message(text = message)
            positiveButton(text = "OK") {}
        }
    }

    private fun onUpdateCountryResponse() {
        dismissProgress()
        val myCountry = viewModel.countryList.filter { it.code == prefUtil.getCountryCode() }.first()
        viewModel.selectedCountry = myCountry
        binding.editlanguage.setText(myCountry.name)
        binding.imgView.setImageResource(resources.getIdentifier(myCountry.code.toString(), "drawable", application.packageName))
    }

    private fun setupClickListeners() {
        binding.editlanguage.setOnClickListener {
            showCountryDialog()
        }
    }

    private fun showCountryDialog() {
        MaterialDialog(this).show {
            title(text = "Select Language")
            listItems(items = viewModel.countryList.map { it.name ?: "" }) { dialog, index, text ->
                binding.editlanguage.text = text
                viewModel.selectedCountry = viewModel.countryList[index]
                binding.imgView.setImageResource(
                    resources.getIdentifier(
                        viewModel.selectedCountry.code.toString(),
                        "drawable",
                        application.packageName
                    )
                )
            }
        }
    }

    companion object {
        fun startAct(activity: Activity) {
            activity.startActivity(Intent(activity, SettingsActivity::class.java))
        }
    }

}