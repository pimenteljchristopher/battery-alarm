package com.fim.batteryalarm;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class HomeActivity extends Fragment{
	private static final int MODE_PRIVATE = 0;
	public BroadcastReceiver mbcr=new BroadcastReceiver()
	  { 
	  AnimationDrawable ChargingAnimation;
	  public void onReceive(Context c, Intent i)
	  {
		  int level=i.getIntExtra("level", 0);
		  int scale = i.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		  int status = i.getIntExtra(BatteryManager.EXTRA_STATUS, -1);;
	      boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
	      TextView battery_level=(TextView)getView().findViewById(R.id.battery_level);
		  battery_level.setText(""+Integer.toString(level)+"%");	  
			Button clickButton = (Button)getView().findViewById(R.id.button1);
			clickButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					  fullBattery();
				}
			});
			Button shutdown_button = (Button)getView().findViewById(R.id.shutdown_button);
			shutdown_button.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					int pid = android.os.Process.myPid();
				    android.os.Process.killProcess(pid);
				    System.exit(0);
				}
			});
		  if(isCharging){
			  chargingBattery(level,scale); 
			  if(level==100){
				  fullBattery();
			  }
		  }
		  else{ 
			chargingBatteryNot();
		  }
       }
	  private void chargingBattery(int level,int scale){
		  int totalSeconds = (100-level)*scale;
		  final int MINUTES_IN_AN_HOUR = 60;
		  final int SECONDS_IN_A_MINUTE = 60;
		  int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
		  int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
		  int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
		  int hours = totalMinutes / MINUTES_IN_AN_HOUR;
		  
		  TextView charging_state=(TextView)getView().findViewById(R.id.charging_state);
		  TextView estimate_time=(TextView)getView().findViewById(R.id.estimate_time);
		  ImageView battery_image = (ImageView)getView().findViewById(R.id.battery_image);
		  
		  battery_image.setBackgroundResource(R.drawable.animetecharge);
		  ChargingAnimation = (AnimationDrawable)battery_image.getBackground();
		  ChargingAnimation.start();
	      charging_state.setText("Charging");
		
		  estimate_time.setText(+hours+":" + minutes + ":" + seconds);
	}
	private void chargingBatteryNot(){
		  TextView charging_state=(TextView)getView().findViewById(R.id.charging_state);
		  ImageView battery_image = (ImageView)getView().findViewById(R.id.battery_image);	
		  TextView estimate_time=(TextView)getView().findViewById(R.id.estimate_time);
		 
		  battery_image.setBackgroundResource(R.drawable.animetecharge);
		  ChargingAnimation = (AnimationDrawable)battery_image.getBackground();
		  ChargingAnimation.stop();
		  battery_image.setBackgroundDrawable(null);
		  battery_image.setBackgroundResource(R.drawable.idle);
		
		  charging_state.setText("Discharging");
	      estimate_time.setText(00+":" + 00 + ":" + 00);
	}
	private void fullBattery(){
	    try{ 
		FileDescriptor fd = null;
		final MediaPlayer mp=new MediaPlayer(); 		
		   if(ring()!=null){
			   FileInputStream fis = new FileInputStream(ring());
			      fd = fis.getFD();	
			      mp.setDataSource(fd);
		   }else{
			   AssetFileDescriptor afd=getActivity().getAssets().openFd("small.mp3");
			   mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
		   }
		   mp.prepare();
		   mp.start();
			   AlertDialog alert = new AlertDialog.Builder( getActivity()).create();
		          alert.setTitle("Battery Alarm");
		          alert.setMessage("You are now fully charged.");
		          alert.setButton("OK", new DialogInterface.OnClickListener() {
		              public void onClick(DialogInterface dialog, int which) {
		            	  mp.stop();
		              }
		          });
		          alert.show();
			    }
			    catch(IOException e){
			    
			    }
	}
	public String ring(){
		SharedPreferences userDetails = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
		String path = userDetails.getString("path", "");
		return  path;
	}
	};
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
	        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);
	        return rootView;
	    }
	public void onActivityCreated(Bundle savedInstanceState){
		   super.onActivityCreated(savedInstanceState);
		   getActivity().registerReceiver(mbcr,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		}
}
