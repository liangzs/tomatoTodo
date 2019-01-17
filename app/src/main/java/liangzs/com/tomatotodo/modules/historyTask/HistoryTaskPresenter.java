package liangzs.com.tomatotodo.modules.historyTask;

import java.util.List;

import liangzs.com.tomatotodo.base.BasePresenter;
import liangzs.com.tomatotodo.common.GlobalApplication;
import liangzs.com.tomatotodo.data.entity.Task;
import liangzs.com.tomatotodo.data.entity.TaskHistory;

/**
 * @author liangzs
 * @Date 2019/1/14
 */
public class HistoryTaskPresenter extends BasePresenter<HistoryTaskContract.View> implements HistoryTaskContract.Presenter {


    @Override
    public List<TaskHistory> queryHistory() {
        List<TaskHistory> tasks = GlobalApplication.getInstance().getSession().getTaskHistoryDao().loadAll();
        return tasks;
    }
}
