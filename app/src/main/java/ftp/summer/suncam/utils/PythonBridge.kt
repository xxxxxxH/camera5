package ftp.summer.suncam.utils

import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.srplab.www.starcore.*
import ftp.summer.suncam.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

private var python: StarObjectClass? = null

private var SrvGroup: StarSrvGroupClass? = null

private var Service: StarServiceClass? = null

private var starcore: StarCoreFactory? = null

fun AppCompatActivity.copyFiles() {
    try {
        if (filesDir.exists()) {
            val list = filesDir.list()
            list?.let {
                if (!list.contains("py_code.py")) {
                    val assetManager = assets
                    val dataSource = assetManager.open("py_code_fix.zip")
                    StarCoreFactoryPath.Install(dataSource, "${filesDir.path}", true)
                }
            } ?: run {
                val assetManager = assets
                val dataSource = assetManager.open("py_code_fix.zip")
                StarCoreFactoryPath.Install(dataSource, "${filesDir.path}", true)
            }
        }
//        val assetManager = assets
//        val dataSource = assetManager.open("py_code_fix.zip")
//        StarCoreFactoryPath.Install(dataSource, "${filesDir.path}", true)
    } catch (e: IOException) {
    }
}

fun AppCompatActivity.initStarCore() {
    StarCoreFactoryPath.StarCoreCoreLibraryPath = applicationInfo.nativeLibraryDir
    StarCoreFactoryPath.StarCoreShareLibraryPath = applicationInfo.nativeLibraryDir
    StarCoreFactoryPath.StarCoreOperationPath = "${filesDir.path}"
    starcore = StarCoreFactory.GetFactory()
    starcore?._SRPLock()
    SrvGroup = starcore?._GetSrvGroup(0)
    Service = SrvGroup?._GetService("test", "123")
    if (Service == null) {
        Service = starcore?._InitSimple("test", "123", 0, 0)
    } else {
        Service?._CheckPassword(false)
    }
    Service?._CheckPassword(false)
    SrvGroup?._InitRaw("python37", Service)
    python = Service!!._ImportRawContext("python", "", false, "")
}

fun AppCompatActivity.getConfig(block: () -> Unit, block2: () -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        var result: Any? = null
        python?._Call("eval", "import requests")
        Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
        result = python?._Call("get_config", resources.getString(R.string.base_url) + "config")
        result?.let { a ->
            a.toString().let { it ->
                withContext(Dispatchers.Main) {
                    if (TextUtils.isEmpty(it)) {
                        block2()
                    } else {
                        handleResult1(it)?.let {
                            "it1 $it".loge("xxxxxxH")
                            handleResult2(it)
                        }?.let {
                            "it2 $it".loge("xxxxxxH")
                            handleResult3(it)
                        }?.let {
                            "it3 $it".loge("xxxxxxH")
                            handleResult4(it)
                        }?.let {
                            "it4 $it".loge("xxxxxxH")
                            handleResult5(it)
                        }?.let {
                            "it5 $it".loge("xxxxxxH")
                            handleResult6(it)
                        }?.let {
                            "it6 $it".loge("xxxxxxH")
                            handleResult7(it)
                        }
                        block()
                    }
                }
            }
        }

    }
}

fun AppCompatActivity.upload(url: String, data: Any, block: (Any) -> Unit) {
    lifecycleScope.launch(Dispatchers.IO) {
        var result: Any? = null
        python?._Call("eval", "import requests")
        Service?._DoFile("python", "${filesDir.path}/py_code.py", "")
        result = python?._Call("upload", url, data)
        withContext(Dispatchers.Main) {
            result?.let {
                "xxxxxxH $it".loge()
                block(it)
            }
        }
    }
}