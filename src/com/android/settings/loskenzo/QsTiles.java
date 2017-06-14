/*
 * Copyright (C) 2017 The los-kenzo Project
 * used for los-kenzo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.loskenzo;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceScreen;

import android.provider.Settings;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class QsTiles extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

    private static final String PREF_COLUMNS = "qs_layout_columns";
    private static final String PREF_ROWS_PORTRAIT = "qs_rows_portrait";
    private static final String PREF_ROWS_LANDSCAPE = "qs_rows_landscape";
    private CustomSeekBarPreference mQsColumns;
    private CustomSeekBarPreference mRowsPortrait;
    private CustomSeekBarPreference mRowsLandscape;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.los_kenzo_qstiles);
	int defaultValue;

        mQsColumns = (CustomSeekBarPreference) findPreference(PREF_COLUMNS);
        int columnsQs = Settings.System.getInt(resolver,
                Settings.System.QS_COLUMNS, 3);
        mQsColumns.setValue(columnsQs / 1);
        mQsColumns.setOnPreferenceChangeListener(this);

        mRowsPortrait = (CustomSeekBarPreference) findPreference(PREF_ROWS_PORTRAIT);
        int rowsPortrait = Settings.System.getInt(resolver,
                Settings.System.QS_ROWS_PORTRAIT, 3);
        mRowsPortrait.setValue(rowsPortrait / 1);
        mRowsPortrait.setOnPreferenceChangeListener(this);

        defaultValue = getResources().getInteger(com.android.internal.R.integer.config_qs_num_rows_landscape_default);
        mRowsLandscape = (CustomSeekBarPreference) findPreference(PREF_ROWS_LANDSCAPE);
        int rowsLandscape = Settings.System.getInt(resolver,
                Settings.System.QS_ROWS_LANDSCAPE, defaultValue);
        mRowsLandscape.setValue(rowsLandscape / 1);
        mRowsLandscape.setOnPreferenceChangeListener(this);
    }

   @Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
	 int intValue;
	 int index;
         ContentResolver resolver = getActivity().getContentResolver();
	 if (preference == mQsColumns) {
            int qsColumns = (Integer) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
            Settings.System.QS_LAYOUT_COLUMNS, qsColumns * 1);
            return true;
          } else if (preference == mRowsPortrait) {
            int rowsPortrait = (Integer) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.QS_ROWS_PORTRAIT, rowsPortrait * 1);
            return true;
        } else if (preference == mRowsLandscape) {
            int rowsLandscape = (Integer) objValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.QS_ROWS_LANDSCAPE, rowsLandscape * 1);
           return true;
	  }
          return false;
      }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.DEVICEINFO;
    }

}
