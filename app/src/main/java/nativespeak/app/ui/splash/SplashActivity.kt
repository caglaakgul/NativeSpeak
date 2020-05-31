package nativespeak.app.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.animation.OvershootInterpolator
import nativespeak.app.R
import nativespeak.app.base.view.BaseActivity
import nativespeak.app.databinding.ActivitySplashBinding
import android.os.Handler
import android.util.Log
import nativespeak.app.ui.login.LoginActivity

class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>(SplashViewModel::class.java) {
    override fun getLayoutRes(): Int = R.layout.activity_splash

    private val handler = Handler()
    override fun init() {
        setupAnimations()

        if (intent.extras != null){
            val senderId = intent.extras?.getString("messageSenderId")
            Log.i("testtest",senderId)
        }


        handler.postDelayed({
         LoginActivity.start(this)
            supportFinishAfterTransition()
        }, 5000)
    }


    private fun setupAnimations() {
        binding.imageViewForeground.post {
            ObjectAnimator.ofFloat(binding.imageViewForeground, "translationY", binding.imageViewForeground.height.toFloat(), 0F).apply {
                duration = 1500
                interpolator = OvershootInterpolator()
                start()
            }
        }

        binding.imageViewLogo.post {
            val scaleY = ObjectAnimator.ofFloat(binding.imageViewLogo, "scaleY", 1f, 1.5f, 1f)
            val scaleX = ObjectAnimator.ofFloat(binding.imageViewLogo, "scaleX", 1f, 1.5f, 1f)

            AnimatorSet().apply {
                playTogether(scaleX, scaleY)
                duration = 1500
                startDelay = 1500
                start()
            }

            // Rotation
            ObjectAnimator.ofFloat(binding.imageViewLogo, "rotation", 0f, 360f).apply {
                duration = 3000
                startDelay = 3000
                start()
            }
        }

        binding.textViewAppName.post {
            ObjectAnimator.ofFloat(binding.textViewAppName, "alpha", 0F, 1F).apply {
                duration = 1500
                startDelay = 3000
                start()
            }
        }

    }
}