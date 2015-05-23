package kiwi.ai_wallet;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;




public class ListViewShowData extends MySQLActivity implements OnItemClickListener{

    private ListView FoodListView;


    String whatBtn,SpinnerClassPos,SpinnerNamePos,InputPrice;

    String SelectFoodClass;
    String SelectFoodName;
    String SelectPrice;

    int selLength;
    double latitude,longitude;//latitude(緯度 Y),longitude(經度 X);   (X-X菊)^2 + (Y-Y菊)^2 <= 1^2
    String php = "http://203.68.252.55/AndroidConnectDB/android_connect_db.php";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listviewshowdata);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //setContentView(R.layout.activity_gpslistviewshowdata);

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


        initView();
        reNewListView();
        setListener();


        sel_count1();
    }

    private void initView(){
        FoodListView = (ListView)findViewById(R.id.listView1);
    }

    private void setListener(){
        FoodListView.setOnItemClickListener(this);
    }

    public final void reNewListView() {

        Intent intent = getIntent();
        Bundle spin_sel_fclass = this.getIntent().getExtras();
        Bundle spin_sel_fname = this.getIntent().getExtras();
        Bundle input_fprice = this.getIntent().getExtras();
        int check_num = intent.getIntExtra("g", 0);
        latitude = intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude",0);




        SelectFoodClass = spin_sel_fclass.getString("spin_sel_fclass");
        SelectFoodName = spin_sel_fname.getString("spin_sel_fname");
        SelectPrice = input_fprice.getString("input_fprice");




        String result = null;
        int selGps = 0;
        try {

            if (check_num == 1) {
                String index = "SELECT * FROM `food` WHERE `fSort` LIKE '%" + SelectFoodName + "%' "
                        + "AND fPrice <= " + SelectPrice +
                        " AND `fClass` LIKE '%" + SelectFoodClass + "%' ORDER BY `fRank` DESC limit 6";
                result = MySQLConnector.executeQuery(index,php);


                JSONArray jsonArray = new JSONArray(result);
                ArrayList<HashMap<String, Object>> pomo = new ArrayList<HashMap<String, Object>>();

                setTitle("查詢資料結果");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    HashMap<String, Object> h2 = new HashMap<String, Object>();

                    h2.put("fName", jsonData.getString("fName"));
                    h2.put("fPrice", jsonData.getString("fPrice"));
                    h2.put("frequency", jsonData.getString("frequency"));
                    h2.put("fStore", jsonData.getString("fStore"));
                    pomo.add(h2);

                    SimpleAdapter adapter = new SimpleAdapter(this, pomo, R.layout.listviewshowdataitem, new String[]
                            {"fName", "fPrice", "fStore"}, new int[]
                            {R.id.fName_txv, R.id.fPrice_txv, R.id.fStore_txv});
                    FoodListView.setAdapter(adapter);
                }

            } else if (check_num == 2) {
                String index = "SELECT *, \n" +
                        "round(6378.138*2*asin(sqrt(pow(sin( (`gY`*pi()/180-`gUserY`*pi()/180)/2),2)+cos(`gY`*pi()/180)*cos(`gUserY`*pi()/180)* pow(sin( (`gX`*pi()/180-`gUserX`*pi()/180)/2),2)))*1000)  'Distance'  from `store information`;";

                result = MySQLConnector.executeQuery(index,php);

                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonData = jsonArray.getJSONObject(i);

                    selGps = Integer.parseInt(jsonData.getString("Distance"));

                    String index_long =  "UPDATE `gps` SET `long` = "+ selGps +" where `gId` = '" + (i+1) + ";'";

                    MySQLConnector.executeQuery(index_long,php);


                    // Toast.makeText(this, "經度" + String.valueOf(longitude) + "\n緯度" + String.valueOf(latitude) + "\n" + String.valueOf(selGps), Toast.LENGTH_SHORT).show();
                }
                //setContentView(R.layout.activity_gpslistviewshowdata);


                String index_sel = "SELECT * from `gps` where `long` < 1500;";
                String result_sumsel =  MySQLConnector.executeQuery(index_sel,php);

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


            }


        }
        catch(Exception e) {
        }
    }
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id){
        // TODO Auto-generated method stub
        String cMM_pannel = "";
        String cMM;
        String array[];
        String listSelPrice = "";
        String listSelfreq = "";

//        Toast.makeText(this,cMM,Toast.LENGTH_SHORT).show();

        try{

            cMM =  FoodListView.getItemAtPosition(position).toString();
            array = cMM.split(",");

            for(int i = 0; i < array.length; i++){  //抓名稱次數加1用 和積分
                String aStr = array[i].substring(1,7);
                String bStr = "fName=";
                boolean Equal = aStr.equals(bStr);

                if(Equal){
                    String cStr = array[i].substring(array[i].length()-1,array[i].length());
                    String dStr = "}";
                    boolean Equal2 = cStr.equals(dStr);

                    if(Equal2){
                        cMM_pannel = array[i].substring(7,array[i].length()-1);
                        String index =  "UPDATE `food` SET `frequency` = `frequency`+1 where `fName` = '"
                                + cMM_pannel + "'";
                        MySQLConnector.executeQuery(index,php);
                    }
                    else{
                        cMM_pannel = array[i].substring(7,array[i].length());
                        String index =  "UPDATE `food` SET `frequency` = `frequency`+1 where `fName` = '"
                                + cMM_pannel + "'";
                        MySQLConnector.executeQuery(index,php);
                    }
                    //Toast.makeText(this, cMM_pannel, Toast.LENGTH_LONG).show();
                }
            }

            for(int i = 0; i < array.length; i++){       //抓價格公式用
                String aStr = array[i].substring(1,8);
                String bStr = "fPrice=";
                boolean Equal = aStr.equals(bStr);

                if(Equal){
                    String cStr = array[i].substring(array[i].length()-1,array[i].length());
                    String dStr = "}";
                    boolean Equal2 = cStr.equals(dStr);

                    if(Equal2){
                        listSelPrice = array[i].substring(8,array[i].length()-1);
                    }
                    else{
                        listSelPrice = array[i].substring(8,array[i].length());
                    }
                    //Toast.makeText(this, cMM_pannel, Toast.LENGTH_LONG).show();
                }
            }
            for(int i = 0; i < array.length; i++){       //抓次數
                String aStr = array[i].substring(1,7);
                String bStr = "freque";
                boolean Equal = aStr.equals(bStr);
                //Toast.makeText(this, "123" + Equal, Toast.LENGTH_LONG).show();
                if(Equal){

                    String cStr = array[i].substring(array[i].length()-1,array[i].length());
                    String dStr = "}";
                    boolean Equal2 = cStr.equals(dStr);

                    if(Equal2){
                        listSelfreq = array[i].substring(11,array[i].length()-1);
                    }
                    else{
                        listSelfreq = array[i].substring(11,array[i].length());
                    }
                    //Toast.makeText(this, cMM_pannel, Toast.LENGTH_LONG).show();
                }
            }

            int rank = (Integer.parseInt(SelectPrice) - Integer.parseInt(listSelPrice))
                    * Integer.parseInt(listSelfreq) * 50 / selLength ;
            String index =  "UPDATE `food` SET `fRank` = "+ rank +" where `fName` = '"
                    + cMM_pannel + "'";
            MySQLConnector.executeQuery(index,php);
            Toast.makeText(this,"你點選了"+ cMM_pannel + "LEVEL UP 喜愛度+1  ><", Toast.LENGTH_LONG).show();
            reNewListView(); // 目前都只有顯示六筆 記得除錯資料筆數 已解決
        }
        catch(Exception e){
            Log.e("log_tag21", e.toString());
        }
    }


    public void sel_count1(){
        String index =  "select `fClass`,  count(*), sum(`fClass`) from `food` where `fPrice` <= " + SelectPrice
                + " AND `fSort` like '%"+ SelectFoodName +"%' AND `fClass` like '%"+ SelectFoodClass +"%' group by `fClass`";
        String result = MySQLConnector.executeQuery(index,php);

        try {

            JSONArray jsonArray = new JSONArray(result);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);

                selLength += Integer.parseInt(jsonData.getString("count(*)"));
            }

        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
			/*Toast.makeText(this, "The planet is " +
					selLength, Toast.LENGTH_LONG).show();	*/
    }
}