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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FoodListActivity extends FoodActivity implements AdapterView.OnItemClickListener {

    private ListView StoreListView,FoodListView;
    private ImageView StorePic;
    private TextView StoreTxt,StoreName;
    private int StorePosition;


    String whatBtn,SpinnerClass,SpinnerName,InputPrice;
    ArrayList<HashMap<String, Object>> StoreList = new ArrayList<HashMap<String, Object>>();
    ArrayList<HashMap<String, Object>> FoodList;
//    List<HashMap<String, Object>> itemList;
    String php = "http://203.68.252.55/AndroidConnectDB/GPS_Connector.php";


    double latitude,longitude;//latitude(緯度 Y),longitude(經度 X);   (X-X菊)^2 + (Y-Y菊)^2 <= 1^2

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
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
//        whatBtn = intent.getStringExtra("whatBtn?");
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude",0);
        SpinnerClass = intent.getStringExtra("SpinnerClassPos");
//        SpinnerName = intent.getStringExtra("SpinnerNamePos");
//        InputPrice = intent.getStringExtra("InputPrice");


    }

    private ArrayList<HashMap<String, Object>> setListView(){

//
//        if(whatBtn.equals("isProposal")){
//            Intent intent = getIntent();
//            latitude = intent.getDoubleExtra("latitude", 0);
//            longitude = intent.getDoubleExtra("longitude", 0);
//
//
//            String index_sum = "UPDATE `ai_pomo`.`health` SET `hUserX` = " + "119.57241" + ", `hUserY` = " + "23.57852" + ";";
//            MySQLConnector.executeQuery(index_sum,php);
//
//
//            try {
//                String indexG = "Create or Replace View HospitalDistanceView AS" +
//                        "SELECT *, round(6378.138*2*asin(sqrt(pow(sin( (`hY`*pi()/180-`hUserY`*pi()/180)/2),2)+cos(`hY`*pi()/180)*cos(`hUserY`*pi()/180)* pow(sin( (`hX`*pi()/180-`hUserX`*pi()/180)/2),2)))*1000) as HospitalDistance from `health`ORDER BY `HospitalDistance` ASC limit 20 ;";
//
//                String resultG  = MySQLConnector.executeQuery(indexG,php);
//                Log.d("ResultG",resultG);
//
//
//                String index_sel = "Select * from `ai_pomo`.`HospitalDistanceView` where `HospitalDistance` < 5000 ORDER BY `HospitalDistance` ASC ;";
//                String result_sumsel =  MySQLConnector.executeQuery(index_sel,php);
//                JSONArray jsonArray2 = new JSONArray(result_sumsel);
//
//                setTitle("查詢資料結果");
//
//                for (int i = 0; i < jsonArray2.length(); i++) {
//                    JSONObject jsonData = jsonArray2.getJSONObject(i);
//                    HashMap<String, Object> h2 = new HashMap<String, Object>();
//                    h2.put("hName", jsonData.getString("hName"));
//                    h2.put("HospitalDistance", jsonData.getString("HospitalDistance") + " 公尺");
//
//
//                    StoreList.add(h2);
//
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return StoreList;


//        if(whatBtn.equals("isProposal")){
            Intent intent = getIntent();
            latitude = intent.getDoubleExtra("latitude", 0);
            longitude = intent.getDoubleExtra("longitude", 0);


            String index_sum = "UPDATE `ai_pomo`.`gps` SET `gUserX` = " + longitude + ", `gUserY` = " + latitude + ";";
        MySQLConnector.executeQuery(index_sum,php);


        try {
            String indexG = "SELECT *, \n" +"round(6378.138*2*asin(sqrt(pow(sin( (`gY`*pi()/180-`gUserY`*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(`gUserY`*pi()/180)* pow(sin( (`gX`*pi()/180-`gUserX`*pi()/180)/2),2)))*1000)  'Distance'  from `gps`;";
            String resultG  = MySQLConnector.executeQuery(indexG,php);
            Log.d("ResultG",resultG);
            JSONArray jsonArray = new JSONArray(resultG);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                int selGps = Integer.parseInt(jsonData.getString("Distance"));
                String index_long =  "UPDATE `gps` SET `long` = "+ selGps +" where `gId` = '" + (i+1) + ";'";
                MySQLConnector.executeQuery(index_long,php);
            }
            String index_rank = "UPDATE `gps` SET `gRank`=`gFrequency`/`long`";
            MySQLConnector.executeQuery(index_rank,php);

            if(hashMapSort().equals("rice")) {
                //特殊加成
                String select = "SELECT DISTINCT `fStore` FROM `food` WHERE `fSort` like '%rice%' ORDER BY `fRank` DESC";
                JSONArray jsonArray1 = new JSONArray(MySQLConnector.executeQuery(select, php));

                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    MySQLConnector.executeQuery("UPDATE `gps` SET `gRank`=`gFrequency`*10/`long`+50 where `gName` = '" + jsonObject.getString("fStore") + "'", php);

                }
            }else if(hashMapSort().equals("noodles")) {
                //特殊加成
                String select = "SELECT DISTINCT `fStore` FROM `food` WHERE `fSort` like '%noodles%' ORDER BY `fRank` DESC";
                JSONArray jsonArray1 = new JSONArray(MySQLConnector.executeQuery(select, php));

                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                    MySQLConnector.executeQuery("UPDATE `gps` SET `gRank`=`gFrequency`*10/`long`+50 where `gName` = '" + jsonObject.getString("fStore") + "'", php);

                }
            }
            String index_sel = "SELECT * from `gps` where `long` < 5000 and `gStoreClass` LIKE '%" + SpinnerClass + "%'order by `gRank` desc;";
            String result_sumsel =  MySQLConnector.executeQuery(index_sel,php);
            JSONArray jsonArray2 = new JSONArray(result_sumsel);

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
                h2.put("gPic", jsonData.getString("gPic"));
                h2.put("Description", jsonData.getString("Description"));

                StoreList.add(h2);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//    }

    return StoreList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LayoutInflater inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.food_alertdialog_object, null);
        StorePic = (ImageView)view.findViewById(R.id.StorePic);
        StoreName = (TextView)view.findViewById(R.id.StoreName);
        StoreTxt = (TextView)view.findViewById(R.id.StoreTxt);
        FoodListView = (ListView)view.findViewById(R.id.FoodListView);

        byte[] decodedString = Base64.decode(StoreList.get(position).get("gPic").toString(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        StorePic.setImageBitmap(decodedByte);

        StoreName.setText(StoreList.get(position).get("gName").toString());
        StoreTxt.setText(StoreList.get(position).get("Description").toString());



        try{
            FoodList = new ArrayList<HashMap<String, Object>>();
            String index_sel = "SELECT * from `food` where `fStore` = '"+ StoreList.get(position).get("gName").toString() + "' ORDER BY `frequency` DESC";
            String result_sumsel =  MySQLConnector.executeQuery(index_sel,php);
            JSONArray jsonArray2 = new JSONArray(result_sumsel);

            setTitle("查詢資料結果");


            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject jsonData = jsonArray2.getJSONObject(i);
                HashMap<String, Object> h2 = new HashMap<String, Object>();
                h2.put("fName", jsonData.getString("fName"));
                h2.put("image", jsonData.getString("image"));
                h2.put("fPrice", jsonData.getString("fPrice"));
                h2.put("fSort", jsonData.getString("fSort"));

                FoodList.add(h2);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        StorePosition = position;

        MyFoodAdapter adapter = new MyFoodAdapter(this);
        FoodListView.setAdapter(adapter);

        FoodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MySQLConnector.executeQuery("UPDATE `food` SET `frequency` = `frequency`+1 where `fName` = '" +
                         FoodList.get(position).get("fName").toString() + "'",php);
                if(FoodList.get(position).get("fSort").toString().equals("rice")) {
                    setFoodRiceRank(Integer.parseInt(getBudget("FoodRice")) + 1);
                }
                else if(FoodList.get(position).get("fSort").toString().equals("noodles")){
                    setFoodNoodleRank(Integer.parseInt(getBudget("FoodNoodle")) + 1);
                }

                MySQLConnector.executeQuery("UPDATE `gps` SET `gFrequency` = `gFrequency`+1 where `gName` = '" +
                        StoreList.get(StorePosition).get("gName").toString() + "'",php);

                intoGpsView();

            }
        });

        AlertDialog.Builder FoodAD = new AlertDialog.Builder(this);
            FoodAD.setView(view);
            FoodAD.setPositiveButton("離開", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   //do nothing and close view
                }
            });


        FoodAD.show();


    }
//
//    private class MyStoreAdapter extends BaseAdapter{
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
//
//
////         itemImageView.setImageBitmap((Bitmap)StoreList.get(position).get("gPic"));
//            TextView StoreListStoreText = (TextView)convertView.findViewById(R.id.StoreListStoreText);
//            StoreListStoreText.setText(StoreList.get(position).get("hName").toString());
//            TextView StoreListDistanceText = (TextView)convertView.findViewById(R.id.StoreListDistanceText);
//            StoreListDistanceText.setText(StoreList.get(position).get("HospitalDistance").toString());
//
//
//            return convertView;
//        }
//    }

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
            byte[] decodedString = Base64.decode(StoreList.get(position).get("gPic").toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            StoreListImage.setImageBitmap(decodedByte);

//         itemImageView.setImageBitmap((Bitmap)StoreList.get(position).get("gPic"));
            TextView StoreListStoreText = (TextView)convertView.findViewById(R.id.StoreListStoreText);
            StoreListStoreText.setText(StoreList.get(position).get("gName").toString());
            TextView StoreListDistanceText = (TextView)convertView.findViewById(R.id.StoreListDistanceText);
            StoreListDistanceText.setText(StoreList.get(position).get("long").toString());


            return convertView;
        }
    }

    private class MyFoodAdapter extends BaseAdapter{
        private LayoutInflater mInflater;

        public MyFoodAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return FoodList.size();
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
            if(convertView == null)convertView = mInflater.inflate(R.layout.food_list_view_object,null);

            final ImageView FoodListImage = (ImageView)convertView.findViewById(R.id.FoodListImage);
            byte[] decodedString = Base64.decode(FoodList.get(position).get("image").toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            FoodListImage.setImageBitmap(decodedByte);

//         itemImageView.setImageBitmap((Bitmap)StoreList.get(position).get("gPic"));
            TextView FoodListStoreText = (TextView)convertView.findViewById(R.id.FoodListStoreText);
            FoodListStoreText.setText(FoodList.get(position).get("fName").toString());
            TextView FoodListPriceText = (TextView)convertView.findViewById(R.id.FoodListPriceText);
            FoodListPriceText.setText(FoodList.get(position).get("fPrice").toString());


            return convertView;
        }
    }

    private void intoGpsView(){
        //Toast.makeText(this, gUserY_pannel, Toast.LENGTH_LONG).show();




        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse("http://maps.google.com/maps?f=d&saddr=" + StoreList.get(StorePosition).get("gY").toString() + "," + StoreList.get(StorePosition).get("gX").toString() +
                "&daddr=" + StoreList.get(StorePosition).get("gUserY").toString() + "," + StoreList.get(StorePosition).get("gUserX").toString() + "&hl=tw"));

        startActivity(it);
    }

    private String hashMapSort(){
        HashMap<String,Integer> hashMap = new HashMap<>();
        hashMap.put("noodles",Integer.parseInt(getBudget("FoodNoodle")));
        hashMap.put("rice",Integer.parseInt(getBudget("FoodRice")));

        List<Map.Entry<String,Integer>> listData = new ArrayList<Map.Entry<String,Integer>>(hashMap.entrySet());

        Collections.sort(listData, new Comparator<Map.Entry<String,Integer>>(){
            public int compare(Map.Entry<String,Integer> entry1,
                               Map.Entry<String,Integer> entry2){
                return (entry2.getValue() - entry1.getValue());
            }
        });
            //取得首筆資料
            return listData.get(0).getKey();


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
