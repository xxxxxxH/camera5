package com.sweetcam.app

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.anythink.banner.api.ATBannerExListener
import com.anythink.banner.api.ATBannerView
import com.anythink.interstitial.api.ATInterstitial
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdListener
import com.jhworks.library.ImageSelector
import com.sweetcam.app.utils.GlideEngine
import com.sweetcam.app.utils.IGlideEngine


class App : MultiDexApplication() {

    companion object {
        var instance: App? = null
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Ktx.initialize(this)
    }

    override fun onCreate() {
        super.onCreate()
        Ktx.getInstance().initStartUp()
        instance = this
        ImageSelector.setImageEngine(IGlideEngine())
    }

    fun buildOpenAdvertisement(listener: ATSplashAdListener?): ATSplashAd {
        return ATSplashAd(this, resources.getString(R.string.top_on_open_ad_id), null, listener, 5000)
    }

    fun buildInsertAdvertisement(): ATInterstitial {
        return ATInterstitial(this, resources.getString(R.string.top_on_insert_ad_id))
    }

    fun buildBannerAdvertisement(listener: ATBannerExListener): ATBannerView {
        val banner = ATBannerView(this)
        banner.setPlacementId(resources.getString(R.string.top_on_banner_ad_id))
        banner.setBannerAdListener(listener)
        return banner
    }
}