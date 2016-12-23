package com.jv.sms.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.adapter.NewSmsAdapter;
import com.jv.sms.adapter.SmsListDataAdapter;
import com.jv.sms.base.BaseFragment;
import com.jv.sms.base.EventBase;
import com.jv.sms.bean.ContactsBean;
import com.jv.sms.mvp.model.NewSmsModel;
import com.jv.sms.mvp.presenter.INewSmsPresenter;
import com.jv.sms.mvp.presenter.NewSmsPresenter;
import com.jv.sms.mvp.view.INewSmsView;
import com.jv.sms.utils.KeyboardUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnEditorAction;
import rx.Observable;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewSmsFragment extends BaseFragment implements INewSmsView {


    @BindView(R.id.et_inputTel)
    AppCompatAutoCompleteTextView etInputTel;
    @BindView(R.id.cb_keyBoard)
    CheckBox cbKeyBoard;
    @BindView(R.id.rv_new_container)
    RecyclerView rvNewContainer;

    private List<ContactsBean> mList;
    private NewSmsAdapter mAdapter;

    private INewSmsPresenter mPresenter;

    String[] array = {"李佳薇", "张三", "李四", "王五", "lis", "132"};

    @Override
    public int getContentViewId() {
        return R.layout.fragment_new_sms;
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new NewSmsPresenter(this);
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {
        rvNewContainer.setLayoutManager(new LinearLayoutManager(mContext));
        mPresenter.findContactsAll();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, array);
        etInputTel.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    protected void rxEvent() {

    }

    @Override
    public void onResume() {
        super.onResume();
        etInputTel.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        KeyboardUtils.showSoftInput2(mContext, etInputTel);
    }

    @Override
    public void onPause() {
        super.onPause();
        KeyboardUtils.hideSoftInput(getActivity());
    }

    @OnCheckedChanged(R.id.cb_keyBoard)
    public void onChecked(CompoundButton button, boolean isChecked) {
        if (isChecked) {
            etInputTel.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        } else {
            etInputTel.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        }
        KeyboardUtils.hideSoftInput(getActivity());
        KeyboardUtils.showSoftInput2(mContext, etInputTel);
    }

    @OnEditorAction(R.id.et_inputTel)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean isOK = true;
        switch (actionId) {
            case EditorInfo.IME_ACTION_NONE:
                Toast.makeText(mContext, "点击-->NONE", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_GO:
                Toast.makeText(mContext, "点击-->GO", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                Toast.makeText(mContext, "点击-->SEARCH", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_SEND:
                Toast.makeText(mContext, "点击-->SEND", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_NEXT:
                Toast.makeText(mContext, "点击-->NEXT", Toast.LENGTH_SHORT).show();
                break;
            case EditorInfo.IME_ACTION_DONE:
                Toast.makeText(mContext, "点击-->OK", Toast.LENGTH_SHORT).show();
                new NewSmsModel().findContactsAll();
                break;
            default:
                isOK = false;
                break;
        }
        return true;
    }

    @Override
    public void setContactsAll(List<ContactsBean> list) {
        if (mList == null) {
            mList = list;
            mAdapter = new NewSmsAdapter(mContext, mList);
            rvNewContainer.setAdapter(mAdapter);
        } else {
            mList.clear();
            mList = list;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void findContactsAllError() {
        Toast.makeText(mContext, "加载联系人失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
