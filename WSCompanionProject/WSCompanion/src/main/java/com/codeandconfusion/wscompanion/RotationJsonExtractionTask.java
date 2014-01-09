package com.codeandconfusion.wscompanion;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.codeandconfusion.model.Rotation;
import com.codeandconfusion.model.Rotations;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class RotationJsonExtractionTask extends AsyncTask<Void, Void, ArrayList<Rotation>> {

    private WeakReference<Context> contextWeakReference;

    public RotationJsonExtractionTask(Context context) {
        contextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    protected void onPreExecute() {
        Context context = contextWeakReference.get();
        if (context != null) {
            Toast.makeText(context, "Start!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected ArrayList<Rotation> doInBackground(Void... params) {
        Context context = contextWeakReference.get();
        ArrayList<Rotation> result = null;

        if (context != null) {
            InputStream jsonInputStream = null;
            StringBuilder jsonStringBuilder = new StringBuilder();

            try {
                jsonInputStream = context.getResources().getAssets().open("rotation.json");

                if (jsonInputStream != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jsonInputStream));
                    String currentLine;

                    while ((currentLine = bufferedReader.readLine()) != null) {
                        jsonStringBuilder.append(currentLine);
                    }

                    Gson gson = new Gson();
                    Rotations rotations = gson.fromJson(jsonStringBuilder.toString(), Rotations.class);
                    result = rotations.getRotations();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (jsonInputStream != null) {
                    try {
                        jsonInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(ArrayList rotations) {
        Context context = contextWeakReference.get();
        if (context != null && rotations != null) {
            Toast.makeText(context, "Stop! We found stuff! " + rotations.size(), Toast.LENGTH_SHORT).show();
        }
    }
}
