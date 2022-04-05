package ftp.summer.suncam.ui.activity

import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ftp.summer.suncam.Item
import ftp.summer.suncam.R
import ftp.summer.suncam.base.BaseActivity
import ftp.summer.suncam.utils.getCacheData
import kotlinx.android.synthetic.main.activity_puzzle.*
import uk.co.ribot.easyadapter.EasyRecyclerAdapter

class ActivityPuzzle : BaseActivity(R.layout.activity_puzzle) {

    private val staggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onConvert() {
        loadingView.show(supportFragmentManager, "")
        getCacheData {
            val adapter = EasyRecyclerAdapter(this@ActivityPuzzle, Item::class.java, it)
            recycler.layoutManager = staggeredGridLayoutManager
            recycler.adapter = adapter
            loadingView.dismiss()
        }
    }
}