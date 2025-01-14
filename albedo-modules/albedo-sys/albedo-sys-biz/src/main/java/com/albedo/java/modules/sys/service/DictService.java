
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

package com.albedo.java.modules.sys.service;

import com.albedo.java.common.core.domain.vo.SelectVo;
import com.albedo.java.modules.sys.domain.DictDo;
import com.albedo.java.modules.sys.domain.dto.DictDto;
import com.albedo.java.modules.sys.domain.dto.DictQueryDto;
import com.albedo.java.modules.sys.domain.vo.DictVo;
import com.albedo.java.plugins.database.mybatis.service.TreeService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author somewhere
 * @since 2019/2/1
 */
public interface DictService extends TreeService<DictDo, DictDto> {

	/**
	 * 获取所有可用排序正序字典
	 *
	 * @return
	 */
	List<DictDo> findAllOrderBySort();

	/**
	 * 获取字典数据
	 *
	 * @param codes
	 * @return
	 */
	Map<String, List<SelectVo>> findCodes(String codes);

	/**
	 * 字典树集合
	 *
	 * @param dictQueryDto
	 * @return
	 */
	IPage<DictVo> findTreeList(DictQueryDto dictQueryDto);

	/**
	 * 批量锁定，解锁
	 *
	 * @param ids
	 */
	void lockOrUnLock(Set<Long> ids);

}
