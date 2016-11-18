package com.example.jesper.breadcrumbs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Stack;

/**
 * Created by jesper on 11/17/16.
 */

public class Breadcrumbs extends LinearLayout {

	private Context context;

	// Stack containing all the breadcrumbs
	protected Stack<String> trail;

	// Listener that triggers when user clicks on breadcrumb link
	protected OnNavigationListener onNavListener;

	// Separator between the breadcrumbs
	protected String separator;

	// Attributes for the links
	protected int linkColor;
	protected float fontSize;
	protected int defaultLinkColor;
	protected int defaultPaintFlags;

	public Breadcrumbs(Context context, AttributeSet attr) {
		super(context, attr);
		init(context);
	}

	public Breadcrumbs(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;

		trail = new Stack<>();

		separator = ">";
		linkColor = Color.parseColor("#0000EE");
		fontSize = 17;

		TextView defaultTextView = new TextView(context);
		defaultLinkColor = defaultTextView.getCurrentTextColor();
		defaultPaintFlags = defaultTextView.getPaintFlags();
	}

	// Adds a breadcrumb to the current trail
	public void addBreadcrumb(String breadcrumb) {

		// If its not the first item add a separator and make the last breadcrumb clickable
		if(!trail.empty()) {
			// Make last breadcrumb clickable
			TextView lastView = getLastView();
			setViewClickable(lastView, true);

			// Add separator
			TextView sep = new TextView(context);
			sep.setText(separator);
			sep.setPadding(8, 0, 8, 0);
			sep.setTextSize(fontSize);
			addView(sep);
		}

		// Add the breadcrumb (not clickable) to the layout
		TextView text = new TextView(context);
		text.setText(breadcrumb);
		text.setTextSize(fontSize);
		addView(text);

		// and also to the stack
		trail.push(breadcrumb);
	}

	// Removes the last breadcrumb in the trail, both from the layout and the stack
	public String removeLast() {
		// If we are not at the first breadcrumb we need to remove 2 views
		// (one for the breadcrumb and one for the separator)
		if(trail.size() != 1)
			removeViewAt(getChildCount() - 1);

		removeViewAt(getChildCount() - 1);
		return trail.pop();
	}

	// Removes all breadcrumbs after the given string
	public void removeAllAfter(String start) {

		// Remove all breadcrumbs till we find the given string
		while(!trail.peek().equals(start))
			removeLast();

		// Make the given string not clickable
		setViewClickable(getLastView(), false);

	}

	// Returns the last view in the layout
	private TextView getLastView() {
		return (TextView) getChildAt(getChildCount() - 1);
	}

	// Make a breadcrumb clickable/not clickable
	private void setViewClickable(TextView view, boolean shouldBeClickable) {

		if(shouldBeClickable) {
			// What should happen when the breadcrumb is clicked
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// Get the text from the breadcrumb
					String text = ((TextView) view).getText().toString();
					removeAllAfter(text); // Remove everything after the breadcrumb

					// If the user has set the listener
					if(onNavListener != null) {
						int hierarchyPos = trail.size() - 1;
						String[] fullPath = getTrail();

						// Send info to the user that the trail has changed and what was clicked
						onNavListener.onNavigate(text, hierarchyPos, fullPath);
					}
				}
			});

			// Change the look of the breadcrumb
			view.setTextColor(linkColor);
			view.setPaintFlags(view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		} else {
			// Remove the functionally of the link
			view.setOnClickListener(null);
			// Reset the look of the breadcrumb (default textview)
			view.setTextColor(defaultLinkColor);
			view.setPaintFlags(defaultPaintFlags);
		}

		view.setClickable(shouldBeClickable);
	}


	// Sets a listener that activates when a breadcrumb is pressed
	public void setOnNavigationListener(OnNavigationListener onNavListener) {
		this.onNavListener = onNavListener;
	}

	// Get the current trail (path from start to current)
	public String[] getTrail() {
		String [] fullPath = new String[trail.size()];
		trail.toArray(fullPath);
		return fullPath;
	}

	// Sets the separator between the breadcrumbs
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	// Returns if the trail has breadcrumbs
	public boolean hasBreadcrumbs() {
		return !trail.empty();
	}

	// Checks if the trail contain a certain breadcrumb
	public boolean contains(String breadcrumb) {
		return trail.contains(breadcrumb);
	}
}
