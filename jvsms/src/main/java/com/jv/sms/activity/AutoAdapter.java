package com.jv.sms.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.bean.ContactsBean;

import java.util.List;

/**
 * Created by 64118 on 2016/12/25.
 */

public class AutoAdapter extends BaseAdapter {

    private List<ContactsBean> mList;
    private Context mContext;

    public AutoAdapter(Context mContext, List<ContactsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_auto_text, null);
        TextView tvName = (TextView) view.findViewById(R.id.tv_item_name);
        tvName.setText(mList.get(position).getLinkmanList().get(position).getName());

        return null;
    }
}
