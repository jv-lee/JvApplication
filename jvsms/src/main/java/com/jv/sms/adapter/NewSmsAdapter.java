package com.jv.sms.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jv.sms.R;
import com.jv.sms.activity.SmsListActivity;
import com.jv.sms.app.JvApplication;
import com.jv.sms.bean.ContactsBean;
import com.jv.sms.bean.LinkmanBean;
import com.jv.sms.bean.SmsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/23.
 */

public class NewSmsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ContactsBean> contactsList;
    private Context mContext;

    private final int WORD = 0x001;
    private final int LINKMAIN = 0x002;

    public NewSmsAdapter(Context mContext, List<ContactsBean> contactsList) {
        this.mContext = mContext;
        this.contactsList = contactsList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == WORD) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_word, parent, false);
            return new WorkViewHolder(view);
        } else if (viewType == LINKMAIN) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_linkman, parent, false);
            return new LinkmanViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int count = -1;
        for (int i = 0; i < contactsList.size(); i++) {
            if(contactsList.get(i) == null)break;
            count++;
            if (position == count) {
                ((WorkViewHolder) holder).bindItemDate(contactsList.get(i).getWork());
            } else {
                List<LinkmanBean> linkmanList = contactsList.get(i).getLinkmanList();
                for (int j = 0; j < linkmanList.size(); j++) {
                    count++;
                    if (position == count) {
                        ((LinkmanViewHolder) holder).bindItemDate(linkmanList.get(j));
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = contactsList.size();
        for (ContactsBean bean : contactsList) {
            if (bean != null) {
                count += bean.getLinkmanList().size();
            }
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        int count = -1;
        for (int i = 0; i < contactsList.size(); i++) {
            count++;
            if (position == count) {
                return WORD;
            }

            List<LinkmanBean> linkmanList = contactsList.get(i).getLinkmanList();
            for (int j = 0; j < linkmanList.size(); j++) {
                count++;
                if (position == count) {
                    return LINKMAIN;
                }
            }
        }
        return super.getItemViewType(position);
    }

    class WorkViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_word)
        TextView tvItemWord;

        public WorkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItemDate(String work) {
            tvItemWord.setText(work);
        }

    }

    class LinkmanViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_item_text)
        TextView tvItemText;
        @BindView(R.id.iv_item_icon)
        ImageView ivItemIcon;
        @BindView(R.id.fl_item_layout)
        FrameLayout flItemLayout;
        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        @BindView(R.id.tv_item_phoneNumber)
        TextView tvItemPhoneNumber;
        @BindView(R.id.ll_item)
        LinearLayout ll_item;

        public LinkmanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItemDate(final LinkmanBean bean) {
            tvItemName.setText(bean.getName());
            tvItemPhoneNumber.setText(bean.getPhoneNumber());
            tvItemText.setText(bean.getName().substring(0, 1));

            //设置背景颜色
            flItemLayout.setBackgroundResource(R.drawable.shape_icon_bg);
            GradientDrawable grad = (GradientDrawable) flItemLayout.getBackground();
            grad.setColor(ContextCompat.getColor(mContext, JvApplication.icon_theme_colors[bean.getColorType()]));

            ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SmsBean smsBean = new SmsBean();
                    smsBean.setName(bean.getName());
                    smsBean.setColorPosition(bean.getColorType());
                    smsBean.setPhoneNumber(bean.getPhoneNumber());
                    smsBean.setThread_id(bean.getThread_id());
                    Intent intent = new Intent(mContext, SmsListActivity.class);
                    intent.putExtra("bean", smsBean);
                    JvApplication.themeId = bean.getColorType();
                    mContext.startActivity(intent);
                    ((Activity) mContext).finish();
                }
            });

        }

    }

}
