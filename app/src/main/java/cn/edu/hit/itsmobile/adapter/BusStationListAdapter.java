package cn.edu.hit.itsmobile.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.edu.hit.itsmobile.R;
import cn.edu.hit.itsmobile.ui.BusStationActivity;

import com.baidu.mapapi.search.busline.BusLineResult.BusStation;


public class BusStationListAdapter extends BaseAdapter{
    private int mResource;
    private Context mContext;
    private List<BusStation> mBusStationList;
    private List<String> mEstimateTime;
    
    public BusStationListAdapter(Context context, int resource, List<BusStation> list){
        mContext = context;
        mResource = resource;
        mBusStationList = list;
    }
    
    public BusStationListAdapter(Context context, int resource, List<BusStation> list, List<String> estimateTime){
        this(context, resource, list);
        this.mEstimateTime = estimateTime;
    }
    
    @Override
    public int getCount() {
        return mBusStationList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBusStationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if(convertView == null){
            convertView = (ViewGroup)LayoutInflater.from(mContext).inflate(mResource, parent, false);
            holder.tvIndex = (TextView)convertView.findViewById(R.id.index);
            holder.tvTitle = (TextView)convertView.findViewById(R.id.title);
            holder.tvTime = (TextView)convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.tvIndex.setText((int)(position + 1) + ".");
        holder.tvTitle.setText(mBusStationList.get(position).getTitle());
        if(mEstimateTime != null && mEstimateTime.size() > 0) {
            holder.tvTime.setText(mEstimateTime.get(position));
            holder.tvTime.setVisibility(View.VISIBLE);
        }
        convertView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BusStationActivity.class);
                intent.putExtra("name", ((BusStation)getItem(position)).getTitle());
                intent.putExtra("latitude", ((BusStation)getItem(position)).getLocation().latitude);
                intent.putExtra("longitude", ((BusStation)getItem(position)).getLocation().longitude);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    
    public class ViewHolder{
        TextView tvIndex;
        TextView tvTitle;
        TextView tvTime;
    }
}