/*
 * Copyright (C) 2016 Federico Paolinelli.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.whiterabbit.androidtimersample;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {
    private static final String STARTED_TIME_ID = "com.whiterabbit.time";
    private SharedPreferences mPreferences;

    public PrefUtils(Context c) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(c);
    }

    public long getStartedTime() {
        return mPreferences.getLong(STARTED_TIME_ID, 0);
    }

    public void setStartedTime(long started) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putLong(STARTED_TIME_ID, started);
        editor.apply();
    }
}
