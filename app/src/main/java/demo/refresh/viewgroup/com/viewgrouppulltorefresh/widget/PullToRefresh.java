package demo.refresh.viewgroup.com.viewgrouppulltorefresh.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import demo.refresh.viewgroup.com.viewgrouppulltorefresh.inf.RefreshCompleteListen;


/**
 *@desc: 
 *@author: yzw
 *@modify: 2016/11/24 14:39
 *@param: 
 *@return 
 **/
public class PullToRefresh extends ViewGroup {
    private HeaderView headerView;//上方的刷新view
    private ViewGroup contentView;//放置主内容的viewgroup
    private float downX;
    private float downY;
    private float moveX;
    private float moveY;
    private float startHeadY;
    private float startContentY;
    private float k = 1f;
    public float totallDis;
    private float maxPullDownDis;//上方的刷新view,最大能滑动的距离
    private float maxContentViewPull;//放置主内容的viewgroup最大能移动的距离
    private float refreshViewDownY;//一开始按下时，上方的刷新view的Y坐标值
    private boolean isRefreshing;//是否正在刷新
    private RefreshCompleteListen refreshCompleteListen;//刷新完成的回调
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshCompleteListen.refresh();
        }
    };
    private boolean isFirstLayout = true;

    public PullToRefresh(Context context) {
        super(context);

    }

    public PullToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
//        viewgroup没有测量子view，所以要手动测量子view
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //在布局时，让上方的刷新控件刚搞偏离出屏幕
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (i == 0) {
                child.layout(0, -child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
            } else {
                getChildAt(i).layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
            }
        }
        //在第一次布局之后，拿到view最大能移动的距离
        if (isFirstLayout) {
            maxPullDownDis = Math.abs(headerView.getY());//拿到刷新控件的高度，即能滑动的最大距离
            maxContentViewPull = Math.abs(contentView.getY());//拿到高度，那么contentview最大能移动到：自身的高度+maxPullDownDis
            isFirstLayout = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                startHeadY = headerView.getY();
                startContentY = contentView.getY();
                refreshViewDownY = headerView.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getRawX();
                moveY = event.getRawY();
                if (Math.abs(moveY - downY) > (Math.abs(moveX - downX))) {//只有竖直方法改变的距离>大于横向滑动的距离时在默认为是下拉或者上拉操作
                    float changeY = moveY - downY;//改变的距离
                    totallDis += cacuteF(changeY) * changeY;
                    float contentY = totallDis + maxContentViewPull;//内容控件应该改变的位置 
                    float refreshY = totallDis - maxPullDownDis;//刷新控件应该摆放的位置
                    if (refreshY > maxPullDownDis)//刷新控件对多只能到0
                        refreshY = maxPullDownDis;
                    if (refreshY < -maxPullDownDis)//刷新控件对多只能偏离到屏幕外面的高度为自己自身的高度
                        refreshY = -maxPullDownDis;
                    if (contentY > maxContentViewPull + maxPullDownDis * 2)//内容控件最多只能到的Y坐标为 自身的高度+最大移动的距离
                        contentY = maxContentViewPull + maxPullDownDis * 2;
                    if (contentY < maxContentViewPull)//内容控件最少必须到的Y坐标为 自身的高度
                        contentY = maxContentViewPull;
                    if (!isRefreshing) {
                        if (refreshY >= 0) {//大于最大移动距离的一般时，要改变文字提示，和刷新图标的动画
                            headerView.startRotateAniDown();
                            headerView.setTipText("-----释放可刷新------");
                            isRefreshing = true;
                        }
                    }
                    if (isRefreshing) {
                        if (refreshY < 0) {
                            headerView.startRotateAniUp();
                            headerView.setTipText("-----下拉可刷新------");
                            isRefreshing = false;
                        }
                    }

                    ViewHelper.setY(contentView, contentY);
                    ViewHelper.setY(headerView, refreshY);
                    downY = moveY;
                    downX = moveX;
                }
                break;
            case MotionEvent.ACTION_UP:
                float dy = headerView.getY() - refreshViewDownY;//抬起时刷新控件的Y坐标-开始按下时控件的Y坐标
                //这里有两种情况 1 一种是下拉到一半，上推到一半 2 下拉没到一半，上推没到一半
                if (dy >= maxPullDownDis * 2 / 2 || (dy > -maxPullDownDis * 2 / 2 && dy < 0)) {
                    ObjectAnimator.ofFloat(headerView, "y", headerView.getY(), 0f).setDuration(300).start();
                    ObjectAnimator.ofFloat(contentView, "y", contentView.getY(), maxContentViewPull + maxPullDownDis).setDuration(300).start();
                    isRefreshing = true;
                    headerView.startRotateAniDown();
                    headerView.setTipText("----正在刷新----");
                    headerView.isImageShow(!isRefreshing);
                    handler.sendMessageDelayed(new Message(), 0);
                }
                if (dy < maxPullDownDis * 2 / 2 && dy > 0 || dy < -maxPullDownDis * 2 / 2) {
                    ObjectAnimator.ofFloat(headerView, "y", headerView.getY(), -maxPullDownDis).setDuration(300).start();
                    ObjectAnimator.ofFloat(contentView, "y", contentView.getY(), maxContentViewPull).setDuration(300).start();
                    isRefreshing = false;
                    headerView.setTipText("----下拉可刷新----");
                    headerView.startRotateAniUp();
                    headerView.isImageShow(!isRefreshing);
                }
                totallDis = 0;
                break;
        }
        return true;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                refreshViewDownY = headerView.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = ev.getRawX();
                moveY = ev.getRawY();
                float changeY = moveY - downY;
                downY = moveY;
                downX = moveX;
                if (changeY + headerView.getY() <= -maxPullDownDis) {//当改变的距离+刷新控件原本的位置>刷新控件像屏幕外的偏移量(负值即<)说明这时候是上拉操作，不拦截事件，交给子view
                    return false;
                } else {//这里为下拉事件
                    //当内容viewgroup中有listview  gridview时，第一个可见的item位置为0，就把下拉的事件自己消费掉，否则就交给子view消费
                    int childCount = contentView.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View view = contentView.getChildAt(i);
                        if (view instanceof AbsListView) {
                            int firstVisiblePosition = ((AbsListView) view).getFirstVisiblePosition();
                            if (firstVisiblePosition == 0) {
                                return true;
                            } else {
                                return false;
                            }
                        } else if (view instanceof RecyclerView) {
                            RecyclerView.LayoutManager layoutManager = ((RecyclerView) view).getLayoutManager();
                            if (layoutManager instanceof LinearLayoutManager) {
                                int orientation = ((LinearLayoutManager) layoutManager).getOrientation();
                                if (orientation == LinearLayoutManager.VERTICAL) {
                                    int firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                                    Log.e("--------位置-------",firstVisibleItemPosition+"");
                                    if (firstVisibleItemPosition == 0) {
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                                else{
                                    return true;
                                }
                            }
                        }
                    }
                    return true;
                }

            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        headerView = (HeaderView) getChildAt(0);
        contentView = (ViewGroup) getChildAt(1);
    }

    public void refreshOk() {
        ObjectAnimator.ofFloat(headerView, "y", headerView.getY(), -maxPullDownDis).setDuration(500).start();
        ObjectAnimator.ofFloat(contentView, "y", contentView.getY(), maxContentViewPull).setDuration(500).start();
        headerView.setTipText("-----继续下拉可刷新------");
        headerView.isImageShow(true);
    }

    public void setRefreshListener(RefreshCompleteListen refreshCompleteListen) {
        this.refreshCompleteListen = refreshCompleteListen;
    }
    //实现阻尼效果，越往下拉越难拉
    public float cacuteF(float changY) {
        if (changY < 0)
            return 1f;
        if (totallDis < maxPullDownDis / 2)
            return 1f;
        if (totallDis < maxPullDownDis * 2 / 3 & totallDis > maxPullDownDis / 2)
            return 0.5f;
        return 0.4f;
    }
}
