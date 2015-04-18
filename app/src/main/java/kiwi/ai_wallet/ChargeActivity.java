package kiwi.ai_wallet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static kiwi.ai_wallet.DbConstants.TABLE_NAME;
import static kiwi.ai_wallet.DbConstants.PHNAME;
import static kiwi.ai_wallet.DbConstants.NAME;
import static kiwi.ai_wallet.DbConstants.TYPE;
import static kiwi.ai_wallet.DbConstants.PRICE;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;


public class ChargeActivity extends MenuActivity{



    private ViewPager ViewPager;/**對應的ViewPager*/
    private View vCalender,vCamera,vScale;/**替每一頁Layout取代號*/
    private List<View> viewList;/**準備拿來裝每一頁*/
    private List<String> titleList;/**申請了一個String數組，用來存儲三個頁面所對應的標題的*/

    public DBHelper dbHelper = null;



    TextView TakePic,SaveBtn,ScaleNumM,ScaleNumD;
    ImageView PhotoPic;


    Spinner consumerType = null;
    String[] buyType = {" 食 "," 衣 "," 住 "," 行 "," 育 "," 樂 "," 其他 "};
    EditText name,priceText;

    Uri imgUri;
    /**建立APP圖檔公用路徑*/
    File dirFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/" + "WalletPic");
    static String fname;


    private long lastDate = 0;

    private int downX;
    private int downY;

    private int upX;
    private int upY;

    private int lastX=0;
    private int lastY=0;

    private ItemArea currentArea;

    private int year = -1;
    private int month;
    private int day;

    private ListView listView;

    private int[] startPoint = new int[2];

    private int listItemCount = 0;
    private int listItemWidth = 0;
    private int listItemHeight = 0;

    ArrayList<ItemArea> areaList = new ArrayList<>();

    private CalendarView calendar;

    private boolean isInitialized = false;

    private boolean finish = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.under_view);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        initView();/**首要步驟，匯入ViewPager及各頁Layout布局資料，不先做後面程式碼會找不到你所指的物件是哪個*/


        calendar.setShowWeekNumber(false);
        calendar.setFirstDayOfWeek(2);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                if(view.getDate() != lastDate) {
                    Log.d("TAG", "onSelectedDayChange(CalendarView view, int year, int month, int day)" + isInitialized);
                    lastDate = view.getDate();
                    ChargeActivity.this.year = year;
                    ChargeActivity.this.month = month;
                    ChargeActivity.this.day = day;
                    finish = true;
                    intoDateList();
                    currentArea = getItemArea(upX, upY);
                }
            }
        });

        listView = (ListView)calendar.findViewById(android.R.id.list);

        openDatabase();
        ChargeTouchListener();/**第三步呼叫方法ChargeTouchListener()架設監聽器*/
        getBarChart();
        setOption();


    }

    /**
     * Start()用來建立ViewPager及滑動標題*/
    private void initView(){
        /**建立一個ViewPager*/
        ViewPager = (ViewPager) findViewById(R.id.chargePager);
        LayoutInflater vInflater = getLayoutInflater();

        /**
         * public View inflate (int resource, ViewGroup root)
         * reSource：View的layout的ID
         * root：如果為null，則將此View作為根,此時既可以應用此View中的其他控件了。
         * 如果!null,則將默認的layout作為View的根。*/
        vCalender = vInflater.inflate(R.layout.calendar_view,null);
        vCamera = vInflater.inflate(R.layout.camera_view,null);
        vScale = vInflater.inflate(R.layout.scale_for_charge,null);

        calendar = (CalendarView)vCalender.findViewById(R.id.CalendarView);
        PhotoPic = (ImageView)vCamera.findViewById(R.id.PhotoPic);
        TakePic = (TextView)vCamera.findViewById(R.id.TakePic);
        SaveBtn = (TextView)vCamera.findViewById(R.id.saveBtn);
        name = (EditText)vCamera.findViewById(R.id.buyName);
        consumerType = (Spinner)vCamera.findViewById(R.id.typeName);
        priceText = (EditText)vCamera.findViewById(R.id.priceNum);
        ScaleNumM = (TextView)vScale.findViewById(R.id.showScaleNumM);
        ScaleNumD = (TextView)vScale.findViewById(R.id.showScaleNumD);

        /**
         * 將要分頁顯示的View裝入數組中*/
        viewList = new ArrayList<>();
        viewList.add(vCamera);
        viewList.add(vCalender);
        viewList.add(vScale);


        /**
         * 在初始化階段增加了這麼一段初始化數組的代碼。*/
        titleList = new ArrayList<>();
        titleList.add("Camera");
        titleList.add("Calender");
        titleList.add("Scale");



        /**
         * 再來要設置ViewPager的適配器拉
         * 很重要關鍵的東西...
         * 資料來源 :
         * http://blog.csdn.net/harvic880925/article/details/38487149
         * */
        PagerAdapter pagerAdapter = new PagerAdapter() {

            /**
             * viewpager不直接處理每一個視圖而是將各個視圖與一個鍵(Key)聯繫起來。這個鍵用來跟踪且唯一代表一個頁面，不僅如此，
             * 該鍵還獨立於這個頁面所在adapter的位置。 當pageradapter將要改變的時候他會調用startUpdate函數，接下來
             * 會調用一次或多次的instantiateItem或者destroyItem。最後在更新的後期會調用finishUpdate。當finishUpdate
             * 返回時instantiateItem返回的對象應該添加到父ViewGroup，destroyItem返回的對象應該被ViewGroup刪除。
             * isViewromObject(View, Object)代表了當前的頁面是否與給定的鍵相關聯。
             *
             * 對於非常簡單的pageradapter或許你可以選擇用page本身作為鍵，在創建並且添加到viewgroup後instantiateItem
             * 方法裡返回該page本身即可destroyItem將會將該page從viewgroup裡面移除。isViewFromObject方法裡面直接
             * 可以返回view == object。

             對於上面兩段話，兩點重點：
             1、第一段說明了，鍵（Key）的概念，首先這裡要清楚的一點是，每個滑動頁面(View)都對應一個Key，而且這個Key值是用來唯
             一追踪這個頁面的，也就是說每個滑動頁面都與一個唯一的Key一一對應。

             2、第二段簡單講了一個應用，即將當前頁面本身的View作為Key。*/

            @Override
            public int getCount() {
                /**
                 * 返回當前有效視圖的個數。*/
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                /**
                 * 對於這個函數就先不做講解，大家目前先知道它要這樣重寫就行了，後面我們會對它進行改寫。
                 *
                 * 功能：該函數用來判斷instantiateItem(ViewGroup, int)函數所返回來的Key與一個頁面視圖是否是代表的同一個視圖
                 * (即它倆是否是對應的，對應的表示同一個View)
                 * 返回值：如果對應的是同一個View，返回True，否則返回False。
                 *
                 * 由於在instantiateItem（）中，我們作為Key返回來的是當前的View，所以在這裡判斷時，我們直接將Key與View看是否相等來判斷是否是同一個View。
                 */
                return view == o;
            }

            @Override//移除項目
            public  void  destroyItem(ViewGroup container,  int  position, Object object) {
                /**
                 * 從當前container中刪除指定位置（position）的View;
                 * 該方法實現的功能是移除一個給定位置的頁面。適配器有責任從容器中刪除這個視圖。
                 * 這是為了確保在finishUpdate(viewGroup)返回時視圖能夠被移除。*/
                container.removeView(viewList.get(position));
            }

            @Override//實例化項目
            public  Object instantiateItem(ViewGroup container,  int  position) {
                /**
                 * 做了兩件事，第一：將當前視圖添加到容器(container)中，第二：返回當前View
                 *
                 * 這個函數的實現的功能是創建指定位置的頁面視圖。適配器有責任增加即將創建的View視圖到這裡給定的container中，
                 * 這是為了確保在finishUpdate(viewGroup)返回時this is be done!
                 * 返回值：返回一個代表新增視圖頁面的Object（Key），這裡沒必要非要返回視圖本身，也可以這個頁面的其它容器。
                 * 其實我的理解是可以代表當前頁面的任意值，只要你可以與你增加的View一一對應即可，比如position變量也可以做為Key
                 *
                 * 1、從說明中可以看到，在代碼中，我們的責任是將指定position的視圖添加到conatiner中
                 * 2、Key的問題：從這個函數就可以看出，該函數返回值就是我們根據參數position增加到conatiner裡的View的所對應的Key！！！！！！！
                 * 3、“it only must ensure this is done by the time it returns fromfinishUpdate(ViewGroup).
                 *  ”這句話在destroyItem（）的函數說明中同樣出現過，這說明在finishUpdate(viewGroup)執行完後，有兩個操作，
                 *  一個是原視圖的移除（不再顯示的視圖），另一個是新增顯示視圖（即將顯示的視圖）
                 *
                 * 在這裡，我們做了兩件事
                 * 第一：將參數里給定的position的視圖，增加到conatiner中，供其創建並顯示、。
                 * 第二：返回當前position的View做為此視圖的Key。還記得API官方文檔中下面這段話麼？
                 * 對於非常簡單的pageradapter或許你可以選擇用page本身作為鍵，在創建並且添加到viewgroup後instantiateItem方法
                 * 裡返回該page本身即可，destroyItem將會將該page從viewgroup裡面移除。isViewFromObject方法裡面直接可以返回view == object。
                 * 這裡就把當前的View當作Key傳過出去！！！！*/
                container.addView(viewList.get(position));


                return  viewList.get(position);
            }

            @Override//實例化標題
            public CharSequence getPageTitle(int position) {
                /**根據位置返回當前所對應的標題。
                 * 也可以把return titleList.get(position);拿掉
                 * 換成這樣，意思是一樣的
                 * switch  (position) {
                 case  0 :
                 return  "Calender" ;
                 case  1 :
                 return  "Camera" ;
                 case  2 :
                 return  "Test" ;

                 default :
                 return  "" ;
                 }  */
                return titleList.get(position);
            }
        };
        ViewPager.setAdapter(pagerAdapter);

        /**下拉選單適配器*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.charge_spinner_item,buyType);
        consumerType.setAdapter(adapter);


        /**如果資料夾不存在*/
        if(!dirFile.exists()){
            /**建立資料夾*/
            dirFile.mkdirs();

        }

    }

    void getBarChart(){


        FrameLayout scale_view = (FrameLayout)vScale.findViewById(R.id.scaleView);
        int persentMonth = scaleComputeOfMonth();
        int persentDay = scaleComputeOfDay();

        String[] titles = new String[] { "預算額", "已花費" };
        List < double []> values = new ArrayList<> ();
        values.add( new  double [] { 100  , 100});
        values.add( new  double [] { persentMonth , persentDay});
        int [] colors = new  int [] { Color.GRAY, Color.RED};
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);//長條圖顏色設置

        /**設置圖形renderer,標題,橫軸,縱軸,橫軸最小值,橫軸最大值,縱軸最大值,縱軸最小值,設定軸寬,設定軸色,標籤顏色*/
        setChartSettings(renderer, "本月/本日花費(%)", "", "", 0.6, 2.6 , 0, 100 , 180f , Color.GRAY, Color.LTGRAY);
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);//在第1條圖形上顯示數據
        renderer.getSeriesRendererAt(1).setDisplayChartValues(true);//在第2條圖形上顯示數據
        renderer.setXLabels(0);//設置x軸標籤數  0為不顯示文字 程式設定文字
        renderer.setYLabels(5);//設置y軸標籤數
        renderer.setXLabelsAlign(Paint.Align.CENTER);//設置x軸標籤置中
        renderer.setYLabelsAlign(Paint.Align.RIGHT);//設置y軸標籤置中
        renderer.setYLabelsColor(0, Color.BLUE);//設置y軸標籤顏色
        renderer.setPanEnabled(false, false);//圖表移動  If you want to lock both axis, then use renderer.setPanEnabled(false, false);
        renderer.setZoomEnabled(false, false);//圖表縮放(x軸,y軸)
        renderer.setZoomRate(1.1f);//放大倍率
        renderer.setBarSpacing(0.5f);//長條圖的間隔
        renderer.setChartValuesTextSize(40);//設置長條圖上面字大小
        renderer.setMarginsColor(Color.argb(0, 0xff, 0, 0));//這句很重要，不能用transparent代替。
        renderer.setBackgroundColor(Color.TRANSPARENT);//設置透明色
        renderer.setApplyBackgroundColor(true);//使背景色生效
        renderer.setMargins(new int[]{200,100,0,100});
        View view = ChartFactory.getBarChartView(this, buildBarDataset(titles, values), renderer, BarChart.Type.STACKED); // Type.STACKED
        scale_view.addView(view);


    }

    protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List< double []> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for ( int i = 0; i < length; i++ ) {
            CategorySeries series = new CategorySeries(titles[i]);
            double [] v = values.get(i);
            int seriesLength = v.length;
            for ( int k = 0; k < seriesLength; k++ ) {
                series.add(v[k]);//加入每筆values資料
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    protected XYMultipleSeriesRenderer buildBarRenderer( int [] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize( 60 );
        renderer.setChartTitleTextSize( 60 );
        renderer.setLabelsTextSize( 40 );
        renderer.setLegendTextSize( 40 );


        int length = colors.length;
        for ( int i = 0; i < length; i++ ) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }
    /**設置圖形renderer,標題,橫軸,縱軸,最小伸縮刻度,最大伸縮刻度,縱軸最大值,縱軸最小值,設定軸寬,設定軸色,標籤顏色*/
    protected  void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax,float width, int axesColor, int labelsColor) {
            renderer.setChartTitle(title);
            renderer.setXTitle(xTitle);
            renderer.setYTitle(yTitle);
            renderer.setXAxisMin(xMin);
            renderer.setXAxisMax(xMax);
            renderer.setYAxisMin(yMin);
            renderer.setYAxisMax(yMax);
            renderer.setBarWidth(width);
            renderer.setAxesColor(axesColor);
            renderer.setLabelsColor(labelsColor);
    }

    private int scaleComputeOfMonth(){
            Cursor cursor = getCursor();
            String select_month = new SimpleDateFormat("yyyyMMdd").format(new Date());
            int sum = 0;

            while (cursor.moveToNext()){
                if(cursor.getString(4).substring(0,6).equals(select_month.substring(0,6))){
                    sum += Double.parseDouble(cursor.getString(3));
                }
            }
    //        SharedPreferences option = getPreferences(MODE_PRIVATE);
            Budget = option.getInt("Budget",20000);
            int persent = (sum*100)/Budget;//算百分比條小數點弄成百分比整數
            if(persent <= 100) {
                ScaleNumM.setText(Html.fromHtml("本月額度<br>" + sum + "<font color = '#FF0000'><big>/</font>" + Budget+ "元"));
            }else if(persent > 100){
                ScaleNumM.setText(Html.fromHtml("本月" + "<font color = '#FF0000'><big>超支<br></font>" + (sum - Budget)+ "元"));
                persent = 100;
            }

        return persent;
    }

    private int scaleComputeOfDay(){
        Cursor cursor = getCursor();
        String select_month = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int sum = 0;

        while (cursor.moveToNext()){
            if(cursor.getString(4).substring(0,8).equals(select_month.substring(0,8))){
                sum += Double.parseDouble(cursor.getString(3));
            }
        }
        //        SharedPreferences option = getPreferences(MODE_PRIVATE);
        Budget = option.getInt("Budget",20000);
        int persent = (sum*100)/(Budget/30);//算百分比條小數點弄成百分比整數
        if(persent <= 100) {
            ScaleNumD.setText(Html.fromHtml("本日額度<br>" + sum + "<font color = '#FF0000'><big>/</font>" + (Budget/30) + "元"));
        }else if(persent > 100){
            ScaleNumD.setText(Html.fromHtml("本日" + "<font color = '#FF0000'><big>超支<br></font>" + (sum - (Budget/30))+ "元"));
            persent = 100;
        }

        return persent;
    }

    private void openDatabase(){
        dbHelper = new DBHelper(this);
    }

    private void closeDatabase(){
        dbHelper.close();
    }

    public void dbadd(){

        Cursor cursor = getCursor();
        String select_month = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int sum = 0;
        if (priceText.getText().toString().equals("")){
            Toast.makeText(this, "請確實輸入金額", Toast.LENGTH_SHORT).show();

        }else {
            while (cursor.moveToNext()) {
                if (cursor.getString(4).substring(0, 6).equals(select_month.substring(0, 6))) {
                    sum += Double.parseDouble(cursor.getString(3));
                }
            }
            Budget = option.getInt("Budget", 20000);
            if ((Double.parseDouble(String.valueOf(sum)) + Double.parseDouble(String.valueOf(priceText.getText()))) >= Budget) {
                Toast.makeText(this, "注意！！超出預算！！", Toast.LENGTH_SHORT).show();
            }
        }
        if (! priceText.getText().toString().equals("")){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            if(fname == null)fname = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
            ContentValues values = new ContentValues();
            values.put(PHNAME,fname);
            values.put(NAME, name.getText().toString());
            values.put(TYPE, consumerType.getSelectedItem().toString());
            values.put(PRICE, priceText.getText().toString());
            db.insert(TABLE_NAME,null,values);

            getBarChart();
            cleanEditText();
//          closeDatabase();
        }

    }

    public Cursor getCursor(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] colums = {_ID,NAME,TYPE,PRICE,PHNAME};

        Cursor cursor = db.query(TABLE_NAME,colums,null,null,null,null,null);
        startManagingCursor(cursor);

        return cursor;
    }

    private void cleanEditText(){

        Intent restart = new Intent(ChargeActivity.this,ChargeActivity.class);
        startActivity(restart);
        finish();

    }

    /**
     * ChargeTouchListener()設置按鍵監聽器*/
    public void ChargeTouchListener(){

        TakePic.setOnClickListener(CameraBtn);
        SaveBtn.setOnClickListener(SaveData);
    }

    /**拍照監聽器*/
    public View.OnClickListener CameraBtn = new View.OnClickListener() {
        /**
         * 相同調用startActivityForResult（）在啟動此系統調用的Activity後，在調用完畢返回結果到當前頁面時，返回結果碼“1”
         * ，對應PHOTO_TAKE_PIC，以便當前頁面知道是從這個按鈕的調用返回的結果；*/
        @Override
        public void onClick(View v) {


            /**利用目前時間組合出一個不會重複的檔名*/
            fname = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
            /**依前面的路徑及檔案名建立Uri物件*/
            imgUri = Uri.parse("file://" + dirFile + "/" + fname);
            Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            /**將Uri加到拍照Intent的額外資料中*/
            CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            startActivityForResult(CameraIntent,0);

        }
    };

    public  View.OnClickListener SaveData = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname == null){
                    fname = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                }

                dbadd();
                getBarChart();
            }
    };


//    public CalendarView.OnLongClickListener LongClick = new CalendarView.OnLongClickListener(){
//
//        @Override
//        public boolean onLongClick(View v) {
//
//            Log.d("test","longClick");
//
//            return false;
//        }
//    };
//
    String checkDate = null;



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
        vw = PhotoPic.getWidth();//取得ImageView的寬度
        vh = PhotoPic.getHeight();//     ''      高度

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
        PhotoPic.setImageBitmap(bmp);

        new AlertDialog.Builder(this)
                .setTitle("圖檔資訊")
                .setMessage("圖檔路徑:" + imgUri.getPath() +
                         "\n 原始尺寸:" + iw + "x" + ih +
                         "\n 載入尺寸:" + bmp.getWidth() + "x" + bmp.getHeight() +
                         "\n 顯示尺寸:" + vw + "x" + vh +
                         "\n 縮小倍率為" + scaleFactor + "倍")
                .setNeutralButton("關閉", null)
                .show();

        /**圖檔壓縮*/
        File picCompression = new File(imgUri.getPath());
        try {
            FileOutputStream out= new FileOutputStream(picCompression);
            if(bmp.compress(Bitmap.CompressFormat.PNG, 10, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *FrameLayout fr=(FrameLayout)findViewById(R.id.FrameLayout01);
     * BitmapFactory.Options options =new BitmapFactory.Options();
     * options.inJustDecodeBounds = true;
     *
     * 獲取這個圖片的寬和高
     * Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/test.jpg", options); //此時返回bm為空
     * options.inJustDecodeBounds =false;
     *
     * 計算縮放比
     * int be = (int)(options.outHeight/(float)200);
     * if (be <= 0) be = 1;
     * options.inSampleSize = be;
     *
     * 重新讀入圖片，注意這次要把options.inJustDecodeBounds 設為 false哦
     * bitmap=BitmapFactory.decodeFile("/sdcard/test.jpg",options);
     * int w = bitmap.getWidth();
     * int h = bitmap.getHeight();
     * System.out.println(w+" "+h);
     * ImageView iv=new ImageView(this);
     * iv.setImageBitmap(bitmap);
     *
     * 這樣我們就可以讀取較大的圖片而不會記憶體溢出了。
     * 如果你想把壓縮後的圖片保存在Sdcard上的話就很簡單了：
     * File file=new File("/sdcard/feng.png");
     * try { FileOutputStream out=newFileOutputStream(file);
     *      if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)){
     *          out.flush();
     *          out.close();
     *          }
     * } catch (FileNotFoundException e){
     *      // TODO Auto-generated catchblock
     *      e.printStackTrace();
     * } catch (IOException e) {
     *      // TODO Auto-generated catchblock
     *      e.printStackTrace(); }
     *      */



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int)event.getX();
                downY = (int)event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                upX = (int)event.getX();
                upY = (int)event.getY();

                int[] pos = new int[2];
                calendar.getLocationOnScreen(pos);

                if (areaList.size()==0) {
                    generateList();
                }

                // make sure it's not move
                if (Math.abs(downX-upX)<3 && Math.abs(downY-upY)<3) {
                    ItemArea area = getItemArea(upX, upY);

                    // on application start up and click the current date, there are stored status.
                    if (currentArea==null || !isInitialized) {
                        Log.d("TAG", "currentArea==null || !isInitialized" + isInitialized);
                        long time = calendar.getDate();
                        Calendar currentCalendar = new GregorianCalendar();
                        currentCalendar.setTimeInMillis(time);
                        year = currentCalendar.get(Calendar.YEAR);
                        month = currentCalendar.get(Calendar.MONTH);
                        day = currentCalendar.get(Calendar.DAY_OF_MONTH);
                        if(pos[0] == 0 && listView.getChildAt(0) != null) intoDateList();


                    }

                    if (area!=null && area.equals(currentArea)) {
                        Log.d("TAG", "area!=null && area.equals(currentArea)");
                        if(pos[0] == 0 && listView.getChildAt(0) != null) intoDateList();

                    }
                } else {
                    // FIXME: still have bug when drag/scroll back
                    // it's move event, list view will scroll up/down, and update the y
                    if (currentArea!=null) {
                        if (downY<upY) {
                            // move down
                            int times = (upY-downY)/listItemHeight;
                            currentArea.top+=listItemHeight*(times+1);
                            currentArea.bottom+=listItemHeight*(times+1);
                        } else {
                            // move up
                            int times = (downY-upY)/listItemHeight;
                            currentArea.top-=listItemHeight*(times+1);
                            currentArea.bottom-=listItemHeight*(times+1);
                        }
                    }
                }
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void generateList() {
        if(listView.getChildAt(0) != null) {
            listItemCount = listView.getChildCount();
            listItemHeight = listView.getChildAt(0).getHeight();
            listItemWidth = listView.getChildAt(0).getWidth();
            listView.getChildAt(0).getLocationOnScreen(startPoint);

            int deltaX = (int) (listItemWidth / 7.0);

            for (int i = 0; i < listItemCount; i++) {
                for (int j = 0; j < 7; j++) {
                    areaList.add(new ItemArea(startPoint[0] + deltaX * j, startPoint[1] + listItemHeight * i,
                            startPoint[0] + deltaX * (j + 1), startPoint[1] + listItemHeight * (i + 1)));
                }

            }
        }
    }

    private void intoDateList() {

        Log.d("TAG", "do your job here");
        String Smonth,SdayOfMonth;
        if(month<10){
            Smonth = "0"+(month + 1);
            /**月份記得+1，因為月份是從0開始算*/
        }
        else{
            Smonth = String.valueOf(month + 1);
        }
        if(day<10){
            SdayOfMonth = "0"+ day;
        }
        else{
            SdayOfMonth = String.valueOf(day);
        }

        if(checkDate == null) {
            checkDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        }

        String findDate = String.valueOf(year) + Smonth + SdayOfMonth;
        Intent intent = new Intent();
        intent.setClass(this, DateListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putString("findDate", findDate);
        intent.putExtras(bundle);
        startActivity(intent);
        if(!finish){
           new DateListActivity().finish();
        }


        checkDate = findDate;


        lastX = upX;
        lastY = upY;

    }

    private ItemArea getItemArea(int x, int y) {
        for (int i=0; i < areaList.size(); i++) {
            if (areaList.get(i).contains(x, y)) {
                return areaList.get(i);
            }
        }
        return null;
    }




    private class ItemArea {
        int left;
        int top;
        int right;
        int bottom;

        ItemArea(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        boolean contains(int x, int y) {
            return x>=left && x<=right && y>=top && y<=bottom;
        }

        boolean equals(ItemArea area) {
            return area!=null &&
                    this.right==area.right &&
                    this.left==area.left &&
                    this.bottom==area.bottom &&
                    this.top==area.top;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        /**添加項目進選單，假如存在該項目的話*/
        getMenuInflater().inflate(R.menu.menu_charge, menu);
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
    protected void onResume() {
        super.onResume();
        handler.post(runnable);
    }

    private Runnable runnable = new Runnable() {
        public void run() {
        //做操作
            handler.sendEmptyMessage(1);
        }

    };
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 1:
            getBarChart();
            finish = false;
            break;
            }
        };
    };
}
