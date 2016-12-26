package com.jv.sms.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.activity.SmsListActivity;
import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.LinkmanBean;
import com.jv.sms.bean.SmsBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 64118 on 2016/12/26.
 */

public class AutoLinkmanAdapter extends ArrayAdapter implements Filterable {

    private List<LinkmanBean> linkmanList;
    private List<LinkmanBean> linkmanLists;
    private Context context;
    private int layoutId;

    public AutoLinkmanAdapter(Context context, int resource, List<LinkmanBean> linkmanList) {
        super(context, resource);
        this.context = context;
        this.layoutId = resource;
        this.linkmanList = linkmanList;
        linkmanLists = linkmanList;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return linkmanList == null ? null : linkmanList.get(position);
    }

    @Override
    public int getCount() {
        return linkmanList == null ? 0 : linkmanList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setItem(linkmanList.get(position));
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new AutoFilter();
    }

    private class ViewHolder {
        //        @BindView(R.id.tv_item_text)
        TextView tvItemText;
        //        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        //        @BindView(R.id.tv_item_phoneNumber)
        TextView tvItemPhoneNumber;
        //        @BindView(R.id.fl_item_layout)
        FrameLayout flItemLayout;

        public ViewHolder(View itemView) {
//            ButterKnife.bind(this, itemView);
            tvItemText = (TextView) itemView.findViewById(R.id.tv_item_text);
            tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            tvItemPhoneNumber = (TextView) itemView.findViewById(R.id.tv_item_phoneNumber);
            flItemLayout = (FrameLayout) itemView.findViewById(R.id.fl_item_layout);
        }

        public void setItem(LinkmanBean bean) {
            tvItemText.setText(bean.getName().substring(0, 1));
            tvItemName.setText(bean.getName());
            tvItemPhoneNumber.setText(bean.getPhoneNumber());
            flItemLayout.setBackgroundResource(R.drawable.shape_icon_bg);
            GradientDrawable grad = (GradientDrawable) flItemLayout.getBackground();
            grad.setColor(ContextCompat.getColor(context, JvApplication.icon_theme_colors[bean.getColorType()]));
        }
    }

    private class AutoFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                constraint = "";
            }
            //过滤操作
            String condition = String.valueOf(constraint).toLowerCase();
            List<LinkmanBean> temp = new ArrayList<>();
            for (LinkmanBean linkmanBean : linkmanLists) {
                if (linkmanBean.getName().toLowerCase().contains(condition)) {
                    temp.add(linkmanBean);
                }
                if (linkmanBean.getPhoneNumber().toLowerCase().contains(condition)) {
                    temp.add(linkmanBean);
                }
            }
            results.values = temp;
            results.count = temp.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            linkmanList = (List<LinkmanBean>) results.values;
            notifyDataSetChanged();
        }
    }

    public void startSmsList(LinkmanBean bean) {
        SmsBean smsBean = new SmsBean();
        smsBean.setName(bean.getName());
        smsBean.setColorPosition(bean.getColorType());
        smsBean.setPhoneNumber(bean.getPhoneNumber());
        smsBean.setThread_id(bean.getThread_id());
        Intent intent = new Intent(context, SmsListActivity.class);
        intent.putExtra("bean", smsBean);
        JvApplication.themeId = bean.getColorType();
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public void startSmsListNew(String str) {
        if (Pattern.compile("[0-9]*").matcher(str).matches()) {
            SmsBean smsBean = new SmsBean();
            smsBean.setName(str);
            smsBean.setPhoneNumber(str);
            smsBean.setThread_id("-1");
            smsBean.setColorPosition(Integer.parseInt(smsBean.getPhoneNumber().substring(smsBean.getPhoneNumber().length() - 1)) == 9 ? 0 : Integer.parseInt(smsBean.getPhoneNumber().substring(smsBean.getPhoneNumber().length() - 1)));
            Intent intent = new Intent(context, SmsListActivity.class);
            intent.putExtra("bean", smsBean);
            JvApplication.themeId = smsBean.getColorPosition();
            context.startActivity(intent);
            ((Activity) context).finish();
        } else {
            Toast.makeText(context, "非联系人请直接输入号码", Toast.LENGTH_SHORT).show();
        }
    }


}
