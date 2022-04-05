package com.sweetcam.app.ui.activity

import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.lcw.library.stickerview.Sticker
import com.sweetcam.app.R
import com.sweetcam.app.base.BaseActivity
import com.sweetcam.app.utils.*
import com.sweetcam.app.view.OptionTitle
import com.sweetcam.app.view.StickerItemView
import com.sweetcam.app.view.dialog.LoadingDlg
import com.sweetcam.app.view.dialog.SaveDlg
import kotlinx.android.synthetic.main.activity_stickers.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityStickers : BaseActivity(R.layout.activity_stickers), IListener {

    private val dialogSave by lazy {
        SaveDlg(this)
    }

    private val dialogLoading by lazy {
        LoadingDlg()
    }

    private val fab by lazy {
        buildFloatActionButton()
    }

    override fun onConvert() {
        loadingView.show(supportFragmentManager, "")
        stickerBg.loadWith(selectImage)
        lifecycleScope.launch(Dispatchers.IO) {
            delay(1000)
            withContext(Dispatchers.Main) {
                buildBottomView()
                loadingView.dismiss()
            }
        }
    }

    private fun buildBottomView() {
        bottomTitle.addView(createBottomTitle())
        optionLl.addView(createBottomOption1())
        optionLl.addView(createBottomOption2())
        optionLl.addView(createBottomOption3())
        optionLl.addView(createBottomOption4())
    }

    private fun createBottomTitle(): OptionTitle {
        val title = OptionTitle(this)
        title.apply {
            setTitle(targetClass)
            getSaveBtn().click {
                loadingView.show(supportFragmentManager, "")
                lifecycleScope.launch(Dispatchers.IO) {
                    ResourceUtils.createBitmapFromView(mainRel)
                    withContext(Dispatchers.Main) {
                        loadingView.dismiss()
                        dialogSave.setParentFab(fab)
                        dialogSave.show(supportFragmentManager, dialogSave.tag)
                    }
                }
            }
        }
        return title
    }

    private fun createBottomOption1(): StickerItemView {
        val item = StickerItemView(this)
        item.setImage1(R.mipmap.sticker_101)
        item.setImage2(R.mipmap.sticker_201)
        item.setImage3(R.mipmap.sticker_301)
        item.setImage4(R.mipmap.sticker_401)
        item.getImage1().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage2().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage3().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage4().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        return item
    }

    private fun createBottomOption2(): StickerItemView {
        val item = StickerItemView(this)
        item.setImage1(R.mipmap.sticker_501)
        item.setImage2(R.mipmap.sticker_601)
        item.setImage3(R.mipmap.sticker_701)
        item.setImage4(R.mipmap.sticker_801)
        item.getImage1().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage2().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage3().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage4().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        return item
    }

    private fun createBottomOption3(): StickerItemView {
        val item = StickerItemView(this)
        item.setImage1(R.mipmap.sticker_901)
        item.setImage2(R.mipmap.sticker_1001)
        item.setImage3(R.mipmap.sticker_1101)
        item.setImage4(R.mipmap.sticker_1201)
        item.getImage1().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage2().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage3().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage4().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        return item
    }

    private fun createBottomOption4(): StickerItemView {
        val item = StickerItemView(this)
        item.setImage1(R.mipmap.sticker_1301)
        item.setImage2(R.mipmap.sticker_1401)
        item.setImage3(R.mipmap.sticker_1501)
        item.setImage4(R.mipmap.sticker_1601)
        item.getImage1().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage2().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage3().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        item.getImage4().click {
            val b = (it as ImageView).drawable as BitmapDrawable
            sticker.addSticker(Sticker(this, b.bitmap))
        }
        return item
    }

    override fun click1() {
        dialogSave.dismiss()
        dialogLoading.setParentFab(fab)
        dialogLoading.show(supportFragmentManager, dialogLoading.tag)
    }

    override fun click2() {

    }
}