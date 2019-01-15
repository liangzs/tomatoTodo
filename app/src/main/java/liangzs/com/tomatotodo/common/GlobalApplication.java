package liangzs.com.tomatotodo.common;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import liangzs.com.tomatotodo.data.greendao.DaoMaster;
import liangzs.com.tomatotodo.data.greendao.DaoSession;

/**
 * @author liangzs
 * @Date 2019/1/14
 */
public class GlobalApplication extends Application {
    private DaoSession mSession;
    private static GlobalApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), DbConfig.DB_NAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mSession = daoMaster.newSession();
    }

    public DaoSession getSession() {
        return mSession;
    }

    public static GlobalApplication getInstance() {
        return application;
    }
}
