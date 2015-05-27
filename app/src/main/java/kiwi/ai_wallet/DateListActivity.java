package kiwi.ai_wallet;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import static kiwi.ai_wallet.DbConstants.NAME;
import static kiwi.ai_wallet.DbConstants.PHNAME;
import static kiwi.ai_wallet.DbConstants.PRICE;
import static kiwi.ai_wallet.DbConstants.TABLE_NAME;
import static android.provider.BaseColumns._ID;
import static kiwi.ai_wallet.DbConstants.TYPE;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



public class DateListActivity extends ChargeActivity implements View.OnClickListener{

    static String DATE = null;
    TextView theDateCostTxv,newDataTxv;
    int theDateCost = 0;
    private ListView listView;
    List<HashMap<String, Object>> itemList;
    File dirFile = null;
    Uri imgUri = null;


    EditText buyNameBefore;
    EditText buyPriceBefore;
    Button takePicBtn;
    Spinner buyTypeBefore;
    ImageView PhotoPicBefore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_list_view);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        initView();
        Start();



    }

    void Start(){
        bundleCatch();
        itemList = getData();
        MyAdapter adapter = new MyAdapter(this);

        listView.setAdapter(adapter);

    }

    void initView(){
        theDateCostTxv = (TextView)findViewById(R.id.theDateCost);
        newDataTxv = (TextView)findViewById(R.id.newData);
        listView = (ListView)findViewById(R.id.listView);

        setListener();
    }

    void setListener(){
        newDataTxv.setOnClickListener(this);
    }

    void bundleCatch(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        DATE = bundle.getString("findDate");
        /**取得圖檔路徑*/
        dirFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/" + "WalletPic");

    }

    private List<HashMap<String, Object>> getData() {
        /**新建一個集合類，用於存放多條數據，Map的key是一個String類型，Map的value是Object類型*/
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        theDateCost = 0;

        Cursor cursor = getCursor();

        while(cursor.moveToNext()) {

            if (DATE.equals(cursor.getString(4).substring(0, 8))) {

                /**依前面的路徑及檔案名建立Uri物件*/
                imgUri = Uri.parse("file://" + dirFile + "/" + cursor.getString(4));
                /**讀取圖檔內容轉換為Bitmap物件*/
                Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath());
                HashMap<String, Object> item = new HashMap<>();

                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String type = cursor.getString(2);
                String price = cursor.getString(3);
                String picname = cursor.getString(4);
                theDateCost += Integer.parseInt(price);

                StringBuilder resultData = new StringBuilder();
                resultData.append("品名：").append(name).append("\n");
                resultData.append("類型：").append(type).append("\n");
                resultData.append("價錢：").append(price).append("元\n");

                item.put("itemImageView", bmp);
                item.put("itemdata", resultData);
                item.put("id",id);
                item.put("picname",picname);
                list.add(item);
            }
        }
        setDateCostTxv();
        return list;
    }

    void setDateCostTxv(){

        theDateCostTxv.setText("當日累計支出"+theDateCost+" 元");
    }


    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount(){
            return itemList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null)convertView = mInflater.inflate(R.layout.list_view_object,null);

            final ImageView itemImageView = (ImageView)convertView.findViewById(R.id.itemImageView);
            itemImageView.setImageBitmap((Bitmap)itemList.get(position).get("itemImageView"));
            TextView itemView = (TextView)convertView.findViewById(R.id.itemView);
            itemView.setText(itemList.get(position).get("itemdata").toString());
            Button deleBtn = (Button)convertView.findViewById(R.id.deleBtn);
            final String id = itemList.get(position).get("id").toString();
            final String picname = itemList.get(position).get("picname").toString();
            deleBtn.setTag(position);
            deleBtn.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {

//                    try{
                    itemList.remove(position);
                    notifyDataSetChanged();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete(TABLE_NAME, _ID + "=" + id, null);
                    Start();
                    Toast.makeText(DateListActivity.this,"已清除該筆紀錄",Toast.LENGTH_SHORT).show();

//                  }catch (Exception e){
//                      Toast.makeText(DateListActivity.this,"當日沒有紀錄",Toast.LENGTH_LONG).show();
                    /**以下刪除功能在android 4.4以上版本不適用*/
                    File f = new File(String.valueOf(dirFile+"/"+picname));
                    f.delete();
//                    }
                }
            });
            return convertView;
        }
    }


    @Override
    public void onClick(View v){
        if(v.getId() == R.id.newData){
            /**補記帳*/
            LayoutInflater inflater = LayoutInflater.from(this);
            final View view = inflater.inflate(R.layout.before_charge_view, null);
            buyNameBefore = (EditText) view.findViewById(R.id.buyNameBefore);
            buyPriceBefore = (EditText) view.findViewById(R.id.buyPriceBefore);
            takePicBtn = (Button) view.findViewById(R.id.takePicBtn);
            buyTypeBefore = (Spinner) view.findViewById(R.id.buyTypeBefore);
            new AlertDialog.Builder(this)
                    .setTitle("補記帳")
                    .setView(view)
                    .setPositiveButton("儲存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newDataTxvInitView();
                            dbadd();
                            Start();
                        }
                    })
                    .show();

        }else{
                /**利用目前時間組合出一個不會重複的檔名*/
                fname = new SimpleDateFormat("HHmmss").format(new Date()) + ".jpg";
                /**依前面的路徑及檔案名建立Uri物件*/
                imgUri = Uri.parse("file://" + dirFile + "/" + DATE + "_" + fname);
                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                /**將Uri加到拍照Intent的額外資料中*/
                CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                startActivityForResult(CameraIntent, 0);
        }
    }

    void newDataTxvInitView(){



//        takePicBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**利用目前時間組合出一個不會重複的檔名*/
//                fname = new SimpleDateFormat("HHmmss").format(new Date()) + ".jpg";
//                /**依前面的路徑及檔案名建立Uri物件*/
//                imgUri = Uri.parse("file://" + dirFile + "/" + DATE + "_" + fname);
//                Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                /**將Uri加到拍照Intent的額外資料中*/
//                CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
//                startActivityForResult(CameraIntent, 0);
//            }
//        });
        newDataTxvSetListener();
    }

    void newDataTxvSetListener(){
        takePicBtn.setOnClickListener(this);
    }

    void showImg(){

        /**圖片寬高，ImageView元件寬高*/
        int iw,ih,vw,vh;
        /**建立選項物件*/
        BitmapFactory.Options option = new BitmapFactory.Options();
        /**讀取圖檔資訊而不載入圖檔*/
        option.inJustDecodeBounds = true;
        /**讀取圖檔資訊存入option中*/
        BitmapFactory.decodeFile(imgUri.getPath(),option);

        iw = option.outWidth;//由option中讀出圖檔寬度
        ih = option.outHeight;//      ''       高度
        vw = PhotoPicBefore.getWidth();//取得ImageView的寬度
        vh = PhotoPicBefore.getHeight();//     ''      高度

        /**計算縮小比率*/
        int scaleFactor = Math.min(iw/vw , ih/vh);
        /**關閉只載入圖檔資訊的選項*/
        option.inJustDecodeBounds = false;
        /**設定縮小比率，例如3則長寬都將縮小為原來的1/3*/
        option.inSampleSize = scaleFactor;
        /**設定在記憶體不夠時，允許系統將圖片內容刪除*/
        option.inPurgeable = true;
        /**讀取圖檔內容轉換為Bitmap物件*/
        Bitmap bmp = BitmapFactory.decodeFile(imgUri.getPath(),option);
        /**顯示*/
        PhotoPicBefore.setImageBitmap(bmp);


        compressImageByQuality(bmp,imgUri.getPath());


    }

    public void dbadd(){

        Cursor cursor = getCursor();
//        String select_month = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int sumM = 0;
        int sumD = 0;
        if (buyPriceBefore.getText().toString().equals("")){
            Toast.makeText(this, "請確實輸入金額", Toast.LENGTH_SHORT).show();

        }else {
            while (cursor.moveToNext()) {
                if (cursor.getString(4).substring(0, 6).equals(DATE.substring(0, 6))) {
                    if(cursor.getString(4).substring(0,8).equals(DATE.substring(0,8))){
                        sumD += Double.parseDouble(cursor.getString(3));
                    }
                    sumM += Double.parseDouble(cursor.getString(3));

                }
            }
            //         Budget = option.getInt("Budget", 20000);
            Log.d("dbadd測試",String.valueOf(sumM));
            if ((sumM + Integer.parseInt(getBudget("RglCost"))) + Integer.parseInt(buyPriceBefore.getText().toString()) >= Integer.parseInt(getBudget("Budget"))) {

                Toast.makeText(this, "注意！！超出月預算！！", Toast.LENGTH_SHORT).show();
            }
            Log.d("dbadd測試",String.valueOf(sumD));
            if (sumD + Integer.parseInt(buyPriceBefore.getText().toString()) >= (Integer.parseInt(getBudget("RglCost")) + Integer.parseInt(getBudget("Budget")))/30) {

                Toast.makeText(this, "注意！！超出日預算！！", Toast.LENGTH_SHORT).show();
            }
        }
        if (! buyPriceBefore.getText().toString().equals("")){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if(fname == null)fname = DATE + "_" + new SimpleDateFormat("HHmmss").format(new Date()) + ".jpg";
            ContentValues values = new ContentValues();
            values.put(PHNAME,fname);
            values.put(NAME, buyNameBefore.getText().toString() + "(補)");
            values.put(TYPE, buyTypeBefore.getSelectedItem().toString().trim());
            values.put(PRICE, buyPriceBefore.getText().toString());
            db.insert(TABLE_NAME,null,values);

            //          getBarChart();
//          cleanEditText();
//          closeDatabase();
        }

    }

    /**這裡是根據不同的標識符判斷是哪個調用返回的結果，然後根據不同的標識符，編寫不同的代碼。*/
    @Override
    protected void onActivityResult(int requestCode,int resulsCode,Intent data) {

        /**處理照片*/
        if (resulsCode == RESULT_OK) {
            showImg();
        }else{
            Toast.makeText(this, "沒有拍到照片", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode,resulsCode,data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date_list, menu);
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
