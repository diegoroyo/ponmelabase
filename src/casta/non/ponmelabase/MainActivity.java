package casta.non.ponmelabase;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button toggleButton;
	
	boolean isToggled;
	
	Handler handler;
	Runnable runnableBase;
	
	SoundPool soundPool;
	int[] soundIds = new int[5];
	int[] streamIds = new int[5];
	
	Button[] soundButtons = new Button[3];
	
	SeekBar seekBar;
	TextView textViewBpm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		isToggled = false;
		
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

		// PONME LA BASE
		
		toggleButton = (Button) findViewById(R.id.button_toggle);

		handler = new Handler();
		runnableBase = new Runnable() {
			@Override
			public void run() {
				streamIds[1] = soundPool.play(soundIds[1], 1, 1, 1, -1, 0.75f + (float) seekBar.getProgress() / 100);
			}
		};

		toggleButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (isToggled) {
						toggleButton.setBackgroundResource(R.drawable.button_active_pressed);
					} else {
						toggleButton.setBackgroundResource(R.drawable.button_unactive_pressed);
					}
					break;
				case MotionEvent.ACTION_UP:
					if (isToggled) {
						isToggled = false;
						stopSound();
						toggleButton.setBackgroundResource(R.drawable.button_unactive_released);
					} else {
						isToggled = true;
						startSound();
						toggleButton.setBackgroundResource(R.drawable.button_active_released);
					}
					break;
				}
				return true;
			}
		});
		
		// SONIDOS VARIOS
		
		soundButtons[0] = (Button) findViewById(R.id.button_sound1);
		soundButtons[1] = (Button) findViewById(R.id.button_sound2);
		soundButtons[2] = (Button) findViewById(R.id.button_sound3);
		
		View.OnTouchListener soundButtonPress = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					soundPool.stop(streamIds[2]);
					soundPool.stop(streamIds[3]);
					soundPool.stop(streamIds[4]);
					switch(v.getId()) {
					case R.id.button_sound1:
						v.setBackgroundResource(R.drawable.button_yeah_pressed);
						streamIds[2] = soundPool.play(soundIds[2], 1, 1, 1, 0, 0.75f + (float) seekBar.getProgress() / 100);
						break;
					case R.id.button_sound2:
						v.setBackgroundResource(R.drawable.button_aja_pressed);
						streamIds[3] = soundPool.play(soundIds[3], 1, 1, 1, 0, 0.75f + (float) seekBar.getProgress() / 100);
						break;
					case R.id.button_sound3:
						v.setBackgroundResource(R.drawable.button_uuh_pressed);
						streamIds[4] = soundPool.play(soundIds[4], 1, 1, 1, 0, 0.75f + (float) seekBar.getProgress() / 100);
						break;
					}
					break;
				case MotionEvent.ACTION_UP:
					switch(v.getId()) {
					case R.id.button_sound1:
						v.setBackgroundResource(R.drawable.button_yeah_released);
						break;
					case R.id.button_sound2:
						v.setBackgroundResource(R.drawable.button_aja_released);
						break;
					case R.id.button_sound3:
						v.setBackgroundResource(R.drawable.button_uuh_released);
						break;
					}
					break;
				}
				return true;
			}
		};
		
		soundButtons[0].setOnTouchListener(soundButtonPress);
		soundButtons[1].setOnTouchListener(soundButtonPress);
		soundButtons[2].setOnTouchListener(soundButtonPress);
		
		// SEEK BAR BPM
		
		seekBar = (SeekBar) findViewById(R.id.seekBar_bpm);
		textViewBpm = (TextView) findViewById(R.id.text_bpm);
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {       
		    @Override       
		    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		    	soundPool.setRate(streamIds[1], 0.75f + (float) progress / 100);
		    	textViewBpm.setText("Ritmo de la base: " + (60 + 80 * progress / 100) + " bpm");
		    }

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});      
	}
	
	public void onResume() {
		super.onResume();
		soundIds[0] = soundPool.load(this, R.raw.ponmelabase, 1);
		soundIds[1] = soundPool.load(this, R.raw.pupuchipuchi, 1);
		
		streamIds[1] = soundPool.play(soundIds[1], 1, 1, 1, -1, 1);
		soundPool.stop(streamIds[1]);
		
		// TODO cambiar los sonidos
		soundIds[2] = soundPool.load(this, R.raw.yeah, 1);
		soundIds[3] = soundPool.load(this, R.raw.aja, 1);
		soundIds[4] = soundPool.load(this, R.raw.uuh, 1);
	}

	public void onPause() {
		super.onPause();
		soundPool.release();
	}

	// Reproduce la base
	private void startSound() {
		// Castañón, ponme la base
		streamIds[0] = soundPool.play(soundIds[0], 1, 1, 1, 0, 1);
	    
		// PU PUCHI PU-CHI
		handler.postDelayed(runnableBase, 3000);
	}

	// Para la base
	private void stopSound() {
		soundPool.stop(streamIds[0]);
		soundPool.stop(streamIds[1]);
		
		handler.removeCallbacks(runnableBase);
	}

}
