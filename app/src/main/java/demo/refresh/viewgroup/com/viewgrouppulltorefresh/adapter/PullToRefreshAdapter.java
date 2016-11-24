package demo.refresh.viewgroup.com.viewgrouppulltorefresh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.refresh.viewgroup.com.viewgrouppulltorefresh.R;


/**
 * @author yuzhiwei
 * @version 1.0
 * @Title:SAFEYE@
 * @Description:
 * @date 2016-03-04
 */
public class PullToRefreshAdapter extends BaseAdapter{
    List<String> list;
    @Override
    public int getCount() {
        if(list!=null){
            return list.size();
        }
       return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item,parent,false);
            viewHolder holder = new viewHolder();
            holder.tx = (TextView) convertView.findViewById(R.id.tx);
            convertView.setTag(holder);
        }
        viewHolder h = (viewHolder) convertView.getTag();
        h.tx.setText(list.get(position));
        return convertView;
    }
    private class viewHolder{
        private TextView tx;
    }
    public  List<String> addAll(List<String> datas){
        if(list==null){
            list = new ArrayList<>();
        }
        list.clear();
        list.addAll(datas);
        notifyDataSetChanged();
        return  list;
    }
}
