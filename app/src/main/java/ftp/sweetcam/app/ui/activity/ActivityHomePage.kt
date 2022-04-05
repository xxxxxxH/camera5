package com.sweetcam.app.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.jhworks.library.ImageSelector
import com.sweetcam.app.R
import com.sweetcam.app.base.BaseActivity
import com.sweetcam.app.utils.nextByTargetClass
import com.sweetcam.app.utils.requestPermission2
import com.sweetcam.app.utils.selectImage
import com.sweetcam.app.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityHomePage : BaseActivity(R.layout.layout_main) {
    override fun onConvert() {
        requestPermission2({

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
}