package kiwi.ai_wallet;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class OptionActivity extends MenuActivity {

    CheckBox alarmCheckBox;
    EditText budgetText;
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
        budgetText = (EditText)findViewById(R.id.setBudget);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        alarmCheckBox = (CheckBox)findViewById(R.id.alarmCheckBox);

        Budget = option.getInt("Budget",20000);
        budgetText.setHint(String.valueOf(Budget));

    }

    void setOption(){
        SharedPreferences.Editor editor = option.edit();

        editor.putInt("Budget",Budget);
        editor.apply();
    }


    private View.OnClickListener saveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Budget = Integer.parseInt(budgetText.getText().toString());
            checkBoxBoolean = alarmCheckBox.isChecked();
            setOption();
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
}
