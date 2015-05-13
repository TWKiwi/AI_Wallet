package kiwi.ai_wallet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class FoodListActivity extends ActionBarActivity {

    private ListView FoodListView;
    String SelectFoodClass;
    String SelectFoodName;
    String SelectPrice;

    String whatBtn,SpinnerClassPos,SpinnerNamePos,InputPrice;

    int selLength;
    double latitude,longitude;//latitude(緯度 Y),longitude(經度 X);   (X-X菊)^2 + (Y-Y菊)^2 <= 1^2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        Log.d("FoodListActivity","onCreate(Bundle savedInstanceState)");
        initView();

        //連線
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
        //連線
    }

    private void initView(){
        Log.d("FoodListActivity","initView()");
        FoodListView = (ListView)findViewById(R.id.listView1);

        getBundle();
    }

    private void getBundle(){
        Log.d("FoodListActivity","getBundle()");
        Intent intent = getIntent();
        whatBtn = intent.getStringExtra("whatBtn?");
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude",0);
        SpinnerClassPos = intent.getStringExtra("SpinnerClassPos");
        SpinnerNamePos = intent.getStringExtra("SpinnerNamePos");
        InputPrice = intent.getStringExtra("InputPrice");

        setListView();
    }

    private void setListView(){
        Log.d("FoodListActivity",whatBtn + " " + SpinnerClassPos + " " + SpinnerNamePos  + " " + InputPrice);

        if(whatBtn.equals("isProposal")){
            Intent intent = getIntent();
            latitude = intent.getDoubleExtra("latitude", 10);
            longitude = intent.getDoubleExtra("longitude", 10);

            String index = "Select * from gps;";
            String result_sum = GPSConnector.executeQuery(index);
            Log.d("ResultSum",result_sum);
            try{
                JSONArray jsonArray = new JSONArray(result_sum);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        String index_sum = "UPDATE `ai_pomo`.`gps` SET `gUserX` = " + longitude + ", `gUserY` = " + latitude + " WHERE `gps`.`gId` = "+ (i+1) +";";
                        GPSConnector.executeQuery(index_sum);

                    }
            }
            catch (JSONException e){

            }

            try {
                String indexG = "SELECT *, \n" +"round(6378.138*2*asin(sqrt(pow(sin( (`gY`*pi()/180-`gUserY`*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(`gUserY`*pi()/180)* pow(sin( (`gX`*pi()/180-`gUserX`*pi()/180)/2),2)))*1000)  'Distance'  from `gps`;";
                String resultG  = GPSConnector.executeQuery(indexG);
                Log.d("ResultG",resultG);
                JSONArray jsonArray = new JSONArray(resultG);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        int selGps = Integer.parseInt(jsonData.getString("Distance"));
                        String index_long =  "UPDATE `gps` SET `long` = "+ selGps +" where `gId` = '" + (i+1) + ";'";
                        GPSConnector.executeQuery(index_long);
                        Toast.makeText(this, "經度" + String.valueOf(longitude) + "\n緯度" + String.valueOf(latitude) + "\n" + String.valueOf(selGps), Toast.LENGTH_SHORT).show();
                    }

                String index_sel = "SELECT * from `gps` where `long` < 1500;";
                String result_sumsel =  GPSConnector.executeQuery(index_sel);
                JSONArray jsonArray2 = new JSONArray(result_sumsel);
                ArrayList<HashMap<String, Object>> pomo = new ArrayList<HashMap<String, Object>>();
                setTitle("查詢資料結果");

                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonData = jsonArray2.getJSONObject(i);
                        HashMap<String, Object> h2 = new HashMap<String, Object>();
                        h2.put("gName", jsonData.getString("gName"));
                        h2.put("long", jsonData.getString("long") + " 公尺");
                        h2.put("gX", jsonData.getString("gX"));
                        h2.put("gY", jsonData.getString("gY"));
                        h2.put("gUserX", jsonData.getString("gUserX"));
                        h2.put("gUserY", jsonData.getString("gUserY"));

                        pomo.add(h2);

                        SimpleAdapter adapter = new SimpleAdapter(this, pomo, R.layout.gpslistviewshowdataitem, new String[]
                                {"gName", "long"}, new int[]
                                {R.id.GPS_Store, R.id.GPS_Distance});
                                FoodListView.setAdapter(adapter);

                    }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_list, menu);
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
