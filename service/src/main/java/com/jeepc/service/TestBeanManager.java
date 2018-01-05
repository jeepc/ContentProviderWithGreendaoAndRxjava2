package com.jeepc.service;

import com.jeepc.rxdao2.greendao.rx.RxDao;
import com.jeepc.rxdao2.greendao.rx.RxUtils;
import com.jeepc.service.greendao.TestBeanDao;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by jeepc on 2018/1/3.
 */

public class TestBeanManager {
    public static boolean saveAll(List<TestBean> list) {
        try {
            TestBeanDao dao = DaoManager.getInstance().getDaoSession().getTestBeanDao();
            dao.insertInTx(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void saveAllRx(List<TestBean> list, final ResultListener l) {
        TestBeanDao dao = DaoManager.getInstance().getDaoSession().getTestBeanDao();
        RxDao<TestBean, Long> rxDao = RxUtils.fromDao(dao);
        rxDao.insertInTx(list)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Iterable<TestBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Iterable<TestBean> testBeans) {
                        if (l != null) l.onResult(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (l != null) l.onResult(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public static void getAllByPage(int page, int count, final ResultListener l) {

        TestBeanDao dao = DaoManager.getInstance().getDaoSession().getTestBeanDao();
        RxUtils.fromQuery(dao.queryBuilder()
                .limit(count).offset(page * count)
                .build()).list().observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<TestBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<TestBean> testBeans) {
                l.onGetAllByPage(testBeans);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    public static long getCount() {
        try {
            TestBeanDao dao = DaoManager.getInstance().getDaoSession().getTestBeanDao();
            return dao.count();
        } catch (Exception e) {
            return 0;
        }

    }


    public interface ResultListener<T> {
        void onResult(boolean success);

        void onGetAllByPage(List<T> list);

        void onGetAllByPageFail();
    }
}
