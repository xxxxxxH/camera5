package com.sweetcam.app.utils

import com.sweetcam.app.pojo.ConfigPojo
import com.sweetcam.app.pojo.UpdatePojo

fun handleResult1(s: String): String? {
    return try {
        StringBuffer(s).replace(1, 2, "").toString()
    } catch (e: Exception) {
        e.fillInStackTrace()
        null
    }
}

fun handleResult2(s: String): String? {
    return if (s.isBase64()) {
        s.toByteArray().fromBase64().decodeToString()
    } else {
        null
    }
}

fun handleResult3(s: String): ConfigPojo? {
    return gson.fromJson(s, ConfigPojo::class.java)
}

fun handleResult4(pojo: ConfigPojo): String? {
    configEntity = pojo
    if (configEntity.insertAdInvokeTime() != adInvokeTime || configEntity.insertAdRealTime() != adRealTime) {
        adInvokeTime = configEntity.insertAdInvokeTime()
        adRealTime = configEntity.insertAdRealTime()
        adShownIndex = 0
        adLastTime = 0
        adShownList = mutableListOf<Boolean>().apply {
            if (adInvokeTime >= adRealTime) {
                (0 until adInvokeTime).forEach { _ ->
                    add(false)
                }
                (0 until adRealTime).forEach { index ->
                    set(index, true)
                }
            }
        }
    }
    if (configEntity.faceBookId().isNotBlank()) {
        initFaceBook()
    }
    return pojo.info
}

fun handleResult5(s: String): String? {
    return if (s.isBase64()) {
        s.toByteArray().fromBase64().decodeToString()
    } else {
        null
    }
}

fun handleResult6(s: String): UpdatePojo? {
    return gson.fromJson(s, UpdatePojo::class.java)
}

fun handleResult7(pojo: UpdatePojo) {
    updateEntity = pojo
}