package demo.refresh.viewgroup.com.viewgrouppulltorefresh.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.refresh.viewgroup.com.viewgrouppulltorefresh.R;

/**
 * @author yuzhiwei
 * @version 1.0
 * @Title:SAFEYE@
 * @Description:
 * @date 2016-03-04
 */
public class HeaderView extends RelativeLayout{
    private TextView tipText;
    private ImageView arrowImg;
    private ProgressBar progressBar;
    public HeaderView(Context context) {
        super(context);
        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void init(Context context){
        View view =  LayoutInflater.from(context).inflate(R.layout.view_refreshview,null);
        arrowImg = (ImageView) view.findViewById(R.id.arrowimg);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        tipText = (TextView) view.findViewById(R.id.tiptext);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
        addView(view);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void startRotateAniDown(){
        ObjectAnimator.ofFloat(arrowImg, "rotation", 0f, 180f).setDuration(500).start();
    }
    public void startRotateAniUp(){
        ObjectAnimator.ofFloat(arrowImg, "rotation",180f, 360f).setDuration(500).start();
    }
    public void isImageShow(boolean isShow){
        if(isShow){
            progressBar.setVisibility(View.GONE);
            arrowImg.setVisibility(View.VISIBLE);
        }else {
            progressBar.setVisibility(View.VISIBLE);
            arrowImg.setVisibility(View.GONE);
        }
    }
    public void setTipText(String tip){
        tipText.setText(tip);
    }
}
