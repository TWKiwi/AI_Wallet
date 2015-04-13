package kiwi.ai_wallet;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MySQLActivity extends Activity {

    Spinner fclass, fname;
    EditText fPrice;
    String fClass_name[] = {"Br", "Lunch and dinner", "L",
            "D", "Dr"};
    String fName_name[] = {"飯", "麵", "麵包", "茶", ""};

    Toast tos;

    public static final boolean DEVELOPER_MODE = BuildConfig.DEBUG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sql);

        initView();
        setListeners();

        /**
         * 第一個 if 是判斷是否為開發模式，第二個 if 是如果在舊 Android 版本執行時就不啟動 StrictMode。
         * StrictMode通常可以捕捉到發生在磁片或網路訪 問的應用主執行緒中，
         * 可以讓主執行緒UI和動畫在磁片讀寫和網路操作時變得更平滑，避免ANR視窗的發生。*/
        if(DEVELOPER_MODE)
        {
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.GINGERBREAD_MR1) {

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads()  //磁碟讀
                        .detectDiskWrites() //磁碟寫
                        .detectNetwork() // 這裡可以替換為detectAll() 就包括了磁片讀寫和網路I/O
                        .penaltyLog() //列印logcat
                        .build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects() //探測SQLite資料庫操作
                        .penaltyLog() //列印logcat
                        .penaltyDeath()
                        .build());
            }
        }
    }

    private Button btn_select_data;

    private void initView() {
        btn_select_data = (Button)findViewById(R.id.button1);
        fclass = (Spinner) findViewById(R.id.spinner_fclass);
        fname = (Spinner) findViewById(R.id.spinner_fname);
        fPrice = (EditText) findViewById(R.id.editText1);

        tos = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    private void setListeners() {
        btn_select_data.setOnClickListener(getDBRecord);
    }

    private Button.OnClickListener getDBRecord = new Button.OnClickListener() {
        public void onClick(View v) {


            TableLayout select_list = (TableLayout)findViewById(R.id.select_table);
            select_list.setStretchAllColumns(true);  //全部列自動填充空白處
            /**
             * LayoutParams相當於一個Layout的屬性封包，封裝了Layout的寬與高*/
            TableLayout.LayoutParams row_layout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams view_layout = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            try {
                /**
                 * 取得三個物件的內容值*/
                int spin_sel_fclass = fclass.getSelectedItemPosition();
                int spin_sel_fname = fname.getSelectedItemPosition();
                String input_fprice = fPrice.getText().toString();

                if(input_fprice.length() != 0){ //如果金額欄非空值

                    String index =  "SELECT * FROM `food` WHERE `fName` LIKE '%"+ fName_name[spin_sel_fname] +"%' "
                            +"AND fPrice <= "+ input_fprice +
                            " AND `fClass` LIKE '%"+ fClass_name[spin_sel_fclass] +"%' ORDER BY `fPrice` ASC";
                    String result = DBConnector.executeQuery(index);

                    JSONArray jsonArray = new JSONArray(result);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        TableRow tr = new TableRow(MySQLActivity.this);
                        tr.setLayoutParams(row_layout);
                        tr.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView DB_fname = new TextView(MySQLActivity.this);
                        DB_fname.setText(jsonData.getString("fName"));
                        DB_fname.setLayoutParams(view_layout);

                        TextView DB_fprice = new TextView(MySQLActivity.this);
                        DB_fprice.setText(jsonData.getString("fPrice"));
                        DB_fprice.setLayoutParams(view_layout);

                        TextView DB_fstore = new TextView(MySQLActivity.this);
                        DB_fstore.setText(jsonData.getString("fStore"));
                        DB_fstore.setLayoutParams(view_layout);

                        tr.addView(DB_fname);
                        tr.addView(DB_fprice);
                        tr.addView(DB_fstore);
                        select_list.addView(tr);

                    }
                }
                else{
                    tos.setText("請輸入金額");
                    tos.show();
                }


            } catch(Exception e) {
                // Log.e("log_tag", e.toString());
            }
        }
    };
}
