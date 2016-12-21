package com.jv.sms.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.base.BaseFragment;
import com.jv.sms.base.EventBase;
import com.jv.sms.utils.KeyboardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnEditorAction;
import rx.Observable;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewSmsFragment extends BaseFragment {


    @BindView(R.id.et_inputTel)
    EditText etInputTel;
    @BindView(R.id.cb_keyBoard)
    CheckBox cbKeyBoard;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_new_sms;
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return null;
    }

    @Override
    protected void initAllView(Bundle savedInstanceState) {


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
                break;
            default:
                isOK = false;
                break;
        }
        return true;
    }

}
