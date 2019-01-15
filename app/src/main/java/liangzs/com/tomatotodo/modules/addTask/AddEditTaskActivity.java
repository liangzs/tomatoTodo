package liangzs.com.tomatotodo.modules.addTask;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import liangzs.com.tomatotodo.R;
import liangzs.com.tomatotodo.base.BaseActivity;
import liangzs.com.tomatotodo.common.GlobalApplication;
import liangzs.com.tomatotodo.common.util.ObjectUtil;
import liangzs.com.tomatotodo.data.entity.Task;

public class AddEditTaskActivity extends BaseActivity<AddEditTaskContract.Presenter, AddEditTaskContract.View> implements AddEditTaskContract.View {
    private EditText mTitle, mContent;
    private ActionBar mActionBar;
    private boolean isEdit;
    private Task task;

    @Override
    public AddEditTaskContract.View initPresentView() {
        return this;
    }

    @Override
    public AddEditTaskContract.Presenter initPresenter() {
        return new AddEditTaskPresenter();
    }

    @Override
    public void initData() {
        isEdit = false;
        /**
         * android.os.Bundle.get(java.lang.String)' on a null object reference
         * 传值没有用bundle直接getExtra，而不是getIntent().getExtras().get("task")
         */
        if (getIntent().getSerializableExtra("task") != null) {
            task = (Task) getIntent().getSerializableExtra("task");
            isEdit = true;
            mTitle.setText(task.getTitle());
            mContent.setText(task.getContent());
        }

    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mTitle = findViewById(R.id.add_task_title);
        mContent = findViewById(R.id.add_task_description);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ObjectUtil.isEmpty(mTitle.getText().toString())) {
                    Snackbar.make(view, "title can't be null", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                task.setTitle(mTitle.getText().toString());
                task.setContent(mContent.getText().toString());
                if (isEdit) {
                    ((GlobalApplication) getApplication()).getSession().getTaskDao().update(task);
                } else {
                    ((GlobalApplication) getApplication()).getSession().getTaskDao().insert(task);
                }
                Log.i("AddTask", "id:" + task.getId());
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_task;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//            int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getWindow();
//                WindowManager.LayoutParams attributes = window.getAttributes();
//                attributes.flags |= flagTranslucentNavigation;
//                window.setAttributes(attributes);
//                getWindow().setStatusBarColor(Color.TRANSPARENT);
//            } else {
//                Window window = getWindow();
//                WindowManager.LayoutParams attributes = window.getAttributes();
//                attributes.flags |= flagTranslucentStatus | flagTranslucentNavigation;
//                window.setAttributes(attributes);
//            }
//        }
//        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void insertCallBack() {

    }
}
