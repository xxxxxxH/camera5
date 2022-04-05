package com.sweetcam.app.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.sweetcam.app.R
import com.sweetcam.app.utils.loadWith

class StickerItemView : LinearLayout {
    private var image1: ImageView? = null
    private var image2: ImageView? = null
    private var image3: ImageView? = null
    private var image4: ImageView? = null

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
        val v = LayoutInflater.from(context).inflate(R.layout.item_stciker_view, this, true)
        image1 = v.findViewById(R.id.image1)
        image2 = v.findViewById(R.id.image2)
        image3 = v.findViewById(R.id.image3)
        image4 = v.findViewById(R.id.image4)
        return v
    }

    fun setImage1(id: Int) {
        image1?.loadWith(id)
    }

    fun getImage1(): ImageView {
        return image1!!
    }

    fun setImage2(id: Int) {
        image2?.loadWith(id)
    }

    fun getImage2(): ImageView {
        return image2!!
    }

    fun setImage3(id: Int) {
        image3?.loadWith(id)
    }

    fun getImage3(): ImageView {

        return image3!!
    }

    fun setImage4(id: Int) {
        image4?.loadWith(id)
    }

    fun getImage4(): ImageView {
        return image4!!
    }
}