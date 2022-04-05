package ftp.summer.suncam

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.roger.catloadinglibrary.CatLoadingView
import ftp.summer.suncam.ui.activity.ActivityImage
import ftp.summer.suncam.utils.click
import ftp.summer.suncam.utils.loadWith
import uk.co.ribot.easyadapter.ItemViewHolder
import uk.co.ribot.easyadapter.PositionInfo
import uk.co.ribot.easyadapter.annotations.LayoutId
import uk.co.ribot.easyadapter.annotations.ViewId

@SuppressLint("NonConstantResourceId")
@LayoutId(R.layout.layout_pic_item)
class Item(view: View) : ItemViewHolder<String>(view) {

    @ViewId(R.id.item)
    lateinit var iv: ImageView
    override fun onSetValues(item: String?, positionInfo: PositionInfo?) {
        iv.loadWith(item!!)
        iv.click {
            context.startActivity(Intent(context, ActivityImage::class.java).apply {
                putExtra("url", item)
            })
        }
    }
}