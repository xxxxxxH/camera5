package com.sweetcam.app.view.dialog

import android.app.Dialog
import android.view.View
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import com.sweetcam.app.R

class LoadingDlg:AAH_FabulousFragment() {
    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = View.inflate(context, R.layout.dialog_share, null)
        setViewMain(v.findViewById(R.id.main))
        setMainContentView(v)
        super.setupDialog(dialog, style)
    }
}