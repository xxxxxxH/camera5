package ftp.summer.suncam.view

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.lcw.library.stickerview.Sticker
import com.lcw.library.stickerview.StickerLayout
import ftp.summer.suncam.R
import ftp.summer.suncam.utils.loadWith
import kotlinx.android.synthetic.main.activity_stickers.*

class OptionMainView:LinearLayout {

    private var stickerBg:ImageView?=null
    private var sticker:StickerLayout?=null
    private var mainRel:RelativeLayout?=null

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

    private fun initView(context: Context) :View{
        val v = LayoutInflater.from(context).inflate(R.layout.option_activity_main, this, true)
        stickerBg = v.findViewById(R.id.stickerBg)
        sticker = v.findViewById(R.id.sticker)
        mainRel = v.findViewById(R.id.mainRel)
        return v
    }

    fun getMainRel():RelativeLayout{
        return mainRel!!
    }

    fun setbg(any:Any){
        stickerBg?.loadWith(any)
    }

    fun addSticker(b:Bitmap){
        sticker?.addSticker(Sticker(context, b))
    }
}