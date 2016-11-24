package demo.refresh.viewgroup.com.viewgrouppulltorefresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import demo.refresh.viewgroup.com.viewgrouppulltorefresh.adapter.PullToRefreshAdapter;
import demo.refresh.viewgroup.com.viewgrouppulltorefresh.adapter.RecyerViewAdapter;
import demo.refresh.viewgroup.com.viewgrouppulltorefresh.inf.RefreshCompleteListen;
import demo.refresh.viewgroup.com.viewgrouppulltorefresh.widget.PullToRefresh;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private RecyclerView recyclerView;
    private GridView gridView;
    private PullToRefreshAdapter adapter;
    private RecyerViewAdapter recyerViewAdapter;
    private List<String> list = new ArrayList<>();
    private PullToRefresh refresh;
    private List<Boolean> isOpens = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refresh = (PullToRefresh) findViewById(R.id.pulltorefresh);
        refresh.setRefreshListener(new RefreshCompleteListen() {
            @Override
            public void refresh() {
                //这里做请求数据 刷新的操作，刷新完成后调用 ，refresh.refreshOk();   list.add(0,data);  adapter.addAll(list); refresh.refreshOk();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //listview  gridview
                                    list.add(0, "aaa");
                                    adapter.addAll(list);
                                    refresh.refreshOk();
                                    
//                                    //recyclerview
//                                    recyerViewAdapter.add("aaa");
//                                    refresh.refreshOk();
                                    
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        adapter = new PullToRefreshAdapter();
        for (int i = 0; i < 100; i++) {
            list.add(i + "");
            isOpens.add(false);
        }
        
        listView = (ListView) findViewById(R.id.lv);
        listView.setAdapter(adapter);
        adapter.addAll(list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "点击了---" + list.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        
//        gridView = (GridView) findViewById(R.id.lv);
//        gridView.setAdapter(adapter);
//        adapter.addAll(list);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, "点击了---" + list.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });

//        
//        recyclerView= (RecyclerView) findViewById(R.id.lv);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyerViewAdapter = new RecyerViewAdapter(this,list);
//        recyclerView.setAdapter(recyerViewAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("--------activity", "qqqq");
        return super.onTouchEvent(event);
    }
}
