package www.gericass.com.toilet.ui.binding

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("image")
    fun loadImage(v: ImageView, imageUrl: String) {
        Picasso.get().load(imageUrl).fit().centerCrop().into(v)
    }
}