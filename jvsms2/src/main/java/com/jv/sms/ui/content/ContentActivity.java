package com.jv.sms.ui.content;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.jv.sms.R;
import com.jv.sms.base.app.AppComponent;
import com.jv.sms.base.mvp.BaseActivity;
import com.jv.sms.Config;
import com.jv.sms.interfaces.ToolbarSetListener;
import com.jv.sms.rx.EventBase;
import com.jv.sms.ui.content.inject.ContentModule;
import com.jv.sms.ui.content.inject.DaggerContentComponent;
import com.jv.sms.utils.KeyboardUtil;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;


import butterknife.BindView;
import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/4/28.
 */

public class ContentActivity extends BaseActivity implements ToolbarSetListener,
        EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb_loadBar)
    ProgressBar pbLoadBar;

    Fragment mFragment;

    @Override
    protected void setThemes() {
        setTheme(Config.themes[Config.themeId]);
    }

    @Override
    protected int bindRootView() {
        return R.layout.activity_content;
    }

    @Override
    protected void bindData() {
        setSupportActionBar(toolbar);
        pbLoadBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        mFragment = new ContentFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container2, mFragment).commit();
        DaggerContentComponent
                .builder()
                .appComponent(appComponent)
                .contentModule(new ContentModule((ContentContract.View) mFragment))
                .build()
                .inject((ContentFragment) mFragment);
    }

    @Override
    public Observable<EventBase> getRxBus() {
        return null;
    }

    @Override
    protected void rxEvent() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtil.hideSoftInput(ContentActivity.this);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sms_menu, menu);
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
        if (mFragment != null && mFragment instanceof ContentFragment) {
            if (!((ContentFragment) mFragment).onKeyDown(keyCode, event)) {
//                finish();
            } else {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        if (mFragment != null && mFragment instanceof ContentFragment) {
            EmojiconsFragment.input(((ContentFragment) mFragment).etSmsContent, emojicon);
        }
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        if (mFragment != null && mFragment instanceof ContentFragment) {
            EmojiconsFragment.backspace(((ContentFragment) mFragment).etSmsContent);
        }

    }
}
