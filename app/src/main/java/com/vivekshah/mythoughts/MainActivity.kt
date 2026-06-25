package com.vivekshah.mythoughts

import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var toolbar: MaterialToolbar
    private lateinit var offlineView: View

    private val BLOG_URL = "https://blog.ervivekshah.com.np"
    private var sessionPageCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)

        webView = findViewById(R.id.webView)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        progressBar = findViewById(R.id.progressBar)
        offlineView = findViewById(R.id.offlineView)

        setupWebView()
        setupSwipeRefresh()

        findViewById<View>(R.id.retryButton).setOnClickListener {
            if (isNetworkAvailable()) {
                offlineView.visibility = View.GONE
                webView.visibility = View.VISIBLE
                webView.reload()
            } else {
                Snackbar.make(
                    swipeRefresh,
                    "Still offline. Please check your connection.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState)
        } else {
            loadBlog()
        }
    }

    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            mediaPlaybackRequiresUserGesture = false
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                progressBar.progress = 0
                swipeRefresh.isEnabled = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
                swipeRefresh.isRefreshing = false
                swipeRefresh.isEnabled = true
                supportActionBar?.title = view?.title ?: getString(R.string.app_name)

                sessionPageCount++
                // Show in-app review prompt after reading 5 pages
                if (sessionPageCount == 5) {
                    showRatingPrompt()
                }
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                if (request?.isForMainFrame == true) {
                    webView.visibility = View.GONE
                    offlineView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    swipeRefresh.isRefreshing = false
                }
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url?.toString() ?: return false
                return if (url.startsWith("https://blog.ervivekshah.com.np") ||
                    url.startsWith("https://mythoughtchapter.blogspot.com")
                ) {
                    false // load inside app
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    true
                }
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                supportActionBar?.title = title ?: getString(R.string.app_name)
            }
        }
    }

    private fun setupSwipeRefresh() {
        swipeRefresh.setColorSchemeResources(R.color.brand_red, R.color.brand_dark)
        swipeRefresh.setOnRefreshListener {
            if (isNetworkAvailable()) {
                webView.reload()
            } else {
                swipeRefresh.isRefreshing = false
                Snackbar.make(swipeRefresh, "No internet connection", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadBlog() {
        if (isNetworkAvailable()) {
            webView.visibility = View.VISIBLE
            offlineView.visibility = View.GONE
            webView.loadUrl(BLOG_URL)
        } else {
            webView.visibility = View.GONE
            offlineView.visibility = View.VISIBLE
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showRatingPrompt() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val rated = prefs.getBoolean("rated", false)
        if (rated) return

        AlertDialog.Builder(this)
            .setTitle("Enjoying My Thoughts? ⭐")
            .setMessage("If you're enjoying the blog app, please take a moment to rate it. Your feedback means a lot!")
            .setPositiveButton("Rate Now ⭐") { _, _ ->
                prefs.edit().putBoolean("rated", true).apply()
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$packageName")
                        )
                    )
                } catch (e: Exception) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        )
                    )
                }
            }
            .setNegativeButton("Maybe Later", null)
            .setNeutralButton("No Thanks") { _, _ ->
                prefs.edit().putBoolean("rated", true).apply()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_home -> {
                webView.loadUrl(BLOG_URL)
                true
            }
            R.id.action_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, webView.title ?: getString(R.string.app_name))
                    putExtra(Intent.EXTRA_TEXT, "${webView.title ?: getString(R.string.app_name)}\n${webView.url ?: BLOG_URL}")
                }
                startActivity(Intent.createChooser(shareIntent, "Share via"))
                true
            }
            R.id.action_browser -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webView.url ?: BLOG_URL)))
                true
            }
            R.id.action_about -> {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(
                        "Personal blog by Vivek Shah.\n\n" +
                        "Sharing experiences, lessons, and life chapters from IIT Roorkee and beyond.\n\n" +
                        "Version ${BuildConfig.VERSION_NAME}"
                    )
                    .setPositiveButton("OK", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Exit") { _, _ ->
                    @Suppress("DEPRECATION")
                    super.onBackPressed()
                }
                .setNegativeButton("Stay", null)
                .show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }
}
