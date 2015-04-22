package kiwi.ai_wallet;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class MySQLActivity extends Activity implements OnClickListener, OnItemSelectedListener,LocationListener{

    Spinner fclass, fname;
    EditText fPrice;

    private Button btn_select_data,btn2;
    String fClass_name_show[] = {"早餐", "早午餐", "午餐",
            "晚餐", "飲品"};
    String fClass_name[] = {"Br", "B", "L",
            "d", "Dr"};

    String break_name1[] = {"米飯主食", "麵類主食", "吐司系列", "漢堡系列","小嚐飲品", "中西式餐點", "全部類別"};
    String break_select[] = {"rice", "noodles", "Toast", "Hamburger","Tea", "CW", "%"};

    String BL_name1[] = {"米飯主食", "麵類主食", "中西式餐點", "精緻茶品", "華麗特調", "全部類別"};
    String BL_select[] = {"rice", "noodles", "CW", "Tea", "Speci", "%"};

    String LD_name1[] = {"米飯主食", "麵類主食", "披薩", "中西式餐點", "精緻茶品", "華麗特調", "全部類別"};
    String LD_select[] = {"rice", "noodles", "Pizza", "CW", "Tea", "Speci", "%"};

    String Drink_name[] = {"茶類飲品", "果汁系列", "特調飲品", "全部類別"};
    String Drink_select[] = {"Tea", "Juice", "Special", "%"};

    int spin_sel_fclass;
    int spin_sel_fname;
    String fName_name[];
    String fName_put [];

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

        btn_select_data.setOnClickListener(this);
        btn2.setOnClickListener(this);
        GPSBtn.setOnClickListener(this);
        fclass.setOnItemSelectedListener(this);

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

        ArrayAdapter<String> fClassAd =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, fClass_name_show);

        fClassAd.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        fClassAd.setDropDownViewResource(R.layout.sql_spinner_item);

        fclass.setAdapter(fClassAd);


    }

    private void initViews() {
        btn_select_data = (Button)findViewById(R.id.button1);
        btn2 = (Button)findViewById(R.id.button2);
        fclass = (Spinner) findViewById(R.id.spinner_fclass);
        fname = (Spinner) findViewById(R.id.spinner_fname);
        fPrice = (EditText) findViewById(R.id.editText1);
        GPSBtn = (Button) findViewById(R.id.GPSBtn);

        mgr = (LocationManager)getSystemService(LOCATION_SERVICE);
        setGPSBtn = (Button)findViewById(R.id.setGPSBtn);
        setGPSBtn.setOnClickListener(setGPS);
    }

    @Override
    public void onClick(View v) {

        spin_sel_fclass = fclass.getSelectedItemPosition();
        spin_sel_fname = fname.getSelectedItemPosition();
        String input_fprice = fPrice.getText().toString();

        Intent it = new Intent();

        int g;

        if(v.getId() == R.id.GPSBtn){
            g = 3;
            it.putExtra("g", g);
            it.putExtra("latitude", latitude);
            it.putExtra("longitude", longitude);
            it.setClass(getApplicationContext(), GPSActivity.class);

            startActivity(it);
        }
        else{

            if(input_fprice.length() != 0 && ((v.getId() == R.id.button1) ||( v.getId() == R.id.button2))){

                it.setClass(getApplicationContext(), ListViewShowData.class);
                Bundle bundle = new Bundle();
                bundle.putString("spin_sel_fclass", fClass_name[spin_sel_fclass]);
                bundle.putString("spin_sel_fname", fName_put[spin_sel_fname]);
                bundle.putString("input_fprice", input_fprice);
                it.putExtras(bundle);

                switch(v.getId()) {
                    case R.id.button1:
                        g = 1;
                        it.putExtra("g", g);
                        break;
                    case R.id.button2:
                        g = 2;
                        it.putExtra("g", g);
                        break;
                }
                startActivity(it);
            }
            else{
                Toast.makeText(this, "請輸入金額 "
                        , Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos,
                               long id) {
        // TODO Auto-generated method stub
        Toast.makeText(parent.getContext(), "你選擇了  " +
                parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();


        switch(pos){
            case 0:
                fName_name = break_name1;
                fName_put = break_select;
                break;
            case 1:
                fName_name = BL_name1;
                fName_put = BL_select;
                break;
            case 4:
                fName_name = Drink_name;
                fName_put = Drink_select;
                break;
            default:
                fName_name = LD_name1;
                fName_put = LD_select;
                break;
        }
        ArrayAdapter<String> fNameAd =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, fName_name);

        fNameAd.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        fNameAd.setDropDownViewResource(R.layout.sql_spinner_item);

        fname.setAdapter(fNameAd);

    }

    @Override
    public void onResume(){
        super.onResume();
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


    OnClickListener setGPS = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    };
}
