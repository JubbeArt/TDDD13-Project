package com.example.jesper.breadcrumbs;

/**
 * Created by jesper on 11/17/16.
 */

public interface OnNavigationListener {
	void onNavigate(String breadcrumbName, int hierarchyLevel, String[] fullPath);

}
