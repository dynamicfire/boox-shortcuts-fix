package com.xuanzhou.booxsms;

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
            "com.android.mms",
            "com.android.mms.ui.ConversationList"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();
    }
}
