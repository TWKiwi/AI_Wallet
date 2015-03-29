package kiwi.ai_wallet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ant.liao.*;


public class MenuActivity extends OptionActivity {

    GifView gif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        /**螢幕不隨手機旋轉*/
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

//        initUI();





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

        ImageView Charge = (ImageView)findViewById(R.id.ChargeTextView);
        Charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ChargeIntent = new Intent(MenuActivity.this,ChargeActivity.class);
                startActivity(ChargeIntent);
            }
        });

        ImageView Smartbutler = (ImageView)findViewById(R.id.SmartbutlerView);
        Smartbutler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Smartbutler = new Intent(MenuActivity.this,SmartbutlerActivity.class);
                startActivity(Smartbutler);
            }
        });

        ImageView Option = (ImageView)findViewById(R.id.OptionView);
        Option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Option = new Intent(MenuActivity.this,OptionActivity.class);
                startActivity(Option);
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
