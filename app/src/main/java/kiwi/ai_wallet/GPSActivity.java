package kiwi.ai_wallet;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class GPSActivity extends ActionBarActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        listView = (ListView) findViewById(R.id.listView3);

        gpslist(toString());


    }

    public void gpslist(String input){

        double latitude,longitude;
        Intent it = getIntent();
        String result ;
        String resultG ;


        latitude = it.getDoubleExtra("latitude", 10);
        longitude = it.getDoubleExtra("longitude", 10);

        String index = "UPDATE `ai_pomo`.`gps` SET `gUserX` = " + longitude + ", `gUserY` = " + latitude + " WHERE `gps`.`gId` = 1;";
        result = GPSConnector.executeQuery(index);

        String indexG = "SELECT *, \n" +
                "round(6378.138*2*asin(sqrt(pow(sin( (`gY`*pi()/180-`gUserY`*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(`gUserY`*pi()/180)* pow(sin( (`gX`*pi()/180-`gUserX`*pi()/180)/2),2)))*1000)  'Distance' from `gps`;";
        resultG = GPSConnector.executeQuery(indexG);
        int selGps = 0;
        try {

            JSONArray jsonArray = new JSONArray(resultG);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonData = jsonArray.getJSONObject(i);

                selGps += Integer.parseInt(jsonData.getString("Distance"));
                Toast.makeText(this, "經度" + String.valueOf(longitude) + "\n緯度" + String.valueOf(latitude) + "\n" + String.valueOf(selGps), Toast.LENGTH_SHORT).show();
            }
            //setContentView(R.layout.activity_gpslistviewshowdata);
            ArrayList<HashMap<String, Object>> pomo = new ArrayList<HashMap<String, Object>>();

            setTitle("查詢資料結果");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonData = jsonArray.getJSONObject(i);
                HashMap<String, Object> h2 = new HashMap<String, Object>();

                h2.put("gName", jsonData.getString("gName"));
                h2.put("Distance", jsonData.getString("Distance"));

                pomo.add(h2);

                SimpleAdapter adapter = new SimpleAdapter(this, pomo, R.layout.activity_gps, new String[]
                        {"gName", "Distance"}, new int[]
                        {R.id.GPS_Store_txv, R.id.GPS_Distance});
                listView.setAdapter(adapter);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_g, menu);
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