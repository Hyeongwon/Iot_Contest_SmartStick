package com.appforblind;

import com.appforblind.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Popup extends Activity {

	TextView Title;
	Button btn1 , btn2;
	
	Typeface typeface_cat;
	
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);	
		
		typeface_cat = Typeface.createFromAsset(getAssets(), "cat.ttf");
		
		setContentView(R.layout.popup);
		
		Title = (TextView) findViewById(R.id.message);
		Title.setTypeface(typeface_cat);
		Title.setText(Html.fromHtml("<b>낙상이 발생하였습니다.</b>"));
		
		btn1 = (Button)findViewById(R.id.confirm1);
		btn1.setTypeface(typeface_cat);
		btn1.setText(Html.fromHtml("<b>확인</b>"));
		btn1.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {

	            finish();
	        }
	    });
		
		btn2 = (Button)findViewById(R.id.confirm2);
		btn2.setTypeface(typeface_cat);
		btn2.setText(Html.fromHtml("<b>취소</b>"));
		btn2.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {

	            finish();
	        }
	    });
		
	}
	
}

