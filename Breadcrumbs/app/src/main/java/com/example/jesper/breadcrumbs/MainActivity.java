package com.example.jesper.breadcrumbs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

	private LinearLayout buttonHolder;
	private Breadcrumbs breadcrumbs;
	private List<Map<String, String[]>> allLevels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		breadcrumbs = (Breadcrumbs) findViewById(R.id.breadcrumbs);
		buttonHolder = (LinearLayout) findViewById(R.id.navButtons);

		// Load in data about the world
		allLevels = createData();
		String root = "Earth";
		// Set the first breadcrumb to be the root
		breadcrumbs.addBreadcrumb(root);
		// Create buttons that navigate forward
		updateButtons(root, 0);

		// Set a listener that updates the button when a breadcrumb is pressed
		breadcrumbs.setOnNavigationListener(new OnNavigationListener() {
			@Override
			public void onNavigate(String breadcrumbName, int hierarchyLevel, String[] fullPath) {
				updateButtons(breadcrumbName, hierarchyLevel);
			}
		});
	}

	// Updates the buttons that navigate forward in the hierarchy
	public void updateButtons(String key, final int level) {
		buttonHolder.removeAllViews(); // Remove old buttons

		if(level >= allLevels.size()) // Cannot have any children
			return;

		if(!allLevels.get(level).containsKey(key)) // Others have children on this level, but this one dont
			return;

		// Get all the children for the current breadcrumb
		String[] children = allLevels.get(level).get(key);

		// Add buttons for the children
		for(final String child : children) {
			Button button = new Button(this);
			button.setText(child);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					// Has clicked a button to go forward
					breadcrumbs.addBreadcrumb(child); // Add the breadcrumb
					updateButtons(child, level + 1); // Get the children for this child (if they exists)
				}
			});

			buttonHolder.addView(button);
		}

	}

	public List<Map<String, String[]>> createData() {

		Map<String, String[]> firstLevel = new HashMap<>();
		firstLevel.put("Earth", new String[]{"Europe", "North America"});

		Map<String, String[]> secondLevel = new HashMap<>();
		secondLevel.put("Europe", new String[]{"Sweden", "Norway", "Denmark"});
		secondLevel.put("North America", new String[]{"USA", "Mexico", "Canada"});

		Map<String, String[]> thirdLevel = new HashMap<>();
		thirdLevel.put("Sweden", new String[]{"Stockholm", "Linköping"});
		thirdLevel.put("Norway", new String[]{"Oslo", "Bergen"});
		thirdLevel.put("Denmark", new String[]{"Copenhagen"});
		thirdLevel.put("USA", new String[]{"New York"});
		thirdLevel.put("Canada", new String[]{"Toronto", "Ottawa"});

		List<Map<String, String[]>> allLevels = new ArrayList<>();
		allLevels.add(firstLevel);
		allLevels.add(secondLevel);
		allLevels.add(thirdLevel);

		return allLevels;
	}
}
