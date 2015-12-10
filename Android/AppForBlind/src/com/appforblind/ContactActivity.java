package com.appforblind;

import java.util.ArrayList;

import com.appforblind.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactActivity extends Activity {

	LinearLayout layout;
	TextView title , name , phone , conInfo;
	Button reg;
	Typeface typeface_cat;
	EditText ea, eb , ec , ed;
	String a , b = "010", c , d , info ="";
	
	static ArrayList<ContactObj> contactList;
	ContactObj contact;
	
	SoundPool sp ;
    int btnSound ;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
        setContentView(R.layout.contact_activity);
        
        contactList =  new ArrayList<ContactObj>();	

        contactList.add(new ContactObj("이수환", "010", "3066", "6421"));
        
        conInfo = (TextView) findViewById(R.id.contactInfo);
        
        
        for(int i=0; i<contactList.size(); i++){        	
        	info = (i+1)+". "+ contactList.get(i).getName()+"\n"+contactList.get(i).getP1() + " - " 
        	+ contactList.get(i).getP2() + " - " +contactList.get(i).getP3()+"\n\n" ;
        }
        
        conInfo.setText(info);    
        conInfo.setTypeface(typeface_cat);
        
        layout = (LinearLayout) findViewById(R.id.contactlayout);
        layout.setBackgroundResource(R.drawable.background);
        
        typeface_cat = Typeface.createFromAsset(getAssets(), "cat.ttf");
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
		btnSound = sp.load(this,R.raw.btn8, 1);
        
		title = (TextView) findViewById(R.id.contactTitle);
		title.setTypeface(typeface_cat);		
		title.setText(Html.fromHtml("<b>보호자 연락처</b>"));
		
		name = (TextView) findViewById(R.id.name);
		name.setTypeface(typeface_cat);		
		name.setText(Html.fromHtml("<b>이  름</b>"));
		
		phone = (TextView) findViewById(R.id.phone);
		phone.setTypeface(typeface_cat);		
		phone.setText(Html.fromHtml("<b>휴대폰</b>"));
		
		ea = (EditText)findViewById(R.id.ea);
		ea.setTypeface(typeface_cat);
		ea.setHint("이름을 입력하세요");
		
		eb = (EditText)findViewById(R.id.eb);
		eb.setTypeface(typeface_cat);
		eb.setText(b);
		
		ec = (EditText)findViewById(R.id.ec);
		ec.setTypeface(typeface_cat);
		ec.setHint("0000");
		
		ed = (EditText)findViewById(R.id.ed);
		ed.setTypeface(typeface_cat);	
		ed.setHint("0000");
		
		reg = (Button) findViewById(R.id.regist);
		reg.setTypeface(typeface_cat);		
		reg.setText(Html.fromHtml("<b>등 록 하 기</b>"));
		
        
		reg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				contact = new ContactObj(ea.getText().toString(), 
						eb.getText().toString(), ec.getText().toString(), ed.getText().toString());
				contactList.add(contact);
				info = "";
				for(int i=0; i<contactList.size(); i++){        	
		        	info += (i+1)+". "+contactList.get(i).getName()+"\n"+contactList.get(i).getP1() + " - " 
		        	+ contactList.get(i).getP2() + " - " +contactList.get(i).getP3()+"\n\n" ;
		        }
				conInfo.setTypeface(typeface_cat);
		        conInfo.setText(info); 
			}
		});
		
		
    }
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
		sp.play(btnSound, 1,1,0,0,1);
	}

}
