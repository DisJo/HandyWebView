package jo.dis.handyweb;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import jo.dis.library.handyweb.LoadingInterceptor;
import jo.dis.library.handyweb.WebViewStateListener;

public class MainActivity extends AppCompatActivity {

    private HandyWebContainerView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (HandyWebContainerView) findViewById(R.id.handy_web_container);
        webView.addLoadingInterceptor(loadingInterceptor);
        webView.addOnWebViewStateListener(webViewStateListener);
        webView.loadUrl("https://www.baidu.com");
    }

    private LoadingInterceptor loadingInterceptor = new LoadingInterceptor() {
        @Override
        public void interceptor(String loadingUrl) {
            Log.d("loadingUrl", loadingUrl);
            webView.loadUrl(loadingUrl);
        }
    };

    private WebViewStateListener webViewStateListener = new WebViewStateListener() {
        @Override
        public void onStartLoading(String url, Bitmap favicon) {

        }

        @Override
        public void onError(WebView view, int errorCode, String description, String failingUrl) {

        }

        @Override
        public void onError(WebView view, WebResourceRequest request, WebResourceError error) {

        }

        @Override
        public void onFinishLoaded(String loadedUrl) {

        }

        @Override
        public void onProgressChanged(WebView view, int progress) {
            setTitle(view.getTitle());
        }
    };

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
