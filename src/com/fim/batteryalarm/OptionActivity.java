package com.fim.batteryalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
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

public class OptionActivity extends Fragment{
	protected static final int REQ_CODE_PICK_SOUNDFILE = 0;
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
		TextView path  = (TextView)getView().findViewById(R.id.path);
	    if (requestCode == REQ_CODE_PICK_SOUNDFILE && resultCode == Activity.RESULT_OK){
	        if ((data != null) && (data.getData() != null)){
	            Uri audioFileUri = data.getData();
	            path.setText(audioFileUri+"path");
	         }
	  }
	 }


	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
	        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_option, container, false);
	        return rootView;
	    }
		public void onActivityCreated(Bundle savedInstanceState){
			   super.onActivityCreated(savedInstanceState);
			 RadioGroup  batteryeffectButton = (RadioGroup)getView().findViewById(R.id.radioGroup1);
				Button clickButton = (Button)getView().findViewById(R.id.button1);
				
			 
			 batteryeffectButton.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			    {
			        @Override
			        public void onCheckedChanged(RadioGroup group, int checkedId) {
			            // checkedId is the RadioButton selected
			        	TextView batteryeffectText = (TextView)getView().findViewById(R.id.batteryEffectText);
				        
			        	if(checkedId== R.id.radioGroupButton0){
			        		batteryeffectText.setText("blink");
			        	}
			        	else if(checkedId== R.id.radioGroupButton1){
			        		batteryeffectText.setText("vibrate");
			        	}
			        	else{
			        		batteryeffectText.setText("vibrate and ring");
			        	}
			        
			        	
			        	
			        }
			    });
			
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
