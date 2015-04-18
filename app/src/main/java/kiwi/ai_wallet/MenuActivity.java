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
import android.os.SystemClock;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ant.liao.*;

import java.util.Calendar;


public class MenuActivity extends ActionBarActivity {

    GifView gif;
    public static SharedPreferences option;
    public static int Budget;//預算屬性
    public static boolean checkBoxBoolean;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

//        initUI();

        setOption();
        alarmManager();


      /**點選StartText字串進入選單畫面的版面配置*/
        ImageView StartView = (ImageView) findViewById(R.id.StartText);
        StartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMenuUI();/**切換UI*/
            }
        });
        addShortcut();
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



    void setOption(){
        option = getPreferences(MODE_PRIVATE);

    }

    public void setImage(){

        int iw,ih,vw,vh;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        
    }


//    private void initUI()
//    {
//        gif = (GifView)findViewById(R.id.gifview01);
//        gif.setGifImage(R.drawable.pomo_small);
//
////                gif1.setShowDimension(300, 300);
//    }




//    /**
//     * 轉換到登入UI*/
//    public void toLoginUI(){
//        setContentView(R.layout.login);
//        TextView LoginText = (TextView)findViewById(R.id.LoginText);
//        LoginText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toMenuUI();
//            }
//        });
//    }

    /**
     * 轉換到記帳UI*/
    public void toMenuUI(){
        setContentView(R.layout.menu_view);

        TextView Charge = (TextView)findViewById(R.id.ChargeTextView);
        Charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChargeIntent = new Intent(MenuActivity.this,ChargeActivity.class);
                startActivity(ChargeIntent);
            }
        });

        TextView Smartbutler = (TextView)findViewById(R.id.SmartbutlerView);
        Smartbutler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Smartbutler = new Intent(MenuActivity.this,SmartbutlerActivity.class);
                startActivity(Smartbutler);

            }
        });




    }

    private void addShortcut() {
        Intent shortcutIntent = new Intent(this,
                ChargeActivity.class); // 啟動捷徑入口，一般用MainActivity，有使用其他入口則填入相對名稱，ex:有使用SplashScreen
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent); // shortcutIntent送入
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                getString(R.string.app_name)); // 捷徑app名稱
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(
                        getApplicationContext(),// 捷徑app圖
                        R.drawable.ic_launcher));
        addIntent.putExtra("duplicate", false); // 只創建一次
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT"); // 安裝
        sendBroadcast(addIntent); // 送出廣播
    }

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



}
