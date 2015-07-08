package kiwi.ai_wallet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class LiveListActivity extends FoodActivity implements AdapterView.OnItemClickListener {

    private ListView StoreListView,FoodListView;
    private ImageView StorePic;
    private TextView StoreTxt,StoreName;
    private int StorePosition;


    String whatBtn,SpinnerClass,SpinnerName,InputPrice;
    ArrayList<HashMap<String, Object>> StoreList = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> LiveList;
    //    List<HashMap<String, Object>> itemList;
    String php = "http://203.68.252.55/AndroidConnectDB/GPS_Connector.php";


    double latitude,longitude;//latitude(緯度 Y),longitude(經度 X);   (X-X菊)^2 + (Y-Y菊)^2 <= 1^2

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        Log.d("FoodListActivity", "onCreate(Bundle savedInstanceState)");
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
        StoreListView = (ListView)findViewById(R.id.StoreList);
        StoreListView.setOnItemClickListener(this);

        getBundle();

//        itemList =
        setListView();
        MyStoreAdapter adapter = new MyStoreAdapter(this);
        StoreListView.setAdapter(adapter);
    }

    private void getBundle(){
        Log.d("FoodListActivity","getBundle()");
        Intent intent = getIntent();
        whatBtn = intent.getStringExtra("whatBtn?");
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude",0);
        SpinnerClass = intent.getStringExtra("SpinnerClassPos");
//        SpinnerName = intent.getStringExtra("SpinnerNamePos");
//        InputPrice = intent.getStringExtra("InputPrice");


    }

    private ArrayList<HashMap<String, Object>> setListView(){


            Intent intent = getIntent();
            latitude = intent.getDoubleExtra("latitude", 0);
            longitude = intent.getDoubleExtra("longitude", 0);


            String index_sum = "UPDATE `ai_pomo`.`live` SET `lUserX` = " + longitude + ", `lUserY` = " + latitude + ";";
            MySQLConnector.executeQuery(index_sum,php);


            try {
                String indexG = "Create or Replace View LiveDistanceView AS SELECT *, round(6378.138*2*asin(sqrt(pow(sin( (`lY`*pi()/180-`lUserY`*pi()/180)/2),2)+cos(`lY`*pi()/180)*cos(`lUserY`*pi()/180)* pow(sin( (`lX`*pi()/180-`lUserX`*pi()/180)/2),2)))*1000) as LiveDistance from `live`ORDER BY `LiveDistance` ASC limit 20 ;";

                String resultG  = MySQLConnector.executeQuery(indexG,php);
                Log.d("ResultG",resultG);


                String index_sel = "Select * from `ai_pomo`.`LiveDistanceView` where `LiveDistance` < 5000 ORDER BY `LiveDistance` ASC;";
                String result_sumsel =  MySQLConnector.executeQuery(index_sel,php);
                JSONArray jsonArray2 = new JSONArray(result_sumsel);

                setTitle("查詢資料結果");

                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject jsonData = jsonArray2.getJSONObject(i);
                    HashMap<String, Object> h2 = new HashMap<String, Object>();
                    h2.put("lName", jsonData.getString("lName"));
                    h2.put("LiveDistance", jsonData.getString("LiveDistance") + " 公尺");
                    h2.put("lX", jsonData.getString("lX"));
                    h2.put("lY", jsonData.getString("lY"));
                    h2.put("lUserX", jsonData.getString("lUserX"));
                    h2.put("lUserY", jsonData.getString("lUserY"));
                    h2.put("lPicture1", jsonData.getString("lPicture1"));


                    StoreList.add(h2);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        return StoreList;



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




        MySQLConnector.executeQuery("UPDATE `live` SET `lFrequency` = `lFrequency`+1 where `lName` = '" +
                StoreList.get(StorePosition).get("lName").toString() + "'",php);

        StorePosition = position;

        intoGpsView();


    }

    private class MyStoreAdapter extends BaseAdapter{
        private LayoutInflater mInflater;

        public MyStoreAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return StoreList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)convertView = mInflater.inflate(R.layout.store_list_view_object,null);

            final ImageView StoreListImage = (ImageView)convertView.findViewById(R.id.StoreListImage);
            byte[] decodedString = Base64.decode(StoreList.get(position).get("lPicture1").toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            StoreListImage.setImageBitmap(decodedByte);

//         itemImageView.setImageBitmap((Bitmap)StoreList.get(position).get("gPic"));
            TextView StoreListStoreText = (TextView)convertView.findViewById(R.id.StoreListStoreText);
            StoreListStoreText.setText(StoreList.get(position).get("lName").toString());
            TextView StoreListDistanceText = (TextView)convertView.findViewById(R.id.StoreListDistanceText);
            StoreListDistanceText.setText(StoreList.get(position).get("LiveDistance").toString());


            return convertView;
        }
    }

//    private class MyStoreAdapter extends BaseAdapter {
//        private LayoutInflater mInflater;
//
//        public MyStoreAdapter(Context context){
//            this.mInflater = LayoutInflater.from(context);
//        }
//        @Override
//        public int getCount() {
//            return StoreList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            if(convertView == null)convertView = mInflater.inflate(R.layout.store_list_view_object,null);
//
//            final ImageView StoreListImage = (ImageView)convertView.findViewById(R.id.StoreListImage);
//            byte[] decodedString = Base64.decode(StoreList.get(position).get("gPic").toString(), Base64.DEFAULT);
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            StoreListImage.setImageBitmap(decodedByte);
//
////         itemImageView.setImageBitmap((Bitmap)StoreList.get(position).get("gPic"));
//            TextView StoreListStoreText = (TextView)convertView.findViewById(R.id.StoreListStoreText);
//            StoreListStoreText.setText(StoreList.get(position).get("gName").toString());
//            TextView StoreListDistanceText = (TextView)convertView.findViewById(R.id.StoreListDistanceText);
//            StoreListDistanceText.setText(StoreList.get(position).get("long").toString());
//
//
//            return convertView;
//        }
//    }



    private void intoGpsView(){
        //Toast.makeText(this, gUserY_pannel, Toast.LENGTH_LONG).show();




        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse("http://maps.google.com/maps?f=d&saddr=" + StoreList.get(StorePosition).get("lY").toString() + "," + StoreList.get(StorePosition).get("lX").toString() +
                "&daddr=" + StoreList.get(StorePosition).get("lUserY").toString() + "," + StoreList.get(StorePosition).get("lUserX").toString() + "&hl=tw"));

        startActivity(it);
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
