package kiwi.ai_wallet;


import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import android.widget.TextView;


import static kiwi.ai_wallet.DbConstants.TABLE_NAME;
import static android.provider.BaseColumns._ID;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class DateListActivity extends ChargeActivity {

    static String DATE = null;
    TextView text;

    private ListView listView;
    List<HashMap<String, Object>> itemList;
    File dirFile = null;
    Uri imgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_list_view);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        Log.d("測試","onCreate");
        text = (TextView)findViewById(R.id.textView);
        Start();
    }

    void Start(){
        bundleCatch();
        itemList = getData();
        MyAdapter adapter = new MyAdapter(this);

        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        Log.d("測試","Start()");
    }

    void bundleCatch(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        DATE = bundle.getString("findDate");
        /**取得圖檔路徑*/
        dirFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/" + "WalletPic");
//        text.setText("bundleCatch已執行");
    }

    private List<HashMap<String, Object>> getData() {
        /**新建一個集合類，用於存放多條數據，Map的key是一個String類型，Map的value是Object類型*/
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();


        Cursor cursor = getCursor();
        Log.d("測試","準備進入for迴圈");
        while(cursor.moveToNext()) {
            Log.d("測試","進入迴圈");
            if (DATE.equals(cursor.getString(4).substring(0, 8))) {
                Log.d("測試","進入比較判斷");
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

                StringBuilder resultData = new StringBuilder();
//                resultData.append("編號：").append(id).append("\n");
                resultData.append("品名：").append(name).append("\n");
                resultData.append("類型：").append(type).append("\n");
                resultData.append("價錢：").append(price).append("元\n");



                item.put("itemImageView", bmp);
                item.put("itemdata", resultData);
                item.put("id",id);
                item.put("picname",picname);
                list.add(item);
//                text.setText(DATE + "itemInput已執行" + resultData + imgUri);
            }
        }
        Log.d("測試","getData()");
        return list;
    }

//    public void del(int id){
//        text.setText(id);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        db.delete(TABLE_NAME, _ID + "=" + id,null);
//
//    }

    public class MyAdapter extends BaseAdapter {
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
