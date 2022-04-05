package ftp.summer.suncam.view.dialog

import android.app.Dialog
import android.view.View
import android.widget.TextView
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import ftp.summer.suncam.R
import ftp.summer.suncam.utils.IListener
import ftp.summer.suncam.utils.click

class ExitDlg(private val listener:IListener):AAH_FabulousFragment() {
    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = View.inflate(context, R.layout.dialog_exit, null)
        v.findViewById<TextView>(R.id.yes).click {
            listener.click1()
        }
        v.findViewById<TextView>(R.id.no).click {
            listener.click2()
        }
        isCancelable = false
        setViewMain(v.findViewById(R.id.main))
        setMainContentView(v)
        super.setupDialog(dialog, style)
    }
}