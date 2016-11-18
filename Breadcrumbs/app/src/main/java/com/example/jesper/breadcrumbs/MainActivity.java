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

		allLevels = createData();
		String root = "Earth";
		breadcrumbs.addBreadcrumb(root);
		updateButtons(root, 0);

		breadcrumbs.setOnNavigationListener(new OnNavigationListener() {
			@Override
			public void onNavigate(String breadcrumbName, int hierarchyLevel, String[] fullPath) {
				updateButtons(breadcrumbName, hierarchyLevel);
			}
		});
	}

	public void updateButtons(String key, final int level) {
		buttonHolder.removeAllViews();

		if(level >= allLevels.size())
			return;

		if(!allLevels.get(level).containsKey(key))
			return;

		String[] children = allLevels.get(level).get(key);

		for(final String child : children) {
			Button button = new Button(this);
			button.setText(child);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					breadcrumbs.addBreadcrumb(child);
					updateButtons(child, level + 1);
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
