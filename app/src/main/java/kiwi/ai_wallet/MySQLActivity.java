package kiwi.ai_wallet;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



public class MySQLActivity extends Activity implements OnClickListener, OnItemSelectedListener,LocationListener{

    Spinner FoodClass,FoodName;
    EditText fPrice;

    private Button SelectDataBtn, ProposalBtn;
    String FoodClassNameDisplay[] = {"早餐", "早午餐", "午餐",
            "晚餐", "飲品"};
    String FoodClassName[] = {"Br", "B", "L",
            "d", "Dr"};

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
        SelectDataBtn.setOnClickListener(this);
        ProposalBtn.setOnClickListener(this);
        GPSBtn.setOnClickListener(this);
        FoodClass.setOnItemSelectedListener(this);
        setGPSBtn.setOnClickListener(this);
    }

    private void initViews() {
        SelectDataBtn = (Button)findViewById(R.id.SelectDataBtn);
        ProposalBtn = (Button)findViewById(R.id.ProposalBtn);
        FoodClass = (Spinner) findViewById(R.id.spinner_fclass);
        FoodName = (Spinner) findViewById(R.id.spinner_fname);
        fPrice = (EditText) findViewById(R.id.inputPrice);
        GPSBtn = (Button) findViewById(R.id.GPSBtn);

        mgr = (LocationManager)getSystemService(LOCATION_SERVICE);
        setGPSBtn = (Button)findViewById(R.id.setGPSBtn);


    }

    @Override
    public void onClick(View v) {
        if(fPrice.length() != 0){

            switch(v.getId()) {
                case R.id.setGPSBtn:
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    break;
                case R.id.GPSBtn:
                    Intent GPSIntent = new Intent(this, FoodListActivity.class);
                    Bundle GPSBundle = new Bundle();
                    GPSBundle.putString("whatBtn?", "isGPS");
                    GPSBundle.putDouble("latitude", latitude);
                    GPSBundle.putDouble("longitude", longitude);
                    GPSIntent.putExtras(GPSBundle);
                    startActivity(GPSIntent);
                    break;
                case R.id.ProposalBtn:
                    if (fPrice.length() == 0) break;
                    Intent ProposalIntent = new Intent(this, FoodListActivity.class);
                    Bundle SelectBundle = new Bundle();
                    SelectBundle.putString("SpinnerClassPos", FoodClassName[FoodClass.getSelectedItemPosition()]);
                    SelectBundle.putString("SpinnerNamePos", fName_put[FoodName.getSelectedItemPosition()]);
                    SelectBundle.putString("InputPrice", fPrice.getText().toString());
                    SelectBundle.putDouble("latitude", latitude);
                    SelectBundle.putDouble("longitude", longitude);
                    SelectBundle.putString("whatBtn?", "isProposal");
                    ProposalIntent.putExtras(SelectBundle);
                    startActivity(ProposalIntent);
                    break;
            }
        }else{
                Toast.makeText(this, "請輸入金額 ", Toast.LENGTH_LONG).show();
        }

        }

////        spin_sel_fclass = FoodClass.getSelectedItemPosition();
////        spin_sel_fname = FoodName.getSelectedItemPosition();
//        Price = fPrice.getText().toString();
//
//        Intent intent = new Intent();
//
//        int g;
//
//        if(v.getId() == R.id.GPSBtn){
//            g = 3;
//            intent.putExtra("g", g);
//            intent.putExtra("latitude", latitude);
//            intent.putExtra("longitude", longitude);
//            intent.setClass(getApplicationContext(), GPSActivity.class);
//
//            startActivity(intent);
//        }
//        else{
//
//            if(Price.length() != 0 && ((v.getId() == R.id.button1) ||( v.getId() == R.id.button2))){
//
//                intent.setClass(getApplicationContext(), ListViewShowData.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("spin_sel_fclass", FoodClassName[FoodClass.getSelectedItemPosition()]);
//                bundle.putString("spin_sel_fname", fName_put[FoodName.getSelectedItemPosition()]);
//                bundle.putString("input_fprice", Price);
//                intent.putExtras(bundle);
//
//                switch(v.getId()) {
//                    case R.id.button1:
//                        g = 1;
//                        intent.putExtra("g", g);
//                        break;
//                    case R.id.button2:
//                        g = 2;
//                        intent.putExtra("g", g);
//                        intent.putExtra("latitude", latitude);
//                        intent.putExtra("longitude", longitude);
//                        break;
//                }
//                startActivity(intent);
//            }
//            else{
//                Toast.makeText(this, "請輸入金額 "
//                        , Toast.LENGTH_LONG).show();
//            }
//
//        }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {
        // TODO Auto-generated method stub
        if(spinnerItemSelect) {
            Toast.makeText(parent.getContext(), "你選擇了  " +
                    parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
        }

        switch(pos){
            case 0:
                fName_name = BreakTypeName;
                fName_put = BreakTypeSelect;
                spinnerItemSelect = true;
                break;
            case 1:
                fName_name = BreakLunchTypeName;
                fName_put = BreakLunchTypeSelect;
                spinnerItemSelect = true;
                break;
            case 4:
                fName_name = DrinkTypeName;
                fName_put = DrinkTypeSelect;
                spinnerItemSelect = true;
                break;
            default:
                fName_name = LunchDinnerTypeName;
                fName_put = LunchDinnerTypeSelect;
                spinnerItemSelect = true;
                break;
        }
        ArrayAdapter<String> fNameAd =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, fName_name);

        fNameAd.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        fNameAd.setDropDownViewResource(R.layout.sql_spinner_item);

        FoodName.setAdapter(fNameAd);

    }

    @Override
    public void onResume(){
        super.onResume();
        spinnerItemSelect = false;
        //取得最佳定位提供者
        String best = mgr.getBestProvider(new Criteria(), true);//true 找出已啟用
        if(best != null){
            GPSBtn.setText("正在定位中...");
            mgr.requestLocationUpdates(best,MIN_TIME,MIN_DIST,this);//註冊監聽器
        }else GPSBtn.setText("請確認有開啟定位功能！");
    }

    @Override
    public void onPause(){
        super.onPause();
        mgr.removeUpdates(this);//取消註冊
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onLocationChanged(Location location) {
        String str = "定位提供者：" + location.getProvider();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        str += String.format("\n緯度:%.5f\n經度:%.5f",
                latitude,
                longitude);
        GPSBtn.setText(str + "\n點我查詢");

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
