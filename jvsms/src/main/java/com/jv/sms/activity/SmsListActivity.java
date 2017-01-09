package com.jv.sms.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jv.sms.R;
import com.jv.sms.app.JvApplication;
import com.jv.sms.base.BaseActivity;
import com.jv.sms.fragment.SmsListFragment;
import com.jv.sms.interfaces.ToolbarSetListener;
import com.jv.sms.utils.KeyboardUtils;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import butterknife.BindView;

public class SmsListActivity extends BaseActivity implements ToolbarSetListener,
        EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb_loadBar)
    ProgressBar pbLoadBar;


    private Fragment mFragment;

    @Override
    public int getContentViewId() {
        return R.layout.activity_sms_list;
    }

    @Override
    protected void setThemes() {
        setTheme(JvApplication.themes[JvApplication.themeId]);
    }


    @Override
    protected void initAllView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        pbLoadBar.setVisibility(View.VISIBLE);
        mFragment = new SmsListFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, mFragment).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideSoftInput(SmsListActivity.this);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sms_list_menu, menu);
        return true;
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public int getToolbarHeight() {
        return toolbar.getMeasuredHeight();
    }

    @Override
    public void showProgressBar() {
        pbLoadBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        pbLoadBar.setVisibility(View.GONE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mFragment != null && mFragment instanceof SmsListFragment) {
            if (!((SmsListFragment) mFragment).onKeyDown(keyCode, event)) {
                finish();
            } else {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        if (mFragment != null && mFragment instanceof SmsListFragment) {
            EmojiconsFragment.input(((SmsListFragment) mFragment).etSmsContent, emojicon);
        }
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        if (mFragment != null && mFragment instanceof SmsListFragment) {
            EmojiconsFragment.backspace(((SmsListFragment) mFragment).etSmsContent);
        }

    }
}
