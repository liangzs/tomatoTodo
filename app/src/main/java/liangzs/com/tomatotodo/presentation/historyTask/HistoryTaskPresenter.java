package liangzs.com.tomatotodo.presentation.historyTask;

import java.util.List;

import liangzs.com.tomatotodo.common.base.BasePresenter;
import liangzs.com.tomatotodo.data.entity.TaskHistory;
import liangzs.com.tomatotodo.common.GlobalApplication;

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
