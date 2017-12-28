package com.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myapplication.adapter.ItemDifferAdapter;
import com.myapplication.adapter.ItemSameAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者: Nathans'Liu
 * 邮箱: a1053128464@qq.com
 * 时间: 2017/12/26 15:38
 * 描述:
 */

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.tv_1)
    TextView mTv1;
    @InjectView(R.id.rv)
    RecyclerView mRecyclerView;
    @InjectView(R.id.tv_3)
    TextView mTv3;
    @InjectView(R.id.tv_4)
    TextView mTv4;
    @InjectView(R.id.tv_5)
    TextView mTv5;
    @InjectView(R.id.tv_6)
    TextView mTv6;
    LinearLayoutManager linearLayoutManager;
    private ItemSameAdapter mSameAdapter;
    private ItemDifferAdapter mDifferAdapter;
    private GridLayoutManager gridLayoutManager;
    //竖着
    private static final int MANAGER_LINEAR_VERTICAL = 0;
    //横着一行
    private static final int MANAGER_LINEAR_HORIZONTAL = 1;
    //Grid 竖着
    private static final int MANAGER_LINEAR_GRIDVIEW_VERTICAL = 2;
    //Grid 横着
    private static final int MANAGER_LINEAR_GRIDVIEW_HORIZONTAL = 3;
    //竖着不同
    private static final int MANAGER_LINEAR_VERTICAL_ = 4;
    //横着不同
    private static final int MANAGER_LINEAR_HORIZONTAL_ = 5;
    //形态变量
    private int intType = 0;
    //item的宽/高
    private int itemW;
    private int itemH;
    private int iResult;
    //存放item宽或高
    private Map<Integer, Integer> mMapList = new HashMap<>();
    private int iposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        mTv1.setText("屏幕宽度：" + screenWidth + "    " + "屏幕高度：" + screenHeight);
        mSameAdapter = new ItemSameAdapter();
        mDifferAdapter = new ItemDifferAdapter();
        initRecyclerVH(LinearLayout.VERTICAL);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mHandler.sendEmptyMessage(1);
            }
        });
    }

    /**
     * 竖着 单行横着
     *
     * @param orientation
     */
    public void initRecyclerVH(int orientation) {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mSameAdapter);
        mSameAdapter.notifyDataSetChanged();
    }

    public void initRecyclerGrid(int orientation) {
        gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mSameAdapter);
        mSameAdapter.notifyDataSetChanged();
    }

    public void initRecyclerVH_(int orientation) {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mDifferAdapter);
        mDifferAdapter.notifyDataSetChanged();
    }


    public int getScollYDistance() {
        if (intType == MANAGER_LINEAR_VERTICAL) {
            //找到即将移出屏幕Item的position,position是移出屏幕item的数量
            int position = linearLayoutManager.findFirstVisibleItemPosition();
            //根据position找到这个Item
            View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
            //获取Item的高
            int itemHeight = firstVisiableChildView.getHeight();
            //算出该Item还未移出屏幕的高度
            int itemTop = firstVisiableChildView.getTop();
            //position移出屏幕的数量*高度得出移动的距离
            int iposition = position * itemHeight;
            //减去该Item还未移出屏幕的部分可得出滑动的距离
            iResult = iposition - itemTop;
            //item宽高
            itemW = firstVisiableChildView.getWidth();
            itemH = firstVisiableChildView.getHeight();
        } else if (intType == MANAGER_LINEAR_HORIZONTAL) {
            //找到即将移出屏幕Item的position,position是移出屏幕item的数量
            int position = linearLayoutManager.findFirstVisibleItemPosition();
            //根据position找到这个Item
            View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
            //获取Item的宽
            int itemWidth = firstVisiableChildView.getWidth();
            //算出该Item还未移出屏幕的高度
            int itemRight = firstVisiableChildView.getRight();
            //position移出屏幕的数量*高度得出移动的距离
            int iposition = position * itemWidth;
            //因为横着的RecyclerV第一个取到的Item position为零所以计算时需要加一个宽
            iResult = iposition - itemRight + itemWidth;
            //item宽高
            itemW = firstVisiableChildView.getWidth();
            itemH = firstVisiableChildView.getHeight();
        } else if (intType == MANAGER_LINEAR_GRIDVIEW_VERTICAL) {
            //得出spanCount几列或几排
            int itemSpanCount = gridLayoutManager.getSpanCount();
            //得出的position是一排或一列总和
            int position = gridLayoutManager.findFirstVisibleItemPosition();
            //需要算出才是即将移出屏幕Item的position
            int itemPosition = position / itemSpanCount ;
            //因为是相同的Item所以取那个都一样
            View firstVisiableChildView = gridLayoutManager.findViewByPosition(position);
            int itemHeight = firstVisiableChildView.getHeight();
            int itemTop = firstVisiableChildView.getTop();
            int iposition = itemPosition * itemHeight;
            iResult = iposition - itemTop;
            //item宽高
            itemW = firstVisiableChildView.getWidth();
            itemH = firstVisiableChildView.getHeight();
        } else if (intType == MANAGER_LINEAR_GRIDVIEW_HORIZONTAL) {
            //得出spanCount
            int itemSpanCount = gridLayoutManager.getSpanCount();
            int position = gridLayoutManager.findFirstVisibleItemPosition();
            //算出item position
            int itemPosition = position / itemSpanCount + 1;
            View firstVisiableChildView = gridLayoutManager.findViewByPosition(position);
            int itemWidth = firstVisiableChildView.getWidth();
            int itemRight = firstVisiableChildView.getRight();
            int iposition = itemPosition * itemWidth;
            iResult = iposition - itemRight;
            //item宽高
            itemW = firstVisiableChildView.getWidth();
            itemH = firstVisiableChildView.getHeight();
        } else if (intType == MANAGER_LINEAR_VERTICAL_ | intType == MANAGER_LINEAR_HORIZONTAL_) {
            iResult = unlikeVertical();
        }
        return iResult;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.type_1:
                intType = MANAGER_LINEAR_VERTICAL;
                initRecyclerVH(LinearLayout.VERTICAL);
                break;
            case R.id.type_2:
                intType = MANAGER_LINEAR_HORIZONTAL;
                initRecyclerVH(LinearLayout.HORIZONTAL);
                break;
            case R.id.type_3:
                intType = MANAGER_LINEAR_GRIDVIEW_VERTICAL;
                initRecyclerGrid(LinearLayout.VERTICAL);
                break;
            case R.id.type_4:
                intType = MANAGER_LINEAR_GRIDVIEW_HORIZONTAL;
                initRecyclerGrid(LinearLayout.HORIZONTAL);
                break;
            case R.id.type_5:
                intType = MANAGER_LINEAR_VERTICAL_;
                initRecyclerVH_(LinearLayout.VERTICAL);
                break;
            case R.id.type_6:
                intType = MANAGER_LINEAR_HORIZONTAL_;
                initRecyclerVH_(LinearLayout.HORIZONTAL);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            mTv6.setText("当前ITEM宽：" + itemW + "" + "   " + "高：" + itemH + "");
            if (intType == MANAGER_LINEAR_VERTICAL | intType == MANAGER_LINEAR_HORIZONTAL
                    | intType == MANAGER_LINEAR_VERTICAL_
                    | intType == MANAGER_LINEAR_HORIZONTAL_) {
                mTv3.setText("首可见位置：" + linearLayoutManager.findFirstVisibleItemPosition());
                mTv4.setText("尾可见位置：" + linearLayoutManager.findLastVisibleItemPosition());
                mTv5.setText("滚动距离：" + getScollYDistance());
            } else if (intType == MANAGER_LINEAR_GRIDVIEW_VERTICAL | intType == MANAGER_LINEAR_GRIDVIEW_HORIZONTAL) {
                mTv3.setText("首可见位置：" + gridLayoutManager.findFirstVisibleItemPosition());
                mTv4.setText("尾可见位置：" + gridLayoutManager.findLastVisibleItemPosition());
                mTv5.setText("滚动距离：" + getScollYDistance());
            }
            return true;
        }
    });

    /**
     * 不同Item VERTICAL
     */
    public int unlikeVertical() {
        int itemWH = 0;
        int itemTR = 0;
        int distance = 0;
        int position = linearLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = linearLayoutManager.findViewByPosition(position);
        //判断是横着还是竖着，得出宽或高
        if (intType == MANAGER_LINEAR_VERTICAL_) {
            itemWH = firstVisiableChildView.getHeight();
        } else if (intType == MANAGER_LINEAR_HORIZONTAL_) {
            itemWH = firstVisiableChildView.getWidth();
        }
        //一层判断mMapList是否为空，若不为空则根据键判断保证不会重复存入
        if (mMapList.size() == 0) {
            mMapList.put(position, itemWH);
        } else {
            if (!mMapList.containsKey(position)) {
                mMapList.put(position, itemWH);
                Log.d("poi", mMapList + "");
            }
        }
        //判断是横着还是竖着，得出未滑出屏幕的距离
        if (intType == MANAGER_LINEAR_VERTICAL_) {
            itemTR = firstVisiableChildView.getTop();
        } else if (intType == MANAGER_LINEAR_HORIZONTAL_) {
            itemTR = firstVisiableChildView.getRight();
        }
        //position为动态获取，目前屏幕Item位置
        for (int i = 0; i < position; i++) {
            //iposition移出屏幕的距离
            iposition = iposition + mMapList.get(i);
        }
        //根据类型拿iposition减未移出Item部分距离，最后得出滑动距离
        if (intType == MANAGER_LINEAR_VERTICAL_) {
            distance = iposition - itemTR;
        } else if (intType == MANAGER_LINEAR_HORIZONTAL_) {
            distance = iposition - itemTR + itemWH;
        }
        //item宽高
        itemW = firstVisiableChildView.getWidth();
        itemH = firstVisiableChildView.getHeight();
        //归零
        iposition = 0;
        return distance;
    }


}
