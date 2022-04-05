package com.sweetcam.app.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jhworks.library.core.callback.ImgProgressListener
import com.jhworks.library.core.vo.MediaUiConfigVo
import com.jhworks.library.engine.IEngine
import com.jhworks.library.view.SlPhotoView

class IGlideEngine:IEngine {
    override fun loadBigImage(
        photoView: SlPhotoView,
        uiConfig: MediaUiConfigVo,
        progressListener: ImgProgressListener?
    ) {

    }

    override fun loadDetailImage(imageView: ImageView, uiConfig: MediaUiConfigVo) {

    }

    override fun loadImage(imageView: ImageView, uiConfig: MediaUiConfigVo) {
        Glide.with(imageView)
            .load(uiConfig.path)
            .placeholder(uiConfig.placeholderResId)
            .error(uiConfig.errorResId)
            .override(uiConfig.width, uiConfig.height)
            .centerCrop()
            .into(imageView)
    }
}