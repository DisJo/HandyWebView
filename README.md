DataCache
---------

A handy webview for Android.

code
----

xml：

 ```
 <jo.dis.handyweb.HandyWebView
     xmlns:app="http://schemas.android.com/apk/res-auto"
     android:id="@+id/web_view"
     android:layout_width="match_parent"
     android:layout_height="wrap_content"
     app:cache_mode="load_cache_else_network"
     app:dom_storage_enabled="true"
     app:app_cache_enabled="true"
     app:java_script_enabled="true"
     app:built_in_zoom_controls="true"
     app:display_zoom_controls="false"
     app:load_with_overview_mode="true"
     app:use_wide_view_port="true"/>
 ```
 
 code:
 
 ```
 HandyWebView webView = (HandyWebView) findViewById(R.id.web_view);
 webView.loadUrl("https://www.baidu.com");
 webView.addLoadingInterceptor(new LoadingInterceptor() {
     @Override
     public void interceptor(String loadingUrl) {
         Log.d("loadingUrl", loadingUrl);
         webView.loadUrl(loadingUrl);
     }
 });
 webView.addOnWebViewStateListener(new WebViewStateListener() {
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
 });
        
 ```
 
Using Android Studio
--------------------

编辑你的 build.gradle 文件，在dependency里面添加:

```
dependencies {
    compile 'jo.dis.handyweb:library:1.0.0'
}
```

Developed By
------------
- Dis Jo - dis123520@gmail.com
