package com.example.tekpreneur.sdcardimages;

import java.io.File;
import java.util.List;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends AppCompatActivity {

	private static final String TAG ="znt" ;
	// Declare variables
	private String[] FilePathStrings;
	private String[] FileNameStrings;
	private File[] listFile;
	File file;
	private HorizontalImageAdapter imageAdapter;
	private HorizontalView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview_main);

		isStoragePermissionGranted();
		// Check for SD Card
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG)
					.show();
		} else {
			// Locate the image folder in your SD Card
			file = new File(Environment.getExternalStorageDirectory()
					+ File.separator + "Demo");
			// Create a new folder if no folder named SDImageTutorial exist
			file.mkdirs();
		}

		if (file.isDirectory()) {
			listFile = file.listFiles();
			// Create a String array for FilePathStrings
			FilePathStrings = new String[listFile.length];
			// Create a String array for FileNameStrings
			FileNameStrings = new String[listFile.length];

			for (int i = 0; i < listFile.length; i++) {
				// Get the path of the image file
				FilePathStrings[i] = listFile[i].getAbsolutePath();
				// Get the name image file
				FileNameStrings[i] = listFile[i].getName();
			}
		}

		 listView = (HorizontalView) findViewById(R.id.gallery);
		//getDrawablesList();
		imageAdapter = new HorizontalImageAdapter(this, FilePathStrings, FileNameStrings);
		listView.setAdapter(imageAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {


			ImageView	imageview = (ImageView) findViewById(R.id.full_image_view);

				// Decode the filepath with BitmapFactory followed by the position
				Bitmap bmp = BitmapFactory.decodeFile(FilePathStrings[position]);

				// Set the decoded bitmap into ImageView
				imageview.setImageBitmap(bmp);
			}

		});

	}

	public boolean isStoragePermissionGranted() {
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
					== PackageManager.PERMISSION_GRANTED) {
				Log.v(TAG, "Permission is granted");
				return true;
			} else {

				Log.v(TAG, "Permission is revoked");
				ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
				return false;
			}
		} else { //permission is automatically granted on sdk<23 upon installation
			Log.v(TAG, "Permission is granted");
			return true;
		}
	}

}
