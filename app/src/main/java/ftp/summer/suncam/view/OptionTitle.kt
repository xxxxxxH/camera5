package ftp.summer.suncam.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ftp.summer.suncam.R
import ftp.summer.suncam.utils.click

class OptionTitle : LinearLayout {

    private var cancel: RelativeLayout? = null
    private var title: TextView? = null
    private var save: RelativeLayout? = null

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
        val v = LayoutInflater.from(context).inflate(R.layout.layout_option_title, this, true)
        cancel = v.findViewById(R.id.cancel)
        cancel?.click {
            (context as AppCompatActivity).finish()
        }
        title = v.findViewById(R.id.title)
        save = v.findViewById(R.id.save)
        return v
    }

    fun setTitle(s:String){
        title?.let {
            it.apply {
                text = s
            }
        }
    }

    fun getSaveBtn():RelativeLayout{
        return save!!
    }
}