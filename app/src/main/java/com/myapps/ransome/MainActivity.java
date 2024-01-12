package com.myapps.ransome;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;

import com.myapps.python.PyException;
import com.myapps.python.Python;
import com.myapps.python.PyObject;
import com.myapps.python.android.AndroidPlatform;
import com.myapps.ransome.*;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.PrintStream;

public class MainActivity extends Activity {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private TextView logTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logTextView = findViewById(R.id.logTextView);

        // Request permissions if necessary
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
			checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Use ActivityCompat for permission requests (part of v4 support library)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                // Handle permission requests for older Android versions (API < 23)
            }
        } else {
            exepy();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
		{
            exepy();
        }
		else
		{
            log("Permission denied. File operations won't work.");
        }
    }

    private void exepy()
	{
        // Start Python if necessary
        if (!Python.isStarted())
		{
            Python.start(new AndroidPlatform(this));
        }
        // Execute the Python script
		try
		{
			Python py = Python.getInstance();
			PyObject object = py.getModule("main").callAttr("main"); // Assuming the main function is in main.py
			final String output = object.toString();
			log("OUTPUT: \n" + output);
		}
		catch (Exception e)
		{
			log("ERROR: \n" + e.toString());
		}
	}
	private void log(String message)
	{
		logTextView.append(message + "\n");

	};

}
