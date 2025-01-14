
/*
 *  Copyright (c) 2019-2022  <a href="https://github.com/somowhere/albedo">Albedo</a>, somewhere (somewhere0813@gmail.com).
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

package com.albedo.java.common.core.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author somewhere
 * @date 2019/2/1 放行参数配置
 */
@Data
@Configuration
@ConditionalOnExpression("!'${ignore}'.isEmpty()")
@ConfigurationProperties(prefix = "ignore")
public class FilterIgnoreProperties {

	/**
	 * 放行终端配置，网关不校验此处的终端
	 */
	private List<String> clients = new ArrayList<>();

	/**
	 * 放行url,放行的url不再被安全框架拦截
	 */
	private List<String> urls = new ArrayList<>();

	/**
	 * 不聚合swagger
	 */
	private List<String> swaggerProviders = new ArrayList<>();

}
