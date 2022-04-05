package ftp.summer.suncam.ui.activity

import ftp.summer.suncam.R
import ftp.summer.suncam.base.BaseActivity
import ftp.summer.suncam.utils.*
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.layout_float.*

class ActivityImage : BaseActivity(R.layout.activity_image) {

    private val url by lazy {
        intent.getStringExtra("url")
    }

    override fun onConvert() {
        url?.let {
            imageView.loadWith(it)
            shareFb.click { _ -> shareWithFacebook(it) }
            shareIns.click { _ -> shareWithIns(it) }
            shareEmail.click { _ -> shareWithEmail(it) }
        }
    }
}