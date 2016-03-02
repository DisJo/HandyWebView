package jo.dis.library.handyweb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;


/**
 * @className: HandyWebView
 * @classDescription: 较便利的webview
 * @author: Dis Jo
 * @createTime: 15/12/29
 */
public class HandyWebView extends WebView {

    private WebViewState state = WebViewState.STOP;

    private List<WebViewStateListener> webViewStateListeners = new ArrayList<>();

    private List<LoadingInterceptor> loadingInterceptors = new ArrayList<>();

    public HandyWebView(Context context) {
        super(context);
        initialize();
    }

    public HandyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(attrs);
    }

    public HandyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    public void addOnWebViewStateListener(WebViewStateListener webViewStateListener) {
        webViewStateListeners.add(webViewStateListener);
    }

    public void addLoadingInterceptor(LoadingInterceptor loadingInterceptor) {
        loadingInterceptors.add(loadingInterceptor);
    }

    private void initialize() {
        setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        setWebViewClient(new WebServiceViewClient());
        setWebChromeClient(new WebServiceChromeClient());
    }

    private void initialize(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.hw);
        setupWebSettings(typedArray);
        typedArray.recycle();
        setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        setWebViewClient(new WebServiceViewClient());
        setWebChromeClient(new WebServiceChromeClient());
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setupWebSettings(TypedArray array) {
        boolean allowContentAccess = array.getBoolean(R.styleable.hw_allow_content_access, true);
        boolean allowFileAccess = array.getBoolean(R.styleable.hw_allow_file_access, true);
        boolean allowFileAccessFromFileURLs = array.getBoolean(R.styleable.hw_allow_file_access_from_file_urls, true);
        boolean allowUniversalAccessFromFileURLs = array.getBoolean(R.styleable.hw_allow_universal_access_from_file_urls, false);
        boolean appCacheEnabled = array.getBoolean(R.styleable.hw_app_cache_enabled, false);
        boolean blockNetworkImage = array.getBoolean(R.styleable.hw_block_network_image, false);
        boolean blockBlockNetworkLoads = array.getBoolean(R.styleable.hw_block_network_loads, false);
        boolean builtInZoomControls = array.getBoolean(R.styleable.hw_built_in_zoom_controls, false);
        int cacheMode = array.getInt(R.styleable.hw_cache_mode, WebSettings.LOAD_DEFAULT);
        boolean databaseEnabled = array.getBoolean(R.styleable.hw_database_enabled, false);
        boolean displayZoomControls = array.getBoolean(R.styleable.hw_display_zoom_controls, false);
        boolean domStorageEnabled = array.getBoolean(R.styleable.hw_dom_storage_enabled, false);
        boolean geolocationEnabled = array.getBoolean(R.styleable.hw_geolocation_enabled, true);
        boolean javaScriptCanOpenWindowsAutomatically = array.getBoolean(R.styleable.hw_java_script_can_open_windows_automatically, false);
        boolean jsEnabled = array.getBoolean(R.styleable.hw_java_script_enabled, false);
        boolean loadWithOverviewMode = array.getBoolean(R.styleable.hw_load_with_overview_mode, false);
        boolean loadsImagesAutomatically  = array.getBoolean(R.styleable.hw_loads_images_automatically, true);
        boolean needInitialFocus = array.getBoolean(R.styleable.hw_need_initial_focus, false);
        boolean saveFormEnabled = array.getBoolean(R.styleable.hw_save_form_data, true);
        boolean supportMultipleWindows = array.getBoolean(R.styleable.hw_support_multiple_windows,false);
        boolean supportZoom = array.getBoolean(R.styleable.hw_support_zoom, true);
        boolean useWideViewPort = array.getBoolean(R.styleable.hw_use_wide_view_port, true);

        WebSettings setting = getSettings();
        setting.setAllowContentAccess(allowContentAccess);
        setting.setAllowFileAccess(allowFileAccess);    // 设置可以访问文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setting.setAllowFileAccessFromFileURLs(allowFileAccessFromFileURLs);
            setting.setAllowUniversalAccessFromFileURLs(allowUniversalAccessFromFileURLs);
        }
        setting.setAppCacheEnabled(appCacheEnabled);
        setting.setBlockNetworkImage(blockNetworkImage);
        setting.setBlockNetworkLoads(blockBlockNetworkLoads);
        setting.setBuiltInZoomControls(builtInZoomControls);    // 缩放支持
        setting.setCacheMode(cacheMode);
        setting.setDatabaseEnabled(databaseEnabled);
        setting.setDisplayZoomControls(displayZoomControls);
        setting.setDomStorageEnabled(domStorageEnabled);
        setting.setGeolocationEnabled(geolocationEnabled);  // 启用地理定位
        setting.setJavaScriptCanOpenWindowsAutomatically(javaScriptCanOpenWindowsAutomatically);    // 支持通过JS打开新窗口
        setting.setJavaScriptEnabled(jsEnabled);
        setting.setLoadWithOverviewMode(loadWithOverviewMode);
        setting.setLoadsImagesAutomatically(loadsImagesAutomatically);  // 支持自动加载图片
        setting.setNeedInitialFocus(needInitialFocus);  // 当webview调用requestFocus时为webview设置节点
        setting.setSaveFormData(saveFormEnabled);
        setting.setSupportMultipleWindows(supportMultipleWindows);
        setting.setSupportZoom(supportZoom);    // 支持缩放
        setting.setUseWideViewPort(useWideViewPort);
    }

    private class WebServiceChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (state == WebViewState.LOADING) {
                for (WebViewStateListener listener : webViewStateListeners) {
                    listener.onProgressChanged(view, newProgress);
                }
            }
        }
    }

    private class WebServiceViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String
                url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (state == WebViewState.STOP) {
                state = WebViewState.LOADING;
                for (WebViewStateListener listener : webViewStateListeners) {
                    listener.onStartLoading(url, favicon);
                }
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            state = WebViewState.ERROR;
            for (WebViewStateListener listener : webViewStateListeners) {
                listener.onError(view, errorCode, description, failingUrl);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            state = WebViewState.ERROR;
            for (WebViewStateListener listener : webViewStateListeners) {
                listener.onError(view, request, error);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (state == WebViewState.LOADING) {
                for (WebViewStateListener listener : webViewStateListeners) {
                    listener.onProgressChanged(view, 100);
                    listener.onFinishLoaded(url);
                }
            }
            state = WebViewState.STOP;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null || loadingInterceptors == null) {
                return false;
            } else {
                for (LoadingInterceptor loadingInterceptor : loadingInterceptors) {
                    loadingInterceptor.interceptor(url);
                }
//                return super.shouldOverrideUrlLoading(view, url);
                return true;
            }
        }

    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }
}
