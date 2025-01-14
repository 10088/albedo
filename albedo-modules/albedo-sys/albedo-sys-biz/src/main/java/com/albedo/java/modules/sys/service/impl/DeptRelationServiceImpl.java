
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

package com.albedo.java.modules.sys.service.impl;

import com.albedo.java.common.core.util.CollUtil;
import com.albedo.java.modules.sys.domain.DeptRelationDo;
import com.albedo.java.modules.sys.domain.dto.DeptDto;
import com.albedo.java.modules.sys.repository.DeptRelationRepository;
import com.albedo.java.modules.sys.service.DeptRelationService;
import com.albedo.java.plugins.database.mybatis.service.impl.BaseServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author somewhere
 * @since 2019/2/1
 */
@Service
@AllArgsConstructor
public class DeptRelationServiceImpl extends BaseServiceImpl<DeptRelationRepository, DeptRelationDo>
	implements DeptRelationService {

	private final DeptRelationRepository deptRelationRepository;

	/**
	 * 维护部门关系
	 *
	 * @param deptDto 部门
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveDeptRelation(DeptDto deptDto) {
		// 增加部门关系表
		DeptRelationDo condition = new DeptRelationDo();
		condition.setDescendant(deptDto.getParentId());
		List<DeptRelationDo> relationList = deptRelationRepository
			.selectList(
				Wrappers.<DeptRelationDo>query().lambda().eq(DeptRelationDo::getDescendant, deptDto.getParentId()))
			.stream().map(relation -> {
				relation.setDescendant(deptDto.getId());
				return relation;
			}).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(relationList)) {
			this.saveBatch(relationList);
		}

		// 自己也要维护到关系表中
		DeptRelationDo own = new DeptRelationDo();
		own.setDescendant(deptDto.getId());
		own.setAncestor(deptDto.getId());
		deptRelationRepository.insert(own);
	}

	/**
	 * 通过ID删除部门关系
	 *
	 * @param id
	 */
	@Override
	public void removeDeptRelationById(Long id) {
		repository.deleteDeptRelationsById(id);
	}

	/**
	 * 更新部门关系
	 *
	 * @param relation
	 */
	@Override
	public void updateDeptRelation(DeptRelationDo relation) {
		repository.deleteDeptRelations(relation);
		List<DeptRelationDo> listDeptRelationDo = repository.findListByDeptDto(relation);
		saveBatch(listDeptRelationDo);
	}

}
