package liangzs.com.tomatotodo.modules.homePage;

import java.util.List;

import liangzs.com.tomatotodo.base.IBasePresenter;
import liangzs.com.tomatotodo.base.IBaseView;
import liangzs.com.tomatotodo.data.entity.Task;

/**
 * @author liangzs
 * @Date 2019/1/12
 */
public class HomePageContract {
    public interface View extends IBaseView {
        void updateList(List<Task> list);

        void upateClock(String timeValue, String type);

        void finishTask();
        void nextTask();

    }


    public interface Presenter extends IBasePresenter<View> {
        void queryDataFromDB();

        void onDestroy();

        void startRecord();

        void startRest();

        void deleteTask(Task task);

        void finishTask(Task task);


    }
}
