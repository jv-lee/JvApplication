package com.jv.sms.ui.newsms;

import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.base.mvp.BaseFragment;
import com.jv.sms.constant.Constant;
import com.jv.sms.entity.ContactsBean;
import com.jv.sms.entity.LinkmanBean;
import com.jv.sms.entity.SmsBean;
import com.jv.sms.rx.EventBase;
import com.jv.sms.ui.newsms.adapter.AutoLinkmanAdapter;
import com.jv.sms.ui.newsms.adapter.NewSmsAdapter;
import com.jv.sms.utils.KeyboardUtil;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnEditorAction;
import rx.Observable;

/**
 * Created by Administrator on 2017/5/3.
 */

public class NewSmsFragment extends BaseFragment<NewSmsContract.Presenter> implements NewSmsContract.View{

    @BindView(R.id.et_inputTel)
    AppCompatAutoCompleteTextView etInputTel;
    @BindView(R.id.cb_keyBoard)
    CheckBox cbKeyBoard;
    @BindView(R.id.rv_container)
    RecyclerView rvContainer;
    @BindView(R.id.ll_rootLayout)
    LinearLayout llRootLayout;


    private List<ContactsBean> contactsList;
    private NewSmsAdapter newSmsAdapter;

    private List<LinkmanBean> linkmanList;
    private AutoLinkmanAdapter linkmanAdapter;

    private LinkmanBean bean;

    @Override
    protected void onCreate() {
    }

    @Override
    public void onResume() {
        super.onResume();
        etInputTel.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        KeyboardUtil.showSoftInput2(mActivity, etInputTel);
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyboardUtil.hideSoftInput(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Constant.text = "";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected View bindRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newsms,null,false);
    }

    @Override
    protected void bindData(Bundle savedInstanceState) {
        rvContainer.setLayoutManager(new LinearLayoutManager(mActivity));
        mPresenter.findContactsAll();
        mPresenter.findLinkmanAll();
        etInputTel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = (LinkmanBean) parent.getItemAtPosition(position);
                etInputTel.setText(bean.getName());
                etInputTel.setSelection(bean.getName().length());
                linkmanAdapter.startSmsList(bean);
            }
        });
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return null;
    }

    @Override
    protected void rxEvent() {

    }

    @OnCheckedChanged(R.id.cb_keyBoard)
    public void onChecked(CompoundButton button, boolean isChecked) {
        if (isChecked) {
            etInputTel.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        } else {
            etInputTel.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        }
        KeyboardUtil.hideSoftInput(getActivity());
        KeyboardUtil.showSoftInput2(mActivity, etInputTel);
    }

    @OnEditorAction(R.id.et_inputTel)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean isOK = true;
        switch (actionId) {
            case EditorInfo.IME_ACTION_NONE:
                Toast.makeText(mActivity, "点击-->NONE", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_GO:
                if (Pattern.compile("[0-9]*").matcher(etInputTel.getText().toString()).matches()) {
                    mPresenter.findLinkmanByPhoneNumber(etInputTel.getText().toString());
                } else {
                    Toast.makeText(mActivity, mActivity.getString(R.string.not_linkman), Toast.LENGTH_SHORT).show();
                }
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                Toast.makeText(mActivity, "点击-->SEARCH", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_SEND:
                Toast.makeText(mActivity, "点击-->SEND", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_NEXT:
                Toast.makeText(mActivity, "点击-->NEXT", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_DONE:
                Toast.makeText(mActivity, "点击-->OK", Toast.LENGTH_SHORT).show();
                break;
            default:
                isOK = false;
                break;
        }
        return true;
    }

    @Override
    public void setContactsAll(List<ContactsBean> list) {
        if (contactsList == null) {
            contactsList = list;
            newSmsAdapter = new NewSmsAdapter(mActivity, contactsList);
            rvContainer.setAdapter(newSmsAdapter);
        } else {
            contactsList.clear();
            contactsList = list;
            newSmsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void findContactsAllError() {
        Toast.makeText(mActivity, mActivity.getString(R.string.load_linkman_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLinkmanAll(List<LinkmanBean> list) {
        if (linkmanList == null) {
            linkmanList = list;
            linkmanAdapter = new AutoLinkmanAdapter(mActivity, R.layout.item_auto_text, list);
            etInputTel.setAdapter(linkmanAdapter);
        } else {
            linkmanList.clear();
            linkmanList = list;
            linkmanAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void findLinkmanAllError() {
        Toast.makeText(mActivity, mActivity.getString(R.string.load_input_linkman_list_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startListSmsBySms(SmsBean smsBean) {
        linkmanAdapter.startSmsList(smsBean);
    }
}
