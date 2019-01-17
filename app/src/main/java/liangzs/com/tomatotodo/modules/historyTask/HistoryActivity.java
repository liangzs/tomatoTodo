package liangzs.com.tomatotodo.modules.historyTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import liangzs.com.tomatotodo.R;
import liangzs.com.tomatotodo.base.BaseActivity;
import liangzs.com.tomatotodo.data.entity.TaskHistory;

import static android.support.v4.util.Preconditions.checkNotNull;

public class HistoryActivity extends BaseActivity<HistoryTaskContract.Presenter, HistoryTaskContract.View> implements HistoryTaskContract.View {
    private ListView listView;
    private List<TaskHistory> data;

    @Override
    public HistoryTaskContract.View initPresentView() {
        return this;
    }

    @Override
    public HistoryTaskContract.Presenter initPresenter() {
        return new HistoryTaskPresenter();
    }

    @Override
    public void initData() {
        data = presenter.queryHistory();
        TasksAdapter tasksAdapter = new TasksAdapter(data);
        listView.setAdapter(tasksAdapter);
    }

    @Override
    public void initView() {
        listView = findViewById(R.id.history_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private static class TasksAdapter extends BaseAdapter {

        private List<TaskHistory> mTasks;

        public TasksAdapter(List<TaskHistory> tasks) {
            setList(tasks);
        }

        public void replaceData(List<TaskHistory> tasks) {
            setList(tasks);
            notifyDataSetChanged();
        }

        @SuppressLint("RestrictedApi")
        private void setList(List<TaskHistory> tasks) {
            mTasks = checkNotNull(tasks);
        }

        @Override
        public int getCount() {
            return mTasks.size();
        }

        @Override
        public TaskHistory getItem(int i) {
            return mTasks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(android.R.layout.two_line_list_item, viewGroup, false);
            }
            TaskHistory taskHistory = mTasks.get(i);
            ((TextView) rowView.findViewById(android.R.id.text1)).setText(taskHistory.getTitle());
            ((TextView) rowView.findViewById(android.R.id.text2)).setText(taskHistory.getContent());

            return rowView;
        }
    }
}
