package com.appforblind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.UUID;

import org.jtransforms.fft.DoubleFFT_1D;

import com.appforblind.R;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class DetectActivity extends Activity {

	Activity activity;
	LinearLayout layout;
	TextView Title , textView1 , textView2 , textView3 , textView4;
	Button btn1,btn2,btn3;
	Switch sw;
	
	Typeface typeface_cat;
	
	SoundPool sp ;
    int btnSound ;
    
    ProgressDialog pd;
    Handler fristhandler;
    Context context;
    
    ////////////////////////////////bluetooth
    BluetoothDevice device;
    BluetoothAdapter badapter;
    BluetoothDevice device_mod;
    BluetoothAdapter badapter_mod;
    String address = "00:18:9A:04:0A:08"; // 스마트폰에 연결되는 블루투스 모듈의
    String address_mod = "00:01:95:21:5A:68"; // 스마트폰에 연결되는 블루투스 모듈의
//    String address = "00:18:9A:04:21:EF"; // 스마트폰에 연결되는 블루투스 모듈의
    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // 통신프로토콜
	BluetoothSocket mmSocket; // 소켓통신에 쓸 소켓
	BluetoothSocket mmSocket_mod; // 소켓통신에 쓸 소켓
	OutputStream output;
	BufferedReader reader;
	BufferedWriter writer;
	OutputStream output_mod;
	BufferedReader reader_mod;
	BufferedWriter writer_mod;
	boolean bluetooth = false;
	
	///////////////////////////////AcyncTask
	BtAsyncTask btAsyncTask;
	FftAsyncTask fftAsyncTask;
	boolean isGoing = false;
	
	////////////////////////////////
	String[] array;
	String value = "";
	String textView_Value = "";
	String accelerometer_X;
	String accelerometer_Y;
	String accelerometer_Z;

	double signalVectorMagnitude;

	final static double svm_TH = 1.7;

	// ///////////////////////////////FFT

	DoubleFFT_1D doubleFFT;
	ArrayList<Double> dataList;
	int windowSize = 75;
	int blockSize = 128;
	double[] fft;
	double[] magnitude;
	double re, im;
	double freq;

	
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
        setContentView(R.layout.detect_activity);
        
        dataList = new ArrayList<Double>();        
        typeface_cat = Typeface.createFromAsset(getAssets(), "cat.ttf");        
        context = getApplicationContext();
                    
        Title = (TextView) findViewById(R.id.detectTitle);
        Title.setTypeface(typeface_cat);
        Title.setText(Html.fromHtml("<b>낙상 감지</b>"));
        
        textView1 = (TextView)findViewById(R.id.detectText1);
        textView1.setTypeface(typeface_cat);
        textView1.setText(Html.fromHtml("<b>낙상 인식 기능</b>")); 
        
        textView2 = (TextView)findViewById(R.id.detectText2);
        textView2.setTypeface(typeface_cat);
        textView2.setText(Html.fromHtml("<b>센서 감도</b>"));
        
        textView3 = (TextView)findViewById(R.id.detectText3);
        textView3.setTypeface(typeface_cat);
        textView3.setText(Html.fromHtml("<b>주의 사항</b>"));
        
        textView4 = (TextView)findViewById(R.id.detectText4);
        textView4.setTypeface(typeface_cat);
        textView4.setText("▶ 대상자가 휴식을 취하거나 보호자가 있을 \n     경우 인식 기능을 꺼주십시오\n"+
        				"▶ 대상자의 활동 가능도에 따라 센서 감도를 \n    조절하십시오. 감도가 높을 수록 작은 움직임에\n    반응합니다.\n"+
        		        "▶ 기능을 켜기 전에 보호자 연락처를 알맞게 \n    등록했는지 확인하십시오.\n"+
        				"▶ 불필요하게 밴드를 흔들지 마십시오. 오작동의 \n    원인이 됩니다.\n");
        
        
        layout = (LinearLayout) findViewById(R.id.detectlayout);
        layout.setBackgroundResource(R.drawable.background);

       		
		btn1 = (Button)findViewById(R.id.sensor1);
		btn1.setTypeface(typeface_cat);
		btn1.setText(Html.fromHtml("<b>낮음</b>"));		
		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				
			}
		});
		
		btn2 = (Button)findViewById(R.id.sensor2);
		btn2.setTypeface(typeface_cat);		
		btn2.setText(Html.fromHtml("<b>중간</b>"));
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				
			}
		});
		
		btn3 = (Button)findViewById(R.id.sensor3);
		btn3.setTypeface(typeface_cat);
		btn3.setText(Html.fromHtml("<b>높음</b>"));
		btn3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				
			}
		});

		sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		btnSound = sp.load(this,R.raw.btn8, 1);
		
		fristhandler = new Handler() {
	        public void handleMessage(Message msg) {
	        	pd.dismiss();
	        }    
		};
        
		createThreadAndDialog();

		sw = (Switch) findViewById(R.id.sw);
		sw.setChecked(false);
		sw.setTypeface(typeface_cat);
		sw.setTextOff(Html.fromHtml("<b>꺼짐</b>"));
		sw.setTextOn(Html.fromHtml("<b>켜짐</b>"));
		sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked)
					isGoing = true;
				else
					isGoing = false;
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
	
	public void bluetoothInit() {
		badapter = BluetoothAdapter.getDefaultAdapter(); 
		device = badapter.getRemoteDevice(address);
		badapter_mod = BluetoothAdapter.getDefaultAdapter(); 
		device_mod = badapter_mod.getRemoteDevice(address_mod);
		try {
			mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
			mmSocket.connect();
			Log.d("1", "c");
			mmSocket_mod = device_mod.createRfcommSocketToServiceRecord(MY_UUID);
			mmSocket_mod.connect();
			Log.d("2", "c");
			
			output = mmSocket.getOutputStream();
			output_mod = mmSocket_mod.getOutputStream();
			
			bluetooth = true;
			reader = new BufferedReader(new InputStreamReader(mmSocket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(mmSocket.getOutputStream()));
			reader_mod = new BufferedReader(new InputStreamReader(mmSocket_mod.getInputStream()));
			writer_mod = new BufferedWriter(new OutputStreamWriter(mmSocket_mod.getOutputStream()));
			
			Toast.makeText(getApplicationContext(),"장비와 정상적으로 연결 되었습니다.", Toast.LENGTH_SHORT).show();
			
			btAsyncTask = new BtAsyncTask(mmSocket , Title);		//통신 스레드 시작
			btAsyncTask.execute();		
			
			fftAsyncTask = new FftAsyncTask();				//fft 스레드 시작			
//			fftAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

			Log.d("MainActivity", "AsyncTask executed");

		} catch (IOException e) {
			bluetooth = false;
			Toast.makeText(getApplicationContext(), "장비연결에 실패했습니다.",	Toast.LENGTH_SHORT).show();			
			e.printStackTrace();
		}
	}	
	
	private void createThreadAndDialog() {
		pd = ProgressDialog.show(this, "SmartBandConnect", "연결중. 잠시만 기다려주세요...", true);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				if (!bluetooth) 	bluetoothInit();
				fristhandler.sendEmptyMessage(0);
				Looper.loop();
			}			
		});
		thread.start();
	}
		 
	@SuppressLint("NewApi")
	public void NotificationBuilder(){
        /**
         * FLAG_CANCEL_CURRENT : 이전에 생성한 PendingIntent 는 취소하고 새롭게 만든다.
         * FLAG_NO_CREATE : 생성된 PendingIntent를 반환한다. 재사용 가능하다.
         * FLAG_ONE_SHOT : 이 flag 로 생성한 PendingIntent 는 일회용이다.
         * FLAG_UPDATE_CURRENT : 이미 생성된 PendingIntent 가 존재하면 해당 Intent 의 내용을 변경한다.
         */
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        
        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setTicker("Notification.Builder");
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setNumber(10);
        mBuilder.setContentTitle("Notification.Builder Title");
        mBuilder.setContentText("Notification.Builder Massage");
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        // mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(true);
        
		mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);

		nm.notify(111, mBuilder.build());
		Intent popupIntent = new Intent(context, Popup.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
		context.startActivity(popupIntent);
	}
	
	public class BtAsyncTask extends AsyncTask<Void, String, Void> {

		public BtAsyncTask(BluetoothSocket socket, TextView tv) {
			try {				
				Title = tv;
				reader = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		protected void onCancelled() {	super.onCancelled();}
		protected void onPostExecute(Void result) {	super.onPostExecute(result);}
		protected Void doInBackground(Void... params) {
			if (this.isCancelled())
				return null;
			receiveLoop();
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			//textView4.setText(textView_Value);
			if(signalVectorMagnitude >= 7.0){
				NotificationBuilder();
				try {
					writer_mod.write("e");
					writer_mod.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void receiveLoop() {
			while (true) {
				if(isGoing){
					try {

						value = reader.readLine();
//						Log.d("value", "present Result without saving " + value);						
						array = value.split(",");

						if (array.length >= 6 && value.substring(0, 1).equals("*")) {
							valueParsingIsSaving();
							publishProgress();
						}

					} catch (IOException e) {
						e.printStackTrace();
						break;
					}
				}				
			}

		}

		public void valueParsingIsSaving() {

			accelerometer_X = array[3];
			accelerometer_Y = array[4];
			accelerometer_Z = array[5];

			signalVectorMagnitude = Math
					.sqrt(Math.pow((Double.parseDouble(accelerometer_X) * SensorManager.STANDARD_GRAVITY),2)
							+ Math.pow((Double.parseDouble(accelerometer_Y) * SensorManager.STANDARD_GRAVITY),2)
							+ Math.pow((Double.parseDouble(accelerometer_Z) * SensorManager.STANDARD_GRAVITY),2));

			signalVectorMagnitude = signalVectorMagnitude/ SensorManager.STANDARD_GRAVITY;

			Log.d("value", "svm " + signalVectorMagnitude);
			textView_Value = String.format("  SVM = %.4f \n\n  ",signalVectorMagnitude);

			dataList.add(signalVectorMagnitude);

		}

	}

	public class FftAsyncTask extends AsyncTask<Void, String, Void> {
		
		public FftAsyncTask() {}
		protected void onCancelled() {super.onCancelled();}
		protected void onPostExecute(Void result) {	super.onPostExecute(result);}
		protected Void doInBackground(Void... params) {
			// start the main loop
			if (this.isCancelled())
				return null;
			receiveLoop();
			return null;
		}
		protected void onProgressUpdate(String... values) {
			if(signalVectorMagnitude >= 7.0){
				NotificationBuilder();
			}
		}
		private void receiveLoop() {
			while (true) {
				
					if (isCancelled()) break;
					publishProgress();
//					if(dataList.size()==windowSize){
//						
//						for(int i=0; i<windowSize; i++)	fft[i] = dataList.indexOf(i);
//						
//						doubleFFT = new DoubleFFT_1D(windowSize);
//						doubleFFT.realForwardFull(fft);
//
//						Log.d("transform","@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//						for (int i = 0; i < (windowSize / 2) - 1; i++) {
//							re = fft[2 * i];
//							im = fft[2 * i + 1];
//							magnitude[i] = Math.sqrt(re * re + im * im);
//							freq = ((double) (i * 50) / (double) (windowSize));
//							Log.d("FileRead", String.format("%.6f", freq) + " : magnitude = "	+ magnitude[i]);
////							try {
////								out.append(String.format("%.6f", freq)+" "+magnitude[i]+"\n");
////								out.flush();
////							} catch (IOException e){e.printStackTrace();}									
//						}
//						
//						dataList.clear();
//						
//					}				
			}
		}		

	}

}
