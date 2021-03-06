package com.jv.daily.entity;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.jv.daily.entity.StoriesBean;
import com.jv.daily.entity.TopStoriesBean;

import com.jv.daily.entity.StoriesBeanDao;
import com.jv.daily.entity.TopStoriesBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig storiesBeanDaoConfig;
    private final DaoConfig topStoriesBeanDaoConfig;

    private final StoriesBeanDao storiesBeanDao;
    private final TopStoriesBeanDao topStoriesBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        storiesBeanDaoConfig = daoConfigMap.get(StoriesBeanDao.class).clone();
        storiesBeanDaoConfig.initIdentityScope(type);

        topStoriesBeanDaoConfig = daoConfigMap.get(TopStoriesBeanDao.class).clone();
        topStoriesBeanDaoConfig.initIdentityScope(type);

        storiesBeanDao = new StoriesBeanDao(storiesBeanDaoConfig, this);
        topStoriesBeanDao = new TopStoriesBeanDao(topStoriesBeanDaoConfig, this);

        registerDao(StoriesBean.class, storiesBeanDao);
        registerDao(TopStoriesBean.class, topStoriesBeanDao);
    }
    
    public void clear() {
        storiesBeanDaoConfig.getIdentityScope().clear();
        topStoriesBeanDaoConfig.getIdentityScope().clear();
    }

    public StoriesBeanDao getStoriesBeanDao() {
        return storiesBeanDao;
    }

    public TopStoriesBeanDao getTopStoriesBeanDao() {
        return topStoriesBeanDao;
    }

}
