package com.examples.dota2tv.settings;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.examples.dota2tv.R;

import android.os.Bundle;

public class SettingsActivity extends SherlockPreferenceActivity {
	private ActionBar mActionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActionBar = getSupportActionBar();

		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		addPreferencesFromResource(R.xml.preferences);

	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(item);
	}
}
