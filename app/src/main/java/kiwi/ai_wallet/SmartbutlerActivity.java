package kiwi.ai_wallet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class SmartbutlerActivity extends MenuActivity implements View.OnClickListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartbutler);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        initView();
    }

    private void initView(){
        TextView smartbutlertxt = (TextView)findViewById(R.id.smartbutlerTxt);
        TextView ControlTxt = (TextView)findViewById(R.id.ControlTxt);
        TextView easyComputeTxt = (TextView)findViewById(R.id.easyComputeTxt);

        smartbutlertxt.setOnClickListener(this);
        ControlTxt.setOnClickListener(this);
        easyComputeTxt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.smartbutlerTxt:    intent = new Intent(this,FoodActivity.class);
                                         startActivity(intent);
                                         break;

            case R.id.ControlTxt :       intent = new Intent(this,OptionActivity.class);
                                         startActivity(intent);
                                         break;

            case R.id.easyComputeTxt :   intent = new Intent(this, ComputerActivity.class);
                                         startActivity(intent);
                                         break;

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smartbutler, menu);
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
