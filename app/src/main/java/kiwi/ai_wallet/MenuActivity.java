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
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

//        initUI();

        setOption();
        test();


      /**點選StartText字串進入選單畫面的版面配置*/
        TextView StartText = (TextView) findViewById(R.id.StartText);
        StartText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMenuUI();/**切換UI*/
            }
        });
        addShortcut();
    }

    void test(){
        Calendar calendar = Calendar.getInstance();

//        calendar.set(Calendar.MONTH, 4);
//        calendar.set(Calendar.YEAR, 2015);
//        calendar.set(Calendar.DAY_OF_MONTH, 14);

//        calendar.set(Calendar.HOUR_OF_DAY, 18);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.AM_PM,Calendar.PM);

        Intent myIntent = new Intent(MenuActivity.this, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MenuActivity.this, 0, myIntent,0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 6*60*60*1000, pendingIntent);
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
