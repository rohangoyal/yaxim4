package com.hihello.app.activity;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hihello.app.R;
import com.hihello.app.common.PathUtils;

public class AudioRecorderActivity extends Activity {
	enum RecordingState {
		record, stop, confirm
	}

	public static final String AUDIO_OUTPUT = "AUDIO_OUTPUT";
	public static final String AUDIO_DURATION = "AUDIO_DURATION";

	void setUpLayout(RecordingState state) {
		findViewById(R.id.layout_recorder).setVisibility(View.GONE);
		findViewById(R.id.layout_stop).setVisibility(View.GONE);
		findViewById(R.id.layout_confirm).setVisibility(View.GONE);
		switch (state) {
		case confirm:
			findViewById(R.id.layout_confirm).setVisibility(View.VISIBLE);
			break;
		case record:
			findViewById(R.id.layout_recorder).setVisibility(View.VISIBLE);
			break;
		case stop:
			findViewById(R.id.layout_stop).setVisibility(View.VISIBLE);
			break;
		}
	}

	private OnClickListener recorder_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setUpLayout(RecordingState.stop);
			startRecording();

		}
	};
	private OnClickListener stop_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setUpLayout(RecordingState.confirm);
			stopRecording();

		}
	};
	private OnClickListener close_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			closeRecording();
			finish();

		}
	};
	private Timer timer;
	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					duration++;
					txt_time.setText(getDurationString());
				}
			});
		}
	};

	private String getDurationString() {
		int _duration = duration / 10;
		if (_duration > 0) {
			int hour = _duration / 3600;
			int minute = _duration / 60;
			int second = _duration % 60;
			DecimalFormat decim = new DecimalFormat("00");
			return decim.format(hour) + ":" + decim.format(minute) + ":"
					+ decim.format(second);
		}
		return "00:00:00";
	}

	int duration = 0;

	void startTimer() {
		timer = new Timer();
		duration = 0;
		timer.schedule(task, 0, 100);
	}

	private OnClickListener save_click = new OnClickListener() {

		@Override
		public void onClick(View v) {
			saveRecording();

		}
	};
	private String outputFile;
	private MediaRecorder myAudioRecorder;
	private TextView txt_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.layout_voice_record);
		super.onCreate(savedInstanceState);
		initializeControlls();
		setUpLayout(RecordingState.record);
		initRecorder();

	}

	private void initRecorder() {
		File file = new File(PathUtils.getUploadAudioDir());
		file.mkdirs();
		outputFile = PathUtils.getUploadAudioDir()
				+ UUID.randomUUID().toString() + ".3gp";
		myAudioRecorder = new MediaRecorder();
		myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		myAudioRecorder.setOutputFile(outputFile);
	}

	protected void saveRecording() {
		Intent intent = new Intent();
		Bundle bnd = new Bundle();
		bnd.putString(AUDIO_OUTPUT, outputFile);
		bnd.putString(AUDIO_DURATION, getDurationString());
		intent.putExtras(bnd);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		stopRecording();
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		stopRecording();
		closeRecording();
		super.onBackPressed();
	}

	protected void closeRecording() {
		try {
			File file = new File(outputFile);
			if (file.exists())
				file.delete();
			setResult(RESULT_CANCELED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void stopRecording() {
		try {
			if (timer != null) {
				timer.cancel();
			}
			if (myAudioRecorder != null) {
				myAudioRecorder.stop();
				myAudioRecorder.release();
				myAudioRecorder = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void startRecording() {
		try {
			myAudioRecorder.prepare();
			myAudioRecorder.start();
			startTimer();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initializeControlls() {
		findViewById(R.id.btn_recorder).setOnClickListener(recorder_click);
		findViewById(R.id.btn_stop).setOnClickListener(stop_click);
		findViewById(R.id.btn_close).setOnClickListener(close_click);
		findViewById(R.id.btn_save).setOnClickListener(save_click);
		txt_time = (TextView) findViewById(R.id.txt_time);
	}
}