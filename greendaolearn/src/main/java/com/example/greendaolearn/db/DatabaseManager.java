package com.example.greendaolearn.db;

import android.content.Context;

import com.example.greendaolearn.greendao.DaoMaster;
import com.example.greendaolearn.greendao.DaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

public class DatabaseManager<T> {

    private volatile static DatabaseManager instance;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    private AbstractDao tDao;

    private DatabaseManager(Context context, String dbName) {
        mDaoSession = getDaoSession(context, dbName);
    }

    //双重检查锁定延迟初始化创建单例，instance必须为volatile而禁止重排序
    public static DatabaseManager getInstance(Context context, String dbName) {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager(context, dbName);
                }
            }
        }
        return instance;
    }

    /**
     * 插入单个对象
     * @param t
     * @param bean
     * @return
     */
    public boolean insertT(T t, Class bean) {
        tDao = getAbstractDao(bean);
        if (tDao != null) {
            tDao.insert(t);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 插入List中所有对象
     * @return
     */
    public boolean insertTx(List<T> ts, Class bean) {
        tDao = getAbstractDao(bean);
        if (tDao != null) {
            tDao.insertInTx(ts);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 查询指定列条件下的所有row
     * @param whereCondition
     */
    public List<T> queryTList(Class bean, WhereCondition whereCondition) {
        tDao = getAbstractDao(bean);
        if (tDao != null) {
            QueryBuilder queryBuilder = tDao.queryBuilder();
            return queryBuilder.where(whereCondition).list();
        } else {
            return null;
        }
    }

    /**
     * 查询所有数据
     * @param bean
     * @return
     */
    public List<T> queryTList(Class bean) {
        tDao = getAbstractDao(bean);
        if (tDao != null) {
            return tDao.loadAll();
        } else {
            return null;
        }
    }

    /**
     * 根据QueryBuilder设置的条件查询
     * @param queryBuilder
     * @return
     */
    public List<T> queryTList(QueryBuilder queryBuilder) {
        return queryBuilder.list();
    }

    /**
     * 查询指定条数数据
     * @param bean
     * @param limit
     * @return
     */
    public List<T> queryTList(Class bean, int limit) {
        tDao = getAbstractDao(bean);
        if (tDao != null) {
            return tDao.queryBuilder().limit(limit).list();
        } else {
            return null;
        }
    }

    public void deleteTList(QueryBuilder queryBuilder) {
        queryBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public void deleteTList(List<T> list, Class bean) {
        tDao = getAbstractDao(bean);
        if (tDao != null) {
            tDao.deleteInTx(list);
        }
    }
    /**
     * delete all
     *
     * @param bean
     */
    public void deleteTList( Class bean) {
        tDao = getAbstractDao(bean);
        if (tDao != null) {
            tDao.deleteAll();
        }
    }

    private AbstractDao getAbstractDao(Class bean) {
        return mDaoSession.getDao(bean); //根据类对象获取对应的Dao
    }
    private DaoSession getDaoSession(Context context, String dbName) {
        if (mDaoSession == null) {
            mDaoMaster = getDaoMaster(context, dbName);
            mDaoSession = mDaoMaster.newSession();
        }
        return mDaoSession;
    }

    private DaoMaster getDaoMaster(Context context, String dbName) {
        if (mDaoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, dbName); //sqLiteOpenHelper
            mDaoMaster = new DaoMaster(helper.getWritableDatabase()); //或回调onCreate方法
        }
        return mDaoMaster;
    }

    public QueryBuilder getQueryBuilder(Class bean) {
        tDao = getAbstractDao(bean);
        if (tDao != null) {
            return tDao.queryBuilder();
        } else {
            return null;
        }
    }
}
