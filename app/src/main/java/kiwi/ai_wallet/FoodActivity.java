package kiwi.ai_wallet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class FoodActivity extends SmartbutlerActivity implements OnClickListener,LocationListener{

    Spinner FoodClass,FoodName;
    EditText fPrice;
    Button ProposalBtn;

    String FoodClassNameDisplay[] = {"早上", "中午", "晚上"};
    String FoodClassName[] = {"Breakfast", "Lunch","dinner"};

    String BreakTypeName[] = {"米飯主食", "麵類主食", "吐司系列", "漢堡系列","小嚐飲品", "中西式餐點", "全部類別"};
    String BreakTypeSelect[] = {"rice", "noodles", "Toast", "Hamburger","Tea", "CW", "%"};

    String BreakLunchTypeName[] = {"米飯主食", "麵類主食", "中西式餐點", "精緻茶品", "華麗特調", "全部類別"};
    String BreakLunchTypeSelect[] = {"rice", "noodles", "CW", "Tea", "Speci", "%"};

    String LunchDinnerTypeName[] = {"米飯主食", "麵類主食", "披薩", "中西式餐點", "精緻茶品", "華麗特調", "全部類別"};
    String LunchDinnerTypeSelect[] = {"rice", "noodles", "Pizza", "CW", "Tea", "Speci", "%"};

    String DrinkTypeName[] = {"茶類飲品", "果汁系列", "特調飲品", "全部類別"};
    String DrinkTypeSelect[] = {"Tea", "Juice", "Special", "%"};

//    int spin_sel_fclass;
//    int spin_sel_fname;
    String fName_name[],fName_put[];
    boolean spinnerItemSelect;

    /**定位工程*/
    static final int MIN_TIME = 5000;
    static final float MIN_DIST = 5;
    LocationManager mgr;
    Button GPSBtn,setGPSBtn;
    TextView smartbutlerTxt,smartliveTxt,smarthealthTxt;
    double latitude,longitude;//latitude(緯度 Y),longitude(經度 X);   (X-X菊)^2 + (Y-Y菊)^2 <= 1^2
    /**定位工程*/



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sql);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        initViews();
        setListener();


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        ArrayAdapter<String> FoodClassAdd =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, FoodClassNameDisplay);

        FoodClassAdd.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        FoodClassAdd.setDropDownViewResource(R.layout.sql_spinner_item);

        FoodClass.setAdapter(FoodClassAdd);


    }

    private void setListener(){

        ProposalBtn.setOnClickListener(this);
        smartbutlerTxt.setOnClickListener(this);
        smartliveTxt.setOnClickListener(this);
        smarthealthTxt.setOnClickListener(this);
        GPSBtn.setOnClickListener(this);
        setGPSBtn.setOnClickListener(this);
    }

    private void initViews() {

        ProposalBtn = (Button)findViewById(R.id.ProposalBtn);
        smartbutlerTxt = (TextView)findViewById(R.id.smartbutlerTxt);
        smartliveTxt = (TextView)findViewById(R.id.smartliveTxt);
        smarthealthTxt = (TextView)findViewById(R.id.smarthealthTxt);
        FoodClass = (Spinner) findViewById(R.id.spinner_fclass);
//        FoodName = (Spinner) findViewById(R.id.spinner_fname);
//        fPrice = (EditText) findViewById(R.id.inputPrice);
        GPSBtn = (Button) findViewById(R.id.GPSBtn);
        setGPSBtn = (Button) findViewById(R.id.setGPSBtn);
        mgr = (LocationManager)getSystemService(LOCATION_SERVICE);
//        setGPSBtn = (Button)findViewById(R.id.setGPSBtn);


    }

    @Override
    public void onClick(View v) {

        Intent ProposalIntent;
            switch(v.getId()) {
                case R.id.setGPSBtn:
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    break;

                case R.id.smartbutlerTxt:
//                    if (fPrice.length() == 0) break;
                    ProposalIntent = new Intent(this, FoodListActivity.class);
                    Bundle SelectBundle = new Bundle();
                    SelectBundle.putString("SpinnerClassPos", FoodClassName[FoodClass.getSelectedItemPosition()]);
//                    SelectBundle.putString("SpinnerNamePos", fName_put[FoodName.getSelectedItemPosition()]);
//                    SelectBundle.putString("InputPrice", fPrice.getText().toString());
                    SelectBundle.putDouble("latitude", latitude);
                    SelectBundle.putDouble("longitude", longitude);
                    SelectBundle.putString("whatBtn?", "isProposal");
                    ProposalIntent.putExtras(SelectBundle);
                    startActivity(ProposalIntent);
                    break;

                case R.id.smartliveTxt:
                    ProposalIntent = new Intent(this,LiveListActivity.class);
                    SelectBundle = new Bundle();
                    SelectBundle.putString("SpinnerClassPos", FoodClassName[FoodClass.getSelectedItemPosition()]);
//                    SelectBundle.putString("SpinnerNamePos", fName_put[FoodName.getSelectedItemPosition()]);
//                    SelectBundle.putString("InputPrice", fPrice.getText().toString());
                    SelectBundle.putDouble("latitude", latitude);
                    SelectBundle.putDouble("longitude", longitude);
                    SelectBundle.putString("whatBtn?", "isProposal");
                    ProposalIntent.putExtras(SelectBundle);
                    startActivity(ProposalIntent);
                    break;

                case R.id.smarthealthTxt:
                    ProposalIntent = new Intent(this,HealthListActivity.class);
                    SelectBundle = new Bundle();
                    SelectBundle.putString("SpinnerClassPos", FoodClassName[FoodClass.getSelectedItemPosition()]);
//                    SelectBundle.putString("SpinnerNamePos", fName_put[FoodName.getSelectedItemPosition()]);
//                    SelectBundle.putString("InputPrice", fPrice.getText().toString());
                    SelectBundle.putDouble("latitude", latitude);
                    SelectBundle.putDouble("longitude", longitude);
                    SelectBundle.putString("whatBtn?", "isProposal");
                    ProposalIntent.putExtras(SelectBundle);
                    startActivity(ProposalIntent);
                    break;


                }
            }


    @Override
    public void onResume(){
        super.onResume();
        spinnerItemSelect = false;
        //取得最佳定位提供者
        String best = mgr.getBestProvider(new Criteria(), true);//true 找出已啟用
        if(best != null){
            GPSBtn.setText("請選擇時段並等候定位完成...");
            mgr.requestLocationUpdates(best,MIN_TIME,MIN_DIST,this);//註冊監聽器
        }else GPSBtn.setText("請確認有開啟定位功能！");
    }

    @Override
    public void onPause(){
        super.onPause();
        mgr.removeUpdates(this);//取消註冊
    }


    @Override
    public void onLocationChanged(Location location) {
        String str = "定位提供者：" + location.getProvider();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        str += String.format("\n緯度:%.5f\n經度:%.5f",
                latitude,
                longitude);
        GPSBtn.setText("定位完成!!\n請選擇時段並點選系統推薦商品!!\n" + str);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}
