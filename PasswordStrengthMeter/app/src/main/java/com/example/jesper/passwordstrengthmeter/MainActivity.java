package com.example.jesper.passwordstrengthmeter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load both password strength meters
		setContentView(R.layout.activity_main);

		// Find the second strength meter and make some custom changes to it
		PasswordStrengthMeter swedish = (PasswordStrengthMeter) findViewById(R.id.swedishMeter);
		List<String> swedishStrengths = Arrays.asList("För kort", "Sämst", "Tokfin");
		String showPassword = "Visa lösenord";

		swedish.setTextRepresentations(swedishStrengths); // Change to swedish texts
		swedish.setShowPasswordText(showPassword);
		swedish.setMaxStrength(2); // Go from 4 (default) levels of strength to 2

		swedish.setShowPassword(false);

		// Change the algorithm for the view
		swedish.setAlgorithm(new PasswordStrengthAlgorithm() {
			@Override
			public int checkPassword(String password) {
				if(password.length() < 3)
					return PasswordStrengthMeter.TOO_SHORT;
				if(password.equals("Buttdick"))
					return PasswordStrengthMeter.FAIR;

				return PasswordStrengthMeter.WEAK;
			}
		});



	}
}
