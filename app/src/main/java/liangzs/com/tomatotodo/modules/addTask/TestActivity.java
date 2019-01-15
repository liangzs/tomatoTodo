package liangzs.com.tomatotodo.modules.addTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import liangzs.com.tomatotodo.R;
import liangzs.com.tomatotodo.data.entity.Task;
import liangzs.com.tomatotodo.modules.homePage.DragListView;
import liangzs.com.tomatotodo.modules.homePage.TaskAdapter;

public class TestActivity extends AppCompatActivity {
    private DragListView listView;
    private List<Task> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        listView = findViewById(R.id.listview);
        listView.setDragViewId(R.id.move_item);

        initData();
    }


    private void initData() {
        data = new ArrayList<>();
        data.add(new Task("1", "content1"));
        data.add(new Task("2", "content2"));
        data.add(new Task("3", "content3"));
        data.add(new Task("4", "content4"));

        TaskAdapter taskAdapter = new TaskAdapter(data, new TaskAdapter.ItemListener() {
            @Override
            public void itemOnclick(Task task) {

            }

            @Override
            public void playOnclick(Task task, int position) {

            }
        });
        listView.setAdapter(taskAdapter);
        taskAdapter.modifySeq(true);
    }
}
