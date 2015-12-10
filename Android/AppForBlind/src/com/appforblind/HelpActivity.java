package com.appforblind;

import com.appforblind.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpActivity extends Activity {

	Typeface typeface_cat;
	
	TextView textview , helpText;
	LinearLayout layout;
	
	SoundPool sp ;
    int btnSound ;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
        setContentView(R.layout.help_activity);
        layout = (LinearLayout) findViewById(R.id.helplayout);
        layout.setBackgroundResource(R.drawable.background);

        typeface_cat = Typeface.createFromAsset(getAssets(), "cat.ttf");
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
		btnSound = sp.load(this,R.raw.btn8, 1);
		
		textview = (TextView) findViewById(R.id.helpTitle);
		textview.setText(Html.fromHtml("<b>도움말</b>"));
		textview.setTypeface(typeface_cat);
		
		helpText = (TextView)findViewById(R.id.helpText);		
		helpText.setTypeface(typeface_cat);		
		
		helpText.setText(
						"1. 낙상인식기능을 켜면 앱을 종료한 상태에서도 지속적으로 낙상을 감지합니다.\n\n"+
						"2. 대상자가 휴식을 취하거나 보호자가 있을 경우 인식 기능을 꺼주십시오. 배터리 손실의 원인이 될 수 있습니다.\n\n" +
						"3. 대상자의 활동 가능도에 따라 센서 감도를 조절 하십시오. 감도가 높을수록 작은 움직임에 반응합니다.\n\n"+
						"4. 기능을 켜기 전에 보호자 연락처를 알맞게 등록 했는지 확인하십시오.\n\n"+
						"5. 기능을 켰을 경우 불필요하게 휴대전화를 흔들지 마십시오 오작동의 원인이 됩니다.\n\n"+
						"6. 낙상 감지시 등록된 보호자 연락처로 메세지가 보내지므로 메시지 송신 요금을 부담해야 할 가능성이 있습니다.\n\n"+
						"7. 불필요한 메시지 송신을 줄이기 위해 필요시에만 낙상 인식 기능을 켜시고 센서 감도를 알맞게 조정하시기 바랍니다.\n\n");
		
		
        
        
    }	

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
		sp.play(btnSound, 1,1,0,0,1);
	}
	
	
	
	

}
