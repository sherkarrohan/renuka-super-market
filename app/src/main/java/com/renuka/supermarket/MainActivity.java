package com.renuka.supermarket;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.JavascriptInterface;

import java.io.OutputStream;
import java.util.Base64;

import androidx.webkit.WebViewAssetLoader;

public class MainActivity extends Activity {
    private WebView web;
    private String pendingData;
    private String pendingMime;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        web = new WebView(this);

        web.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void saveFile(String base64, String mime, String filename) {
                pendingData = base64;
                pendingMime = mime;

                try {
                    Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    i.setType(mime);
                    i.putExtra(Intent.EXTRA_TITLE, filename);
                    startActivityForResult(i, 1001);
                } catch (Exception e) {
                    pendingData = null;
                    pendingMime = null;
                }
            }
        }, "Android");

        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(false);
        s.setAllowContentAccess(false);

        final WebViewAssetLoader assetLoader =
            new WebViewAssetLoader.Builder()
                .addPathHandler("/assets/",
                    new WebViewAssetLoader.AssetsPathHandler(this))
                .build();

        web.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(
                    WebView view, WebResourceRequest request) {
                return assetLoader.shouldInterceptRequest(request.getUrl());
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(
                    WebView view, String url) {
                return assetLoader.shouldInterceptRequest(
                    Uri.parse(url));
            }
        });

        web.loadUrl(
            "https://appassets.androidplatform.net/assets/index.html");

        setContentView(web);
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001 &&
            resultCode == RESULT_OK &&
            data != null &&
            data.getData() != null &&
            pendingData != null) {

            try {
                byte[] bytes = Base64.getDecoder().decode(pendingData);
                Uri uri = data.getData();

                OutputStream out =
                    getContentResolver().openOutputStream(uri);

                out.write(bytes);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            pendingData = null;
            pendingMime = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
