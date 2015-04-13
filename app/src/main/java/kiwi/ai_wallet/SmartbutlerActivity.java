package kiwi.ai_wallet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class SmartbutlerActivity extends OptionActivity implements View.OnClickListener {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartbutler);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

    }

    private void initView(){
        TextView smartbutlerTxt = (TextView)findViewById(R.id.smartbutlerTxt);
        TextView ControlTxt = (TextView)findViewById(R.id.ControlTxt);
        TextView easyCompute = (TextView)findViewById(R.id.easyComputeTxt);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.smartbutlerTxt :   Intent intent = new Intent(SmartbutlerActivity.this,MySQLActivity.class);
                                         startActivity(intent);
                                         break;

            case R.id.ControlTxt :       intent = new Intent(SmartbutlerActivity.this,OptionActivity.class);
                                         startActivity(intent);
                                         break;

            case R.id.easyComputeTxt :   intent = new Intent(SmartbutlerActivity.this, ComputerActivity.class);
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
