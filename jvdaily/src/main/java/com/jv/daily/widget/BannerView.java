package com.jv.daily.widget;

import android.content.Context;
import android.os.Handler;
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

import com.bumptech.glide.Glide;
import com.jv.daily.R;

import java.util.ArrayList;
import java.util.List;

public class BannerView extends RelativeLayout {
    private ViewPager mViewPager;
    private LinearLayout mLinearLayout;
    private Context mContext;
    private ImageView[] mIndicator;
    private Handler mHandler = new Handler();
    final List<String> mList = new ArrayList<>();
    private OnBannerItemClickListener mOnBannerItemClickListener;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            mHandler.postDelayed(mRunnable, 5000);
        }
    };
    private int mItemCount;

    public interface OnBannerItemClickListener {
        void onClick(int position);
    }

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        View.inflate(mContext, R.layout.widget_bannerview, this);
        // 取到布局中的控件
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_points);
    }

    /**
     * 给banner中的viewpager设置数据
     *
     * @param list
     */
    public void setList(List<String> list) {
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

    private void initIndicator(List<String> list, Context context) {
        mIndicator = new ImageView[mItemCount];
        for (int i = 0; i < mIndicator.length; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(12, 12);
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
        private List<String> imagesUrl;
        private Context context;

        public BannerPagerAdapter(List<String> imagesUrl, Context context) {
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
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            // 联网取图片，根据自己的情况修改
            Glide.with(mContext).load(mList.get(position % mList.size())).into(imageView);
            Log.i("num", position % mList.size() + "");
            container.addView(imageView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnBannerItemClickListener.onClick(position % mList.size());
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