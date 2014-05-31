package es.us.colometer.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsValueSelection extends Activity {

    // Attributes ----------------------------------------------------------------------------------
    private String mColorModel;

    // Activity methods ----------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("APP LIFECYCLE", "SettingsValueSelection onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.color_model_list);

        SharedPreferences userPreferences = getSharedPreferences("colometerPreferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = userPreferences.edit();
        String colorModel = userPreferences.getString("colorModel", "RGB");
        RadioButton radioButton;

        // Set as checked the corresponding radio button
        if(colorModel.equals("RGB")){
            radioButton = (RadioButton) findViewById(R.id.rgb_radio_button);
            radioButton.setChecked(true);
        }
        else if(colorModel.equals("NV21")){
            radioButton = (RadioButton) findViewById(R.id.nv21_radio_button);
            radioButton.setChecked(true);
        }

        // Add radio button listeners
        radioButton = (RadioButton) findViewById(R.id.rgb_radio_button);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorModel = "RGB";
                updateOptions();
                editor.putString("colorModel", "RGB");
                editor.apply();
            }
        });

        radioButton = (RadioButton) findViewById(R.id.nv21_radio_button);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mColorModel = "NV21";
                updateOptions();
                editor.putString("colorModel", "NV21");
                editor.apply();
            }
        });
    }

    @Override
    protected void onDestroy() {
        Log.d("APP LIFECYCLE", "SettingsValueSelection onDestroy");
        super.onDestroy();

        SharedPreferences userPreferences = getSharedPreferences("colometerPreferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = userPreferences.edit();

        editor.commit();
    }

    // Ancillary methods ---------------------------------------------------------------------------
    /**
     * Checks the selected option and unchecks the other options
     * */
    private void updateOptions(){
        RadioGroup options = (RadioGroup) findViewById(R.id.color_model_options_group);

        for(int i=0; i<options.getChildCount(); i++){
            RadioButton option = (RadioButton) options.getChildAt(i);
            int optionId = option.getId();

            if(mColorModel.equals("RGB") && optionId == R.id.rgb_radio_button)
                option.setChecked(true);
            else if(mColorModel.equals("NV21") && optionId == R.id.nv21_radio_button)
                option.setChecked(true);
            else
                option.setChecked(false);
        }
    }

}
