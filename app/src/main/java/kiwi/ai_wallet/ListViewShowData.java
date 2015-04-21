package kiwi.ai_wallet;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
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

    private ListView listView;


    //
    /*private DB_Picture loadPic;
    private Handler mHandler;
    private ProgressBar progressBar;
    private ImageView imageView;
    private final static String url = "http://uploadingit.com/file/lltpirkd9pk3jbuw/raccoon.png";*/

    String sel_fclass ;
    String sel_fname ;
    String in_fprice ;

    int selLength;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listviewshowdata);
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

        listView = (ListView)findViewById(R.id.listView1);

        listView.setOnItemClickListener(this);

        renewListView(toString());
        sel_count1();
    }

    public final void renewListView(String input) {

        Intent it = getIntent();
        Bundle spin_sel_fclass = this.getIntent().getExtras();
        Bundle spin_sel_fname = this.getIntent().getExtras();
        Bundle input_fprice = this.getIntent().getExtras();
        int check_num = it.getIntExtra("g", 0);




        sel_fclass = spin_sel_fclass.getString("spin_sel_fclass");
        sel_fname = spin_sel_fname.getString("spin_sel_fname");
        in_fprice = input_fprice.getString("input_fprice");


        String result = null;
        String resultG = null;
        try {

            if (check_num == 1) {
                String index = "SELECT * FROM `food` WHERE `fSort` LIKE '%" + sel_fname + "%' "
                        + "AND fPrice <= " + in_fprice +
                        " AND `fClass` LIKE '%" + sel_fclass + "%' ORDER BY `fRank` DESC limit 6";
                result = MySQLConnector.executeQuery(index);

            } else if (check_num == 2) {
                String index = "select * from "
                        + "(select * from `food` where `fClass` like '%" + sel_fclass + "%' "
                        + "AND `fSort` LIKE '%" + sel_fname + "%' "
                        + "AND `fPrice` <= " + in_fprice + " order by rand()) "
                        + "as a limit 6";
                result = MySQLConnector.executeQuery(index);

            }

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
                listView.setAdapter(adapter);
            }


        }
        catch(Exception e) {
        }
    }
    ArrayList<String> selected = new ArrayList<String>();
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

            cMM =  listView.getItemAtPosition(position).toString();
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
                        MySQLConnector.executeQuery(index);
                    }
                    else{
                        cMM_pannel = array[i].substring(7,array[i].length());
                        String index =  "UPDATE `food` SET `frequency` = `frequency`+1 where `fName` = '"
                                + cMM_pannel + "'";
                        MySQLConnector.executeQuery(index);
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

            int rank = (Integer.parseInt(in_fprice) - Integer.parseInt(listSelPrice))
                    * Integer.parseInt(listSelfreq) * 50 / selLength ;
            String index =  "UPDATE `food` SET `fRank` = "+ rank +" where `fName` = '"
                    + cMM_pannel + "'";
            MySQLConnector.executeQuery(index);
            Toast.makeText(this,"你點選了"+ cMM_pannel + "LEVEL UP 喜愛度+1  ><", Toast.LENGTH_LONG).show();
            renewListView(toString()); // 目前都只有顯示六筆 記得除錯資料筆數 已解決
        }
        catch(Exception e){
            Log.e("log_tag21", e.toString());
        }
    }





    public void sel_count1(){
        String index =  "select `fClass`,  count(*), sum(`fClass`) from `food` where `fPrice` <= " + in_fprice
                + " AND `fSort` like '%"+ sel_fname +"%' AND `fClass` like '%"+ sel_fclass +"%' group by `fClass`";
        String result = MySQLConnector.executeQuery(index);

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