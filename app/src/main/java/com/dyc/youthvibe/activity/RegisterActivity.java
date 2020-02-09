package com.dyc.youthvibe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dyc.youthvibe.R;

public class RegisterActivity extends AppCompatActivity {


    ProgressBar progressBar;
    Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = findViewById(R.id.toolbar);

        final WebView webView = findViewById(R.id.webView);

        progressBar = findViewById(R.id.progressBar);

        webView.getSettings().setJavaScriptEnabled(true);

        setSupportActionBar(toolbar);

        webView.loadUrl("http://youthvibe.lpu.in/accounts");


        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                view.evaluateJavascript(
                        "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                        new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String html) {

                                Toast.makeText(RegisterActivity.this, "Login with the Same Credentials.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();

                                Log.d("HTML", html);
                                // code here
                            }
                        });
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //   activity.setTitle("Loading...");
                //activity.setProgress(progress * 100);

                if(progress == 100) {
                    progressBar.setVisibility(View.GONE);


                    webView.evaluateJavascript(
                            "toggleSignup(); document.getElementById(\"top\").style.display = \"none\";" +
                                    "document.getElementsByClassName(\"mobile-nav\")[0].style.display = \"none\";" +
                                    "document.getElementsByTagName(\"footer\")[0].style.display = \"none\";" +
                                    "document.getElementsByTagName(\"p\")[1].style.display = \"none\";" +
                                    "document.getElementsByClassName(\"form-toggle\")[0].style.display = \"none\";" +
                                    "$(\"iframe\").remove();" +
                                    "for (var i=2; i<document.getElementsByTagName(\"input\").length; i++)\n" +
                                    "document.getElementsByTagName(\"input\")[i].setAttribute(\"required\", \"\");",
                            new ValueCallback<String>() {
                                @Override
                                public void onReceiveValue(String html) {
                                    Log.d("HTML", html);
                                    // code here
                                }
                            });


                }
            }
        });


    }
}
