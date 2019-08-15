package com.dycm.applib1.db.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.zhuorui.securities.market.socket.vo.kline.MinuteKlineData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig minuteKlineDataDaoConfig;

    private final MinuteKlineDataDao minuteKlineDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        minuteKlineDataDaoConfig = daoConfigMap.get(MinuteKlineDataDao.class).clone();
        minuteKlineDataDaoConfig.initIdentityScope(type);

        minuteKlineDataDao = new MinuteKlineDataDao(minuteKlineDataDaoConfig, this);

        registerDao(MinuteKlineData.class, minuteKlineDataDao);
    }
    
    public void clear() {
        minuteKlineDataDaoConfig.clearIdentityScope();
    }

    public MinuteKlineDataDao getMinuteKlineDataDao() {
        return minuteKlineDataDao;
    }

}
