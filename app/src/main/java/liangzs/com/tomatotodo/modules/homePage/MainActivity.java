package liangzs.com.tomatotodo.modules.homePage;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import liangzs.com.tomatotodo.R;
import liangzs.com.tomatotodo.base.BaseActivity;
import liangzs.com.tomatotodo.domain.common.util.ObjectUtil;
import liangzs.com.tomatotodo.data.entity.Task;
import liangzs.com.tomatotodo.modules.addTask.AddEditTaskActivity;
import liangzs.com.tomatotodo.modules.historyTask.HistoryActivity;

import static liangzs.com.tomatotodo.modules.homePage.MainPresenter.FINISH;
import static liangzs.com.tomatotodo.modules.homePage.MainPresenter.NONE;
import static liangzs.com.tomatotodo.modules.homePage.MainPresenter.REST;
import static liangzs.com.tomatotodo.modules.homePage.MainPresenter.WORK;

public class MainActivity extends BaseActivity<HomePageContract.Presenter, HomePageContract.View>
        implements NavigationView.OnNavigationItemSelectedListener, HomePageContract.View {
    private static final String TAG = "MainActivity";
    private static final int ADD_TASK_RESULT_CODE = 100;
    private TaskAdapter taskAdapter;
    private DragListView listView;
    //    private ScrollChildSwipeRefreshLayout swipeRefreshLayout;
    private AlertDialog dialog;
    private Button btTime;
    private boolean isRecord;
    @MainPresenter.ClockType
    private String clockType = NONE;
    private FloatingActionButton fab;
    private List<Task> tasks;
    private Task currentTask;
    private Drawable drawable;


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
        public void playOnclick(Task task, int position) {
            if (WORK.equals(task.getCloclType())) {
                dialog.setTitle("是否关闭当前番茄任务");
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
            currentTask = task;
            currentTask.setCloclType(WORK);
            presenter.startRecord();
            taskAdapter.changePlayStatus(currentTask);
            taskAdapter.change(position, 0);
            isRecord = true;
        }
    };

    @Override
    public void initData() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(MainActivity.this).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
        }
        taskAdapter = new TaskAdapter(new ArrayList<Task>(), itemListener);
        listView.setAdapter(taskAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.i(TAG, "longClick");
                dialog.setTitle("是否删除当前项");
                dialog.setMessage("tips:删除当前数据项");
                dialog.setButton(-1, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteTask(tasks.get(position));
                        taskAdapter.deleteTask(tasks.get(position));
                    }
                });
                dialog.show();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //修改任务
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra("task", tasks.get(position));
                startActivityForResult(intent, ADD_TASK_RESULT_CODE);
            }
        });
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
        btTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ObjectUtil.isEmpty(tasks)) {
                    return;
                }
                switch (clockType) {
                    case WORK:
                        dialog.setTitle("番茄任务");
                        dialog.setMessage("是否关闭当前任务");
                        dialog.setButton(-1, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.startRecord();
                            }
                        });
                        break;
                    case REST:
                        dialog.setTitle("番茄任务");
                        dialog.setMessage("是否跳过休息");
                        dialog.setButton(-1, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                starTask();
                            }
                        });
                        break;
                    case FINISH:
                        dialog.setTitle("提交当前任务");
                        dialog.setMessage("任务已完成，进行下一个任务");
                        dialog.setButton(-1, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                presenter.finishTask(currentTask);
                                tasks.remove(currentTask);
                                taskAdapter.notifyDataSetChanged();
                                if (ObjectUtil.isEmpty(tasks)) {
                                    showShort(getString(R.string.toast_no_task));
                                    return;
                                }
                                presenter.startRest();
                            }
                        });
                        break;
                    default:
                        dialog.setTitle("番茄任务");
                        dialog.setMessage("是否开启任务");
                        dialog.setButton(-1, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ObjectUtil.isEmpty(tasks)) {
                                    showShort(getString(R.string.toast_no_task));
                                    return;
                                }
                                starTask();
                            }
                        });
                        break;
                }
                dialog.show();
            }
        });
    }

    /**
     * 开日任务
     */
    private void starTask() {
        tasks.get(0).setCloclType(WORK);
        currentTask = tasks.get(0);
        clockType = WORK;
        taskAdapter.notifyDataSetChanged();
        presenter.startRecord();

    }


    @Override
    public void initView() {
        listView = findViewById(R.id.task_list);
        listView.setDragViewId(R.id.move_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        fab = findViewById(R.id.fab);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btTime = findViewById(R.id.bt_clock);

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_page) {
            // Handle the camera action
        } else if (id == R.id.history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.setting) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateList(List<Task> list) {
        tasks = list;
        if (!ObjectUtil.isEmpty(list)) {
            taskAdapter.setData(tasks);
        }
    }


    @Override
    public void upateClock(final String timeValue, final String clockTyp) {
        clockType = clockTyp;
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                btTime.setText(timeValue);
                if (WORK.equals(clockTyp)) {
                    btTime.setTextColor(Color.RED);
                    drawable = getDrawable(R.mipmap.ic_clear_search_api_holo_light);
                } else if (REST.equals(clockTyp)) {
                    btTime.setTextColor(Color.BLUE);
                    drawable = getDrawable(R.mipmap.ic_clear_search_api_holo_light);
                } else {
                    btTime.setTextColor(Color.BLACK);
                    drawable = getDrawable(R.mipmap.ic_checkmark_light);

                }
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                btTime.setCompoundDrawables(null, null, drawable, null);
            }
        });
    }

    /**
     * 是否自动下一个任务
     */
    @Override
    public void nextTask() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                starTask();
            }
        });
    }

    @Override
    public void finishTask() {
        currentTask.setCloclType(REST);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                taskAdapter.changePlayStatus(currentTask);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
