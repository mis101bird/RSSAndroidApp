package rss.rssplayer;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    private Button button01;
    private ListView listView;
    private EditText text01;
    ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    private SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //bind layout and Activity
        button01 = (Button)findViewById(R.id.button);
        text01 = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.listView);
        button01.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
               sendMessage(v);
            }

        });

        adapter = new SimpleAdapter(
                this,
                list,
                R.layout.myviewlist,
                new String[] { "title","link","description","category","pubDate" },
                new int[] { R.id.textView7, R.id.textView6, R.id.textView3,R.id.textView4,R.id.textView5 } );

        // Android4.0 以上規定抓網址要用Thread或加以下
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        /**************************************/
    }
    public void sendMessage(View view) {
        Toast.makeText(this, "HI~", Toast.LENGTH_SHORT).show();
        int port = 9000;
        try {
            if(text01.getText().toString()!="") {
                URL url = new URL("http://140.121.197.137:8080/RSServlet/RSS?search="+text01.getText().toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); //Use http

                InputStreamReader r = new InputStreamReader(urlConnection.getInputStream()); //轉接器
                java.io.BufferedReader reader = new java.io.BufferedReader(r);
                String s = reader.readLine();

                JSONArray obj = new JSONArray(s);// [ {...}, {...}, {...} .... ]
                for (int i = 0; i < obj.length(); i++) {

                    HashMap<String, String> item = new HashMap<String, String>(); //裝每個ListView item的data

                    item.put("title", obj.getJSONObject(i).getString("title"));
                    item.put("link",obj.getJSONObject(i).getString("link"));
                    item.put( "description","description: "+obj.getJSONObject(i).getString("description") );
                    item.put( "category","category: "+obj.getJSONObject(i).getString("category") );
                    item.put("pubDate", "pubDate: " + obj.getJSONObject(i).getString("pubDate"));

                    list.add(item);

                }

                listView.setAdapter(adapter); //將數據轉成ListView能接受的Item格式並inflate完成
                listView.setItemsCanFocus(false); //?取的焦?目???了listview里的item
                listView.setOnItemClickListener(new ListListener(list, this)); //設ListView的每個Item listener ( List為Data集合, this 為ListView所在的Activity)

            }

        }catch(Exception e){
            Toast.makeText(this,e.toString() , Toast.LENGTH_SHORT).show();
            }

    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) { // menu 新版已很少用
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // menu 新版已很少用
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
