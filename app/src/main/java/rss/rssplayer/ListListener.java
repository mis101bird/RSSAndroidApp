package rss.rssplayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by J on 2015/4/15.
 */
public class ListListener implements AdapterView.OnItemClickListener {

    Activity activity;
    ArrayList<HashMap<String,String>> map; //HashMap<String,String>���C��ListView Item��Data

    public ListListener( ArrayList<HashMap<String,String>> map, Activity activity) { //�ۻs�غc�l
        this.map = map;
        this.activity = activity;

    }

    public void onItemClick(AdapterView<?> parent, View view, int pos, long is) { // pos ��Item��������m�A�����Item
        Intent i = new Intent(Intent.ACTION_VIEW); // Bundle�ΨӶǸ�Ƶ�Activity(�]), Intent�Ψӳs���}�ҤU�@�ӭ���(Activity)
        if(map.get(pos).get("link").substring(0,9).equals("<![CDATA[")) {
            i.setData(Uri.parse(map.get(pos).get("link").substring(9,map.get(pos).get("link").length()-3)));
        }
        else
        {
            i.setData(Uri.parse(map.get(pos).get("link")));

        }
        activity.startActivity((i)); //start intent to Activity

    }
}
