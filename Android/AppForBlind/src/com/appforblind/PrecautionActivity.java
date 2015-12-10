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
		title.setText(Html.fromHtml("<b>���� ���� ���</b>"));
        
		text = (TextView)findViewById(R.id.preText);		
		text.setTypeface(typeface_cat);		
		
		text.setText(Html.fromHtml("<b>[ ���� ���� ����]</b>"));
		text.append(
						"\n1. ���� �� ������ ���� ���\n\n"+
						"2. ���� ����� ���� ���� ���\n\n" +
						"3. �ȱ� ������ ���\n\n"+
						"4. ���� ���밡 ���ġ ���� ���� ������ ������ ���\n\n"+
						"5. ������ ���ڱ� ������ �������ﶧ\n\n"+
						"6. �ٴ��� �̲����� ���\n\n"+
						"7. �ٴ��� ���ǿ� ���Ͽ� �Ѿ����� ���\n\n");
		text.append(Html.fromHtml("<b>[ ���� ���� ��ħ] </b>"));
		text.append(
				"\n1. ���� ����� �ڵ�����\n\n"+
				"2. �ٴ��� �������� �ʰ� ������\n\n" +
				"3. ��� �ϱ� �� ����� �غ��� ��\n\n"+
				"4. �ʿ��ϴٸ� �����̳� ��Ŀ ���� ���� ������ �غ���\n\n"+
				"5. �������� ������ ��� ��ȣ�ڿ� �Բ� ������\n\n"+
				"6. ��� �ϱ� �� ����� �غ��� ��\n\n"+
				"7. �ٴ��� ���⸦ �����ϰ� �߿� �´� �Ź��� ������\n\n");
        
    }
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
		sp.play(btnSound, 1,1,0,0,1);
	}

}
