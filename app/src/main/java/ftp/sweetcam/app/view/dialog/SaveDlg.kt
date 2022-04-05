package com.sweetcam.app.view.dialog

import android.app.Dialog
import android.view.View
import android.widget.TextView
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import com.sweetcam.app.R
import com.sweetcam.app.utils.IListener
import com.sweetcam.app.utils.click

class SaveDlg(private val listener: IListener) : AAH_FabulousFragment() {

    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = View.inflate(context, R.layout.dialog_save, null)
        v.findViewById<TextView>(R.id.yes).click { listener.click1() }
        isCancelable = false
        setViewMain(v.findViewById(R.id.main))
        setMainContentView(v)
        super.setupDialog(dialog, style)
    }
}