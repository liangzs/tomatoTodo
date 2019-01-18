package liangzs.com.tomatotodo.presentation.historyTask;

import java.util.List;

import liangzs.com.tomatotodo.common.base.IBasePresenter;
import liangzs.com.tomatotodo.common.base.IBaseView;
import liangzs.com.tomatotodo.data.entity.TaskHistory;

/**
 * @author liangzs
 * @Date 2019/1/14
 */
public class HistoryTaskContract {
    public interface View extends IBaseView {

    }

    public interface Presenter extends IBasePresenter<View> {
        List<TaskHistory> queryHistory();
    }
}
