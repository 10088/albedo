package com.albedo.java.modules.monitor.service.impl;

import cn.hutool.core.date.DateUtil;
import com.albedo.java.common.core.util.StringUtil;
import com.albedo.java.common.core.vo.PageModel;
import com.albedo.java.modules.monitor.domain.RedisInfo;
import com.albedo.java.modules.monitor.domain.ReportSearchType;
import com.albedo.java.modules.monitor.service.RedisService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author somewhere
 * @date 2021-03-08
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

	@Resource
	private RedisConnectionFactory redisConnectionFactory;

	/**
	 * Redis详细信息
	 */
	@Override
	public IPage<RedisInfo> getRedisInfo() {
		Properties info = redisConnectionFactory.getConnection().info();
		List<RedisInfo> infoList = new ArrayList<>();
		RedisInfo redisInfo;
		for (Map.Entry<Object, Object> entry : info.entrySet()) {
			redisInfo = new RedisInfo();
			redisInfo.setKey(StringUtil.toString(entry.getKey()));
			redisInfo.setValue(StringUtil.toString(entry.getValue()));
			infoList.add(redisInfo);
		}
		return new PageModel<>(infoList, infoList.size());
	}

	@Override
	public Long getKeySize() {
		Long dbSize = redisConnectionFactory.getConnection().dbSize();
		return dbSize;
	}

	@Override
	public String getUsedMemory() {
		Properties info = redisConnectionFactory.getConnection().info();
		for (Map.Entry<Object, Object> entry : info.entrySet()) {
			String key = StringUtil.toString(entry.getKey());
			if ("used_memory".equals(key)) {
				return StringUtil.toString(entry.getValue());
			}
		}
		return "0";
	}

	/**
	 * 查询redis信息for报表
	 *
	 * @param reportSearchType
	 * @return
	 */
	@Override
	public Map<String, JSONArray> getMapForReport(ReportSearchType reportSearchType) {
		Map<String, JSONArray> mapJson = Maps.newHashMap();
		JSONArray json = new JSONArray();
		if (ReportSearchType.INFO.equals(reportSearchType)) {
			List<RedisInfo> redisInfo = getRedisInfo().getRecords();
			for (RedisInfo info : redisInfo) {
				Map<String, Object> map = Maps.newHashMap();
				BeanMap beanMap = BeanMap.create(info);
				for (Object key : beanMap.keySet()) {
					map.put(key + "", beanMap.get(key));
				}
				json.add(map);
			}
			mapJson.put("data", json);
			return mapJson;
		}
		for (int i = 0; i < 5; i++) {
			JSONObject jo = new JSONObject();
			Map<String, Object> map;
			if (ReportSearchType.KEY.equals(reportSearchType)) {
				jo.put("value", getKeySize());
			} else {
				Integer used_memory = Integer.valueOf(getUsedMemory());
				jo.put("value", used_memory / 1000);
			}
			String create_time = DateUtil.formatTime(DateUtil.date(System.currentTimeMillis() - (4 - i) * 1000));
			jo.put("name", create_time);
			json.add(jo);
		}
		mapJson.put("data", json);
		return mapJson;
	}
}