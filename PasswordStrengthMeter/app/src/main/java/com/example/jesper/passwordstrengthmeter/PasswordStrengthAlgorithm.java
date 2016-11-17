package com.example.jesper.passwordstrengthmeter;

/**
 * Created by jesper on 10/31/16.
 */

// Interface for an algorithm for the password strength meter view
public interface PasswordStrengthAlgorithm {

	// Takes in the current password and returns the ID of the strength according to the current algorithm
	int checkPassword(String password);

}
