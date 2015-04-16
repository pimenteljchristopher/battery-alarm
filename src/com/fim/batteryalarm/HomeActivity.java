package com.fim.batteryalarm;

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
import android.os.BatteryManager;
import android.os.Bundle;
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
	      
	      TextView tv=(TextView)getView().findViewById(R.id.textView1);
		  tv.setText(""+Integer.toString(level)+"%");	  
	
		  
			Button clickButton = (Button)getView().findViewById(R.id.button1);
			clickButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					  fullBattery();
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
		 
		  TextView info=(TextView)getView().findViewById(R.id.textView3);
		  TextView charging=(TextView)getView().findViewById(R.id.textView2);
		  TextView estimated=(TextView)getView().findViewById(R.id.textView4);
		  ImageView chargeAnimate = (ImageView)getView().findViewById(R.id.chargeView);
		 
		  chargeAnimate.setBackgroundResource(R.drawable.animetecharge);
		  ChargingAnimation = (AnimationDrawable)chargeAnimate.getBackground();
		  ChargingAnimation.start();
	      charging.setText("CHARGING");
		  info.setText("Estimated Finished Time");
		  estimated.setText(+hours+":" + minutes + ":" + seconds);
		 
		
	}
	private void chargingBatteryNot(){
		  ImageView chargeAnimate = (ImageView)getView().findViewById(R.id.chargeView);	
		  TextView estimated=(TextView)getView().findViewById(R.id.textView4);
		 
		  chargeAnimate.setBackgroundResource(R.drawable.animetecharge);
		  ChargingAnimation = (AnimationDrawable)chargeAnimate.getBackground();
		  ChargingAnimation.stop();
		  chargeAnimate.setBackgroundDrawable(null);
		  chargeAnimate.setBackgroundResource(R.drawable.idle);
		
	      estimated.setText(00+":" + 00 + ":" + 00);
	}
	private void fullBattery(){
		 try{ 
			   AssetFileDescriptor afd=getActivity().getAssets().openFd(ring());
			   final MediaPlayer mp=new MediaPlayer();
			   mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
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
//			    	tv.setText(e+" ::error::");
			    }
	}

	public String ring(){
//		SharedPreferences userDetails = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
//		String path = userDetails.getString("path", "");
//		if(path == null){
//			 path = "small.mp3";
//		}
		String path  = "small.mp3";
		
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
