package com.appforblind;

import com.appforblind.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PrecautionActivity extends Activity {

	LinearLayout layout;
	TextView title , text;
	Typeface typeface_cat;
	
	SoundPool sp ;
    int btnSound ;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_enter, R.anim.start_exit);     
        setContentView(R.layout.precaution_activity);
        
        layout = (LinearLayout) findViewById(R.id.precautionlayout);
        layout.setBackgroundResource(R.drawable.background);

        typeface_cat = Typeface.createFromAsset(getAssets(), "cat.ttf");
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
		btnSound = sp.load(this,R.raw.btn8, 1);
		
		title = (TextView) findViewById(R.id.preTitle);
		title.setTypeface(typeface_cat);		
		title.setText(Html.fromHtml("<b>낙상 예방 방법</b>"));
        
		text = (TextView)findViewById(R.id.preText);		
		text.setTypeface(typeface_cat);		
		
		text.setText(Html.fromHtml("<b>[ 낙상 위험 요인]</b>"));
		text.append(
						"\n1. 앞이 잘 보이지 않을 경우\n\n"+
						"2. 잠을 충분히 자지 못한 경우\n\n" +
						"3. 걷기 불편한 경우\n\n"+
						"4. 영양 섭취가 충분치 못해 뼈와 근육이 약해진 경우\n\n"+
						"5. 혈압이 갑자기 낮아져 어지러울때\n\n"+
						"6. 바닥이 미끄러운 경우\n\n"+
						"7. 바닥의 물건에 의하여 넘어지는 경우\n\n");
		text.append(Html.fromHtml("<b>[ 낙상 예방 지침] </b>"));
		text.append(
				"\n1. 잠을 충분히 자도록함\n\n"+
				"2. 바닥을 어지럽지 않게 정리함\n\n" +
				"3. 운동을 하기 전 충분히 준비운동을 함\n\n"+
				"4. 필요하다면 지팡이나 워커 등의 보조 도구를 준비함\n\n"+
				"5. 움직임이 불편한 경우 보호자와 함께 동행함\n\n"+
				"6. 운동을 하기 전 충분히 준비운동을 함\n\n"+
				"7. 바닥의 물기를 제거하고 발에 맞는 신발을 착용함\n\n");
        
    }
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
		sp.play(btnSound, 1,1,0,0,1);
	}

}
