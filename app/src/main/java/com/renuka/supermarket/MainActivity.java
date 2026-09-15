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
import java.nio.charset.StandardCharsets;

import androidx.webkit.WebViewAssetLoader;

public class MainActivity extends Activity {
    private WebView web;
    private String pendingCsv;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        web = new WebView(this);

        web.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void saveCsv(String data, String filename) {
                pendingCsv = data;
                try {
                    Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    i.setType("text/csv");
                    i.putExtra(Intent.EXTRA_TITLE, filename);
                    startActivityForResult(i, 1001);
                } catch (Exception e) {}
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
            pendingCsv != null) {

            try {
                Uri uri = data.getData();
                OutputStream out =
                    getContentResolver().openOutputStream(uri);
                out.write(pendingCsv.getBytes(StandardCharsets.UTF_8));
                out.close();
            } catch (Exception e) {}

            pendingCsv = null;
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
