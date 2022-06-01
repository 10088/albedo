package com.albedo.java.modules.tenant.strategy.impl;

import com.albedo.java.common.core.constant.ParameterKey;
import com.albedo.java.common.core.context.ContextUtil;
import com.albedo.java.modules.sys.domain.ApplicationDo;
import com.albedo.java.modules.sys.domain.MenuDo;
import com.albedo.java.modules.sys.domain.ParameterDo;
import com.albedo.java.modules.sys.domain.UserDo;
import com.albedo.java.modules.sys.domain.enums.ApplicationAppTypeEnum;
import com.albedo.java.modules.sys.service.*;
import com.albedo.java.modules.tenant.domain.dto.TenantConnectDto;
import com.albedo.java.modules.tenant.strategy.InitSystemStrategy;
import com.baidu.fsg.uid.UidGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化规则:
 * 手动创建数据
 *
 * @author somewhere
 * @date 2020年04月05日13:14:28
 */
@Service("COLUMN")
@Slf4j
@RequiredArgsConstructor
public class ColumnInitSystemStrategy implements InitSystemStrategy {
	private static final String NOTICE = "notice";
	private static final String TODO = "todo";
	private static final String DONE = "done";
	private static final String STARTED = "started";
	private static final String ORG = "org";
	private static final String STATION = "station";
	private static final String USER = "user";
	private static final String MENU = "menu";
	private static final String ROLE = "role";
	private static final String DICTIONARY = "dictionary";
	private static final String AREA = "area";
	private static final String PARAMETER = "parameter";
	private static final String APPLICATION = "application";
	private static final String ONLINE = "online";
	private static final String OPT_LOG = "optLog";
	private static final String LOGIN_LOG = "loginLog";
	private static final String SMS = "sms";
	private static final String SMS_TEMPLATE = "smsTemplate";
	private static final String MSG = "msg";
	private static final String ATTACHMENT = "attachment";
	private final MenuService menuService;
	private final RoleService roleService;
	private final RoleMenuService roleMenuService;
	private final ApplicationService applicationService;
	private final DictService dictService;
	private final ParameterService parameterService;
	private final UidGenerator uidGenerator;
	private final UserService userService;

	/**
	 * 我*，这种方式太脑残了，但想不出更好的方式初始化数据，希望各位大神有好的初始化方法记得跟我说声！！！
	 * 写这段代码写得想去si ~~~
	 * <p>
	 * 不能用 SCHEMA 模式的初始化脚本方法： 因为id 会重复，租户编码会重复！
	 *
	 * @param tenantConnect 待初始化租户编码
	 * @return 是否陈成功
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean initConnect(TenantConnectDto tenantConnect) {
		String tenant = tenantConnect.getTenant();
		// 初始化数据
		//1, 生成并关联 ID TENANT
		ContextUtil.setTenant(tenant);

		// 菜单 资源 角色 角色_资源 字典 参数
		List<MenuDo> menuDoList = new ArrayList<>();
		Map<String, Long> menuMap = new HashMap<>();
//        boolean menuFlag = initMenu(menuList, menuMap);
////
////        List<Resource> resourceList = new ArrayList<>();
////        boolean resourceFlag = initResource(resourceList, menuMap);
//
//        // 角色
//        Long roleId = uidGenerator.getUid();
//        boolean roleFlag = initRole(roleId);
//
//        // 资源权限
//        boolean roleAuthorityFlag = initRoleAuthority(menuList, roleId);
//
//        // 字典
//        initDictionary();

		//参数
		initParameter();

		initApplication();

		// 内置超级管理员
		initSuperUser();

//        return menuFlag  && roleFlag && roleAuthorityFlag;
		return true;
	}

	private boolean initApplication() {
		List<ApplicationDo> list = new ArrayList<>();
		list.add(ApplicationDo.builder().clientId("albedo_web").clientSecret("albedo_web_secret").website("http://somewhere.top:10000/albedo-web/").name("微服务快速开发管理后台").appType(ApplicationAppTypeEnum.PC).state(true).build());
		return applicationService.saveBatch(list);
	}

	private boolean initSuperUser() {
		UserDo userDo = UserDo.builder()
			.username("admin").password("11111")
			.nickname("超级管理员").avatar("cnrhVkzwxjPwAaCfPbdc.png")
			.build();
		return userService.initUser(userDo);
	}

	private boolean initParameter() {
		List<ParameterDo> list = new ArrayList<>();
		list.add(ParameterDo.builder().key(ParameterKey.LOGIN_POLICY).name("登录策略").value(ParameterKey.LoginPolicy.MANY.name()).describe("ONLY_ONE:一个用户只能登录一次; MANY:用户可以任意登录; ONLY_ONE_CLIENT:一个用户在一个应用只能登录一次").state(true).readonly(true).build());
		return parameterService.saveBatch(list);
	}

//    private boolean initRoleAuthority(List<Menu> menuList, Long roleId) {
//        List<RoleMenu> roleAuthorityList = new ArrayList<>();
//        menuList.forEach(item ->
//                roleAuthorityList.add(RoleAuthority.builder().authorityType(AuthorizeType.MENU).authorityId(item.getId()).roleId(roleId).build())
//        );
//        resourceList.forEach(item ->
//                roleAuthorityList.add(RoleAuthority.builder().authorityType(AuthorizeType.RESOURCE).authorityId(item.getId()).roleId(roleId).build())
//        );
//        return roleAuthorityService.saveBatch(roleAuthorityList);
//    }
//
//    private boolean initRole(Long roleId) {
//        Role role = Role.builder().id(roleId).name("超级管理员").code("SUPER_ADMIN").describe("内置超级管理员").dsType(DataScopeType.ALL).readonly(true).build();
//        return roleService.save(role);
//    }
//
//    private boolean initMenu(List<Menu> menuList, Map<String, Long> menuMap) {
//        Long workbenchId = uidGenerator.getUid();
//        Long organizationId = uidGenerator.getUid();
//        Long resourcesId = uidGenerator.getUid();
//        Long activitiId = uidGenerator.getUid();
//        Long systemId = uidGenerator.getUid();
//        Long gatewayId = uidGenerator.getUid();
//        // 1级菜单
//        menuList.add(Menu.builder().id(workbenchId).label("工作台").path("/workbench").component("Layout").icon("fa fa-tachometer").sortValue(20).readonly(true).build());
//        menuList.add(Menu.builder().id(organizationId).label("组织管理").path("/org").component("Layout").icon("fa fa-users").sortValue(30).readonly(true).build());
//        menuList.add(Menu.builder().id(resourcesId).label("资源中心").path("/resources").component("Layout").icon("fa fa-cloud").sortValue(40).readonly(true).build());
//        menuList.add(Menu.builder().id(activitiId).label("流程管理").path("/activiti").component("Layout").icon("fa fa-retweet").sortValue(50).readonly(true).build());
//        menuList.add(Menu.builder().id(systemId).label("系统设置").path("/system").component("Layout").icon("fa fa-gears").sortValue(60).readonly(true).build());
//        menuList.add(Menu.builder().id(gatewayId).label("网关管理").path("/gateway").component("Layout").icon("fa fa-sort-amount-asc").sortValue(70).readonly(true).build());
//
//        // 工作台
//        Long noticeId = uidGenerator.getUid();
//        menuMap.put(NOTICE, noticeId);
//        menuList.add(Menu.builder().id(noticeId).parentId(workbenchId).label("通知公告").path("/workbench/notice").component("albedo/workbench/notice/index").sortValue(10).readonly(true).build());
//        Long todoId = uidGenerator.getUid();
//        menuMap.put(TODO, todoId);
//        menuList.add(Menu.builder().id(todoId).parentId(workbenchId).label("待我审批").path("/workbench/todo").component("albedo/workbench/todo/index").sortValue(20).readonly(true).build());
//        Long doneId = uidGenerator.getUid();
//        menuMap.put(DONE, doneId);
//        menuList.add(Menu.builder().id(doneId).parentId(workbenchId).label("我已审批").path("/workbench/done").component("albedo/workbench/done/index").sortValue(30).readonly(true).build());
//        Long startedId = uidGenerator.getUid();
//        menuMap.put(STARTED, startedId);
//        menuList.add(Menu.builder().id(startedId).parentId(workbenchId).label("我发起的").path("/workbench/started").component("albedo/workbench/started/index").sortValue(40).readonly(true).build());
//
//        // 组织管理
//        Long orgId = uidGenerator.getUid();
//        menuMap.put(ORG, orgId);
//        menuList.add(Menu.builder().id(orgId).parentId(organizationId).label("机构管理").path("/org/org").component("albedo/org/org/index").sortValue(10).readonly(true).build());
//        Long stationId = uidGenerator.getUid();
//        menuMap.put(STATION, stationId);
//        menuList.add(Menu.builder().id(stationId).parentId(organizationId).label("岗位管理").path("/org/station").component("albedo/org/station/index").sortValue(20).readonly(true).build());
//        Long userId = uidGenerator.getUid();
//        menuMap.put(USER, userId);
//        menuList.add(Menu.builder().id(userId).parentId(organizationId).label("用户管理").path("/org/user").component("albedo/org/user/index").sortValue(30).readonly(true).build());
//
//        // 资源中心
//        Long msgId = uidGenerator.getUid();
//        menuMap.put(MSG, msgId);
//        menuList.add(Menu.builder().id(msgId).parentId(resourcesId).label("消息中心").path("/resources/msg").component("albedo/resources/msg/index").sortValue(10).readonly(true).build());
//        Long smsTemplateId = uidGenerator.getUid();
//        menuMap.put(SMS_TEMPLATE, smsTemplateId);
//        menuList.add(Menu.builder().id(smsTemplateId).parentId(resourcesId).label("短信模版").path("/resources/smsTemplate").component("albedo/resources/smsTemplate/index").sortValue(20).readonly(true).build());
//        Long smsId = uidGenerator.getUid();
//        menuMap.put(SMS, smsId);
//        menuList.add(Menu.builder().id(smsId).parentId(resourcesId).label("短信中心").path("/resources/sms").component("albedo/resources/sms/index").sortValue(30).readonly(true).build());
//        Long attachmentId = uidGenerator.getUid();
//        menuMap.put(ATTACHMENT, attachmentId);
//        menuList.add(Menu.builder().id(attachmentId).parentId(resourcesId).label("附件管理").path("/resources/attachment").component("albedo/resources/attachment/index").sortValue(40).readonly(true).build());
//
//        // 流程管理
//        Long leaveId = uidGenerator.getUid();
//        Long reId = uidGenerator.getUid();
//        menuList.add(Menu.builder().parentId(activitiId).label("流程部署").path("/activiti/deploymentManager").component("albedo/activiti/deploymentManager/index").sortValue(10).readonly(true).build());
//        menuList.add(Menu.builder().parentId(activitiId).label("模型管理").path("/activiti/modelManager").component("albedo/activiti/modelManager/index").sortValue(20).readonly(true).build());
//        menuList.add(Menu.builder().id(leaveId).parentId(activitiId).label("请假流程").path("/activiti/leave").component("Layout").sortValue(30).readonly(true).build());
//        menuList.add(Menu.builder().id(reId).parentId(activitiId).label("报销流程").path("/activiti/reimbursement").component("albedo/activiti/reimbursement/Index").sortValue(40).readonly(true).build());
//        menuList.add(Menu.builder().parentId(leaveId).label("请假管理").path("/activiti/leave/instant").component("albedo/activiti/leave/instantManager/index").sortValue(1).readonly(true).build());
//        menuList.add(Menu.builder().parentId(leaveId).label("请假任务").path("/activiti/leave/ruTask").component("albedo/activiti/leave/ruTask/index").sortValue(2).readonly(true).build());
//        menuList.add(Menu.builder().parentId(reId).label("报销管理").path("/activiti/reimbursement/instantManager").component("albedo/activiti/reimbursement/instantManager/index").sortValue(1).readonly(true).build());
//        menuList.add(Menu.builder().parentId(reId).label("报销任务").path("/activiti/reimbursement/ruTask").component("albedo/activiti/reimbursement/ruTask/index").sortValue(2).readonly(true).build());
//
//        // 系统管理
//        Long menuId = uidGenerator.getUid();
//        menuMap.put(MENU, menuId);
//        menuList.add(Menu.builder().id(menuId).parentId(systemId).label("菜单管理").path("/system/menu").component("albedo/system/menu/index").sortValue(10).readonly(true).build());
//        Long roleId = uidGenerator.getUid();
//        menuMap.put(ROLE, roleId);
//        menuList.add(Menu.builder().id(roleId).parentId(systemId).label("角色管理").path("/system/role").component("albedo/system/role/index").sortValue(20).readonly(true).build());
//        Long dictionaryId = uidGenerator.getUid();
//        menuMap.put(DICTIONARY, dictionaryId);
//        menuList.add(Menu.builder().id(dictionaryId).parentId(systemId).label("字典管理").path("/system/dictionary").component("albedo/system/dictionary/index").sortValue(30).readonly(true).build());
//        Long areaId = uidGenerator.getUid();
//        menuMap.put(AREA, areaId);
//        menuList.add(Menu.builder().id(areaId).parentId(systemId).label("地区管理").path("/system/area").component("albedo/system/area/index").sortValue(40).readonly(true).build());
//        Long parameterId = uidGenerator.getUid();
//        menuMap.put(PARAMETER, parameterId);
//        menuList.add(Menu.builder().id(parameterId).parentId(systemId).label("参数管理").path("/system/parameter").component("albedo/system/parameter/index").sortValue(50).readonly(true).build());
//        Long optLogId = uidGenerator.getUid();
//        menuMap.put(OPT_LOG, optLogId);
//        menuList.add(Menu.builder().id(optLogId).parentId(systemId).label("操作日志").path("/system/optLog").component("albedo/system/optLog/index").sortValue(60).readonly(true).build());
//        Long loginLogId = uidGenerator.getUid();
//        menuMap.put(LOGIN_LOG, loginLogId);
//        menuList.add(Menu.builder().id(loginLogId).parentId(systemId).label("登录日志").path("/system/loginLog").component("albedo/system/loginLog/index").sortValue(70).readonly(true).build());
//        Long onlineId = uidGenerator.getUid();
//        menuMap.put(ONLINE, onlineId);
//        menuList.add(Menu.builder().id(onlineId).parentId(systemId).label("在线用户").path("/system/online").component("albedo/system/online/index").sortValue(80).readonly(true).build());
//        Long applicationApi = uidGenerator.getUid();
//        menuMap.put(APPLICATION, applicationApi);
//        menuList.add(Menu.builder().id(applicationApi).parentId(systemId).label("应用管理").path("/system/application").component("albedo/system/application/index").sortValue(90).readonly(true).build());
//
//        // 网关管理
//        menuList.add(Menu.builder().parentId(gatewayId).label("限流规则").path("/gateway/ratelimiter").component("albedo/gateway/ratelimiter/index").sortValue(10).readonly(true).build());
//        menuList.add(Menu.builder().parentId(gatewayId).label("阻止访问").path("/gateway/blocklist").component("albedo/gateway/blocklist/index").sortValue(20).readonly(true).build());
//        return menuService.saveBatch(menuList);
//    }
//
//    private boolean initDictionary() {
//        List<Dictionary> dictionaryList = new ArrayList<>();
//        Integer nationSort = 1;
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("01").name("汉族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("02").name("壮族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("03").name("满族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("04").name("回族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("05").name("苗族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("06").name("维吾尔族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("07").name("土家族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("08").name("彝族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("09").name("蒙古族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("10").name("藏族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("11").name("布依族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("12").name("侗族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("13").name("瑶族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("14").name("朝鲜族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("15").name("白族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("16").name("哈尼族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("17").name("哈萨克族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("18").name("黎族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("19").name("傣族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("20").name("畲族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("21").name("傈僳族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("22").name("仡佬族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("23").name("东乡族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("24").name("高山族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("25").name("拉祜族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("26").name("水族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("27").name("佤族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("28").name("纳西族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("29").name("羌族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("30").name("土族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("31").name("仫佬族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("32").name("锡伯族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("33").name("柯尔克孜族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("34").name("达斡尔族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("35").name("景颇族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("36").name("毛南族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("37").name("撒拉族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("38").name("塔吉克族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("39").name("阿昌族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("40").name("普米族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("41").name("鄂温克族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("42").name("怒族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("43").name("京族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("44").name("基诺族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("45").name("德昂族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("46").name("保安族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("47").name("俄罗斯族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("48").name("裕固族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("49").name("乌兹别克族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("50").name("门巴族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("51").name("鄂伦春族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("52").name("独龙族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("53").name("塔塔尔族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("54").name("赫哲族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("55").name("珞巴族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("56").name("布朗族").sortValue(nationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.NATION).label("民族")
//                .code("57").name("其他").sortValue(nationSort++).readonly(true).build());
//
//        Integer positionStatusSort = 1;
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.POSITION_STATUS).label("职位状态")
//                .code("01").name("在职").sortValue(positionStatusSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.POSITION_STATUS).label("职位状态")
//                .code("02").name("请假").sortValue(positionStatusSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.POSITION_STATUS).label("职位状态")
//                .code("03").name("离职").sortValue(positionStatusSort++).readonly(true).build());
//
//        Integer educationSort = 1;
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.EDUCATION).label("学历")
//                .code("01").name("小学").sortValue(educationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.EDUCATION).label("学历")
//                .code("02").name("中学").sortValue(educationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.EDUCATION).label("学历")
//                .code("03").name("高中").sortValue(educationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.EDUCATION).label("学历")
//                .code("04").name("专科").sortValue(educationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.EDUCATION).label("学历")
//                .code("05").name("本科").sortValue(educationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.EDUCATION).label("学历")
//                .code("06").name("硕士").sortValue(educationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.EDUCATION).label("学历")
//                .code("07").name("博士").sortValue(educationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.EDUCATION).label("学历")
//                .code("08").name("博士后").sortValue(educationSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.EDUCATION).label("学历")
//                .code("20").name("其他").sortValue(educationSort++).readonly(true).build());
//
//        Integer areaLevelSort = 1;
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.AREA_LEVEL).label("行政区划")
//                .code("01").name("国家").sortValue(areaLevelSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.AREA_LEVEL).label("行政区划")
//                .code("02").name("省份/直辖市").sortValue(areaLevelSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.AREA_LEVEL).label("行政区划")
//                .code("03").name("地市").sortValue(areaLevelSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.AREA_LEVEL).label("行政区划")
//                .code("04").name("区县").sortValue(areaLevelSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.AREA_LEVEL).label("行政区划")
//                .code("05").name("乡镇").sortValue(areaLevelSort++).readonly(true).build());
//
//
//        Integer orgTypeSort = 1;
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.ORG_TYPE).label("机构类型")
//                .code("01").name("单位").sortValue(orgTypeSort++).readonly(true).build());
//        dictionaryList.add(Dictionary.builder().type(DictionaryType.ORG_TYPE).label("机构类型")
//                .code("02").name("部门").sortValue(orgTypeSort++).readonly(true).build());
//        return dictionaryService.saveBatch(dictionaryList);
//    }

	@Override
	public boolean reset(String tenant) {
		//TODO 待实现
		// 1，清空所有表的数据
		// 2，重新初始化 tenant
		// 3，重新初始化 业务数据
		//        init(tenant);
		return true;
	}

	@Override
	public boolean delete(List<Long> ids, List<String> tenantCodeList) {
		// 清空所有表中当前租户的数据
		//TODO 待实现
		// 1,查询系统中的所有表
		// 删除该租户的所有数据
		return true;
	}
}
