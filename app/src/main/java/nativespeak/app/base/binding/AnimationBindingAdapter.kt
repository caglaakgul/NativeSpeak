package nativespeak.app.base.binding

import android.animation.ObjectAnimator
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import nativespeak.app.util.ThemeUtil

class AnimationBindingAdapter {

    companion object {
        @BindingAdapter("bottomToTop")
        @JvmStatic
        fun animationBottomToTop(view: ImageView, duration: Long) {
            view.post {
                val anim = ObjectAnimator.ofFloat(view, "translationY", view.height.toFloat(), ThemeUtil.dpToPx(view.context, 180).toFloat())
                anim.duration = 1300
                anim.start()
            }
        }
    }
}