package com.tekkom.meawapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class LearnActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private ActionBar toolbar;
    PDFView pdfView;
    private String fileURL;
    private static final String TAG = LearnActivity.class.getSimpleName();
    private static final int WRITE_REQUEST_CODE = 300;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        toolbar = getSupportActionBar();
        Intent intent = getIntent();
        String namaMateri = intent.getStringExtra("namaMateri");
        String deskripsi = intent.getStringExtra("deskripsi");
        String image = intent.getStringExtra("image");
        fileURL = intent.getStringExtra("fileURL");
        toolbar.setTitle("Materi - "+namaMateri);
        pdfView = (PDFView)findViewById(R.id.pdfView);
        new RetrievePDFStream().execute(fileURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.action_bar_student_fragment, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.downloadMateri){
            Toast.makeText(this, "Download mulai", Toast.LENGTH_SHORT).show();
            if (CheckForSDCard.isSDCardPresent()) {

                //check if app has permission to write to the external storage.
                if (EasyPermissions.hasPermissions(LearnActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Get the URL entered
                    new DownloadFile().execute(fileURL);

                } else {
                    //If permission is not present request for the same.
                    EasyPermissions.requestPermissions(LearnActivity.this, "This app needs access to your file storage so that it can write files.", WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);
                }


            } else {
                Toast.makeText(getApplicationContext(),
                        "SD Card not found", Toast.LENGTH_LONG).show();

            }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, LearnActivity.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //Download the file once permission is granted
        new DownloadFile().execute(fileURL);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "Permission has denied", Toast.LENGTH_SHORT).show();
    }


    class RetrievePDFStream extends AsyncTask<String, Void, InputStream>
    {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                if(urlConnection.getResponseCode()==200){
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            catch (IOException e)
            {
                return null;
            }
            return inputStream;
        }
        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).spacing(10).enableAnnotationRendering(true).enableSwipe(true).load();
        }
    }

    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /*
          Before starting background thread
          Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(LearnActivity.this);
            this.progressDialog.setMessage("Downloading file. Please wait...");
            this.progressDialog.setIndeterminate(false);
            this.progressDialog.setMax(100);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(true);
            this.progressDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());
                //Append timestamp to file name
                fileName = timestamp + "_" + fileName;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "MEAWFolder/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }
                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();
        }
    }
}
