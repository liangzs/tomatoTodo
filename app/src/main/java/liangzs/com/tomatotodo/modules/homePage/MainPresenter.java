package liangzs.com.tomatotodo.modules.homePage;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import liangzs.com.tomatotodo.base.BasePresenter;
import liangzs.com.tomatotodo.common.GlobalApplication;
import liangzs.com.tomatotodo.data.entity.Task;
import liangzs.com.tomatotodo.data.entity.TaskHistory;

/**
 * @author liangzs
 * @Date 2019/1/14
 */
public class MainPresenter extends BasePresenter<HomePageContract.View> implements HomePageContract.Presenter {
    private Timer timer;
    private int remainCount;//60min
    private int min, second;
    private String clockResult;

    @Override
    public void queryDataFromDB() {
        List<Task> tasks = GlobalApplication.getInstance().getSession().getTaskDao().loadAll();
        view.updateList(tasks);
    }


    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void startRecord() {
//        remainCount = 1800;
        remainCount = 5;
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                remainCount--;
                min = remainCount / 60;
                second = remainCount % 60;
                clockResult = min + ":" + second;
                if (second < 10) {
                    clockResult = min + ":0" + second;
                }
                view.upateClock(clockResult, WORK);
                if (remainCount <= 0) {
                    taskComplet();
                    this.cancel();
                }
            }
        }, 0, 1000);
    }

    @Override
    public void startRest() {
        remainCount = 3;
//        remainCount = 300;
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                remainCount--;
                min = remainCount / 60;
                second = remainCount % 60;
                clockResult = min + ":" + second;
                if (second < 10) {
                    clockResult = min + ":0" + second;
                }
                view.upateClock(clockResult, REST);
                if (remainCount <= 0) {
                    taskComplet();
                    cancel();
                }
            }
        }, 0, 1000);
    }

    @Override
    public void deleteTask(Task task) {
        GlobalApplication.getInstance().getSession().getTaskDao().delete(task);
    }

    public void taskComplet() {
        view.upateClock("00:00", NONE);
        view.nextTask();
    }

    @Override
    public void finishTask(Task task) {
        TaskHistory taskHistory = new TaskHistory(task);
        GlobalApplication.getInstance().getSession().getTaskHistoryDao().insert(taskHistory);
    }

    public static final String WORK = "WORK";
    public static final String REST = "REST";
    public static final String NONE = "NONE";

    @StringDef({
            WORK, REST, NONE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ClockType {
    }


}
