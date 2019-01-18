package liangzs.com.tomatotodo.presentation.homePage;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import liangzs.com.tomatotodo.common.base.BasePresenter;
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
        remainCount = 1800;
//        remainCount = 5;
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
                    view.upateClock("00:00", FINISH);
                    view.finishTask();
                    this.cancel();
                }
            }
        }, 0, 1000);
    }

    @Override
    public void startRest() {
//        remainCount = 6;
        remainCount = 300;
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
                    view.upateClock("00:00", REST);
                    view.nextTask();
                    cancel();
                }
            }
        }, 0, 1000);
    }

    @Override
    public void deleteTask(Task task) {
        GlobalApplication.getInstance().getSession().getTaskDao().delete(task);
    }


    @Override
    public void finishTask(Task task) {
        TaskHistory taskHistory = new TaskHistory(task);
        GlobalApplication.getInstance().getSession().getTaskHistoryDao().insert(taskHistory);
        GlobalApplication.getInstance().getSession().getTaskDao().delete(task);
    }

    public static final String WORK = "WORK";
    public static final String REST = "REST";
    public static final String NONE = "NONE";
    public static final String FINISH = "FINISH";

    @StringDef({
            WORK, REST, NONE, FINISH
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ClockType {
    }


}
