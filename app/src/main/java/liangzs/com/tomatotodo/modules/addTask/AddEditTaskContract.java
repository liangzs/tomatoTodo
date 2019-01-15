package liangzs.com.tomatotodo.modules.addTask;

import liangzs.com.tomatotodo.base.IBasePresenter;
import liangzs.com.tomatotodo.base.IBaseView;
import liangzs.com.tomatotodo.data.entity.Task;

/**
 * @author liangzs
 * @Date 2019/1/14
 */
public class AddEditTaskContract {
    public interface View extends IBaseView {
        void insertCallBack();

    }

    public interface Presenter extends IBasePresenter<View> {
        void insertTask(Task task);
    }
}
