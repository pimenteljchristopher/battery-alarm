package com.fim.batteryalarm;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class HomeActivity extends Fragment{
	public BroadcastReceiver mbcr=new BroadcastReceiver()
	  {
		 
	  AnimationDrawable ChargingAnimation;
	  public void onReceive(Context c, Intent i)
	  {
		  TextView tv=(TextView)getView().findViewById(R.id.textView1);
		  TextView charging=(TextView)getView().findViewById(R.id.textView2);
		  TextView info=(TextView)getView().findViewById(R.id.textView3);
		  TextView estimated=(TextView)getView().findViewById(R.id.textView4);
		  ImageView chargeAnimate = (ImageView)getView().findViewById(R.id.chargeView);
		  int level=i.getIntExtra("level", 0);
		  int levelr = i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		  int scale = i.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		  int timeIncrease =0;
		  int futurelevel = level+1;
		  int status = i.getIntExtra(BatteryManager.EXTRA_STATUS, -1);;
	      boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
	      int totalSeconds = 0;
	      chargeAnimate.setBackgroundResource(R.drawable.animetecharge);
		  ChargingAnimation = (AnimationDrawable)chargeAnimate.getBackground();
		  for(int loop=level;loop<=futurelevel;loop++){
			  timeIncrease = loop;  
		  }
		  if(isCharging == true){
			  totalSeconds = (scale-level)*timeIncrease;
		  }
		  else{
			//  totalSeconds = level*timeIncrease;
		  }

		  final int MINUTES_IN_AN_HOUR = 60;
		  final int SECONDS_IN_A_MINUTE = 60;
		  int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
		  int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
		  int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
		  int hours = totalMinutes / MINUTES_IN_AN_HOUR;
		  
		  tv.setText(""+Integer.toString(level)+"%");	  
		//  AnimationDrawable ChargingAnimation = (AnimationDrawable) getResources().getDrawable(R.drawable.animetecharge);
		 // ChargingAnimation.setBounds(0, 0, 250, 250);
		
		  
		  if(isCharging == true){
			 ChargingAnimation.start();
		     charging.setText("CHARGING");
			 info.setText("Estimated Finished Time");
			 estimated.setText(+hours + ":" + minutes + ":" + seconds );
		}
		  else{ 
			ChargingAnimation.stop();
			chargeAnimate.setBackgroundDrawable(null);
			chargeAnimate.setBackgroundResource(R.drawable.idle);
			charging.setText("DISCHARGE");
			info.setText("Battery Life");
		    estimated.setText(+hours + ":" + minutes + ":" + seconds );
		}
		  if(level==100 && isCharging == true){
//			   try
//			   {
//			   AssetFileDescriptor afd=getAssets().openFd("small.mp3");
//			   final MediaPlayer mp=new MediaPlayer();
//			   mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//			   mp.prepare();
//			   //start song
//			   mp.start();
//			   AlertDialog alert = new AlertDialog.Builder( MainActivity.this).create();
//		          alert.setTitle("Battery Alam");
//		          alert.setMessage("You are now fully charged.");
//		          alert.setButton("OK", new DialogInterface.OnClickListener() {
//		              public void onClick(DialogInterface dialog, int which) {
//		                  // TODO Auto-generated method stub
//		            	  mp.stop();
//		              }
//		          });
//		          alert.show();
//			    }
//			    catch(IOException e){
//			    }
		  }
	   
   }


	};
	

	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
//		registerReceiver(mbcr,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_home, container, false);
	        return rootView;
	    }
	
	public void onActivityCreated(Bundle savedInstanceState){
		   super.onActivityCreated(savedInstanceState);
		   getActivity().registerReceiver(mbcr,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		   
		 	   }
}
