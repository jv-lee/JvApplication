package com.jv.sms.ui.content;

import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import com.jv.sms.R;
import com.jv.sms.base.app.AppComponent;
import com.jv.sms.base.mvp.BaseActivity;
import com.jv.sms.constant.Constant;
import com.jv.sms.interfaces.ToolbarSetListener;
import com.jv.sms.rx.EventBase;
import com.jv.sms.ui.content.inject.ContentModule;
import com.jv.sms.ui.content.inject.DaggerContentComponent;
import com.jv.sms.utils.KeyboardUtils;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;



import butterknife.BindView;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/28.
 */

public class ContentActivity extends BaseActivity<ContentContract.Presenter> implements ToolbarSetListener,
        EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb_loadBar)
    ProgressBar pbLoadBar;

    ContentFragment contentFragment;

    @Override
    protected void setThemes() {
        setTheme(Constant.themes[Constant.themeId]);
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
        contentFragment = new ContentFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, contentFragment);
        DaggerContentComponent
                .builder()
                .appComponent(appComponent)
                .contentModule(new ContentModule(contentFragment))
                .build()
                .inject(contentFragment);
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
                KeyboardUtils.hideSoftInput(ContentActivity.this);
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
        if (contentFragment != null && contentFragment instanceof ContentFragment) {
            if (!contentFragment.onKeyDown(keyCode, event)) {
//                finish(); swipeBack 自动调用finish
            } else {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        if (contentFragment != null && contentFragment instanceof ContentFragment) {
            EmojiconsFragment.input(contentFragment.etSmsContent, emojicon);
        }
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        if (contentFragment != null && contentFragment instanceof ContentFragment) {
            EmojiconsFragment.backspace(contentFragment.etSmsContent);
        }

    }
}
