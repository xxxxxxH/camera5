package ftp.summer.suncam.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.roger.catloadinglibrary.CatLoadingView
import ftp.summer.suncam.R
import ftp.summer.suncam.utils.click
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OptionAge : LinearLayout {

    private val loadingView by lazy {
        CatLoadingView()
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context): View {
        val v = LayoutInflater.from(context).inflate(R.layout.option_age, this, true)
        v.findViewById<ImageView>(R.id.age1).click { itemClick() }
        v.findViewById<ImageView>(R.id.age2).click { itemClick() }
        v.findViewById<ImageView>(R.id.age3).click { itemClick() }
        v.findViewById<ImageView>(R.id.age4).click { itemClick() }
        return v
    }

    private fun itemClick() {
        (context as AppCompatActivity).apply {
            lifecycleScope.launch(Dispatchers.IO) {
                loadingView.show(supportFragmentManager, "")
                delay(3000)
                withContext(Dispatchers.Main) {
                    loadingView.dismiss()
                }
            }
        }
    }
}