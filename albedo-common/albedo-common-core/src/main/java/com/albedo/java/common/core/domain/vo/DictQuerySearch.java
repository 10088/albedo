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

package com.albedo.java.common.core.domain.vo;

import cn.hutool.core.util.EscapeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * Created by somewhere on 2017/3/2.
 *
 * @author somewhere
 */
@Data
@Schema
@ToString
public class DictQuerySearch {

	private String dictQueries;

	public void setDictQueries(String dictQueries) {
		this.dictQueries = EscapeUtil.unescapeHtml4(dictQueries);
	}

}
