/*
 *  Copyright (c) 2019-2021  <a href="https://github.com/somowhere/albedo">Albedo</a>, somewhere (somewhere0813@gmail.com).
 *  <p>
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.albedo.java.common.runner;

import com.albedo.java.common.core.annotation.BaseInit;
import com.albedo.java.common.core.util.ObjectUtil;
import com.albedo.java.common.core.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by somewhere on 2017/5/17.
 *
 * @author 837158334@qq.com
 */
@Slf4j
public class ContextInitRunner implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.debug(">>>>> spring BaseInit start <<<<<");
		// spring初始化完毕后，通过反射调用所有使用BaseInit注解的afterPropertiesSet方法
		Map<String, Object> baseServices = SpringContextHolder.getApplicationContext()
			.getBeansWithAnnotation(BaseInit.class);
		if (ObjectUtil.isNotEmpty(baseServices)) {
			for (Object service : baseServices.values()) {
				log.debug(">>>>> {}.baseInit()", service.getClass().getName());
				try {
					BaseInit annotation = AnnotationUtils.findAnnotation(service.getClass(), BaseInit.class);
					Method initMapper = service.getClass().getMethod(annotation.method());
					initMapper.invoke(service);
				} catch (Exception e) {
					log.error("service " + service.getClass().getName() + " BaseInit error {}", e);
				}
			}
		}
		log.debug(">>>>> spring BaseInit end <<<<<");
	}

}
