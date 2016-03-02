package jo.dis.library.handyweb;

/**
 * @className: LoadingInterceptor
 * @classDescription: webview的url截获
 * @author: Dis Jo
 * @createTime: 15/12/29
 */
public interface LoadingInterceptor {
    void interceptor(String loadingUrl);
}
