package com.kushagragoel.wikimediaassignment.ui.listdetail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kushagragoel.wikimediaassignment.databinding.SearchDetailFragmentBinding
import com.kushagragoel.wikimediaassignment.helper.ModuleConstants
import java.lang.ref.WeakReference

class SearchDetailFragment : Fragment() {

    companion object {
        fun newInstance() = SearchDetailFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = SearchDetailFragmentBinding.inflate(inflater)
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = AppWebViewClients(binding.searchDetailProgress,
            binding.webView, binding.failureMessageTextView)
        val url: String? = requireArguments().getString(ModuleConstants.WIKI_URL_BUNDLE_KEY_NAME)
        if (url.isNullOrEmpty()) {
            binding.webView.visibility = View.GONE
            binding.failureMessageTextView.visibility = View.VISIBLE
        } else {
            if (ModuleConstants.checkNetworkConnectivity(requireContext())) {
                binding.failureMessageTextView.visibility = View.GONE
                binding.webView.visibility = View.VISIBLE
                binding.webView.loadUrl(url)
            } else {
                binding.webView.visibility = View.GONE
                binding.failureMessageTextView.visibility = View.VISIBLE
            }
        }
        return binding.root
    }

    inner class AppWebViewClients(progressBar: ProgressBar?, webView: WebView, textView: TextView) : WebViewClient() {
        var progressBar: WeakReference<ProgressBar?> = WeakReference(progressBar)
        var webView: WeakReference<WebView> = WeakReference(webView)
        var textView: WeakReference<TextView> = WeakReference(textView)

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            webView.get()!!.visibility = View.VISIBLE
            textView.get()!!.visibility = View.GONE
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            if (progressBar.get() != null) progressBar.get()!!.visibility = View.GONE
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(view, request, error)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                webView.get()!!.visibility = View.GONE
            }
            if (progressBar.get() != null)
                progressBar.get()!!.visibility = View.GONE
            webView.get()!!.visibility = View.GONE
            textView.get()!!.visibility = View.VISIBLE
        }

        init {
            if (this.progressBar.get() != null)
                this.progressBar.get()!!.visibility = View.VISIBLE
        }
    }

}