package com.lipt0n.recyclerviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;


/**
 * Created by Granden on 2017/8/19.
 */

public class GrandenItemDecoration extends RecyclerView.ItemDecoration {

    private DecorationCallback callback;
    private int barHeight;
    private Paint dividerPaint;
    private Context context;
    private TextPaint textPaint;
    private Paint.FontMetrics fontMetrics;
    private float bar_content_padding;
    private Bitmap bitmap;
    private Rect text_rect=new Rect();

    public GrandenItemDecoration(Context context, DecorationCallback callback) {
        this.context = context;
        this.callback = callback;
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(DensityUtil.dip2px(context, 25));
        fontMetrics = new Paint.FontMetrics();
        textPaint.getFontMetrics(fontMetrics);
        bar_content_padding= context.getResources().getDimensionPixelSize(R.dimen.bar_content_padding);
        barHeight = context.getResources().getDimensionPixelSize(R.dimen.height);
        dividerPaint.setColor(context.getResources().getColor(R.color.itemColor));
        bitmap=BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round);
        ScaleBitmap();
    }

    private void ScaleBitmap() {
        Matrix matrix=new Matrix();

        float scale=bitmap.getWidth()>barHeight?Float.valueOf(barHeight)/Float.valueOf(bitmap.getHeight()):Float.valueOf(bitmap.getHeight())/Float.valueOf(barHeight);
        matrix.postScale(scale,scale);
        bitmap= Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            Log.e(TAG, "......... "+parent.getChildAt(0).getTop() );
            int position = parent.getChildAdapterPosition(child);
            float top = child.getTop()-barHeight;
            float bottom = top + barHeight;
            String content = callback.getContent(position);
            textPaint.getTextBounds(content,0, content.length(),text_rect);

            //除了first item以外,当前item和下一个item的文字比比较,只要不一样就马上要画一条新的bar
            if(isFirstInGroup(position)) {
                c.drawRect(left, top, right, bottom, dividerPaint);
                c.drawText(content, bar_content_padding+bitmap.getWidth(), bottom-fontMetrics.descent, textPaint);
                c.drawBitmap(bitmap,bar_content_padding ,bottom-bitmap.getHeight(),textPaint);
            }
            }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position= parent.getChildAdapterPosition(view);
        //除了first item以外,当前item和下一个item的文字比比较,只要不一样就马上要腾出一条bar的位置
        if(isFirstInGroup(position)){
            outRect.top=barHeight;

        }

    }

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {

            View child = parent.getChildAt(0);
            int position = parent.getChildAdapterPosition(child);
            float top = child.getTop();
            String content = callback.getContent(position);


            textPaint.getTextBounds(content,0, content.length(),text_rect);
            //LogU.e("parent.getChildAt(0).getTop()"+parent.getChildAt(0).getTop());


            ////因为要实现吸顶的效果,所以必须是当前可见的第一个item的文字和下一个item的文字进行对比.

            if (parent.getChildAt(0).getTop()<= -parent.getChildAt(0).getHeight()+barHeight&&isFirstInGroup(position+1)) {
                    c.drawRect(left, top, right, child.getBottom(), dividerPaint);
                    c.drawText(content, bar_content_padding+bitmap.getWidth(), child.getBottom()-fontMetrics.descent, textPaint);
                    c.drawBitmap(bitmap, bar_content_padding ,child.getBottom()-bitmap.getHeight(),textPaint);
            } else {
                c.drawRect(left, 0, right, barHeight, dividerPaint);
                c.drawText(content, bar_content_padding+bitmap.getWidth(), barHeight-fontMetrics.descent, textPaint);

                c.drawBitmap(bitmap,bar_content_padding ,0,textPaint);
            }
        }
    }

    public interface DecorationCallback {
        String getContent(int position);


    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            // 因为是根据 字符串内容的相同与否 来判断是不是同意组的，所以此处的标记id 要是String类型
            // 如果你只是做联系人列表，悬浮框里显示的只是一个字母，则标记id直接用 int 类型就行了
            String prevGroupId = callback.getContent(pos - 1);
            String groupId = callback.getContent(pos);
            //判断前一个字符串 与 当前字符串 是否相同
            if (prevGroupId.equals(groupId)) {
                return false;
            } else {
                return true;
            }
        }
    }
}
