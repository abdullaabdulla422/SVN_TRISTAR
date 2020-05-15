package com.tristar.voicerecorder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaSyncEvent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tristar.db.DataBaseHelper;
import com.tristar.main.FinalStatus;
import com.tristar.main.ProcessOrderDetail;
import com.tristar.main.R;
import com.tristar.main.RecordDiligence;
import com.tristar.object.ProcessAddressForServer;
import com.tristar.utils.CustomAlertDialog;
import com.tristar.utils.SessionData;

@SuppressWarnings("ALL")
public class VoiceRecorder extends Activity implements View.OnClickListener {
	private static final String LOG_TAG = "VoiceRecorder";
	public static String mFileName = null;
	public static String flagForFinishActivity = "";

	private MediaRecorder mRecorder = null;

		
	private MediaPlayer mPlayer = null;
	DataBaseHelper database;
	ImageButton btnMic, btnRecorder, btnPause, btnStop;
	ImageView btnBacktoFinalStatus;
	TextView textCount, textBacktoFinalStatus, txt_worker;
	Button cancel, buttonCompleteServe, buttonEnterAttempt,buttonDeleteAttempt;
	ProcessAddressForServer processOrderToDisplayInDetailView;
	int processOrderID;
	String Flag = null;
	Bundle extra;
	private FileInputStream fis;



	private void startPlaying() {
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			
			mPlayer.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}
	}

	private void stopPlaying() {
		mPlayer.release();
		mPlayer = null;
	}

	private void startRecording() {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
			mRecorder.start();
		} catch (IOException e) {
			Log.e(LOG_TAG, "prepare() failed");
		}


	}

	private void stopRecording() {
		if(mRecorder != null) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}

	}

	public VoiceRecorder() {
		
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	
		setContentView(R.layout.activity_voice_recorder);
		textCount = (TextView) findViewById(R.id.textViewcountdown);
		textBacktoFinalStatus = (TextView) findViewById(R.id.textViewbacktofinalstatus);
		
		extra = getIntent().getExtras();
		if(extra != null)
			processOrderID = extra.getInt("processOrderID");
		mFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		mFileName = mFileName + "/WinServe_Data/Audio";
		new File(mFileName).mkdirs();
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmm");
		mFileName += "/win_"+format.format(date)+".mp3";
		database = DataBaseHelper.getInstance();
		
		txt_worker = (TextView) findViewById(R.id.txt_voice_worker);
		btnMic = (ImageButton) findViewById(R.id.imageButtonMic);
		btnPause = (ImageButton) findViewById(R.id.imageButtonPause);
		btnRecorder = (ImageButton) findViewById(R.id.imageButtonRecord);
		btnStop = (ImageButton) findViewById(R.id.imageButtonStop);
		btnBacktoFinalStatus = (ImageView) findViewById(R.id.imageButtonbacktofinalstatus);
		cancel = (Button) findViewById(R.id.buttonRecordCancel);
		buttonCompleteServe = (Button) findViewById(R.id.buttonCompleteServe);
		buttonDeleteAttempt = (Button) findViewById(R.id.buttonDeleteAttempt);
		buttonEnterAttempt = (Button) findViewById(R.id.buttonEnterAttempt);
		
		try {
			processOrderToDisplayInDetailView = database.getProcessOrderValuesFromDBToDisplayInDetailView(processOrderID);
			txt_worker.setText(processOrderToDisplayInDetailView.getWorkorder());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		textBacktoFinalStatus.setOnClickListener(this);
		btnMic.setOnClickListener(this);
		btnPause.setOnClickListener(this);
		btnRecorder.setOnClickListener(this);
		
		btnStop.setOnClickListener(this);
		btnBacktoFinalStatus.setOnClickListener(this);
		btnPause.setClickable(false);
		btnStop.setClickable(false);
		
		buttonCompleteServe.setClickable(false);
		buttonDeleteAttempt.setClickable(false);
		btnPause.setImageResource(R.drawable.play);
		cancel.setOnClickListener(this);
		buttonEnterAttempt.setOnClickListener(this);
		buttonCompleteServe.setOnClickListener(this);
		buttonDeleteAttempt.setOnClickListener(this);
		
		btnPause.setClickable(false);
		btnStop.setClickable(false);
		btnPause.setAlpha(.5f);
		btnStop.setAlpha(.5f);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mRecorder != null) {
			mRecorder.release();
			mRecorder = null;
		}

		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageButtonRecord:
			
			{
				
				Flag = "recording";
				btnRecorder.setClickable(false);
				btnRecorder.setAlpha(.5f);
				new CountDownTimer(4000, 1000) {
	
					@SuppressLint("SetTextI18n")
					@Override
					public void onTick(long time) {
						time /= 1000;
						if(time == 1) {
							textCount.setText("Start");
						} else {
							textCount.setText(String.valueOf(time));
						}
					}
	
					@SuppressLint("SetTextI18n")
					@Override
					public void onFinish() {
						textCount.setText("Recording...");
						startRecording();
						btnBacktoFinalStatus.setVisibility(View.INVISIBLE);
						textBacktoFinalStatus.setVisibility(View.INVISIBLE);
						btnPause.setClickable(false);
						btnStop.setClickable(true);
						btnStop.setAlpha(1f);
						
					}
				}.start();
			}
			break;
		case R.id.imageButtonPause:
			Flag = "playing";
			btnBacktoFinalStatus.setVisibility(View.INVISIBLE);
			textBacktoFinalStatus.setVisibility(View.INVISIBLE);
			btnPause.setImageResource(R.drawable.pause);
			btnRecorder.setClickable(false);
			btnPause.setClickable(false);
			btnRecorder.setAlpha(.5f);
			
			btnStop.setClickable(true);
			btnStop.setAlpha(1f);
			
			startPlaying();
			break;
		case R.id.imageButtonStop:
			btnRecorder.setClickable(true);
			btnRecorder.setAlpha(1f);
			btnPause.setAlpha(1f);
			btnPause.setClickable(true);
			buttonCompleteServe.setClickable(true);
			buttonDeleteAttempt.setClickable(true);
			btnBacktoFinalStatus.setVisibility(View.VISIBLE);
			textBacktoFinalStatus.setVisibility(View.VISIBLE);
			btnRecorder.setImageResource(R.drawable.record);
			if (Flag.contentEquals("recording")) {
				stopRecording();
				textCount.setText("");
			} else {
				stopPlaying();
			}
			btnStop.setClickable(false);
			btnStop.setAlpha(.5f);
			btnPause.setImageResource(R.drawable.play);
			break;
		case R.id.imageButtonbacktofinalstatus:
			flagForFinishActivity = "Canceled";
			finish();
			Intent intent = new Intent(VoiceRecorder.this, ProcessOrderDetail.class);
			intent.putExtra("processOrderID", processOrderID);
			startActivity(intent);
			break;
		case R.id.textViewbacktofinalstatus:
			flagForFinishActivity = "Canceled";
			finish();
			Intent processIntent = new Intent(VoiceRecorder.this, ProcessOrderDetail.class);
			processIntent.putExtra("processOrderID", processOrderID);
			startActivity(processIntent);
			break;
		case R.id.buttonRecordCancel:
			flagForFinishActivity = "Canceled";
			finish();
			Intent recordIntent = new Intent(VoiceRecorder.this, ProcessOrderDetail.class);
			recordIntent.putExtra("processOrderID", processOrderID);
			startActivity(recordIntent);
			break;
		
		case R.id.buttonEnterAttempt:
			flagForFinishActivity = "Completed";
			ByteArrayOutputStream baoss = new ByteArrayOutputStream();
			try {
	    		fis = new FileInputStream(new File(mFileName));
			
		    byte[] buf = new byte[1024];
		         int n;
			
				while (-1 != (n = fis.read(buf)))
		             baoss.write(buf, 0, n);
				byte[] audiobytes = baoss.toByteArray();
				
				SessionData.getInstance().setAudioData(audiobytes);
				Log.d("nterAttempt_Audio",""+SessionData.getInstance().getAudioData());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		         
			

			Intent record = new Intent(VoiceRecorder.this, RecordDiligence.class);
			record.putExtra("processOrderID", processOrderID);
			
			new CustomAlertDialog(this, getResources().getString(R.string.alert_enter_attempt), record).show();
			
			break;
			case R.id.buttonDeleteAttempt:
				flagForFinishActivity = "Deleted";
				SessionData.getInstance().setAudioData(null);
				Log.d("Delete_Audio",""+SessionData.getInstance().getAudioData());


				File file = new File(mFileName);
				boolean deleted = file.delete();

				finish();
				Intent intents = new Intent(VoiceRecorder.this, ProcessOrderDetail.class);
				intents.putExtra("processOrderID", processOrderID);
				startActivity(intents);

				break;
		case R.id.buttonCompleteServe:
			flagForFinishActivity = "Completed";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
			fis = new FileInputStream(new File(mFileName));
			
		    byte[] buf = new byte[1024];
		         int n;
			
				while (-1 != (n = fis.read(buf)))
		             baos.write(buf, 0, n);
				byte[] audiobytes = baos.toByteArray();
				
				SessionData.getInstance().setAudioData(audiobytes);
				Log.d("Completed_Audio",""+SessionData.getInstance().getAudioData());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		         
			finish();
			Intent complete = new Intent(VoiceRecorder.this, FinalStatus.class);
			complete.putExtra("processOrderID", processOrderID);
			startActivity(complete);
			break;
		default:
			break;
		}
	}
	
	public void base64conversion() {
		
	}

	@SuppressLint("NewApi")
	public void recorder() {
		AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC,
				44100, AudioFormat.CHANNEL_IN_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, 100);
		MediaSyncEvent syncEvent = MediaSyncEvent.createEvent(MediaSyncEvent.SYNC_EVENT_PRESENTATION_COMPLETE);
		record.startRecording(syncEvent);
	}

	@Override
	public void onBackPressed() {
		finish();
		Intent processIntent = new Intent(VoiceRecorder.this, ProcessOrderDetail.class);
		processIntent.putExtra("processOrderID", processOrderID);
		startActivity(processIntent);
	}
}
