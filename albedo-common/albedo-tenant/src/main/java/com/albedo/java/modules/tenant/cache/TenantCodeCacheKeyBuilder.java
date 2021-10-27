package com.albedo.java.modules.tenant;

import com.albedo.java.common.core.cache.model.CacheKeyBuilder;
import com.albedo.java.common.core.constant.CacheKeyBuilderConstants;
import com.albedo.java.common.core.util.StrPool;

import java.time.Duration;

/**
 * 系统用户 KEY
 * <p>
 * #c_tenant
 *
 * @author zuihou
 * @date 2020/9/20 6:45 下午
 */
public class TenantCodeCacheKeyBuilder implements CacheKeyBuilder {
    @Override
    public String getTenant() {
        return StrPool.EMPTY;
    }

    @Override
    public String getPrefix() {
        return CacheKeyBuilderConstants.TENANT_CODE;
    }

    @Override
    public Duration getExpire() {
        return Duration.ofHours(24);
    }
}