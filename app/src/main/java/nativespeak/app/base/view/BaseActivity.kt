package nativespeak.app.base.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerAppCompatActivity
import nativespeak.app.R
import nativespeak.app.base.viewmodel.BaseViewModel
import nativespeak.app.util.PrefUtil
import javax.inject.Inject

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>(private val mViewModelClass: Class<VM>) : DaggerAppCompatActivity(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var prefUtil: PrefUtil

    @LayoutRes
    abstract fun getLayoutRes(): Int

    private var progressDialog: Dialog? = null

    val binding by lazy { DataBindingUtil.setContentView(this, getLayoutRes()) as DB }
    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(mViewModelClass) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        init()
    }

    fun showProgress() {
        if (progressDialog == null) {
            progressDialog = Dialog(this).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCancelable(false)
                setContentView(R.layout.dialog_progress)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
        if (progressDialog?.isShowing == false) progressDialog?.show()
    }

    fun dismissProgress() {
        progressDialog?.let {
            if (it.isShowing) { it.dismiss() }
        }
    }

    open fun init() {}
}