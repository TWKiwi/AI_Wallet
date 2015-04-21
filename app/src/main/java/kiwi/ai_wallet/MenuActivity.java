package kiwi.ai_wallet;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ant.liao.*;

import java.util.Calendar;


public class MenuActivity extends ActionBarActivity {


    protected SharedPreferences optionSpr;
    protected int Budget;//預算屬性
    protected int RegularCost;//預算屬性
    protected int ScaleTS;
//    protected SharedPreferences.Editor optSprEdt;
    protected PendingIntent pendingIntent;
    protected GifView gif;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        setGif();
        setImage();
//        setBudget(18000);
//        setRegularCost(0);
        alarmManager();
        Log.d("Menu",String.valueOf(Budget)+ "/" + String.valueOf(RegularCost));

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
        optionSpr = getSharedPreferences("Option",0);
        Log.d("刷新ScaleTS前",getBudget("ScaleTS"));
        SharedPreferences.Editor optSprEdt = optionSpr.edit();
        Log.d("刷新ScaleTS中",getBudget("ScaleTS"));
        optSprEdt.putInt("ScaleTS", i).commit();
        Log.d("刷新ScaleTS後",getBudget("ScaleTS"));
    }

    public void setBudget(int i){
        optionSpr = getSharedPreferences("Option",0);
        Log.d("刷新Budget前",getBudget("Budget") + "/" + getBudget("RglCost"));
        SharedPreferences.Editor optSprEdt = optionSpr.edit();
        Log.d("刷新Budget中",getBudget("Budget") + "/" + getBudget("RglCost"));
        optSprEdt.putInt("Budget", i).commit();
        Log.d("刷新Budget後",getBudget("Budget") + "/" + getBudget("RglCost"));
    }

    public void setRegularCost(int i){
        optionSpr = getSharedPreferences("Option", 0);
        Log.d("刷新RglCost前",getBudget("Budget") + "/" + getBudget("RglCost"));
        SharedPreferences.Editor optSprEdt = optionSpr.edit();
        Log.d("刷新RglCost中",getBudget("Budget") + "/" + getBudget("RglCost"));
        optSprEdt.putInt("RglCost",i).commit();
        Log.d("刷新RglCost後",getBudget("Budget") + "/" + getBudget("RglCost"));
    }

    public String getBudget(String s){
        optionSpr = getSharedPreferences("Option", 0);

        switch (s){
            case "Budget" : Budget = optionSpr.getInt("Budget", 18000);
                            s = String.valueOf(Budget);
                            break;
            case "RglCost" : RegularCost = optionSpr.getInt("RglCost",0);
                            s = String.valueOf(RegularCost);
                            break;
            case "ScaleTS" : ScaleTS = optionSpr.getInt("ScaleTS",1);
                            s = String.valueOf(ScaleTS);
        }

        Log.d("取得設置",String.valueOf(Budget)+ "/" + String.valueOf(RegularCost) + "/" +String.valueOf(ScaleTS));
        return s;
    }



    public void setImage(){

        ImageView StartText = (ImageView)findViewById(R.id.StartText);

        StartText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                toMenuUI();
                return false;
            }
        });

    }


    /**
     * 轉換到記帳UI*/
    public void toMenuUI(){
        setContentView(R.layout.menu_view);

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
        gif = (GifView) findViewById(R.id.gifview01);
        gif.setGifImage(R.drawable.head);
        gif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMenuUI();/**切換UI*/
            }
        });
        gif.setGifImageType(GifView.GifImageType.COVER);
    }

}
