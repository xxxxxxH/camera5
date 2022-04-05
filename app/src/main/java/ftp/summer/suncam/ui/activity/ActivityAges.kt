package ftp.summer.suncam.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ftp.summer.suncam.R
import ftp.summer.suncam.base.BaseActivity
import ftp.summer.suncam.utils.*
import ftp.summer.suncam.view.OptionAge
import ftp.summer.suncam.view.OptionTitle
import ftp.summer.suncam.view.dialog.LoadingDlg
import ftp.summer.suncam.view.dialog.SaveDlg
import kotlinx.android.synthetic.main.activity_ages.*

class ActivityAges : BaseActivity(R.layout.activity_ages),IListener {
    private val title by lazy {
        OptionTitle(this)
    }
    private val saveDialog by lazy {
        SaveDlg(this)
    }

    private val loadingDialog by lazy {
        LoadingDlg()
    }

    private val fab by lazy {
        buildFloatActionButton()
    }

    private val option by lazy {
        OptionAge(this)
    }
    override fun onConvert() {
        optionMainView.setbg(selectImage)
        bottomOption.addView(title)
        bottomOption.addView(option)
        title.setTitle(targetClass)
        title.getSaveBtn().click {
            saveDialog.setParentFab(fab)
            saveDialog.show(supportFragmentManager,"")
        }
    }

    override fun click1() {
        saveDialog.dismiss()
        loadingDialog.setParentFab(fab)
        loadingDialog.show(supportFragmentManager,"")
    }

    override fun click2() {

    }
}