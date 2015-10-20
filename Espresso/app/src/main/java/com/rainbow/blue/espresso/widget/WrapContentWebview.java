package com.rainbow.blue.espresso.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class WrapContentWebview extends WebView {

    public static final String tag = "injectView";
    public static final String tag1 = WrapContentWebview.class.getSimpleName();
    private boolean isFullScreen = false;

    public WrapContentWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WrapContentWebview(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        if (!isFullScreen) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Rect outRect = new Rect();
        getWindowVisibleDisplayFrame(outRect);
        Log.d(tag1, "measure:[ width: " + width + ",height: " + height + "],-screen:[width: " + outRect.width() + ",height: " + (outRect.height()) + " ]");
        height = height < (outRect.height()) ? (outRect.height()) : height;
        int realHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        Log.d(WrapContentWebview.class.getSimpleName(), "measure:[ width: " + width + ",height: " + height + "],-screen:[width: " + outRect.width() + ",height: " + (outRect.height()) + " ]" + "realHeight:" + realHeight);

        int calHeight = MeasureSpec.getSize(realHeight);
        Log.d(tag1, "cal height:" + calHeight);
        super.onMeasure(widthMeasureSpec, realHeight);
    }

    public void setFullScreen(boolean isFull) {
        this.isFullScreen = isFull;
        requestLayout();
    }

    @SuppressLint("NewApi")
    private void init() {

        /**
         * setUserAgent with authorization
         */
        {
            if (isInEditMode()) return;

        }
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        // 设置开启缓存
        this.getSettings().setAppCacheEnabled(true);

        this.setWebViewClient(new BridgeWebViewClient());
        this.setWebChromeClient(new BridgeWebChromeClient());

    }

    class BridgeWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();
            Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
            return true;
        }

    }

    class BridgeWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(tag, "shouldOverrideUrlLoading$" + url);
            loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e(tag, "onPageStarted$" + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(tag, "onPageFinished$" + url);
        }

        @Override
        public void onFormResubmission(WebView view, android.os.Message dontResend, android.os.Message resend) {
            super.onFormResubmission(view, dontResend, resend);
            Log.e(tag, "onFormResubmission");
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.e(tag, "onLoadResource$" + url);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
            Log.e(tag, "onReceivedHttpAuthRequest");
        }

        @SuppressLint("NewApi")
        @Override
        public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
            super.onReceivedLoginRequest(view, realm, account, args);
            Log.e(tag, "onReceivedLoginRequest");
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Log.e(tag, "onReceivedSslError");
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
            Log.e(tag, "onScaleChanged");
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);
            Log.e(tag, "onUnhandledKeyEvent");
        }

        @Override
        @Deprecated
        public void onTooManyRedirects(WebView view, android.os.Message cancelMsg, android.os.Message continueMsg) {
            super.onTooManyRedirects(view, cancelMsg, continueMsg);
            Log.e(tag, "onTooManyRedirects");
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.e(tag, "onReceivedError$" + failingUrl);
        }
    }

}
