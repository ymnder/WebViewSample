package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WebView.setWebContentsDebuggingEnabled(true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ViewPagerFragment())
            .commit()

    }
}

class ViewPagerFragment : Fragment() {

    companion object {
        private const val PAGE_LIMIT = 2
    }

    private lateinit var pager: ViewPager
    private lateinit var adapter: PagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pager, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pager = view.findViewById<ViewPager>(R.id.viewPager)
        pager.offscreenPageLimit = PAGE_LIMIT

        updatePager()
    }

    fun updatePager() {
        adapter = PagerAdapter(childFragmentManager)
        pager.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}

class PagerAdapter(
    fm: FragmentManager,
    private val list: MutableList<Fragment> = mutableListOf()
) : FragmentStatePagerAdapter(fm) {

    init {
        PageFragment.PageItems.values().forEach { _ ->
            list.add(PageFragment.newInstance())
        }
    }

    override fun getItem(position: Int): Fragment = list[position]

    override fun getCount(): Int = PageFragment.PageItems.values().size
}

class PageFragment : Fragment() {

    companion object {
        fun newInstance(): Fragment = PageFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_page, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = view.findViewById<WebView>(R.id.webView)
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE

        //プリロード画像がキャッシュされないようにします。
        webView.clearCache(true)
        //JSの有効化
        webView.settings.javaScriptEnabled = true

        webView.loadUrl("file:///android_asset/Web/index.html")

        // 読み込み時にページ横幅を画面幅に合わせる
        webView.settings.useWideViewPort = true
        // ワイドビューポートの有効・無効の指定(trueならページ全体表示)
        webView.settings.loadWithOverviewMode = true
    }

    enum class PageItems {
        PAGE_A,
        PAGE_B,
        PAGE_C,
        PAGE_D,
        PAGE_E,
        ;
    }
}
