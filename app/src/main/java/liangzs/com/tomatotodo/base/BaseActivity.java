package liangzs.com.tomatotodo.base;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity<P extends IBasePresenter<V>, V extends IBaseView> extends AppCompatActivity implements IBaseView {
    public P presenter;
    public V view;

    public abstract V initPresentView();

    public abstract P initPresenter();

    public abstract void initData();

    public abstract void initView();

    public abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        presenter = initPresenter();
        view = initPresentView();
        if (presenter != null && view != null) {
            presenter.attach(view);
        }
        initView();
        initData();
//        StatusUtil.compat(this, R.color.colorPrimary);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dettach();
    }

    @Override
    public void requestLoading() {

    }

    @Override
    public void hideLoading() {

    }
}