package kiwi.ai_wallet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import android.widget.AdapterView.OnItemClickListener;


public class GPSActivity extends ActionBarActivity implements OnItemClickListener {

    ListView listView;
    double latitude, longitude;
    String gX_pannel = "";
    String gY_pannel = "";
    String gUserX_pannel = "";
    String gUserY_pannel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        listView = (ListView) findViewById(R.id.listView3);
        listView.setOnItemClickListener(this);

        gpslist();


    }

    public void gpslist(){

        Intent intent = getIntent();

        latitude = intent.getDoubleExtra("latitude", 10);
        longitude = intent.getDoubleExtra("longitude", 10);

        /**先更新資料庫端的gUser資料*/

        String index = "Select * from gps;";
        String result_sum = GPSConnector.executeQuery(index);
        try{
            JSONArray jsonArray = new JSONArray(result_sum);
            for (int i = 0; i < jsonArray.length(); i++) {

                /**更新每筆商店的gUserX和gUserY"欄位，(i+1)是因為資料庫id是從1開始非0"*/
                String index_sum = "UPDATE `ai_pomo`.`gps` SET `gUserX` = " + longitude + ", `gUserY` = " + latitude + " WHERE `gps`.`gId` = "+ (i+1) +";";
                GPSConnector.executeQuery(index_sum);
            }
        }
        catch (JSONException e){
        }


        /**計算每筆資料距離使用者當下位置的距離*/
        try {
            String indexG = "SELECT *, \n" +"round(6378.138*2*asin(sqrt(pow(sin( (`gY`*pi()/180-`gUserY`*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(`gUserY`*pi()/180)* pow(sin( (`gX`*pi()/180-`gUserX`*pi()/180)/2),2)))*1000)  'Distance'  from `gps`;";
            String resultG  = GPSConnector.executeQuery(indexG);
            JSONArray jsonArray = new JSONArray(resultG);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    int selGps = Integer.parseInt(jsonData.getString("Distance"));
                    String index_long =  "UPDATE `gps` SET `long` = "+ selGps +" where `gId` = '" + (i+1) + ";'";
                    GPSConnector.executeQuery(index_long);
                    // Toast.makeText(this, "經度" + String.valueOf(longitude) + "\n緯度" + String.valueOf(latitude) + "\n" + String.valueOf(selGps), Toast.LENGTH_SHORT).show();
                }

            /**真正在篩選距離小餘1500的店家*/
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Toast.makeText(this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();


        String cMM;
        String array[];


        try {

            cMM = listView.getItemAtPosition(position).toString();
            array = cMM.split(",");

            for (int i = 0; i < array.length; i++) {  //抓gX
                String aStr = array[i].substring(1, 4);
                //Toast.makeText(this, aStr, Toast.LENGTH_LONG).show();
                String bStr = "gX=";
                boolean Equal = aStr.equals(bStr);
                //Toast.makeText(this, aStr, Toast.LENGTH_LONG).show();
                if (Equal) {
                    String cStr = array[i].substring(array[i].length() - 1, array[i].length());
                    String dStr = "}";
                    boolean Equal2 = cStr.equals(dStr);

                    if (Equal2) {
                        gX_pannel = array[i].substring(4, array[i].length() - 1);
                    } else {
                        gX_pannel = array[i].substring(4, array[i].length());
                    }
                }
            }

            for (int i = 0; i < array.length; i++) {  //抓gY
                String aStr = array[i].substring(1, 4);
                //Toast.makeText(this, aStr, Toast.LENGTH_LONG).show();
                String bStr = "gY=";
                boolean Equal = aStr.equals(bStr);
                //Toast.makeText(this, aStr, Toast.LENGTH_LONG).show();
                if (Equal) {
                    String cStr = array[i].substring(array[i].length() - 1, array[i].length());
                    String dStr = "}";
                    boolean Equal2 = cStr.equals(dStr);

                    if (Equal2) {
                        gY_pannel = array[i].substring(4, array[i].length() - 1);
                    } else {
                        gY_pannel = array[i].substring(4, array[i].length());
                    }
                }
            }

            for (int i = 0; i < array.length; i++) {  //抓gUserX
                String aStr = array[i].substring(1, 8);
                //Toast.makeText(this, aStr, Toast.LENGTH_LONG).show();
                String bStr = "gUserX=";
                boolean Equal = aStr.equals(bStr);
                //Toast.makeText(this, aStr, Toast.LENGTH_LONG).show();
                if (Equal) {
                    String cStr = array[i].substring(array[i].length() - 1, array[i].length());
                    String dStr = "}";
                    boolean Equal2 = cStr.equals(dStr);

                    if (Equal2) {
                        gUserX_pannel = array[i].substring(8, array[i].length() - 1);
                    } else {
                        gUserX_pannel = array[i].substring(8, array[i].length());
                    }
                }
            }

            for (int i = 0; i < array.length; i++) {  //抓gUserX
                String aStr = array[i].substring(1, 8);
                //Toast.makeText(this, aStr, Toast.LENGTH_LONG).show();
                String bStr = "gUserY=";
                boolean Equal = aStr.equals(bStr);
                //Toast.makeText(this, aStr, Toast.LENGTH_LONG).show();
                if (Equal) {
                    String cStr = array[i].substring(array[i].length() - 1, array[i].length());
                    String dStr = "}";
                    boolean Equal2 = cStr.equals(dStr);

                    if (Equal2) {
                        gUserY_pannel = array[i].substring(8, array[i].length() - 1);
                    } else {
                        gUserY_pannel = array[i].substring(8, array[i].length());
                    }
                }
            }

            //Toast.makeText(this, gUserY_pannel, Toast.LENGTH_LONG).show();
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse("http://maps.google.com/maps?f=d&saddr=" + gUserY_pannel + "," + gUserX_pannel +
                    "&daddr=" + gY_pannel + "," + gX_pannel + "&hl=tw"));
            startActivity(it);

            //gpslist(toString());

        } catch (Exception e) {
            Log.e("log_tag21", e.toString());
        }
    }

}