package com.appforblind;

import java.util.Timer;
import java.util.TimerTask;

import com.appforblind.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroActivity extends Activity {

	ImageView introIcon , appName;	
	TextView introAppName;
	LinearLayout layout;
	
	Typeface typeface_cat;
	
	int check = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro_acticity);

		introIcon = (ImageView) findViewById(R.id.introIcon);
		//appName = (ImageView) findViewById(R.id);
		layout = (LinearLayout) findViewById(R.id.introlayout);
		introAppName = (TextView) findViewById(R.id.introAppName);

		typeface_cat = Typeface.createFromAsset(getAssets(), "cat.ttf");
		layout.setBackgroundResource(R.drawable.background);
		
		introAppName.setTypeface(typeface_cat);
		

		final Timer mTimer = new Timer();

		TimerTask mTask = new TimerTask() {			
			@Override
			public void run() {						
				if(check==1){
					runOnUiThread(new Runnable() {					
						public void run() {
							introIcon.setImageResource(R.drawable.intro3);							
						}
					});					
				}else if(check==2){
					runOnUiThread(new Runnable() {					
						public void run() {
							introIcon.setImageResource(R.drawable.intro2);							
						}
					});				
				}else if(check==3){
					runOnUiThread(new Runnable() {					
						public void run() {
							introIcon.setImageResource(R.drawable.intro1);							
						}
					});				
				}else if(check==5){
					runOnUiThread(new Runnable() {					
						public void run() {
							introAppName.setText(Html.fromHtml("<b> 길 라 잡 이 </b>"));							
						}
					});	
				}else if(check==9){//9
					mTimer.cancel();
					 Intent intent = new Intent(IntroActivity.this, MainActivity.class);
					 startActivity(intent);					
					 finish();
					 overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
				}				
				check++;
			}
		};				
		mTimer.schedule(mTask,500,500);
	}	
}
