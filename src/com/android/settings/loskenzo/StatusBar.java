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

public class StatusBar extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener {

private static final String PREF_SMART_PULLDOWN = "smart_pulldown";
private ListPreference mSmartPulldown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.los_kenzo_statusbar);
	ContentResolver resolver = getActivity().getContentResolver();
        mSmartPulldown = (ListPreference) findPreference(PREF_SMART_PULLDOWN);
        mSmartPulldown.setOnPreferenceChangeListener(this);
        int smartPulldown = Settings.System.getInt(resolver,
                "qs_smart_pulldown", 0);
        mSmartPulldown.setValue(String.valueOf(smartPulldown));
        updateSmartPulldownSummary(smartPulldown);

    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.DEVICEINFO;
    }

    @Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
         ContentResolver resolver = getActivity().getContentResolver();
	final Resources res = getResources();
	if (preference == mSmartPulldown) {
            int smartPulldown = Integer.valueOf((String) newValue);
            Settings.System.putInt(resolver, "qs_smart_pulldown", smartPulldown);
            updateSmartPulldownSummary(smartPulldown);
            return true;
          }
          return false;
      }

    private void updateSmartPulldownSummary(int value) {
        Resources res = getResources();

        if (value == 0) {
            // Smart pulldown deactivated
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_off));
        } else if (value == 3) {
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_none_summary));
        } else {
            String type = res.getString(value == 1
                    ? R.string.smart_pulldown_dismissable
                    : R.string.smart_pulldown_ongoing);
            mSmartPulldown.setSummary(res.getString(R.string.smart_pulldown_summary, type));
        }
    }

}
