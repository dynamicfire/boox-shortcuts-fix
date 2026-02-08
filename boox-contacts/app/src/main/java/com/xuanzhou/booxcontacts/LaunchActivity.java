package com.xuanzhou.booxcontacts;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class LaunchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName(
            "com.android.contacts",
            "com.android.contacts.activities.PeopleActivity"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();
    }
}
