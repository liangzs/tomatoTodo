package liangzs.com.tomatotodo.modules.homePage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import liangzs.com.tomatotodo.R;
import liangzs.com.tomatotodo.base.BaseActivity;
import liangzs.com.tomatotodo.common.util.ObjectUtil;
import liangzs.com.tomatotodo.data.entity.Task;
import liangzs.com.tomatotodo.modules.addTask.AddEditTaskActivity;
import liangzs.com.tomatotodo.modules.addTask.TestActivity;

import static liangzs.com.tomatotodo.modules.homePage.MainPresenter.REST;
import static liangzs.com.tomatotodo.modules.homePage.MainPresenter.WORK;

public class MainActivity extends BaseActivity<HomePageContract.Presenter, HomePageContract.View>
        implements NavigationView.OnNavigationItemSelectedListener, HomePageContract.View {
    private static final int ADD_TASK_RESULT_CODE = 100;
    private TaskAdapter taskAdapter;
    private DragListView listView;
    //    private ScrollChildSwipeRefreshLayout swipeRefreshLayout;
    private AlertDialog dialog;
    private Button btTime;
    private boolean isRecord;
    @MainPresenter.ClockType
    private String clockType;
    private FloatingActionButton fab;


    @Override
    public HomePageContract.View initPresentView() {
        return this;
    }

    @Override
    public HomePageContract.Presenter initPresenter() {
        return new MainPresenter();
    }

    TaskAdapter.ItemListener itemListener = new TaskAdapter.ItemListener() {
        @Override
        public void itemOnclick(Task task) {
            //修改任务
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            intent.putExtra("task", task);
            startActivityForResult(intent, ADD_TASK_RESULT_CODE);
        }

        @Override
        public void playOnclick(Task task, int position) {
            if (dialog == null) {
                dialog = new AlertDialog.Builder(MainActivity.this).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                dialog.setTitle("是否关闭当前番茄任务");
            }
            if (task.isPlaying()) {
                dialog.setButton(-1, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialog.show();
                return;
            }
            if (isRecord) {
                return;
            }
            presenter.startRecord();
            task.setIsPlaying(true);
            taskAdapter.changePlayStatus(task);
            taskAdapter.change(position, 0);
            isRecord = true;
        }
    };

    @Override
    public void initData() {
        taskAdapter = new TaskAdapter(new ArrayList<Task>(), itemListener);
        listView.setAdapter(taskAdapter);
    }


    @Override
    public void initView() {
        listView = findViewById(R.id.task_list);
        listView.setDragViewId(R.id.move_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (taskAdapter.isModifySeq()) {
                    taskAdapter.modifySeq(false);
                    fab.setImageResource(R.drawable.ic_add);
                    return;
                }
                //添加任务
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                startActivityForResult(intent, ADD_TASK_RESULT_CODE);
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        swipeRefreshLayout =
//                findViewById(R.id.refresh_layout);
//        swipeRefreshLayout.setColorSchemeColors(
//                ContextCompat.getColor(this, R.color.colorPrimary),
//                ContextCompat.getColor(this, R.color.colorAccent),
//                ContextCompat.getColor(this, R.color.colorPrimaryDark)
//        );
//        // Set the scrolling view in the custom SwipeRefreshLayout.
//        swipeRefreshLayout.setScrollUpChild(listView);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                presenter.queryDataFromDB();
//            }
//        });
        btTime = findViewById(R.id.bt_clock);
        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new AlertDialog.Builder(MainActivity.this).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                }
                if (clockType == WORK) {
                    dialog.setTitle("番茄任务");
                    dialog.setMessage("是否关闭当前任务");
                    dialog.setButton(-1, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.startRecord();
                        }
                    });
                } else if (clockType == REST) {
                    dialog.setTitle("番茄任务");
                    dialog.setMessage("是否跳过休息");
                    dialog.setButton(-1, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                } else {
                    dialog.setTitle("番茄任务");
                    dialog.setMessage("是否开启任务");
                    dialog.setButton(-1, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
                dialog.show();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (taskAdapter.isModifySeq()) {
            taskAdapter.modifySeq(false);
            fab.setImageResource(R.drawable.ic_add);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.queryDataFromDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.modify_seq) {
            fab.setImageResource(R.drawable.ic_done);
            taskAdapter.modifySeq(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateList(List<Task> list) {
//        swipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        if (!ObjectUtil.isEmpty(list)) {
            taskAdapter.setData(list);
        }
    }

    @Override
    public void upateClock(final String timeValue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btTime.setText(timeValue);
            }
        });
    }
}
