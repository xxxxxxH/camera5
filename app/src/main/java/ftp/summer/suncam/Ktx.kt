package ftp.summer.suncam

import android.app.Application
import android.os.Build
import android.webkit.WebView
import androidx.multidex.BuildConfig
import com.anythink.core.api.ATSDK
import com.anythink.core.api.NetTrafficeCallback
import ftp.summer.suncam.utils.loge
import com.tencent.mmkv.MMKV
import kotlin.system.measureTimeMillis

class Ktx private constructor(application: Application) {

    companion object {
        @Volatile
        private var INSTANCE: ftp.summer.suncam.Ktx? = null

        fun initialize(application: Application) =
            ftp.summer.suncam.Ktx.Companion.INSTANCE ?: synchronized(this) {
                ftp.summer.suncam.Ktx.Companion.INSTANCE ?: ftp.summer.suncam.Ktx(application)
                    .apply { ftp.summer.suncam.Ktx.Companion.INSTANCE = this }
            }


        fun getInstance() =
            ftp.summer.suncam.Ktx.Companion.INSTANCE ?: throw NullPointerException("Have you invoke initialize() before?")
    }

    val app = application

    fun initStartUp() {
        measureTimeMillis {
            MMKV.initialize(app)
            initOther()
        }.let {
            "application initTime -> ${it}".loge()
        }
    }

    private fun initOther() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = Application.getProcessName()
            if (app.packageName != processName) {
                WebView.setDataDirectorySuffix(processName)
            }
        }

        ATSDK.checkIsEuTraffic(app, object : NetTrafficeCallback {
            override fun onResultCallback(isEU: Boolean) {
                if (isEU && ATSDK.getGDPRDataLevel(app) == ATSDK.UNKNOWN) {
                    ATSDK.showGdprAuth(app)
                }
            }

            override fun onErrorCallback(errorMsg: String) {
            }
        })

        ATSDK.setNetworkLogDebug(BuildConfig.DEBUG)
        ATSDK.integrationChecking(app)
        ATSDK.init(
            app,
            app.getString(R.string.top_on_app_id),
            app.getString(R.string.top_on_app_key)
        )
    }
}