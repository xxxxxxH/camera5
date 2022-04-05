package ftp.summer.suncam.ui.activity

import android.content.Intent
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import ftp.summer.suncam.R
import ftp.summer.suncam.base.BaseActivity
import ftp.summer.suncam.utils.*
import kotlinx.android.synthetic.main.activity_face_book.*
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ActivitySplash : BaseActivity(R.layout.activity_splash) {

    override fun onConvert() {
        registerEventBus()
        getConfig({
            routerWhereByConfig({
                next(ActivityHomePage::class.java, true)
            }, {
                it?.let {
                    runOnUiThread {
                        activitySplashIvFb.isVisible = true
                    }
                } ?: kotlin.run {
                    next(ActivityHomePage::class.java, false)
                }
            }, {
                activitySplashIvFb.isVisible = true
            })
        }, {
            showToast("get config error, app will finishing")
            lifecycleScope.launch(Dispatchers.IO) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        })
        /*lifecycleScope.requestConfig {
            if (isLogin) {
                jumpToMain()
            } else {
                if (configEntity.needLogin()) {
                    if (configEntity.needDeepLink() && configEntity.faceBookId().isNotBlank()) {
                        fetchAppLink(configEntity.faceBookId()) {
                            "initFaceBook $it".loge()
                            it?.let {
                                runOnUiThread {
                                    activitySplashIvFb.isVisible = true
                                }
                            } ?: kotlin.run {
                                jumpToMain()
                            }
                        }
                    } else {
                        activitySplashIvFb.isVisible = true
                    }
                } else {
                    jumpToMain()
                }
            }
        }*/

        activitySplashIvFb.click {
            next(ActivityLogin::class.java, false)
        }
    }

    private fun jumpToMain() {
        startActivity(Intent(this, ActivityHomePage::class.java))
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: ftp.summer.suncam.BusDestroyEvent) {
        finish()
    }

    override fun onPause() {
        super.onPause()
        canShowAd = false
    }

    private var canShowAd = true

    override fun onInterstitialAdLoaded() {
        if (configEntity.isOpenAdReplacedByInsertAd()) {
            if (canShowAd) {
                showInsertAd(isForce = true)
                canShowAd = false
            }
        }
    }

    override fun onSplashAdLoaded() {
        if (!configEntity.isOpenAdReplacedByInsertAd()) {
            if (canShowAd) {
                canShowAd = false
                activitySplashRl?.let {
                    showOpenAdImpl(it, "")
                }
            }
        }
    }

}