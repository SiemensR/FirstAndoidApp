package com.example.user.myapplication;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import java.io.File;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ProgressBar;
import android.util.Log;
import android.widget.Toast;

import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_CODE = 1;
    String ShowOrHideWebViewInitialUse = "show";
    private WebView webview ;
    private ProgressBar spinner;
    private static final String TAG = "MyActivity";
    boolean procedureComplete = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                downloadAndUnzipContent();
                unpackContent();
                    setContentView(R.layout.activity_main);
                    webview = (WebView) findViewById(R.id.webView);
                    spinner = (ProgressBar) findViewById(R.id.progressBar1);
                    webview.setWebViewClient(new CustomWebViewClient());

                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.getSettings().setDomStorageEnabled(true);
                    webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
                    File lFile = new File(Environment.getExternalStorageDirectory() + "/" + "storage/tmp/index.html");
                    webview.loadUrl("file:///" + lFile.getAbsolutePath());

                } else {
                    requestPermission();
                }
        }
    }

        // This allows for a splash screen
        // (and hide elements once the page loads)

        public class CustomWebViewClient extends WebViewClient {

            @Override
            public void onPageStarted(WebView webview, String url, Bitmap favicon) {

                // only make it invisible the FIRST time the app is run
                if (ShowOrHideWebViewInitialUse.equals("show")) {
                    webview.setVisibility(webview.INVISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                ShowOrHideWebViewInitialUse = "hide";
                spinner.setVisibility(View.GONE);

                view.setVisibility(webview.VISIBLE);
                super.onPageFinished(view, url);

            }
        }


    private void downloadAndUnzipContent(){
        new DownloadFile().execute("http://anton-sementsov.bplaced.net/package.zip", "package.zip");
    }

    private void unpackContent(){
        new UnpackZip().execute("/storage/tmp/", "package.zip");
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadAndUnzipContent();
                unpackContent();
                setContentView(R.layout.activity_main);
                webview = (WebView) findViewById(R.id.webView);
                spinner = (ProgressBar) findViewById(R.id.progressBar1);
                webview.setWebViewClient(new CustomWebViewClient());

                webview.getSettings().setJavaScriptEnabled(true);
                webview.getSettings().setDomStorageEnabled(true);
                webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
                File lFile = new File(Environment.getExternalStorageDirectory() + "/" + "storage/tmp/index.html");
                webview.loadUrl("file:///" + lFile.getAbsolutePath());
            } else {
                Toast.makeText(MainActivity.this, "Permission Denied... \n You Should Allow External Storage Permission To Download Files.", Toast.LENGTH_LONG).show();
            }
        }
    }

}