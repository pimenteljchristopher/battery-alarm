package com.fim.batteryalarm;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class OptionActivity extends Fragment{
	protected static final int REQ_CODE_PICK_SOUNDFILE = 0;
	protected static final int MODE_PRIVATE = 0;
	public BroadcastReceiver mbcr=new BroadcastReceiver()
	  {
		 public void onReceive(Context c, Intent i)
		  { 
			 HomeActivity ha = new HomeActivity();
			 TextView healthView = (TextView)getView().findViewById(R.id.healthtext);
			 TextView techView = (TextView)getView().findViewById(R.id.techtext);
			 TextView tempView = (TextView)getView().findViewById(R.id.temptext);
			 TextView voltView = (TextView)getView().findViewById(R.id.volttext);
			 TextView path  = (TextView)getView().findViewById(R.id.path);
			 int health = i.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
			 String BatteryHealth = " No Information";
			 String  tech= i.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
			 int temp = i.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
			 int volt = i.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
			 
			  if (health == BatteryManager.BATTERY_HEALTH_COLD){BatteryHealth = "Cold";}
		      if (health == BatteryManager.BATTERY_HEALTH_DEAD){BatteryHealth = "Dead";}
		      if (health == BatteryManager.BATTERY_HEALTH_GOOD){BatteryHealth = "Good";}
		      if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){BatteryHealth = "Over-Voltage";}
		      if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT){BatteryHealth = "Overheat";}
		      if (health == BatteryManager.BATTERY_HEALTH_UNKNOWN){BatteryHealth = "Unknown";}
		      if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE){BatteryHealth = "Unspecified Failure";}
		      
		  	SharedPreferences userDetails = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
			String pathVar = userDetails.getString("path", "");
			if(pathVar==""){
				pathVar = "small.mp3";
			}
				
		     path.setText(pathVar+"");
			 healthView.setText(BatteryHealth+"");
			 techView.setText(tech+"");
			 tempView.setText(temp+"");
			 voltView.setText(volt+"");	 
		  }
	  };
	public String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Audio.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
		TextView path  = (TextView)getView().findViewById(R.id.path);
	    if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK){
	        if ((data != null) && (data.getData() != null)){
	            Uri audioFileUri = data.getData();
	           String audioFilePath = getRealPathFromURI(getActivity(),audioFileUri);
	           
	            SharedPreferences userDetails = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
	            Editor edit = userDetails.edit();
	            edit.clear();
	            edit.putString("path",audioFilePath);
	            edit.commit();
	            path.setText(audioFilePath);
	         }
	  }
	 }	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
	        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_option, container, false);
	        return rootView;
	    }
		public void onActivityCreated(Bundle savedInstanceState){
			   super.onActivityCreated(savedInstanceState);
			   AdView mAdView = (AdView)getView().findViewById(R.id.adView);
		        AdRequest adRequest = new AdRequest.Builder().build();
		        mAdView.loadAd(adRequest);
			   getActivity().registerReceiver(mbcr,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
				Button clickButton = (Button)getView().findViewById(R.id.button1);
				clickButton.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						Intent intent;
						intent = new Intent();
						intent.setAction(Intent.ACTION_GET_CONTENT);
						intent.setType("audio/mpeg");
						startActivityForResult(Intent.createChooser(intent, getString(R.string.select_audio_file_title)), REQ_CODE_PICK_SOUNDFILE);
					}
		});
        }	
}
