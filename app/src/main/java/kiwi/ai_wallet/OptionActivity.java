package kiwi.ai_wallet;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


public class OptionActivity extends SmartbutlerActivity {

    CheckBox alarmCheckBox;
    EditText mbudgetText;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        initView();

        saveBtn.setOnClickListener(saveBtnClick);



    }

    private void initView(){
        mbudgetText = (EditText)findViewById(R.id.setMBudget);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        alarmCheckBox = (CheckBox)findViewById(R.id.alarmCheckBox);

        Budget = option.getInt("Budget",20000);
        mbudgetText.setHint(String.valueOf(Budget));


    }




    private View.OnClickListener saveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

//           setOption();

            SharedPreferences.Editor editor = option.edit();
            editor.clear();


            Budget = Integer.parseInt(mbudgetText.getText().toString());
            editor.putInt("Budget",Budget);
            editor.apply();
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();

    }
}
