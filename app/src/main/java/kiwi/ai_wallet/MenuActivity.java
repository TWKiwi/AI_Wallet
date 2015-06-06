package kiwi.ai_wallet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.*;

import java.util.Calendar;


public class MenuActivity extends ActionBarActivity implements View.OnClickListener{


    private SharedPreferences optionSpr;
    private int Budget;//預算屬性
    private int RegularCost;//預算屬性
    protected int ScaleTS,FoodRiceRank,FoodNoodleRank;

//    protected SharedPreferences.Editor optSprEdt;
    protected PendingIntent pendingIntent;
    protected GifView gif;
    public static boolean timeTick;
    protected CountDownTimer gifCountDownTimer;


    private Button FoodBtn,ClothBtn,LiveBtn,WalkBtn,EduBtn,FunBtn,OtherBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setGif();

        if(timeTick){
            setContentView(R.layout.menu_view);
            toMenuUI();
        }else if(!timeTick) {
            timeTick = true;
            Log.d("Timer","gifRunner");
            gifRunner();
        }

        alarmManager();
//        Log.d("Menu",String.valueOf(Budget)+ "/" + String.valueOf(RegularCost));

    }

    private void initView(){
        FoodBtn = (Button)findViewById(R.id.FoodBtn);
        ClothBtn = (Button)findViewById(R.id.ClothBtn);
        LiveBtn = (Button)findViewById(R.id.LiveBtn);
        WalkBtn = (Button)findViewById(R.id.WalkBtn);
        EduBtn = (Button)findViewById(R.id.EduBtn);
        FunBtn = (Button)findViewById(R.id.FunBtn);
        OtherBtn = (Button)findViewById(R.id.OtherBtn);

        setListener();
    }

    private void setListener(){
        FoodBtn.setOnClickListener(this);
        ClothBtn.setOnClickListener(this);
        LiveBtn.setOnClickListener(this);
        WalkBtn.setOnClickListener(this);
        EduBtn.setOnClickListener(this);
        FunBtn.setOnClickListener(this);
        OtherBtn.setOnClickListener(this);
    }

    private void gifRunner(){
        gifCountDownTimer = new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("Timer","onTick");
            }

            public void onFinish() {
                Log.d("Timer","onFinish");
                gifCountDownTimer.cancel();
                setContentView(R.layout.menu_view);
                toMenuUI();
//                setContentView(R.layout.choose_search);
//                initView();
            }
        }.start();
    }



    void alarmManager(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.MONTH, 4);
//        calendar.set(Calendar.YEAR, 2015);
//        calendar.set(Calendar.DAY_OF_MONTH, 14);

        calendar.set(Calendar.HOUR_OF_DAY, 18);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.AM_PM,Calendar.PM);

        Intent myIntent = new Intent(MenuActivity.this, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MenuActivity.this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        /**
         * type：表示警报类型，一般可以取的值是AlarmManager.RTC和AlarmManager.RTC_WAKEUP。如果将type参数值设为AlarmManager.RTC，表示是一个正常的定时器，
         * 如果将type参数值设为AlarmManager.RTC_WAKEUP，除了有定时器的功能外，还会发出警报声（例如，响铃、震动）。

         * triggerAtTime：第1次运行时要等待的时间，也就是执行延迟时间，单位是毫秒。

         * interval：表示执行的时间间隔，单位是毫秒。

         * operation：一个PendingIntent对象，表示到时间后要执行的操作。PendingIntent与Intent类似，可以封装Activity、BroadcastReceiver和Service。但与Intent不同的是，PendingIntent可以脱离应用程序而存在。

         */
    }

    public void setScaleTextStyle(int i){
//        optionSpr = getSharedPreferences("Option",0);

        SharedPreferences.Editor optSprEdt = optionSpr.edit();

        optSprEdt.putInt("ScaleTS", i).commit();

    }

    public void setBudget(int i){
//        optionSpr = getSharedPreferences("Option",0);

        SharedPreferences.Editor optSprEdt = optionSpr.edit();

        optSprEdt.putInt("Budget", i).commit();

    }

    public void setRegularCost(int i){
//        optionSpr = getSharedPreferences("Option", 0);

        SharedPreferences.Editor optSprEdt = optionSpr.edit();

        optSprEdt.putInt("RglCost",i).commit();

    }

    public void setFoodRiceRank(int i){
//        optionSpr = getSharedPreferences("Option", 0);

        SharedPreferences.Editor optSprEdt = optionSpr.edit();

        optSprEdt.putInt("rice",i).commit();

    }

    public void setFoodNoodleRank(int i){
//        optionSpr = getSharedPreferences("Option", 0);

        SharedPreferences.Editor optSprEdt = optionSpr.edit();

        optSprEdt.putInt("noodles",i).commit();

    }

    public String getBudget(String s){
//        optionSpr = getSharedPreferences("Option", 0);

        switch (s){
            case "Budget" : Budget = optionSpr.getInt("Budget", 18000);
                            s = String.valueOf(Budget);
                            break;
            case "RglCost" : RegularCost = optionSpr.getInt("RglCost",0);
                            s = String.valueOf(RegularCost);
                            break;
            case "ScaleTS" : ScaleTS = optionSpr.getInt("ScaleTS",1);
                            s = String.valueOf(ScaleTS);
                            break;
            case "FoodRice" : FoodRiceRank = optionSpr.getInt("rice",0);
                              s = String.valueOf(FoodRiceRank);
                              break;
            case "FoodNoodle" : FoodNoodleRank = optionSpr.getInt("noodles",0);
                              s = String.valueOf(FoodNoodleRank);
                              break;

        }

//        Log.d("取得設置",String.valueOf(Budget)+ "/" + String.valueOf(RegularCost) + "/" +String.valueOf(ScaleTS));
        return s;
    }



    /**
     * 轉換到記帳UI*/
    public void toMenuUI(){


        try {
            TextView Charge = (TextView) findViewById(R.id.ChargeTextView);
            Charge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ChargeIntent = new Intent(MenuActivity.this, ChargeActivity.class);
                    startActivity(ChargeIntent);
                }
            });

            TextView Smartbutler = (TextView) findViewById(R.id.SmartbutlerView);
            Smartbutler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent Smartbutler = new Intent(MenuActivity.this, SmartbutlerActivity.class);
                    startActivity(Smartbutler);

                }
            });
        }catch(Exception e){
            Intent intent = new Intent(this,OptionActivity.class);
            startActivity(intent);
            Toast.makeText(this,"預算金額有問題，請確認金額設定無誤",Toast.LENGTH_SHORT).show();
        }

    }
/**桌面捷徑有問題*/
//    private void addShortcut() {
//        Intent shortcutIntent = new Intent(this,
//                StartActivity.class); // 啟動捷徑入口，一般用MainActivity，有使用其他入口則填入相對名稱，ex:有使用SplashScreen
//        shortcutIntent.setAction(Intent.ACTION_MAIN);
//        Intent addIntent = new Intent();
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent); // shortcutIntent送入
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
//                getString(R.string.app_name)); // 捷徑app名稱
//        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
//                Intent.ShortcutIconResource.fromContext(
//                        getApplicationContext(),// 捷徑app圖
//                        R.drawable.ic_launcher));
//        addIntent.putExtra("duplicate", false); // 只創建一次
//        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT"); // 安裝
//        sendBroadcast(addIntent); // 送出廣播
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void setGif(){
        optionSpr = getSharedPreferences("Option",0);
        gif = (GifView) findViewById(R.id.gifview01);
        gif.setGifImage(R.drawable.small_blue);
        gif.setGifImageType(GifView.GifImageType.COVER);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.FoodBtn :
                intent = new Intent(this,FoodActivity.class);
                startActivity(intent);
                break;
            case R.id.ClothBtn :
                intent = new Intent(this,MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.LiveBtn :
                intent = new Intent(this,MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.WalkBtn :
                intent = new Intent(this,MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.EduBtn :
                intent = new Intent(this,MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.FunBtn :
                intent = new Intent(this,MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.OtherBtn :
                intent = new Intent(this,MenuActivity.class);
                startActivity(intent);
                break;
        }
    }
}
