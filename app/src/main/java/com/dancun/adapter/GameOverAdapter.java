package com.dancun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dancun.activity.R;
import java.util.List;
import java.util.Map;

public class GameOverAdapter extends BaseAdapter {
    private List<Map<String,String>> list;
    private Context mContext;
    public GameOverAdapter(Context mContext,List<Map<String,String>> list){
        this.list=list;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh ;
        if(convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.game_over_item,null);
            vh.tv_keywords=convertView.findViewById(R.id.tv_keywords);
            vh.tv_type=convertView.findViewById(R.id.tv_type);
            vh.tv_words=convertView.findViewById(R.id.tv_words);
            convertView.setTag(vh);
        }else
            vh= (ViewHolder) convertView.getTag();
        Map<String,String> map = (Map<String, String>) getItem(position);
        if(!Boolean.parseBoolean(map.get("type"))){
            vh.tv_keywords.setText(map.get("rightkey"));
        }
        vh.tv_type.setText(map.get("type"));
        vh.tv_words.setText(map.get("keyword"));
        return convertView;
    }
    class ViewHolder{
        public TextView tv_words,tv_type,tv_keywords;
    }
}
