package com.example.jesper.passwordstrengthmeter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

/**
 * Created by jesper on 10/31/16.
 */

public class PasswordStrengthMeter extends LinearLayout {

	protected Context context;

	// Childviews for the password strength meter
	protected EditText field;
	protected ProgressBar progressBar;
	protected CheckBox showPassword;
	protected TextView showPasswordText;
	protected TextView output;

	// The current algorithm to check the password
	protected PasswordStrengthAlgorithm algorithm;

	// ID for the current strength, e.g. ID for TOO_SHORT is 0
	protected int currentID;

	// The IDs for the default strengths
	public static final int TOO_SHORT = 0;
	public static final int WEAK = 1;
	public static final int FAIR = 2;
	public static final int GOOD = 3;
	public static final int STRONG = 4;

	// Info about the default strengths
	protected List<String> textRepresentations = Arrays.asList("Too short", "Weak", "Fair", "Good", "Strong");
	protected List<Integer> strengthColors = Arrays.asList(Color.RED, Color.GRAY, Color.YELLOW, Color.BLUE, Color.GREEN);
	protected List<Integer> strengths = Arrays.asList(0, 1,	2, 3, 4);

	public PasswordStrengthMeter(Context context, AttributeSet attr) {
		super(context, attr);
		this.context = context;

		setOrientation(VERTICAL);
		currentID = 0;

		// Create a layout from the xml-file
		LinearLayout main = (LinearLayout) inflate(context, R.layout.password_strength_meter, null);

		// Get all the views from the inflated layout and save them for later
		field = (EditText) main.findViewById(R.id.passwordField);
		progressBar = (ProgressBar) main.findViewById(R.id.progressBar);
		showPassword = (CheckBox) main.findViewById(R.id.showPassword);
		showPasswordText = (TextView) main.findViewById(R.id.showPasswordText);
		output = (TextView) main.findViewById(R.id.strengthOutput);

		// Changes the edittext to show/hide the password
		showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int inputType = isChecked ? TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : TYPE_TEXT_VARIATION_PASSWORD;

				field.setInputType(TYPE_CLASS_TEXT | inputType);
				field.setSelection(field.getText().length());
			}
		});

		// Adds a textlistner for the edittext that runs the current algorithm
		field.addTextChangedListener(new TextWatcher() {
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void afterTextChanged(Editable editable) {
				currentID = algorithm.checkPassword(editable.toString());

				output.setText(textRepresentations.get(currentID));
				progressBar.setProgress(strengths.get(currentID));
				progressBar.setProgressTintList(ColorStateList.valueOf(strengthColors.get(currentID)));
			}
		});

		// Sets a default algorithm for the password
		algorithm = new PasswordStrengthAlgorithm() {
			@Override
			public int checkPassword(String password) {
				if(password.length() < 8)
					return TOO_SHORT;

				int strengthPoints = 0;

				if(!password.toLowerCase().equals(password) && password.matches(".*[a-zA-Z].*"))
					strengthPoints++;
				if(password.length() >= 12)
					strengthPoints++;
				if(password.matches(".*\\d+.*"))
					strengthPoints++;
				if(password.matches(".*[a-zA-Z].*"))
					strengthPoints++;
				if(password.matches(".*[^A-Za-z0-9].*"))
					strengthPoints++;

				if(strengthPoints <= 2)
					return WEAK;
				else if(strengthPoints == 3)
					return FAIR;
				else if(strengthPoints == 4)
					return GOOD;
				else
					return STRONG;
			}
		};

		// Adds the inflated linearlayout to the current layout
		addView(main);
	}

	// Adds a custom strength with a text to display, a strength (default range is 0-4),
	// and a color for the progressbar
	public int addPasswordStrength(String textRepresentation, int strength, int color) {
		textRepresentations.add(textRepresentation);
		strengths.add(strength);
		strengthColors.add(color);

		return textRepresentations.size() - 1;
	}

	// Increase or decrease the max strength (default is 4)
	public void setMaxStrength(int maxStrength) {
		progressBar.setMax(maxStrength);
	}

	// Returns the current strength of the password
	public int getCurrentStrength() {
		return strengths.get(currentID);
	}

	// Changes the text show how strong the password is
	public void setTextRepresentations(List<String> textRepresentations) {
		this.textRepresentations = textRepresentations;
	}

	// Changes set text for the checkbox to show password
	public void setShowPasswordText(String text) {
		showPasswordText.setText(text);
	}

	// Changes the algorithm for checking the password
	public void setAlgorithm(PasswordStrengthAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

}
