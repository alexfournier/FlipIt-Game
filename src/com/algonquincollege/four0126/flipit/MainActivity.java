package com.algonquincollege.four0126.flipit;

import java.util.Observable;
import java.util.Observer;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import model.FlipItModel;

public class MainActivity extends Activity implements Observer, OnClickListener {

	private FlipItModel model;
	private Button resetButton;
	private ImageButton imageButton;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		model = new FlipItModel(4, 4);
		model.addObserver(this);

		// register reset button clicks
		resetButton = (Button) findViewById(R.id.resetButton);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		resetButton.setOnClickListener(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        // GET from the view which menu item was selected
	        case R.id.action_reset:
	            model.reset();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	// HANDLE IMAGE BUTTONS
	public void handleImageButton(View v) {
		String name = getResources().getResourceEntryName(v.getId());

		// GET which view object was clicked
		int r = name.charAt(1) - '0';
		int c = name.charAt(3) - '0';

		// SET the model to its new state with the information from the view
		model.flipValueAt(r, c);
		System.out.println("Row: " + r + " Column: " + c);
	}

	// RESET BUTTON
	public void handleResetButton() {
		model.reset();
		progressBar.setProgress(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Event handling for the calculate and reset buttons.
	 * 
	 * @param view
	 *            v
	 */
	@Override
	public void onClick(View v) {
		if (v == resetButton) {
			this.handleResetButton();
		} else {
			this.handleImageButton(v);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if (model.isWon() == true) {
			Toast.makeText(MainActivity.this, "You Won!", Toast.LENGTH_LONG)
					.show();
		}
		UpdateBoard();
	}

	private void UpdateBoard() {
		// LOOP AND CHANGE IMAGES
		progressBar.setProgress(model.getProgress());

		for (int i = 0; i < model.getRows(); i++) {
			for (int j = 0; j < model.getColumns(); j++) {
				System.out.println("Flipping cell at row " + i + ", column "
						+ j);

				String StringId = "r" + i + "c" + j;

				int imageButtonId = getResources().getIdentifier(StringId,
						"id", getPackageName());

				imageButton = (ImageButton) findViewById(imageButtonId);

				if (model.getValueAt(i, j) == true) {
					imageButton.setImageResource(R.drawable.happy);
				} else {
					imageButton.setImageResource(R.drawable.unhappy);
				}
			}
		}

	}
}