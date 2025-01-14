///*
// *  Copyright (c) 2019-2022  <a href="https://github.com/somowhere/albedo">Albedo</a>, somewhere (somewhere0813@gmail.com).
// *  <p>
//
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *  <p>
// * https://www.gnu.org/licenses/lgpl.html
// *  <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.albedo.java.common.core.util;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.serializer.*;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.Timestamp;
//import java.util.*;
//
///**
// * @author somewhere
// * @description
// * @date 2020/5/31 17:08
// */
//public class Json extends com.alibaba.fastjson.JSON {
//
//	protected static Logger logger = LoggerFactory.getLogger(Json.class);
//
//	private static SerializeFilter valueFilter = new ValueFilter() {
//		@Override
//		public Object process(Object obj, String s, Object v) {
//			if (v == null) {
//				return "";
//			}
//			return v;
//		}
//	};
//
//	/**
//	 * 默认的处理时间
//	 *
//	 * @param obj
//	 * @return
//	 */
//	public static String toJsonString(Object obj) {
//		return toJSONStringWithDateFormat(obj, DateUtil.TIME_FORMAT, SerializerFeature.WriteDateUseDateFormat);
//	}
//
//	public static Object toJson(Object obj) {
//		if (obj != null) {
//			if (obj instanceof Collection) {
//				JSONArray json = new JSONArray();
//				json.addAll((Collection<? extends Object>) obj);
//				return json;
//			} else if (obj instanceof Map) {
//				return new JSONObject((Map<String, Object>) obj);
//			}
//		}
//		return obj;
//	}
//
//	public static final String toJsonDateString(Object object, String dataFmt, String... filters) {
//		return toJsonString(object, dataFmt, Lists.newArrayList(filters), false);
//	}
//
//	public static final String toJsonString(Object object, String... filters) {
//		return toJsonString(object, DateUtil.TIME_FORMAT, Lists.newArrayList(filters), false);
//	}
//
//	public static final String toJsonString(Object object, String dataFmt, List<String> filter,
//											boolean isWriteNullStringAsEmpty) {
//		SerializeWriter out = new SerializeWriter();
//		try {
//			JSONSerializer serializer = new JSONSerializer(out);
//			serializer.config(SerializerFeature.WriteNullStringAsEmpty, false);
//			serializer.config(SerializerFeature.WriteDateUseDateFormat, true);
//			if (StringUtil.isNotEmpty(dataFmt)) {
//				serializer.setDateFormat(dataFmt);
//			}
//			if (isWriteNullStringAsEmpty) {
//				setFilter(serializer, valueFilter);
//			}
//			if (filter != null) {
//				String[] strs = new String[filter.size()];
//				for (int i = 0; i < filter.size(); i++) {
//					strs[i] = filter.get(i);
//				}
//				ComplexPropertyPreFilter f = new ComplexPropertyPreFilter(strs);
//				setFilter(serializer, f);
//
//			}
//			serializer.write(object);
//
//			return out.toString();
//		} finally {
//			out.close();
//		}
//	}
//
//	private static void setFilter(JSONSerializer serializer, SerializeFilter filter) {
//		if (filter == null) {
//			return;
//		}
//
//		if (filter instanceof PropertyPreFilter) {
//			serializer.getPropertyPreFilters().add((PropertyPreFilter) filter);
//		}
//
//		if (filter instanceof NameFilter) {
//			serializer.getNameFilters().add((NameFilter) filter);
//		}
//
//		if (filter instanceof ValueFilter) {
//			serializer.getValueFilters().add((ValueFilter) filter);
//		}
//
//		if (filter instanceof PropertyFilter) {
//			serializer.getPropertyFilters().add((PropertyFilter) filter);
//		}
//
//		if (filter instanceof BeforeFilter) {
//			serializer.getBeforeFilters().add((BeforeFilter) filter);
//		}
//
//		if (filter instanceof AfterFilter) {
//			serializer.getAfterFilters().add((AfterFilter) filter);
//		}
//	}
//
//	/**
//	 * 禁止循环引用
//	 *
//	 * @param object
//	 * @param dateFormat
//	 * @return
//	 */
//	public static String toJsonStringDisAbleCircule(Object object, String dateFormat) {
//		SerializeConfig mapping = new SerializeConfig();
//		SimpleDateFormatSerializer simpleDateFormatSerializer = new SimpleDateFormatSerializer(dateFormat);
//		mapping.put(Date.class, simpleDateFormatSerializer);
//		mapping.put(Timestamp.class, simpleDateFormatSerializer);
//		return Json.toJSONString(object, mapping, SerializerFeature.DisableCircularReferenceDetect);
//	}
//
//	public static Map<String, Object> toMap(JSONObject jsonObject) {
//		Map<String, Object> data = Maps.newHashMap();
//		// 将json字符串转换成jsonObject
//		Iterator<String> it = jsonObject.keySet().iterator();
//		// 遍历jsonObject数据，添加到Map对象
//		while (it.hasNext()) {
//			String key = String.valueOf(it.next());
//			Object value = jsonObject.get(key);
//			data.put(key, value);
//		}
//		return data;
//	}
//
//	public static List<Map<String, Object>> toMapList(JSONArray jsonArray) {
//		List<Map<String, Object>> list = Lists.newArrayList();
//		for (int i = 0; i < jsonArray.size(); i++) {
//			list.add(toMap(jsonArray.getJSONObject(i)));
//		}
//		return list;
//	}
//
//}
