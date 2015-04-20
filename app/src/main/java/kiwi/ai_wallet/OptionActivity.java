package kiwi.ai_wallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class OptionActivity extends SmartbutlerActivity implements View.OnClickListener{

    EditText budgetText,regularCostText;
    TextView aboutUs;
    Button saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        initView();



    }

    private void initView(){
        budgetText = (EditText)findViewById(R.id.setMBudget);
        regularCostText = (EditText)findViewById(R.id.regularCost);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        aboutUs = (TextView)findViewById(R.id.aboutUs);



        budgetText.setHint(String.valueOf(getBudget("Budget")));
        regularCostText.setHint(String.valueOf(getBudget("RglCost")));

        saveBtn.setOnClickListener(saveBtnClick);
        aboutUs.setOnClickListener(aboutUsClick);
    }

    private View.OnClickListener aboutUsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder bdr = new AlertDialog.Builder(OptionActivity.this);
            bdr.setMessage("打劫組是由一群對創作與行動裝置抱有熱忱的學生組織而成，目的希望能夠靠著自己的能力去證明，即使在離島就學，在教育程度及能力也能有傑出的表現，並透過參與各種公開比賽磨練自己心智，未來二三十餘年能在職場闖出一片天空．");
            bdr.setTitle("關於打劫組...");
            bdr.show();
        }
    };


    private View.OnClickListener saveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        try {
            if (budgetText.getText().length() == 0 && budgetText.getHint().length() == 0) {

                Toast.makeText(OptionActivity.this, "每月的預算額怎麼可以是空的呢", Toast.LENGTH_SHORT).show();
                return;

            }else if (budgetText.getHint().length() != 0) {
        //                optionSpr = getSharedPreferences("Option",0);
        //                optSprEdt = optionSpr.edit();
                if (budgetText.getText().length() == 0) budgetText.setText(budgetText.getHint());
                if (Integer.parseInt(budgetText.getHint().toString()) == 0 || Integer.parseInt(budgetText.getText().toString()) == 0) {
                    Toast.makeText(OptionActivity.this, "每月的預算額怎麼可以是0呢", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("檢測", budgetText.getText().toString());
                setBudget(Integer.parseInt(budgetText.getText().toString()));
                if (regularCostText.length() == 0 && regularCostText.getHint().length() == 0)
                    regularCostText.setText("0");
                if (regularCostText.getText().length() == 0)
                    regularCostText.setText(regularCostText.getHint());
                Log.d("檢測", regularCostText.getText().toString());
                setRegularCost(Integer.parseInt(regularCostText.getText().toString()));

        //                optSprEdt.putInt("Budget", Integer.parseInt(getBudget(String.valueOf(Budget))));
        //                optSprEdt.putInt("RglCost", Integer.parseInt(getBudget(String.valueOf(RegularCost))));
        //                optSprEdt.commit();
                Toast.makeText(OptionActivity.this, "儲存成功", Toast.LENGTH_LONG).show();

            }
        }catch (Exception e){
            Toast.makeText(OptionActivity.this,"儲存失敗!!\n每月預算不能為空或為0唷",Toast.LENGTH_SHORT).show();
        }
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
