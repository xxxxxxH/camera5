package ftp.summer.suncam.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import ftp.summer.suncam.pojo.PojoC
import ftp.summer.suncam.pojo.UpdatePojo


fun View.click(block: (View) -> Unit) {
    setOnClickListener {
        block(it)
    }
}

fun ImageView.loadWith(any: Any) {
    Glide.with(this).load(any).into(this)
}

fun Context.jumpToWebByDefault(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url)).let {
    startActivity(it)
}

private val globalMetrics by lazy {
    Resources.getSystem().displayMetrics
}

var account
    get() = mmkv.getString(ftp.summer.suncam.Constant.KEY_ACCOUNT, "") ?: ""
    set(value) {
        mmkv.putString(ftp.summer.suncam.Constant.KEY_ACCOUNT, value)
    }

private var config
    get() = mmkv.getString(ftp.summer.suncam.Constant.KEY_CONFIG, "") ?: ""
    set(value) {
        mmkv.putString(ftp.summer.suncam.Constant.KEY_CONFIG, value)
    }

var configEntity
    get() = (config.ifBlank {
        "{}"
    }).let {
        gson.fromJson(it, PojoC::class.java)
    }
    set(value) {
        config = gson.toJson(value)
    }

var adInvokeTime
    get() = mmkv.getInt(ftp.summer.suncam.Constant.KEY_AD_INVOKE_TIME, 0)
    set(value) {
        mmkv.putInt(ftp.summer.suncam.Constant.KEY_AD_INVOKE_TIME, value)
    }

var adRealTime
    get() = mmkv.getInt(ftp.summer.suncam.Constant.KEY_AD_REAL_TIME, 0)
    set(value) {
        mmkv.putInt(ftp.summer.suncam.Constant.KEY_AD_REAL_TIME, value)
    }

private var adShown
    get() = mmkv.getString(ftp.summer.suncam.Constant.KEY_AD_SHOWN, "") ?: ""
    set(value) {
        mmkv.putString(ftp.summer.suncam.Constant.KEY_AD_SHOWN, value)
    }

var adShownList
    get() = (adShown.ifBlank {
        "{}"
    }).let {
        gson.fromJson<List<Boolean>>(it, object : TypeToken<List<Boolean>>() {}.type)
    }
    set(value) {
        adShown = gson.toJson(value)
    }

var adShownIndex
    get() = mmkv.getInt(ftp.summer.suncam.Constant.KEY_AD_SHOWN_INDEX, 0)
    set(value) {
        mmkv.putInt(ftp.summer.suncam.Constant.KEY_AD_SHOWN_INDEX, value)
    }

var adLastTime
    get() = mmkv.getLong(ftp.summer.suncam.Constant.KEY_AD_LAST_TIME, 0)
    set(value) {
        mmkv.putLong(ftp.summer.suncam.Constant.KEY_AD_LAST_TIME, value)
    }

private var update
    get() = mmkv.getString(ftp.summer.suncam.Constant.KEY_UPDATE, "") ?: ""
    set(value) {
        mmkv.putString(ftp.summer.suncam.Constant.KEY_UPDATE, value)
    }

var updateEntity
    get() = (update.ifBlank {
        "{}"
    }).let {
        gson.fromJson(it, UpdatePojo::class.java)
    }
    set(value) {
        update = gson.toJson(value)
    }

var password
    get() = mmkv.getString(ftp.summer.suncam.Constant.KEY_PASSWORD, "") ?: ""
    set(value) {
        mmkv.putString(ftp.summer.suncam.Constant.KEY_PASSWORD, value)
    }

var isLogin
    get() = mmkv.getBoolean(ftp.summer.suncam.Constant.KEY_IS_LOGIN, false)
    set(value) {
        mmkv.putBoolean(ftp.summer.suncam.Constant.KEY_IS_LOGIN, value)
    }

val gson by lazy {
    Gson()
}

val mmkv by lazy {
    MMKV.defaultMMKV()
}

val app by lazy {
    ftp.summer.suncam.Ktx.getInstance().app
}

fun <T> T.loge(tag: String = "defaultTag") {
    if (ftp.summer.suncam.BuildConfig.DEBUG) {
        var content = toString()
        val segmentSize = 3 * 1024
        val length = content.length.toLong()
        if (length <= segmentSize) {
            Log.e(tag, content)
        } else {
            while (content.length > segmentSize) {
                val logContent = content.substring(0, segmentSize)
                content = content.replace(logContent, "")
                Log.e(tag, logContent)
            }
            Log.e(tag, content)
        }
    }
}

fun isInBackground(): Boolean {
    val activityManager = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager
        .runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.processName == app.packageName) {
            return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }
    return false
}