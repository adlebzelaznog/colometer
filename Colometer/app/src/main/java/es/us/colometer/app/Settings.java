package es.us.colometer.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import es.us.colometer.app.Color.ColorFormats;

public class Settings extends Activity{

    // Attributes ----------------------------------------------------------------------------------
    private ColorFormats mColorFormat;
    private int mFocusRadius;

    // Activity methods ----------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load user preferences and display them on the screen so the user can change them
        loadUserPreferences();

        // TODO: use custom "option layout" instead of linear layout


        setContentView(R.layout.config);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        // Save user preferences on sharedPreferences
        SharedPreferences preferences = getSharedPreferences("colometerPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = preferences.edit();

        preferencesEditor.putInt("focusRadius", mFocusRadius);
        switch (mColorFormat){
            case RGB:
                preferencesEditor.putString("colorModel", "RGB");
                break;
            case NV21:
                preferencesEditor.putString("colorModel", "NV21");
                break;
        }

        preferencesEditor.commit();
    }

    // Ancillary methods ---------------------------------------------------------------------------
    private void loadUserPreferences(){
        SharedPreferences userPreferences = getSharedPreferences("colometerPreferences", Context.MODE_PRIVATE);

        mFocusRadius = userPreferences.getInt("focusRadius", 20);
        mColorFormat = ColorFormats.valueOf(userPreferences.getString("colorModel", "RGB"));

    }
}
