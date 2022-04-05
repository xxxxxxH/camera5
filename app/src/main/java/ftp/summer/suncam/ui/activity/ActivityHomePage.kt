package ftp.summer.suncam.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.jhworks.library.ImageSelector
import ftp.summer.suncam.R
import ftp.summer.suncam.base.BaseActivity
import ftp.summer.suncam.utils.*
import ftp.summer.suncam.view.dialog.ExitDlg
import ftp.summer.suncam.view.topon.TopOnNativeAdView
import kotlinx.android.synthetic.main.layout_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityHomePage : BaseActivity(R.layout.layout_main),IListener {

    private val exitDlg by lazy {
        ExitDlg(this)
    }

    private val fab by lazy {
        buildFloatActionButton()
    }

    override fun onConvert() {
        requestPermission2({
            mainAd.removeAllViews()
            mainAd.addView(TopOnNativeAdView(this))
        }, {
            lifecycleScope.launch(Dispatchers.IO) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    showToast("no permissions app will finish")
                    finish()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) {
                val temp = ImageSelector.getSelectResults(data)
                selectImage = temp!![0]
                nextByTargetClass()
            }
        }
    }

    override fun onBackPressed() {
        exitDlg.setParentFab(fab)
        exitDlg.show(supportFragmentManager, "")
    }

    override fun click1() {
        exitDlg.dismiss()
        finish()
    }

    override fun click2() {
        exitDlg.dismiss()
    }
}