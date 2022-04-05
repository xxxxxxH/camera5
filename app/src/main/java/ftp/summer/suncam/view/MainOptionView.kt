package ftp.summer.suncam.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import ftp.summer.suncam.R
import ftp.summer.suncam.ui.activity.ActivityHomePage
import ftp.summer.suncam.ui.activity.ActivityPuzzle
import ftp.summer.suncam.utils.*

class MainOptionView : LinearLayout {

    private var optionSticker: ImageView? = null
    private var optionSlimming: ImageView? = null
    private var optionCartoon: ImageView? = null
    private var optionPuzzle: ImageView? = null
    private var optionAge: ImageView? = null
    private var optionCamera: ImageView? = null

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
        val v = LayoutInflater.from(context).inflate(R.layout.layout_main_option, this, true)
        optionSticker = v.findViewById(R.id.optionSticker)
        optionSticker.optionClick {
            (context as ActivityHomePage).showInsertAd()
            (context as AppCompatActivity).openAlbum()
        }
        optionSlimming = v.findViewById(R.id.optionSlimming)
        optionSlimming.optionClick {
            (context as ActivityHomePage).showInsertAd()
            (context as AppCompatActivity).openAlbum()
        }
        optionCartoon = v.findViewById(R.id.optionCartoon)
        optionCartoon.optionClick {
            (context as ActivityHomePage).showInsertAd()
            (context as AppCompatActivity).openAlbum()
        }
        optionPuzzle = v.findViewById(R.id.optionPuzzle)
        optionPuzzle.optionClick {
            (context as ActivityHomePage).showInsertAd()
            (context as AppCompatActivity).next(ActivityPuzzle::class.java, false)
        }
        optionAge = v.findViewById(R.id.optionAge)
        optionAge.optionClick {
            (context as ActivityHomePage).showInsertAd()
            (context as AppCompatActivity).openAlbum()
        }
        optionCamera = v.findViewById(R.id.optionCamera)
        optionCamera?.click {
            (context as AppCompatActivity).openCamera()
        }
        return v
    }

}