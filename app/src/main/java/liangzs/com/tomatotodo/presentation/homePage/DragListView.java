package liangzs.com.tomatotodo.presentation.homePage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * @author liangzs
 * @Date 2019/1/15
 */
public class DragListView extends ListView {

    private int dragViewId;
    private WindowManager.LayoutParams windowParams;
    private WindowManager windowManager;
    private ImageView dragImageView;

    private int offsetScreenTop; //距离屏幕顶部的位置
    private int offsetViewTop;  //手指按下位置距离item顶部的位置
    private int dragPosition;
    private int srcY; //用于判断滑动方向

    public DragListView(Context context) {
        super(context);
    }

    public DragListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setDragViewId(int dragViewId) {
        this.dragViewId = dragViewId;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            int rawY = (int) ev.getRawY();

            int currentPostion = dragPosition = pointToPosition(x, y);
            if (currentPostion == AdapterView.INVALID_POSITION) {
                return super.onInterceptTouchEvent(ev);
            }

            //getChildAt是获取可见位置的item
            ViewGroup itemView = (ViewGroup) getChildAt(currentPostion - getFirstVisiblePosition());
            offsetScreenTop = rawY - y;
            offsetViewTop = y - itemView.getTop();
            // 获取可拖拽的图标
            View dragger = itemView.findViewById(dragViewId);
            if (dragger != null && x > dragger.getLeft() && dragger.getVisibility() == View.VISIBLE) {

                itemView.setDrawingCacheEnabled(true);// 开启cache.
                Bitmap bm = Bitmap.createBitmap(itemView.getDrawingCache());// 根据cache创建一个新的bitmap对象.
                itemView.setDrawingCacheEnabled(false);// 一定关闭cache，否则复用会出现错乱
                startDrag(bm, y);
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (dragImageView != null) {
            int y = (int) ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    srcY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    onDrag(y);
                    getChildAt(dragPosition - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);
                    break;
                case MotionEvent.ACTION_UP:
                    stopDrag();
                    getChildAt(dragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void startDrag(Bitmap bm, int y) {
        /***
         * 初始化window.
         */
        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = y - offsetViewTop + offsetScreenTop;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE// 不需获取焦点
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE// 不需接受触摸事件
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON// 保持设备常开，并保持亮度不变。
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;// 窗口占满整个屏幕，忽略周围的装饰边框（例如状态栏）。此窗口需考虑到装饰边框的内容。

        // windowParams.format = PixelFormat.TRANSLUCENT;// 默认为不透明，这里设成透明效果.
        windowParams.windowAnimations = 0;// 窗口所使用的动画设置

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bm);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(imageView, windowParams);
        dragImageView = imageView;
    }


    /**
     * 拖动景象
     *
     * @param y
     */
    private void onDrag(int y) {
        int offsetTop = y - offsetViewTop; //顶部不能出界
        if (dragImageView != null && offsetTop >= 0 && offsetTop <= getChildAt(getChildCount() - 1).getTop()) {
            windowParams.alpha = 0.8f;// 透明度
            windowParams.y = y - offsetViewTop + offsetScreenTop;// 移动y值.//记得要加上dragOffset，windowManager计算的是整个屏幕.(标题栏和状态栏都要算上)
            windowManager.updateViewLayout(dragImageView, windowParams);// 时时移动.
        }

        onChange(y);

        scrollListView(y);

    }

    /**
     * 同步滑动ListView
     *
     * @param y
     */
    private void scrollListView(int y) {
        View view = getChildAt(dragPosition - getFirstVisiblePosition());
        int offsetY = srcY - y;

        if (y < getHeight() / 3 && y < srcY) { //listview向上滑
            setSelectionFromTop(dragPosition, offsetY + view.getTop());
        } else if (y > getHeight() / 3 * 2 && y > srcY) { //listview向下滑
            setSelectionFromTop(dragPosition, offsetY + view.getTop());
        }
        srcY = y;
    }


    /**
     * 同步改变item的位置
     *
     * @param y
     */
    private void onChange(int y) {
        int currentPostion = pointToPosition(0, y);

        if (currentPostion == AdapterView.INVALID_POSITION) {
            currentPostion = dragPosition;
        }

        if (dragPosition != currentPostion) {
            TaskAdapter adapter = (TaskAdapter) getAdapter();
            adapter.change(dragPosition, currentPostion);
            swich(dragPosition, currentPostion);
        }

        dragPosition = currentPostion;

    }

    /***
     * 切换隐藏的位置
     */
    private void swich(int start, int end) {
        getChildAt(start - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
        getChildAt(end - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);
    }

    /**
     * 停止拖动，删除影像
     */
    public void stopDrag() {
        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }
}