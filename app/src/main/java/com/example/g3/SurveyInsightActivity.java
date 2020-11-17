package com.example.g3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SurveyInsightActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_insight);
        webView = (WebView) findViewById(R.id.mSurveyResultWebView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://app.powerbi.com/reportEmbed?repo" +
                "rtId=3c155143-d316-41c9-9a9f-e08377412f39&autoAuth=t" +
                "rue&ctid=ae5ed6e2-682f-4436-a3d2-d07186f2c1da&config=eyJj" +
                "bHVzdGVyVXJsIjoiaHR0cHM6Ly93YWJpLXNvdXRoLWVhc3QtYXNpYS1yZW" +
                "RpcmVjdC5hbmFseXNpcy53aW5kb3dzLm5ldC8ifQ%3D%3D");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    // Back button
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}



