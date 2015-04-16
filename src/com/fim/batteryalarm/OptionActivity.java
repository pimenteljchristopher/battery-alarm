package com.fim.batteryalarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	protected static final int MODE_PRIVATE = 0;
	
	
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
		
				Button clickButton = (Button)getView().findViewById(R.id.button1);
				SharedPreferences userDetails = getActivity().getSharedPreferences("userdetails", MODE_PRIVATE);
				String setting_effect_ring = userDetails.getString("setting_effect_ring", "");
				
			 
			
			
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
