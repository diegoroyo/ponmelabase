package casta.non.ponmelabase;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends Activity {

	Button toggleButton;
	boolean isToggled, readyToToggle;
	SeekBar bpmBar;
	MediaPlayer mpBase, mpPuchi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// toggled
		isToggled = false;
		readyToToggle = true;
		toggleButton = (Button) findViewById(R.id.button_toggle);

		// Añadir el boton y su funcionamiento
		toggleButton.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (readyToToggle) {
						isToggled = !isToggled;
						if (isToggled) {
							toggleButton.setBackgroundResource(R.drawable.button_unactive_pressed);
						} else {
							toggleButton.setBackgroundResource(R.drawable.button_active_pressed);
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					if (isToggled) {
						startSound();
						toggleButton.setBackgroundResource(R.drawable.button_active_released);
					} else {
						stopSound();
						toggleButton.setBackgroundResource(R.drawable.button_unactive_released);
					}
					break;
				}
				return true;
			}
		});
	}

	// Reproduce la base
	public void startSound() {
		// MediaPlayer para los dos sonidos
		mpBase = MediaPlayer.create(getApplicationContext(), R.raw.ponmelabase);
		mpPuchi = MediaPlayer.create(getApplicationContext(), R.raw.pupuchipuchi);
		mpBase.start();
		
		readyToToggle = false;
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mpPuchi.start();
				mpPuchi.setLooping(true);
				readyToToggle = true;
			}
		}, 3250);
	}

	// Para la base
	public void stopSound() {
		mpBase.stop();
		mpPuchi.stop();
	}

}
