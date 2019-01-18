package liangzs.com.tomatotodo.common.base;

/**
 * @author liangzs
 * @Date 2019/1/14
 */
public interface IBasePresenter<V extends IBaseView> {
    void attach(V view);

    void dettach();
}
