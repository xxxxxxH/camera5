package com.sweetcam.app.utils

import android.content.Intent
import android.os.Build
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect

fun AppCompatActivity.showToast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.routerWhereByConfig(block1: () -> Unit, block2: () -> Unit, block3: () -> Unit) {
    if (isLogin) {
        block1()
    } else {
        if (configEntity.needLogin()) {
            if (configEntity.faceBookId().isNotBlank()) {
                block2()
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

fun AppCompatActivity.clearCookie(block: () -> Unit){
    CookieSyncManager.createInstance(app)
    val cookieManager = CookieManager.getInstance();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        cookieManager.removeSessionCookies(null);
        cookieManager.removeAllCookie();
        cookieManager.flush();
    } else {
        cookieManager.removeSessionCookies(null);
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }
    block()
}

fun AppCompatActivity.setWebView(webView: WebView,block1: () -> Unit){
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
        block1()

    }
}