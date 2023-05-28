package com.github.framework.lock.support;

import com.github.framework.lock.*;
import com.github.framework.lock.enums.LockProviderType;
import com.github.framework.lock.exception.LockException;
import lombok.extern.slf4j.Slf4j;
import org.github.framework.common.lang.CheckUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class LockManagerImpl extends ApplicationObjectSupport implements LockManager, InitializingBean, DisposableBean {

    private Map<LockProviderType, LockProvider>  providerMap = new HashMap<LockProviderType, LockProvider>();

    @Override
    public DistributionLock createLock(LockInfo lockInfo) {

        LockProvider lockProvider = getLockProvider(lockInfo);

        DistributionLock distributionLock = lockProvider.createMutexLock(lockInfo);

        log.info("Create Mutex Lock success -> ID:{},URI:{},TYPE:{}",lockInfo.getId(),lockInfo.getLockURI(),lockInfo.getLockType());

        if (distributionLock == null) {
            return new EmptyDistributionLock();
        }

        return distributionLock;
    }

    @Override
    public DistributionLock createLock(String module, String id) {

        return createLock(SimpleLockInfo.of(id,module));
    }

    @Override
    public DistributionLock createLock(String module, String id, boolean locked) {
        DistributionLock distributionLock = createLock(module,id);
        if (locked) {
            distributionLock.lock();
        }
        return distributionLock;
    }

    @Override
    public DistributionReadWriteLock createReadWriteLock(LockInfo lockInfo) {

        LockProvider lockProvider = getLockProvider(lockInfo);

        return lockProvider.createReadWriteLock(lockInfo);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, LockProvider> beans = getApplicationContext().getBeansOfType(LockProvider.class);
        CheckUtils.notEmpty(beans,"Not found any LockProvider!!! Please check your Spring Configuration file!!!");

        for (Map.Entry<String,LockProvider> entry : beans.entrySet()) {
            providerMap.put(entry.getValue().getLockProviderType(),entry.getValue());
        }

    }

    @Override
    public void destroy() throws Exception {
        providerMap.clear();
    }

    private LockProvider getLockProvider(LockInfo lockInfo) {
        CheckUtils.notNull(lockInfo,"the parameter 'lockInfo' is not null!");

        log.debug("Will create DistributionLock-> ID:{},URI:{}",lockInfo.getId(),lockInfo.getLockURI());

        LockProvider lockProvider = providerMap.get(lockInfo.getProviderType());
        if (lockProvider == null) {
            throw new LockException(String.format("Not found LockProvider '%s',Please check your Spring configuration!",lockInfo.getProviderType()));
        }
        return lockProvider;
    }
}
