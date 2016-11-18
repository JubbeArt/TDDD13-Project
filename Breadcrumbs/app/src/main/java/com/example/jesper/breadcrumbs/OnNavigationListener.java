package com.example.jesper.breadcrumbs;

/**
 * Created by jesper on 11/17/16.
 */

// Listener for when a breadcrumb is pressed
public interface OnNavigationListener {

	// Triggers when a breadcrumb is pressed, passes information about the pressed breadcrumb and
	// the state of the trail
	void onNavigate(String breadcrumbName, int hierarchyLevel, String[] trail);

}
