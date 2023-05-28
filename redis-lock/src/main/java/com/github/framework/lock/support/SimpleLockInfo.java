package com.github.framework.lock.support;

import com.github.framework.lock.LockInfo;
import com.github.framework.lock.annotation.Locking;
import com.github.framework.lock.constants.LockConstants;
import com.github.framework.lock.enums.LockProviderType;
import com.github.framework.lock.enums.LockType;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.github.framework.common.lang.CustomStringUtils;

public class SimpleLockInfo implements LockInfo {

    /** id 对应Locking.id() */
    private String id;

    private String module;

    private Long waitTime;

    private Long expiredTime;

    private LockProviderType providerType;

    private String lockedAlert;

    private Long createTime;

    private LockType lockType;

    private String uuid;

    public static SimpleLockInfo of(String id, String module,Long waitTime,Long expiredTime) {
        SimpleLockInfo lock = new SimpleLockInfo();
        lock.setId(id);
        lock.setModule(module);
        lock.setExpiredTime(expiredTime == null ? LockConstants.DEFAULT_EXPIRED_TIME : expiredTime);
        lock.setWaitTime(waitTime == null ? LockConstants.DEFAULT_WAIT_TIME : waitTime);
        lock.setProviderType(LockConstants.DEFAULT_LOCK_PROVIDER);
        lock.createTime = System.currentTimeMillis();
        lock.uuid = CustomStringUtils.uuid();
        return lock;
    }

    public static SimpleLockInfo of(String id, String module) {
        return of(id,module,LockConstants.DEFAULT_WAIT_TIME,LockConstants.DEFAULT_EXPIRED_TIME);
    }

    public static SimpleLockInfo of(Locking locking, String targetLockid) {
        SimpleLockInfo lock = new SimpleLockInfo();

        lock.createTime = System.currentTimeMillis();
        lock.setId(targetLockid);
        lock.setModule(locking.module());
        lock.setExpiredTime(locking.expiredTime());
        lock.setWaitTime(locking.waitTime());
        lock.setProviderType(locking.provider());
        lock.setLockedAlert(locking.lockedAlert());

        return lock;
    }


    @Override
    public Boolean isExpired() {
        return (System.currentTimeMillis() - createTime) > this.expiredTime ;
    }

    public void setLockedAlert(String lockedAlert) {
        this.lockedAlert = lockedAlert;
    }

    @Override
    public String getLockURI() {
        String path;
        if (providerType == LockProviderType.REDIS) {
            path = LockConstants.DEFAULT_REDIS_KEY_PREFIX + module + ":" + id;
        } else {
            path = LockConstants.DEFAULT_ZOOKEEPER_PATH_PREFIX + "/" + module + "/" + id;
            path = path.replaceAll("//","/");
        }
        return path;
    }


    @Override
    public String toString() {
        return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Override
    public Long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    @Override
    public String getLockedAlert() {
        return lockedAlert;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public LockProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(LockProviderType providerType) {
        this.providerType = providerType;
    }

    @Override
    public LockType getLockType() {
        return lockType;
    }

    public void setLockType(LockType lockType) {
        this.lockType = lockType;
    }
}
