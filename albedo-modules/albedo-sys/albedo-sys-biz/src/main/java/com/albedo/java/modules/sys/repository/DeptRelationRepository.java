
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

package com.albedo.java.modules.sys.repository;

import com.albedo.java.modules.sys.domain.DeptRelationDo;
import com.albedo.java.plugins.database.mybatis.repository.BaseRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author somewhere
 * @since 2019/2/1
 */
@Mapper
public interface DeptRelationRepository extends BaseRepository<DeptRelationDo> {

	/**
	 * 删除部门关系表数据
	 *
	 * @param id 部门ID
	 */
	void deleteDeptRelationsById(Long id);

	/**
	 * 更改部分关系表数据
	 *
	 * @param deptRelationDo
	 * @return Integer
	 */
	Integer deleteDeptRelations(DeptRelationDo deptRelationDo);

	/**
	 * @param deptRelationDo
	 * @return List<DeptRelation>
	 */
	List<DeptRelationDo> findListByDeptDto(DeptRelationDo deptRelationDo);

}
