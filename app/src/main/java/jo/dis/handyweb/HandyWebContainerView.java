package jo.dis.handyweb;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import jo.dis.library.handyweb.HandyWebView;
import jo.dis.library.handyweb.LoadingInterceptor;
import jo.dis.library.handyweb.WebViewStateListener;

/**
 * @className: HandyWebContainerView
 * @classDescription: 较便利的webview
 * @author: Dis Jo
 * @createTime: 15/12/29
 */
public class HandyWebContainerView extends RelativeLayout {

    private HandyWebView handyWebView;
    private ProgressBar progressBar;
    private ViewGroup errorView;

    private final Animation animation = new AlphaAnimation(1f, 0f);

    public HandyWebContainerView(Context context) {
        super(context);
        initalize();
    }

    public HandyWebContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initalize();
        setupWebSetting(attrs);
    }

    public HandyWebContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initalize();
        setupWebSetting(attrs);
    }

    private void initalize() {
        bindViews();
        bindWebViewState();
        animation.setDuration(1000);
    }

    private void setupWebSetting(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.hw);
        handyWebView.setupWebSettings(typedArray);
        typedArray.recycle();
    }

    private void bindViews() {
        View.inflate(getContext(), R.layout.layout_handy_web_container, this);
        handyWebView = (HandyWebView) findViewById(R.id.web_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        errorView = (ViewGroup) findViewById(R.id.error_view);
        Button reloadBtn = (Button) findViewById(R.id.reload_button);
        reloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (handyWebView != null) {
                    handyWebView.reload();
                }
            }
        });
    }

    private void bindWebViewState() {
        handyWebView.addOnWebViewStateListener(new WebViewStateListener() {
            @Override
            public void onStartLoading(String url, Bitmap favicon) {
                progressBar.clearAnimation();
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
                errorView.setVisibility(GONE);
            }

            @Override
            public void onError(WebView view, int errorCode, String description,
                                String failingUrl) {
                progressBar.setVisibility(GONE);
                handyWebView.setVisibility(GONE);
                errorView.setVisibility(VISIBLE);
            }

            @Override
            public void onError(WebView view, WebResourceRequest request, WebResourceError error) {
                progressBar.setVisibility(GONE);
                handyWebView.setVisibility(GONE);
                errorView.setVisibility(VISIBLE);
            }

            @Override
            public void onFinishLoaded(String loadedUrl) {
                progressBar.startAnimation(animation);
                progressBar.setVisibility(GONE);
                handyWebView.setVisibility(VISIBLE);
                errorView.setVisibility(GONE);
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (handyWebView.getVisibility() != VISIBLE && progress > 80) {
                    handyWebView.setVisibility(VISIBLE);
                }
                progressBar.setProgress(progress);
            }
        });
    }

    public void addOnWebViewStateListener(WebViewStateListener webViewStateListener) {
        handyWebView.addOnWebViewStateListener(webViewStateListener);
    }

    public void addLoadingInterceptor(LoadingInterceptor loadingInterceptor) {
        handyWebView.addLoadingInterceptor(loadingInterceptor);
    }

    public void loadUrl(String url) {
        handyWebView.loadUrl(url);
    }

    public void reloadUrl() {
        handyWebView.reload();
    }

    public boolean canGoBack() {
        return handyWebView.canGoBack();
    }

    public void goBack() {
        handyWebView.goBack();
    }

    public String getTitle() {
        return handyWebView.getTitle();
    }

    public String getUrl() {
        return handyWebView.getUrl();
    }

    public String getUserAgentString() {
        return handyWebView.getSettings().getUserAgentString();
    }

    public void setUserAgentString(String ua) {
        handyWebView.getSettings().setUserAgentString(ua);
    }

    public WebSettings getSettings() {
        return handyWebView.getSettings();
    }
}
