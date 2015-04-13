package kiwi.ai_wallet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.GpsStatus;
import android.renderscript.Sampler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ComputerActivity extends SmartbutlerActivity implements View.OnClickListener{

    TextView totalNumText,computeNumText,cptTextView0,cptTextView1,cptTextView2,cptTextView3,cptTextView4,cptTextView5,
            cptTextView6,cptTextView7,cptTextView8,cptTextView9,cptTextViewD,cptTextViewM,cptTextViewS,cptTextViewA,
            cptTextViewE,cptTextViewpoint,cptTextViewC,cptTextViewB;
    String Nu1 = "0",Nu2 = "0";
    int Cal_index = 0;
    int Index_Value = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        initView();
        OnClick();

    }

    private void initView(){

        totalNumText = (TextView)findViewById(R.id.totalNumText);
        computeNumText = (TextView)findViewById(R.id.computeNumText);
        cptTextView0 = (TextView)findViewById(R.id.cptTextView0);
        cptTextView1 = (TextView)findViewById(R.id.cptTextView1);
        cptTextView2 = (TextView)findViewById(R.id.cptTextView2);
        cptTextView3 = (TextView)findViewById(R.id.cptTextView3);
        cptTextView4 = (TextView)findViewById(R.id.cptTextView4);
        cptTextView5 = (TextView)findViewById(R.id.cptTextView5);
        cptTextView6 = (TextView)findViewById(R.id.cptTextView6);
        cptTextView7 = (TextView)findViewById(R.id.cptTextView7);
        cptTextView8 = (TextView)findViewById(R.id.cptTextView8);
        cptTextView9 = (TextView)findViewById(R.id.cptTextView9);
        cptTextViewD = (TextView)findViewById(R.id.cptTextViewD);
        cptTextViewM = (TextView)findViewById(R.id.cptTextViewM);
        cptTextViewS = (TextView)findViewById(R.id.cptTextViewS);
        cptTextViewA = (TextView)findViewById(R.id.cptTextViewA);
        cptTextViewE = (TextView)findViewById(R.id.cptTextViewE);
        cptTextViewpoint = (TextView)findViewById(R.id.cptTextViewpoint);
        cptTextViewC = (TextView)findViewById(R.id.cptTextViewC);
        cptTextViewB = (TextView)findViewById(R.id.cptTextViewＢ);


    }

    private void OnClick(){
        cptTextView0.setOnClickListener(OnClick);
        cptTextView1.setOnClickListener(OnClick);
        cptTextView2.setOnClickListener(OnClick);
        cptTextView3.setOnClickListener(OnClick);
        cptTextView4.setOnClickListener(OnClick);
        cptTextView5.setOnClickListener(OnClick);
        cptTextView6.setOnClickListener(OnClick);
        cptTextView7.setOnClickListener(OnClick);
        cptTextView8.setOnClickListener(OnClick);
        cptTextView9.setOnClickListener(OnClick);
        cptTextViewD.setOnClickListener(OnClick);
        cptTextViewM.setOnClickListener(OnClick);
        cptTextViewS.setOnClickListener(OnClick);
        cptTextViewA.setOnClickListener(OnClick);
        cptTextViewE.setOnClickListener(OnClick);
        cptTextViewpoint.setOnClickListener(OnClick);
        cptTextViewC.setOnClickListener(OnClick);
        cptTextViewB.setOnClickListener(OnClick);


    }

    private TextView.OnClickListener OnClick = new TextView.OnClickListener() {
        @Override
        public void onClick(View v) {
            String Str_Value;
            Str_Value = totalNumText.getText().toString();
            switch (v.getId()){
                case R.id.cptTextView0 : disPlayNum("0"); break;
                case R.id.cptTextView1 : disPlayNum("1"); break;
                case R.id.cptTextView2 : disPlayNum("2"); break;
                case R.id.cptTextView3 : disPlayNum("3"); break;
                case R.id.cptTextView4 : disPlayNum("4"); break;
                case R.id.cptTextView5 : disPlayNum("5"); break;
                case R.id.cptTextView6 : disPlayNum("6"); break;
                case R.id.cptTextView7 : disPlayNum("7"); break;
                case R.id.cptTextView8 : disPlayNum("8"); break;
                case R.id.cptTextView9 : disPlayNum("9"); break;
                case R.id.cptTextViewpoint : disPlayNum("."); break;

                case R.id.cptTextViewA : Caculate(0 , Str_Value); break;
                case R.id.cptTextViewS : Caculate(1 , Str_Value); break;
                case R.id.cptTextViewM : Caculate(2 , Str_Value); break;
                case R.id.cptTextViewD : Caculate(3 , Str_Value); break;
                case R.id.cptTextViewE : Caculate(99 , Str_Value); break;
                case R.id.cptTextViewC : totalNumText.setText("0"); break;

            }
        }
    };

    private void disPlayNum(String s){
        String str;
        String Zero = "0";
        str = totalNumText.getText().toString();
        //確認數字是不是0 或是 有無按下運算鍵
        if (Zero.equals(str) || Index_Value != 0){
            totalNumText.setText("");
            totalNumText.setText(s);
            Index_Value = 0;
        }else{totalNumText.setText(str+s);}
    }
    private void Caculate(int Cal_value, String Cal_Nu){
        double x;
        String str_index;
        switch (Cal_value){
            case 0 :
                Nu1 = Cal_Nu;
                Cal_index = 0;
                totalNumText.setText("");
                break;
            case 1 :
                Nu1 = Cal_Nu;
                Cal_index = 1;
                totalNumText.setText("");
                break;
            case 2 :
                Nu1 = Cal_Nu;
                Cal_index = 2;
                totalNumText.setText("");
                break;
            case 3 :
                Nu1 = Cal_Nu;
                Cal_index = 3;
                totalNumText.setText("");
                break;
            case 99 :
                Nu2 = totalNumText.getText().toString();
                double i = Double.valueOf(Nu1);
                double j = Double.valueOf(Nu2);
                switch (Cal_index){
                    case 0 :
                        x = i + j;
                        str_index = Double.toString(x);
                        totalNumText.setText(str_index);
                        break;
                    case 1 :
                        x = i - j;
                        str_index = Double.toString(x);
                        totalNumText.setText(str_index);
                        break;
                    case 2 :
                        x = i * j;
                        str_index = Double.toString(x);
                        totalNumText.setText(str_index);
                        break;
                    case 3 :
                        x = i / j;
                        str_index = Double.toString(x);
                        totalNumText.setText(str_index);
                        break;
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_computer, menu);
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