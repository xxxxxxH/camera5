package com.sweetcam.app.base

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.anythink.banner.api.ATBannerExListener
import com.anythink.interstitial.api.ATInterstitial
import com.anythink.splashad.api.ATSplashAd
import com.roger.catloadinglibrary.CatLoadingView
import com.sweetcam.app.App
import com.sweetcam.app.R
import com.sweetcam.app.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity(layoutId: Int) : AppCompatActivity(layoutId) {

    private var isBackground = false
    private var topOnInterstitialAd: ATInterstitial? = null
    private var openAd: ATSplashAd? = null
    private var bannerListener: ATBannerExListener? = null
    val loadingView by lazy {
        CatLoadingView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //把python环境copy进去手机
        copyFiles()
        initStarCore()


        topOnInterstitialAd = App.instance!!.buildInsertAdvertisement()
        topOnInterstitialAd?.setAdListener(buildInsertAdListener({
            onInterstitialAdLoaded()
        }, {
            topOnInterstitialAd = App.instance!!.buildInsertAdvertisement()
            topOnInterstitialAd?.setAdListener(it)
        }, {
            onInterstitialAdHidden()
        }))
        topOnInterstitialAd?.load()

        openAd = App.instance!!.buildOpenAdvertisement(buildOpenAdListener({
            onSplashAdLoaded()
        }, {
            lifecycleScope.launch(Dispatchers.IO) {
                delay(3000)
                openAd?.onDestory()
                openAd = App.instance!!.buildOpenAdvertisement(it)
                openAd?.loadAd()
            }
        }))
        openAd?.loadAd()
        bannerListener = buildBannerAdListener()
        onConvert()
        addBannerAd()
    }

    abstract fun onConvert()

    open fun onInterstitialAdHidden() {}

    open fun onInterstitialAdLoaded() {}
    open fun onSplashAdLoaded() {}

    fun registerEventBus() {
        object : BaseLifeCycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                EventBus.getDefault().register(this@BaseActivity)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                EventBus.getDefault().unregister(this@BaseActivity)
            }
        }.bindWithLifecycle(this)
    }

    override fun onStop() {
        super.onStop()
        isBackground = isInBackground()
    }

    override fun onResume() {
        super.onResume()
        if (isBackground) {
            isBackground = false
            val content = findViewById<ViewGroup>(android.R.id.content)
            (content.getTag(R.id.open_ad_view_id) as? FrameLayout)?.let {
                showOpenAd(it)
            } ?: kotlin.run {
                FrameLayout(this).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    content.addView(this)
                    content.setTag(R.id.open_ad_view_id, this)
                    showOpenAd(this)
                }
            }
        }
    }

    fun showOpenAdImpl(viewGroup: ViewGroup, tag: String = ""): Boolean {
        openAd?.let {
            if (it.isAdReady) {
                it.show(this, viewGroup)
                return true
            }
        }
        return false
    }


    private fun showInsertAdImpl(tag: String = ""): Boolean {
        topOnInterstitialAd?.let {
            if (it.isAdReady) {
                it.show(this)
                return true
            }
        }
        return false
    }


    private fun showOpenAd(viewGroup: ViewGroup, tag: String = ""): Boolean {
        if (configEntity.isOpenAdReplacedByInsertAd()) {
            return showInsertAd(tag = tag)
        } else {
            return showOpenAdImpl(viewGroup, tag = tag)
        }
    }

    fun showInsertAd(showByPercent: Boolean = false, isForce: Boolean = false, tag: String = ""): Boolean {
        if (isForce) {
            return showInsertAdImpl(tag)
        } else {
            if (configEntity.isCanShowInsertAd()) {
                if ((showByPercent && configEntity.isCanShowByPercent()) || (!showByPercent)) {
                    if (System.currentTimeMillis() - adLastTime > configEntity.insertAdOffset() * 1000) {
                        var showInsertAd = false
                        if (adShownList.getOrNull(adShownIndex) == true) {
                            showInsertAd = showInsertAdImpl(tag)
                        }
                        adShownIndex++
                        if (adShownIndex >= adShownList.size) {
                            adShownIndex = 0
                        }
                        return showInsertAd
                    }
                }
            }
            return false
        }
    }

    fun addBannerAd() {
        val content = findViewById<ViewGroup>(android.R.id.content)
        val frameLayout = FrameLayout(this)
        val p = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        frameLayout.layoutParams = p

        val linearLayout = LinearLayout(this)
        val p1 = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        linearLayout.layoutParams = p1

        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000)
            val banner = App.instance!!.buildBannerAdvertisement(bannerListener!!)
            banner.loadAd()
            withContext(Dispatchers.Main) {
                val p2 =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, dp2px(this@BaseActivity, 50f))
                p2.gravity = Gravity.BOTTOM
                banner.layoutParams = p2
                linearLayout.addView(banner)
                frameLayout.addView(linearLayout)
                content.addView(frameLayout)
            }
        }
    }
}