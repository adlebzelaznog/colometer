package es.us.colometer.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import es.us.colometer.app.Color.ColorFormats;

public class Settings extends Activity{

    // Attributes ----------------------------------------------------------------------------------
    private ColorFormats mColorFormat;
    private int mFocusRadius;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mPreferencesEditor;

    // Activity methods ----------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.config);



        // TODO: use custom "option layout" instead of linear layout
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Antes estaba en el onCreate
        loadUserPreferences();

        loadOptions();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        mPreferences = getSharedPreferences("colometerPreferences", Context.MODE_PRIVATE);
        mPreferencesEditor = mPreferences.edit();

        // Save user preferences on sharedPreferences
        mPreferencesEditor.putInt("focusRadius", mFocusRadius);
        switch (mColorFormat){
            case RGB:
                mPreferencesEditor.putString("colorModel", "RGB");
                break;
            case NV21:
                mPreferencesEditor.putString("colorModel", "NV21");
                break;
        }

        mPreferencesEditor.commit();
    }

    // Ancillary methods ---------------------------------------------------------------------------
    private void loadUserPreferences(){
        mPreferences = getSharedPreferences("colometerPreferences", Context.MODE_PRIVATE);

        mFocusRadius = mPreferences.getInt("focusRadius", 20);
        mColorFormat = ColorFormats.valueOf(mPreferences.getString("colorModel", "RGB"));
    }

    private void loadOptions(){
        mPreferences = getSharedPreferences("colometerPreferences", Context.MODE_PRIVATE);
        mPreferencesEditor = mPreferences.edit();

        // Add buttons or seek bars

        SeekBar barToListen = (SeekBar) findViewById(R.id.focus_radius_seekbar);
        barToListen.setMax(40);

        RelativeLayout oneToListen = (RelativeLayout) findViewById(R.id.color_model_option);
        // Add listeners
        oneToListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadOptionChoices();
            }
        });

        barToListen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mFocusRadius = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPreferencesEditor.putInt("focusRadius", mFocusRadius);
                mPreferencesEditor.commit();
            }
        });
    }

    private void loadOptionChoices(){
        Intent intent = new Intent(this, SettingsValueSelection.class);
        startActivity(intent);
    }

}
