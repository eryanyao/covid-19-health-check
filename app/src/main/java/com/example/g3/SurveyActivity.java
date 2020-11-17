package com.example.g3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SurveyActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        webView = (WebView) findViewById(R.id.mWebView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://forms.office.com/Pages/ResponsePage.a" +
                "spx?id=4tZeri9oNkSj0tBxhvLB2iBKZKHFr79Kjmx" +
                "F1C9qLrpUNjJKNUc1S1pQUTRQNkRWWEtPM0pHS0RVQi4u");
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





