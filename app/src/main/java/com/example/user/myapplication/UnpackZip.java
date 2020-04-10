package com.example.user.myapplication;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnpackZip extends AsyncTask<String, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String path = Environment.getExternalStorageDirectory().toString() + strings[0];
        String zipname = strings[1];
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

            InputStream is;
            ZipInputStream zis;
            try
            {
                is = new FileInputStream(path + zipname);
                zis = new ZipInputStream(new BufferedInputStream(is));
                ZipEntry ze;

                while((ze = zis.getNextEntry()) != null)
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int count;

                    String filename = ze.getName();
                    FileOutputStream fout = new FileOutputStream(path + filename);

                    // reading and writing
                    while((count = zis.read(buffer)) != -1)
                    {
                        baos.write(buffer, 0, count);
                        byte[] bytes = baos.toByteArray();
                        fout.write(bytes);
                        baos.reset();
                    }

                    fout.close();
                    zis.closeEntry();
                }

                zis.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}