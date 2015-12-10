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
		textview.setText(Html.fromHtml("<b>����</b>"));
		textview.setTypeface(typeface_cat);
		
		helpText = (TextView)findViewById(R.id.helpText);		
		helpText.setTypeface(typeface_cat);		
		
		helpText.setText(
						"1. �����νı���� �Ѹ� ���� ������ ���¿����� ���������� ������ �����մϴ�.\n\n"+
						"2. ����ڰ� �޽��� ���ϰų� ��ȣ�ڰ� ���� ��� �ν� ����� ���ֽʽÿ�. ���͸� �ս��� ������ �� �� �ֽ��ϴ�.\n\n" +
						"3. ������� Ȱ�� ���ɵ��� ���� ���� ������ ���� �Ͻʽÿ�. ������ �������� ���� �����ӿ� �����մϴ�.\n\n"+
						"4. ����� �ѱ� ���� ��ȣ�� ����ó�� �˸°� ��� �ߴ��� Ȯ���Ͻʽÿ�.\n\n"+
						"5. ����� ���� ��� ���ʿ��ϰ� �޴���ȭ�� ����� ���ʽÿ� ���۵��� ������ �˴ϴ�.\n\n"+
						"6. ���� ������ ��ϵ� ��ȣ�� ����ó�� �޼����� �������Ƿ� �޽��� �۽� ����� �δ��ؾ� �� ���ɼ��� �ֽ��ϴ�.\n\n"+
						"7. ���ʿ��� �޽��� �۽��� ���̱� ���� �ʿ�ÿ��� ���� �ν� ����� �ѽð� ���� ������ �˸°� �����Ͻñ� �ٶ��ϴ�.\n\n");
		
		
        
        
    }	

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
		sp.play(btnSound, 1,1,0,0,1);
	}
	
	
	
	

}
