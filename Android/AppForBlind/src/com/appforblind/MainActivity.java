package com.appforblind;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
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
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener  {

	Typeface typeface_cat;
	LinearLayout layout;	
	TextView menuTextView , microText , signalText , activationText , fallText;
		
	Switch beaconSwitch , stickSwitch , fallSwitch;
		
	SoundPool sp ;
    int btnSound ;
    int alramSound ;
    ////////////////////////////////
    ProgressDialog pd;
    Handler fristhandler;
    String speak = "";
    
	// //////////////////////////////bluetooth_to_stick
	BluetoothDevice deviceStick;
	BluetoothAdapter badapter;	
	String addressStick = "00:18:9A:04:21:F3"; // ����Ʈ���� ����Ǵ� ������� �����	
	UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // �����������
	BluetoothSocket mmSocketStick; // ������ſ� �� ����	
	OutputStream outputStick;
	BufferedReader readerStick;
	BufferedWriter writerStick;	
	boolean bluetooth_Stick = false;
	
	// //////////////////////////////bluetooth_to_stick
	BluetoothDevice deviceFall;	
	
	String addressFall = "00:18:9A:04:0A:08"; 	
	BluetoothSocket mmSocketFall; 	
	OutputStream outputFall;
	BufferedReader readerFall;
	BufferedWriter writerFall;	
	boolean bluetooth_Fall = false;
	// //////////////////////////////bluetooth_mod
	BluetoothDevice device_mod;
	String address_mod = "00:01:95:21:5A:68"; // ����Ʈ���� ����Ǵ� ������� �����
	BluetoothSocket mmSocket_mod; // ������ſ� �� ����
	OutputStream output_mod;
	BufferedReader reader_mod;
	BufferedWriter writer_mod;
	boolean bluetooth_mod = false;
	////////////////////////////////////AsyncTask
	StickAsyncTask stickAsyncTask;
	FallAsyncTask fallAsyncTask;
	String valueStick = "";
	String valueFall = "";
	
	boolean beaconSignal = false;
	boolean stickSignal = false;
	boolean fallSignal = false;
	boolean detectObstacle = false;
	boolean fallCheckIsGoing = false;

	// //////////////////////////////FFT
	String[] array;
	
	
	String accelerometer_X;
	String accelerometer_Y;
	String accelerometer_Z;

	ArrayList<Double> dataList;
	double signalVectorMagnitude;
	final static double svm_TH = 1.7;
	
	////////////////////////////////////beacon
	
	private TextToSpeech mTts;

	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;
	private boolean Search_state;
	private Handler mHandler;
	int user_data_count = 0;

	private static final int REQUEST_ENABLE_BT = 1;
	private static final long SCAN_PERIOD = 100000;

	BLE_Device mBLE_Device;
	
	boolean sayingLocation = false;
	boolean mScanning_run = false;
	
	int index = 0;
	double distanceAcuracy = 0;
	
	Vibrator vibrator;
	long[] pattern = MorseCodeConverter.pattern("beacon");
	
	public void onInit(int status) {		
		if (status == TextToSpeech.SUCCESS) {			
			int result = mTts.setLanguage(Locale.KOREAN);
			if (result == TextToSpeech.LANG_MISSING_DATA|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("speech", "Language is not available.");
			} 
		} 
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sp = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
		btnSound = sp.load(this,R.raw.btn8, 1);
		alramSound = sp.load(this,R.raw.btn, 1);
		
		fristhandler = new Handler() {
	        public void handleMessage(Message msg) {
	        	pd.dismiss();
	        }    
		};
		
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mTts = new TextToSpeech(this, this);
		mHandler = new Handler();
		mBLE_Device = new BLE_Device();
		
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

	
//
//		if (mBluetoothAdapter == null) {
//			Toast.makeText(this, "ble not support2", Toast.LENGTH_SHORT).show();
//			finish();
//			return;
//		}
		dataList = new ArrayList<Double>();  
		
		layout = (LinearLayout) findViewById(R.id.mainayout);
		layout.setBackgroundResource(R.drawable.background);
		
		typeface_cat = Typeface.createFromAsset(getAssets(), "cat.ttf");
		
		menuTextView = (TextView) findViewById(R.id.menuText);
		menuTextView.setTypeface(typeface_cat);
		menuTextView.setText(Html.fromHtml("<b>Menu</b>"));
		
		fallText = (TextView) findViewById(R.id.fallText);
		fallText.setTypeface(typeface_cat);
		fallText.setText(Html.fromHtml("<b>���� ����</b>"));
		
		microText = (TextView) findViewById(R.id.microText);
		microText.setTypeface(typeface_cat);
		microText.setText(Html.fromHtml("<b>����Ʈ ������</b>"));		
		
		signalText = (TextView) findViewById(R.id.signalText);
		signalText.setTypeface(typeface_cat);
		signalText.setText(Html.fromHtml("<b>��ȣ�� ����</b>"));
		
		beaconSwitch = (Switch) findViewById(R.id.beaconSw);		
				
		stickSwitch = (Switch) findViewById(R.id.stickSw);			
		
		fallSwitch = (Switch) findViewById(R.id.fallSw);		
		
		createThreadAndDialog();
		
		
	}
	
	private void createThreadAndDialog() {
		pd = ProgressDialog.show(this, "SmartStick", "������. ��ø� ��ٷ��ּ���...", true);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				if (!bluetooth_Stick) 	bluetoothInit();
				fristhandler.sendEmptyMessage(0);
				Looper.loop();
			}			
		});
		thread.start();
	}
	
	public void bluetoothInit() {
		badapter = BluetoothAdapter.getDefaultAdapter(); 
		//badapterFall = BluetoothAdapter.getDefaultAdapter();
		deviceStick = badapter.getRemoteDevice(addressStick);
		deviceFall = badapter.getRemoteDevice(addressFall);
		device_mod = badapter.getRemoteDevice(address_mod);
		
		try {
			mmSocketStick = deviceStick.createRfcommSocketToServiceRecord(MY_UUID);
			mmSocketStick.connect();
			
			mmSocketFall = deviceFall.createRfcommSocketToServiceRecord(MY_UUID);
			mmSocketFall.connect();
			
			mmSocket_mod = device_mod.createRfcommSocketToServiceRecord(MY_UUID);
			mmSocket_mod.connect();
			
			
			
			outputStick = mmSocketStick.getOutputStream();
			outputFall = mmSocketFall.getOutputStream();
			output_mod = mmSocket_mod.getOutputStream();
					
			bluetooth_Stick = true;
			bluetooth_Fall = true;
			bluetooth_mod = true;
			
			readerStick = new BufferedReader(new InputStreamReader(mmSocketStick.getInputStream()));
			writerStick = new BufferedWriter(new OutputStreamWriter(mmSocketStick.getOutputStream()));
			readerFall = new BufferedReader(new InputStreamReader(mmSocketFall.getInputStream()));
			writerFall = new BufferedWriter(new OutputStreamWriter(mmSocketFall.getOutputStream()));
			reader_mod = new BufferedReader(new InputStreamReader(mmSocket_mod.getInputStream()));
			writer_mod = new BufferedWriter(new OutputStreamWriter(mmSocket_mod.getOutputStream()));
			
			Toast.makeText(getApplicationContext(),"���� ���������� ���� �Ǿ����ϴ�.", Toast.LENGTH_SHORT).show();
			
			stickAsyncTask = new StickAsyncTask();		//��� ������ ����
			stickAsyncTask.execute();		
			
			fallAsyncTask = new FallAsyncTask();			
			fallAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			
			Log.d("MainActivity", "AsyncTask executed");

		} catch (IOException e) {
			bluetooth_Stick = false;
			bluetooth_Fall = false;
			bluetooth_mod = false;			
			Toast.makeText(getApplicationContext(), "��񿬰ῡ �����߽��ϴ�.",	Toast.LENGTH_SHORT).show();			
			e.printStackTrace();
		}
	}
	
	public class StickAsyncTask extends AsyncTask<Void, String, Void> {

		public StickAsyncTask() {}
		protected void onCancelled() {	super.onCancelled();}
		protected void onPostExecute(Void result) {	super.onPostExecute(result);}
		protected Void doInBackground(Void... params) {
			if (this.isCancelled())
				return null;
			receiveLoop();
			return null;
		}

		@SuppressWarnings("deprecation")
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);			
			
			if(valueStick.equals("bon")){
				
				mTts.speak("��ȣ�� ���� ���� ����", TextToSpeech.QUEUE_ADD, null);
				beaconSwitch.setChecked(true);
				
				if (!mScanning_run) {
					scanLeDevice(true);
					mScanning_run = true;
					// �ڵ鷯���� �޼����� ����...0���� ��ĵ�� �����ϵ�����...
					mscaneHandler.sendEmptyMessageDelayed(0, 100);
				} 				
				Log.d("value", "beacon on");
				
			}else if(valueStick.equals("boff")){
				
				if (mScanning_run) {
					mScanning_run = false;                
					mscaneHandler.sendEmptyMessageDelayed(2, 100);
				}
				
				mTts.speak("��ȣ�� ���� ���� ����", TextToSpeech.QUEUE_ADD, null);
				beaconSwitch.setChecked(false);
				Log.d("value", "beacon off");
				
			}else if(valueStick.equals("con")){
				
				mTts.speak("��ֹ� ���� ���� ����", TextToSpeech.QUEUE_ADD, null);
				stickSwitch.setChecked(true);
				Log.d("value", "Stick on");
				
			}else if(valueStick.equals("coff")){
				
				mTts.speak("��ֹ� ���� ���� ����", TextToSpeech.QUEUE_ADD, null);
				stickSwitch.setChecked(false);
				Log.d("value", "Stick off");
				
			}else if(valueStick.equals("fon")){
				
				mTts.speak("���� ���� ���� ����", TextToSpeech.QUEUE_ADD, null);
				fallSwitch.setChecked(true);	
				fallCheckIsGoing = true;
				Log.d("value", "Fall on");
				
			}else if(valueStick.equals("foff")){
				
				mTts.speak("���� ���� ���� ����", TextToSpeech.QUEUE_ADD, null);
				fallSwitch.setChecked(false);
				fallCheckIsGoing = false;
				Log.d("value", "Fall off");
				
			}else if(valueStick.equals("d")){
				sp.play(alramSound, 1, 10, 10, 1, 1);
				detectObstacle = false;
				try {
					writer_mod.write("e");
					writer_mod.flush();
				} catch (IOException e) {e.printStackTrace();}
				
			}else{}
			
		}

		private void receiveLoop() {
			while (true) {
				try {
					valueStick = readerStick.readLine();
				
					
					
					publishProgress();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		

	}
	
	public class FallAsyncTask extends AsyncTask<Void, String, Void> {

		public FallAsyncTask() {}
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
			if(signalVectorMagnitude >= 7.0){
				
				NotificationBuilder();
				sp.play(alramSound, 1, 10, 10, 1, 1);
				
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
				if(fallCheckIsGoing){
					try {

						valueFall = readerFall.readLine();
						//Log.d("value", "present Result without saving " + valueFall);						
						array = valueFall.split(",");

						if (array.length >= 6 && valueFall.substring(0, 1).equals("*")) {
							
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

//			Log.d("value", "svm " + signalVectorMagnitude);
			
			dataList.add(signalVectorMagnitude);

		}

	}
	
	// ��ĵ�� ������...

	private void scanLeDevice(final boolean enable) {
		user_data_count = 0;
		if (enable) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					// �˻� �������� ����...
					Search_state = true;
					// ��ĵ���� �ƴ�. ����.
					mScanning = false;
					// ��ĵ�� �����.
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
				}
			}, SCAN_PERIOD);
			// ��ĵ������ ����
			mScanning = true;
			// �ҷ����� ����Ϳ��� ��ĵ�� ������...
			mBluetoothAdapter.startLeScan(mLeScanCallback);

		} else {
			// �˻� �������� ����
			Search_state = true;
			// ��ĵ���� ����
			mScanning = false;
			// ��ĵ�� ������
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}

	}
		
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int rssi,
				final byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					// Juno
					if (device.getName().contains("BoT")) {
						Log.d("t", "Dbg__DeviceScan st = 02 " + rssi);
						mBLE_Device.rssi[user_data_count] = rssi;

						Log.d("t", "Dbg__DeviceScan st = 03 ");

						parseAdvertisementPacket(scanRecord);

						// ///////////////////////////////////////////////
						Log.d("tt", "Dbg__ DeviceScan =1 ");

						byte[] array_byte = mBLE_Device.user_alldata[user_data_count].getBytes();

						String user_TX_level = "";
						switch (array_byte[5]) {
						case (byte) 0x08:
							user_TX_level = "8";
							break;
						case (byte) 0x06:
							user_TX_level = "6";
							break;
						case (byte) 0x02:
							user_TX_level = "2";
							break;
						case (byte) 0xef:
							switch (array_byte[7]) {
							case (byte) 0xbe:
								user_TX_level = "-2";
								break;
							case (byte) 0xba:
								user_TX_level = "-6";
								break;
							case (byte) 0xb6:
								user_TX_level = "-10";
								break;
							case (byte) 0xb2:
								user_TX_level = "-14";
								break;
							case (byte) 0xae:
								user_TX_level = "-18";
								break;

							}

							break;
						}

						Log.d("tt", "Dbg__ DeviceScan =2 ");
						byte[] inputBytes1 = mBLE_Device.user_alldata[user_data_count]
								.getBytes(); // ????? ?????? ??? ???
						int start_0 = 0;
						boolean start_bool = false;
						for (byte b : inputBytes1) {
							if (inputBytes1[start_0] == (byte) 0x00) {

								if (start_bool) {
									break;
								}
								start_bool = true;

							}
							start_0++;
						}

						Log.d("tt", "Dbg__ DeviceScan =3 ");
						mBLE_Device.user_data[user_data_count] = new String(Arrays.copyOfRange(array_byte, start_0 + 1,	start_0 + 12)); // ????? ??????
						mBLE_Device.user_TX_level[user_data_count] = user_TX_level;

						Log.d("tt", "Dbg__ DeviceScan =4 ");

						distanceAcuracy = calculateAccuracy(Integer.parseInt(user_TX_level),mBLE_Device.rssi[user_data_count]);
						Log.d("distanceInfo",""+ distanceAcuracy);
						
						if (mBLE_Device.rssi[user_data_count] + 99 > 30	&& sayingLocation == false) {
							sayingLocation = true;
							char color = mBLE_Device.user_data[user_data_count].charAt(0);
							Log.d("data", "data = " + color);
							sayLocation(color);

						}

						user_data_count++;

						// /////////////////////////////////////////////////////

					}
				}
				
				double calculateAccuracy(int txPower, double rssi) {
					if (rssi == 0) {
						return -1.0; // if we cannot determine accuracy, return -1.
					}

					double ratio = rssi*1.0/txPower;
					if (ratio < 1.0) {
						return Math.pow(ratio,10);
					}
					else {
						double accuracy =  (0.89976)*Math.pow(ratio,7.7095) + 0.111;						
						return accuracy;
					}
				}

				void parseAdvertisementPacket(final byte[] scanRecord) { 

					byte[] advertisedData = Arrays.copyOfRange(scanRecord, 0,scanRecord.length);
					final StringBuilder stringBuilder = new StringBuilder(advertisedData.length);
					Log.d("t", "Dbg__DeviceScan st = 1 ");

					for (byte byteChar : advertisedData) {
						stringBuilder.append((char) byteChar);
					}

					byte[] inputBytes = stringBuilder.toString().getBytes();
					String hexString = "";

					for (byte b : inputBytes) {
						hexString += Integer.toString((b & 0xF0) >> 4, 16);
						hexString += Integer.toString(b & 0x0F, 16);
					}
					Log.w("t", String.format("Dbg__hex = ") + hexString);

					mBLE_Device.user_alldata[user_data_count] = stringBuilder.toString();

				}

			});

		}
	};
	
	private final Handler mscaneHandler = new Handler() { //
    	@Override
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
    		case 0:
    			user_data_count = 0;

    			mBLE_Device.setclear();
    			
                // ��ĳ�������� ����
    			 mScanning = true;

                // ��ĵ ����...
    	         mBluetoothAdapter.startLeScan(mLeScanCallback);

                // ����Ʈ ������Ʈ...
    	         

                // 8���Ŀ� ���� �޼����� ����.
    	         if(mScanning_run){
                     // �ڵ鷯���� �޼����� 8���Ŀ� �޼����� ����.
                     sayingLocation = false;
    	        	 mscaneHandler.sendEmptyMessageDelayed(1, 5000);
                 }

    		break;

    		case 1:
                // ��ĵ ����� ����
    		    mScanning = false;

                // ��ĵ�� ������.
                mBluetoothAdapter.stopLeScan(mLeScanCallback);

                // 2���Ŀ� �ٽ� ��ĵ �޼����� ����...
               if(mScanning_run) {
                   mscaneHandler.sendEmptyMessageDelayed(0, 200);
               }
              
    		break;

    		case 2:

                // ��ĵ�� ������.
    		    mScanning = false;

                // ��ĵ�� ���߰�...
                mBluetoothAdapter.stopLeScan(mLeScanCallback);

                // ��� �޼����� ������.
                mscaneHandler.removeMessages(0);
                
    		break;
    		}
    		
    	}
    };

	static class BLE_Device {
		String[] user_alldata = new String[500];
		String[] user_data = new String[500];
		String[] user_TX_level = new String[500];
		int[] rssi = new int[500];

		public void setclear() {
			for (int i = 0; i < 500; i++) {
				user_alldata[i] = "";
				user_data[i] = "";
				user_TX_level[i] = "";
				rssi[i] = 0;
			}

		}
	}

	@SuppressWarnings("deprecation")
	private void sayLocation(char color) {

		String location = null;
		Log.d("color ", color + "a");
		if (color == '1')
			location = "�Ķ��� �Դϴ�.";
		else if (color == '3') {
			location = "������ �Դϴ�. ���ݸ� ��ٷ� �ּ���.";
			vibrator.vibrate(pattern, -1);
		}

		else {
			int num = color - '4' + 3;
			int time = 10 - (num / 5);
			location = "�Ķ����� " + time + " �� ���ҽ��ϴ�. ���� ��ȣ�� ��ٷ��ּ���";
		}

		mTts.speak(location, TextToSpeech.QUEUE_FLUSH, null);

	}
	
	public void NotificationBuilder(){
        /**
         * FLAG_CANCEL_CURRENT : ������ ������ PendingIntent �� ����ϰ� ���Ӱ� �����.
         * FLAG_NO_CREATE : ������ PendingIntent�� ��ȯ�Ѵ�. ���� �����ϴ�.
         * FLAG_ONE_SHOT : �� flag �� ������ PendingIntent �� ��ȸ���̴�.
         * FLAG_UPDATE_CURRENT : �̹� ������ PendingIntent �� �����ϸ� �ش� Intent �� ������ �����Ѵ�.
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
		//Intent popupIntent = new Intent(context, Popup.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		
		//context.startActivity(popupIntent);
	}
	
	

}
