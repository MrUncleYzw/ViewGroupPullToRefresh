package demo.refresh.viewgroup.com.viewgrouppulltorefresh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import demo.refresh.viewgroup.com.viewgrouppulltorefresh.inf.RecyclerViewItemClickListener;


/**
 * <p>版权所有：2016-深圳市赛为安全技术服务有限公司</p>
 * <p>项目名称：安全眼</p>
 * <p/>
 * <p>类描述：${todo}(用一句话描述该文件做什么)</p>
 * <p>创建人：余志伟</p>
 * <p>创建时间：2016-03-15 16:35</p>
 * <p>修改人：       </p>
 * <p>修改时间：   </p>
 * <p>修改备注：   </p>
 *
 * @version V3.1
 */
public class RecyerViewAdapter extends RecyclerView.Adapter<RecyerViewAdapter.VH> implements RecyclerViewItemClickListener {
    private List<String> list ;
    private Context context;
    public RecyerViewAdapter(Context context, List<String> list){
        this.list = list;
        this.context = context;
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(View.inflate(context, android.R.layout.simple_list_item_2, null));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.mTextView.setText(list.get(position));
        holder.setPosition(position);
        holder.setListener(this);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void itemClickListener(int position) {
        Toast.makeText(context, "点击了"+position, Toast.LENGTH_SHORT).show();
    }

    public static class VH extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private RecyclerViewItemClickListener listener;
        private int position;
        public VH(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            mTextView.setClickable(true);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClickListener(position);
                }
            });
            mTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.itemClickListener(position);
                    return true;
                }
            });
        }
        public void setListener(RecyclerViewItemClickListener listener){
            this.listener = listener;
        }
        public void setPosition(int position){
            this.position = position;
        }
    }
    public  void add(String item){
        list.add(0,item);
//        notifyDataSetChanged();
        notifyItemInserted(0);
        
    }
}
