package liangzs.com.tomatotodo.base;

/**
 * @author liangzs
 * @Date 2019/1/14
 */
public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {

    public V view;

    @Override
    public void attach(V view) {
        this.view = view;
    }

    @Override
    public void dettach() {
        if (view != null) {
            view = null;
        }
    }

}
