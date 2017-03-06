package com.jv.daily.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jv.daily.R;
import com.jv.daily.mvp.module.NewsBean;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class BannerView extends RelativeLayout {
    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;
    private Context mContext;
    private ImageView[] mIndicator;
    private Handler mHandler = new Handler();
    private TextView tvTitle;

    List<NewsBean.TopStoriesBean> mList = new ArrayList<>();

    private OnBannerItemClickListener mOnBannerItemClickListener;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            mHandler.postDelayed(mRunnable, 5000);
        }
    };
    private int mItemCount;

    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
        initWidget(context, attrs);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
        initWidget(context, attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
        init();
        initWidget(context, attrs);

    }

    public interface OnBannerItemClickListener {
        void onClick(int position);
    }


    private void init() {
        View.inflate(mContext, R.layout.widget_bannerview, this);
        // 取到布局中的控件
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_points);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    private void initWidget(Context context, AttributeSet attrs) {
        Log.i("Banner", "initWidget()");
        //获取当前控件定义的属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
    }


    @Override
    public void setContentDescription(CharSequence contentDescription) {
        List<NewsBean.TopStoriesBean> beans = new Gson().fromJson(contentDescription.toString(), new TypeToken<List<NewsBean.TopStoriesBean>>() {
        }.getType());
        setList(beans);
    }

    /**
     * 给banner中的viewpager设置数据
     */
    public void setList(List<NewsBean.TopStoriesBean> list) {
        if (mList.size() == 0) {
            mList.addAll(list);
            mItemCount = mList.size();
            initView();
        }

    }

    /**
     * banner item的点击监听
     *
     * @param onBannerItemClickListener
     */
    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        mOnBannerItemClickListener = onBannerItemClickListener;
    }

    private void initView() {
        // 给viewpager设置adapter
        BannerPagerAdapter bannerPagerAdapter = new BannerPagerAdapter(mList, mContext);
        mViewPager.setAdapter(bannerPagerAdapter);
        // 初始化底部点指示器
        initIndicator(mList, mContext);
        mViewPager.setCurrentItem(500 * mItemCount);
        mViewPager.setOffscreenPageLimit(0);

        // 给viewpager设置滑动监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switchIndicator(position % mItemCount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                cancelRecycle();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startRecycle();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //指示器下标
    private void initIndicator(List<NewsBean.TopStoriesBean> list, Context context) {
        mIndicator = new ImageView[mItemCount];
        for (int i = 0; i < mIndicator.length; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(18, 18);
            params.setMargins(6, 0, 6, 0);
            ImageView imageView = new ImageView(context);
            mIndicator[i] = imageView;
            if (i == 0) {
                mIndicator[i].setBackgroundResource(R.drawable.ptt_banner_dian_focus);
            } else {
                mIndicator[i].setBackgroundResource(R.drawable.ptt_banner_dian_white);
            }
            mLinearLayout.addView(imageView, params);
        }
        if (mItemCount == 1) {
            mLinearLayout.setVisibility(View.GONE);
        } else {
            mLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void switchIndicator(int selectItems) {
        for (int i = 0; i < mIndicator.length; i++) {
            if (i == selectItems) {
                mIndicator[i].setBackgroundResource(R.drawable.ptt_banner_dian_focus);
            } else {
                mIndicator[i].setBackgroundResource(R.drawable.ptt_banner_dian_white);
            }
        }
    }

    public List<NewsBean.TopStoriesBean> getList() {
        return mList;
    }

    private void startRecycle() {
        mHandler.postDelayed(mRunnable, 5000);
    }

    private void cancelRecycle() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            startRecycle();
        } else {
            cancelRecycle();
        }
    }


    private class BannerPagerAdapter extends PagerAdapter {
        private List<NewsBean.TopStoriesBean> imagesUrl;
        private Context context;

        public BannerPagerAdapter(List<NewsBean.TopStoriesBean> imagesUrl, Context context) {
            this.imagesUrl = imagesUrl;
            this.context = context;
        }

        @Override
        public int getCount() {
            return mItemCount == 1 ? 1 : Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final int index = position % mList.size();
            NewsBean.TopStoriesBean bean = mList.get(index);

            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            // 联网取图片，根据自己的情况修改
            Glide.with(context).load(bean.getImage()).into(imageView);
            tvTitle.setText(bean.getTitle());

            container.addView(imageView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnBannerItemClickListener.onClick(index);
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}