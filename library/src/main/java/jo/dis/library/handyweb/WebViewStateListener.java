package jo.dis.library.handyweb;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

/**
 * Created by Dis on 15/12/29
 */
public interface WebViewStateListener {

    void onStartLoading(String url, Bitmap favicon);

    void onError(WebView view, int errorCode, String description, String failingUrl);

    void onError(WebView view, WebResourceRequest request, WebResourceError error);

    void onFinishLoaded(String loadedUrl);

    void onProgressChanged(WebView view, int progress);

}
