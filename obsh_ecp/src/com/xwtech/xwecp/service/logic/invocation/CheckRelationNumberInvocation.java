package com.xwtech.xwecp.service.logic.invocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.springframework.context.ApplicationContext;

import com.xwtech.xwecp.XWECPApp;
import com.xwtech.xwecp.communication.IRemote;
import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.msg.RequestParameter;
import com.xwtech.xwecp.pojo.ChildBusinessInfo;
import com.xwtech.xwecp.service.BaseServiceInvocationResult;
import com.xwtech.xwecp.service.ILogicalService;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.logic.pojo.Effect;
import com.xwtech.xwecp.service.logic.pojo.ErrorMapping;
import com.xwtech.xwecp.service.logic.pojo.GetMsngSubPackDT;
import com.xwtech.xwecp.service.logic.pojo.QRY050029Result;
import com.xwtech.xwecp.service.logic.pojo.RelationPkg;
import com.xwtech.xwecp.service.server.DefaultServiceImpl.StringTeletext;
import com.xwtech.xwecp.teletext.BossTeletextUtil;
import com.xwtech.xwecp.util.DateTimeUtil;
import com.xwtech.xwecp.util.ParseXmlConfig;

/**
 * 亲情号码组合开通校验
 * 
 * @author 王平
 */
public class CheckRelationNumberInvocation extends BaseInvocation implements
		ILogicalService {
	private static final Logger logger = Logger
			.getLogger(CheckRelationNumberInvocation.class);

	private BossTeletextUtil bossTeletextUtil;

	private IRemote remote;

	private WellFormedDAO wellFormedDAO;

	private Map<String, String> map = null;

	private Map<String, String> mapCode = null;

	private ParseXmlConfig config;

	private static final String RELATION_CODE = "QQHMZHKT";

	/**
	 * 两个号码不属于同一个城市
	 */
	private static final String ERROR_CODE_DIFFERENT_CITY = "different_city";
	/**
	 * 号码不存在
	 */
	private static final String ERROR_CODE_MOBILE_NOT_FOUND = "mobile_not_found";
	/**
	 * 不许创建亲情号码组合
	 */
	private static final String ERROR_CODE_CREATE_RELATION_NOT_ALLOWED = "create_relation_not_allowed";
	/**
	 * 亲情号码组合已经加满
	 */
	private static final String ERROR_CODE_RELATION_FULL = "relation_full";
	/**
	 * 提示使用已有组合亲情号码套餐
	 */
	private static final String ERROR_CODE_DEFAULT_SET_MEAL_PROMPT = "default_set_meal_prompt";
	/**
	 * 不许加入亲情号码
	 */
	private static final String ERROR_CODE_JOIN_RELATION_NOT_ALLOWED = "join_relation_not_allowed";
	/**
	 * 两个号码分别属于两个不同的亲情号码组合
	 */
	private static final String ERROR_CODE_TWO_GROUP = "two_group";

	/**
	 * 亲情号码组合上限
	 */
	private static final int RELATION_MAX = 5;

	/**
	 * 最低消费30元
	 */
	private static final String RELATION_30 = "QQHMZHKT_ZDXF30Y";
	/**
	 * 最低消费50元
	 */
	private static final String RELATION_50 = "QQHMZHKT_ZDXF50Y";
	/**
	 * 1元功能费
	 */
	private static final String RELATION_1 = "QQHMZHKT_1YGNF";

	/**
	 * 亲情号码组合套餐开通规则（新建圈子可开通）
	 */
	private static final String OPER_TYPE_NEW_RELATION_ALLOWED_OPEN = "1";

	/**
	 * 亲情号码组合套餐开通规则（既有圈子可开通）
	 */
	private static final String OPER_TYPE_EXIST_RELATION_ALLOWED_OPEN = "2";

	/**
	 * 亲情号码组合套餐开通规则（既有圈子和新建圈子都可开通）
	 */
	private static final String OPER_TYPE_ALL_RELATION_ALLOWED_OPEN = "3";

	public CheckRelationNumberInvocation() {
		ApplicationContext springCtx = XWECPApp.SPRING_CONTEXT;
		this.bossTeletextUtil = (BossTeletextUtil) (springCtx
				.getBean("bossTeletextUtil"));
		this.remote = (IRemote) (springCtx.getBean("bossRemote"));
		this.wellFormedDAO = (WellFormedDAO) (springCtx
				.getBean("wellFormedDAO"));
		this.config = new ParseXmlConfig();

		if (null == this.map )
		{
			this.map = new HashMap<String, String>();
			this.map.put("4778", "最低消费30元");
			this.map.put("4779", "最低消费50元");
			this.map.put("4907", "1元功能费");
		}
		
		if(null == this.mapCode)
		{
			this.mapCode = new HashMap<String, String>();
			this.mapCode.put("4778", RELATION_30);
			this.mapCode.put("4779", RELATION_50);
			this.mapCode.put("4907", RELATION_1);
		}
	}

	public BaseServiceInvocationResult executeService(String accessId,
			ServiceConfig config, List<RequestParameter> params) {

		QRY050029Result res = new QRY050029Result();

		try {
			res.setResultCode(LOGIC_SUCESS);
			res.setErrorMessage("");

			String phoneNum = (String) getParameters(params, "phoneNum");
			UserInfo userLogin = getUserInfoByNumber(accessId, config, res,
					phoneNum);
			String subNum = (String) getParameters(params, "subNum");
			UserInfo invitedUser = getUserInfoByNumber(accessId, config, res,
					subNum);

			if (invitedUser.userId == null || "".equals(invitedUser.userId)) { // 用户号码不存在
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(ERROR_CODE_MOBILE_NOT_FOUND);
				res.setErrorMessage("请输入一个正确的手机号码");
			} else { // 用户号码存在
				if (this.isSameCity(userLogin, invitedUser)) { // 如果是同一城市
					params = setUserInfo(userLogin, params);
					Result loginMobileResult = getUserRelationInfo(accessId,
							config, params);
					// 当前登录用户的亲情号码组合
					List<RelationNum> loginMobileRelationList = loginMobileResult
							.getRelationNum();

					params = setUserInfo(invitedUser, params);
					Result invitedMobileResult = getSubMobileRelationInfo(
							accessId, config, params);

					String[] operInfo = operInfo(userLogin, invitedUser,
							loginMobileResult, invitedMobileResult);
					// 1：交换 2：不交换
					String isExchange = operInfo[0];
					// open:开通 add:增加
					String operType = operInfo[1];

					// 要不要交换主从号码
					res.setIsExchange(isExchange);
					// 开通方式
					res.setOperType(operType);

					if (loginMobileRelationList == null
							|| loginMobileRelationList.size() == 0) { // 登录用户没有属于任何亲情号码组合
						loginMobileHasNoRelation(loginMobileResult,
								invitedMobileResult, userLogin, invitedUser,
								res);
					} else { // 登录用户属于一个亲情号码组合
						loginMobileHasRelation(loginMobileResult,
								invitedMobileResult, userLogin, invitedUser,
								res);
					}
				} else { // 如果不是同一城市
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(ERROR_CODE_DIFFERENT_CITY);
					res.setErrorMessage("被邀请号码跟您不属于同一城市");
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
	}

	/**
	 * @param userLogin
	 * @param invitedUser
	 * @return 1：交换 2：不交换
	 */
	private String[] operInfo(UserInfo loginUser, UserInfo invitedUser,
			Result loginMobileResult, Result invitedMobileResult) {
		String[] strs = new String[2];
		String isExchange = "2";
		String operType = "open";

		// 当前登录用户预约下个月关闭，且没有预约开通
		boolean loginUserBookNextMonthClose = isBookNextMonthClose(loginUser
				.getMobile(), loginMobileResult.getRelationNum());
		// 被邀请用户预约下个月关闭，且没有预约开通
		boolean invitedUserBookNextMonthClose = isBookNextMonthClose(
				invitedUser.getMobile(), invitedMobileResult.getRelationNum());

		if (loginUserBookNextMonthClose) { // 当前登录用户预约下个月关闭，且没有预约开通
			if (invitedUserBookNextMonthClose) { // 被邀请用户预约下个月关闭，且没有预约开通
				isExchange = "2";
			} else { // 被邀请用户正常使用，或预约开通
				isExchange = "1"; // 被邀请用户是主号
				if (invitedMobileResult.getRelationNum().size() == 0) {
					operType = "open";
				} else {
					operType = "add";
				}
			}
		} else { // 当前登录用户正常使用，或预约开通

			// 如果该用户已经有组合，则为主号
			if (loginMobileResult.getRelationNum().size() > 0) {
				isExchange = "2";
			} else {
				isExchange = "1";
			}

			operType = "add";
		}

		if (loginMobileResult.getRelationNum().size() == 0
				&& invitedMobileResult.getRelationNum().size() == 0) {
			operType = "open";
		}

		strs[0] = isExchange;
		strs[1] = operType;

		return strs;
	}

	// 登录用户不属于亲情号码组合
	private void loginMobileHasNoRelation(Result loginMobileResult,
			Result invitedMobileResult, UserInfo loginUser,
			UserInfo invitedUser, QRY050029Result res) {
		// 被邀请用户的亲情号码组合
		List<RelationNum> invitedMobileRelationList = invitedMobileResult
				.getRelationNum();

		if (isOpen(invitedUser.getMobile(), invitedMobileRelationList)) { // 对方已经开通亲情号码组合
			if (this.isBookClose(invitedUser.getMobile(), invitedMobileResult
					.getRelationNum())) { // 对方已经预约关闭亲情号码组合
				if (this.isCreateRelationAllowed(loginUser.getDdrCity(),
						loginUser.getBrand())) { // 允许创建亲情号码组合
					List<RelationPkg> relationPkgList = this.getSetMealList(
							loginUser.getDdrCity(), loginUser.getBrand());
					res.setRelationPkgList(relationPkgList);

					List<Effect> effectList = new ArrayList<Effect>();
					Effect effect = new Effect();
					effect.setEffectId("2");
					effect.setEffectName("次月");
					effectList.add(effect);
					res.setEffectList(effectList);
				} else { // 允许创建亲情号码组合
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(ERROR_CODE_CREATE_RELATION_NOT_ALLOWED);
					res.setErrorMessage("您所在的城市不允许开通亲情号码组合");
				}
			} else { // 对方没有预约关闭亲情号码组合
				List<RelationNum> relationNumList = invitedMobileResult
						.getRelationNum();
				String relationCode = "";
				for (RelationNum relationNum : relationNumList) {
					String offerType = relationNum.getOfferType();
					if (offerType != null && !"".equals(offerType)) {
						relationCode = offerType;
						break;
					}
				}

				List<PkgUse> pkgUseList = invitedMobileResult.getPkgUse();
				String relationName = "";
				for (PkgUse pkgUse : pkgUseList) {
					String relation = pkgUse.getName();
					if (relation != null && !"".equals(relation)) {
						relationName = relation;
						break;
					}
				}

				if (this.isJoinRelationAllowed(loginUser.getDdrCity(),
						relationCode, loginUser.getBrand(), true)) { // 后台配置允许客户加入该套餐的亲情号码组合
					if (this.isRelationFull(invitedMobileResult
							.getRelationNum())) { // 对方亲情号码组合已满
						res.setResultCode(LOGIC_ERROR);
						res.setErrorCode(ERROR_CODE_RELATION_FULL);
						res.setErrorMessage("对方亲情号码组合已满，您不能加入");
					} else { // 对方亲情号码组合未满

						List<RelationPkg> relationPkgList = new ArrayList<RelationPkg>();
						RelationPkg relationPkg = new RelationPkg();
						relationPkg.setRelationId(relationCode);
						relationPkg.setRelationName(relationName);
						relationPkgList.add(relationPkg);
						res.setRelationPkgList(relationPkgList);

						List<Effect> effectList = new ArrayList<Effect>();
						Effect effect = new Effect();
						effect.setEffectId("1");
						effect.setEffectName("次日");
						effectList.add(effect);

						effect = new Effect();
						effect.setEffectId("2");
						effect.setEffectName("次月");
						effectList.add(effect);
						res.setEffectList(effectList);

						res.setResultCode(LOGIC_INFO);
						// 提示默认的亲情号码套餐（点击弹出介绍），提示用户是否需要加入，什么时候生效
						res.setErrorCode(ERROR_CODE_DEFAULT_SET_MEAL_PROMPT);
					}
				} else { // 后台配置不允许客户加入该套餐的亲情号码组合
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(ERROR_CODE_JOIN_RELATION_NOT_ALLOWED);
					res.setErrorMessage("您要求加入的亲情号码组合的套餐不可以被申请加入");
				}
			}
		} else { // 对方未开通亲情号码组合
			String ddrCity = loginUser.getDdrCity();
			String brand = loginUser.getBrand();
			if (this.isCreateRelationAllowed(ddrCity, brand)) { // 这个手机号码所在的城市允许开通亲情号码组合
				List<RelationPkg> relationPkgList = this.getSetMealList(
						ddrCity, brand);
				res.setRelationPkgList(relationPkgList);

				List<Effect> effectList = new ArrayList<Effect>();
				Effect effect = new Effect();
				effect.setEffectId("1");
				effect.setEffectName("次日");
				effectList.add(effect);

				effect = new Effect();
				effect.setEffectId("2");
				effect.setEffectName("次月");
				effectList.add(effect);
				res.setEffectList(effectList);
			} else { // 这个手机号码所在的城市不允许开通亲情号码组合
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(ERROR_CODE_CREATE_RELATION_NOT_ALLOWED);
				res.setErrorMessage("您所在的城市不允许开通亲情号码组合");
			}
		}
	}

	// 登录用户属于亲情号码组合
	private void loginMobileHasRelation(Result loginMobileResult,
			Result invitedMobileResult, UserInfo loginUser,
			UserInfo invitedUser, QRY050029Result res) {
		if (isBookNextMonthClose(loginUser.getMobile(), loginMobileResult
				.getRelationNum())) { // 登录用户预约月底关闭
			if (this.isSameCity(loginUser, invitedUser)) { // 属于同一地市
				// 对方是否没有开通亲情号码组合
				boolean hasNoRelation = false;

				List<RelationNum> invitedRelationNumList = invitedMobileResult
						.getRelationNum();
				if (isOpen(invitedUser.getMobile(), invitedRelationNumList)) { // 对方已经开通亲情号码组合
					if (isBookClose(invitedUser.getMobile(),
							invitedRelationNumList)) { // 对方亲情号码已经预约关闭
						hasNoRelation = true;
					} else { // 对方亲情号码正常使用
						res.setResultCode(LOGIC_ERROR);
						res.setErrorCode(ERROR_CODE_TWO_GROUP);
						res.setErrorMessage("您和被邀请号码属于两个不同的亲情号码组合，不能成为亲情号码");
					}
				} else {// 对方未开通亲情号码组合
					hasNoRelation = true;
				}

				if (hasNoRelation) { // 对方未开通亲情号码组合 或 对方已经预约关闭亲情号码组合
					String ddrCity = invitedUser.getDdrCity();
					String brand = invitedUser.getBrand();
					if (isCreateRelationAllowed(ddrCity, brand)) { // 后台配置允许新建亲情号码组合
						List<RelationPkg> relationPkgList = this
								.getSetMealList(ddrCity, brand);
						res.setRelationPkgList(relationPkgList);

						List<Effect> effectList = new ArrayList<Effect>();
						Effect effect = new Effect();
						effect.setEffectId("2");
						effect.setEffectName("次月");
						effectList.add(effect);
						res.setEffectList(effectList);
					} else { // 后台配置不允许新建亲情号码组合
						res.setResultCode(LOGIC_ERROR);
						res
								.setErrorCode(ERROR_CODE_CREATE_RELATION_NOT_ALLOWED);
						res.setErrorMessage("您所在的城市不允许开通亲情号码组合");
					}
				}
			} else { // 不属于同一地市
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(ERROR_CODE_DIFFERENT_CITY);
				res.setErrorMessage("被邀请号码跟您不属于同一城市");
			}
		} else { // 登录用户正常使用亲情号码
			if (isRelationFull(loginMobileResult.getRelationNum())) { // 自己亲情号码组合已满
				res.setResultCode(LOGIC_ERROR);
				res.setErrorCode(ERROR_CODE_RELATION_FULL);
				res.setErrorMessage("您所在的亲情号码组合已满，被邀请号码不能加入");
			} else {
				if (this.isSameCity(loginUser, invitedUser)) { // 属于同一地市
					// 获取套餐名称
					List<RelationNum> relationNumList = loginMobileResult
							.getRelationNum();
					String relationCode = "";
					for (RelationNum relationNum : relationNumList) {
						String offerType = relationNum.getOfferType();
						if (offerType != null && !"".equals(offerType)) {
							relationCode = offerType;
							break;
						}
					}

					if (isJoinRelationAllowed(loginUser.getDdrCity(),
							relationCode, loginUser.getBrand(), false)) { // 后台配置允许客户所属套餐加入亲情号码组合
						List<Effect> effectList = new ArrayList<Effect>();
						Effect effect = new Effect();

						// 是否可以被邀请加入亲情号码组合
						boolean isAvailable = false;

						List<RelationNum> invitedRelationNumList = invitedMobileResult
								.getRelationNum();
						if (isOpen(invitedUser.getMobile(),
								invitedRelationNumList)) { // 对方已经开通亲情号码组合
							if (this.isBookNextMonthClose(invitedUser
									.getMobile(), invitedRelationNumList)) { // 对方已经预约关闭
								effect.setEffectId("2");
								effect.setEffectName("次月");
								effectList.add(effect);

								isAvailable = true;
							} else { // 对方未预约关闭
								res.setResultCode(LOGIC_ERROR);
								res.setErrorCode(ERROR_CODE_TWO_GROUP);
								res
										.setErrorMessage("被邀请号码属于一个亲情号码组合且没有预约退出，不能被邀请加入");
							}
						} else { // 对方未开通亲情号码组合
							effect.setEffectId("1");
							effect.setEffectName("次日");
							effectList.add(effect);

							effect = new Effect();
							effect.setEffectId("2");
							effect.setEffectName("次月");
							effectList.add(effect);

							isAvailable = true;
						}

						if (isAvailable) {
							// 根据当前的地市，查找当前使用的套餐
							List<RelationPkg> relationPkgList = this
									.getSetMealList(loginUser.getDdrCity(),
											loginUser.getBrand());
							List<RelationPkg> currentRelationPkgList = new ArrayList<RelationPkg>();
							for (RelationPkg relation : relationPkgList) {
								String relationId = relation.getRelationId();
								if (relationId != null
										&& relationId.equals(relationCode)) {
									currentRelationPkgList.add(relation);
									break;
								}
							}
							res.setRelationPkgList(currentRelationPkgList);
							res.setEffectList(effectList);
						}
					} else { // 后台配置不允许客户所属套餐加入亲情号码组合
						res.setResultCode(LOGIC_ERROR);
						res.setErrorCode(ERROR_CODE_JOIN_RELATION_NOT_ALLOWED);
						res.setErrorMessage("您的亲情号码所使用的套餐不可以被申请加入");
					}
				} else { // 不属于同一地市
					res.setResultCode(LOGIC_ERROR);
					res.setErrorCode(ERROR_CODE_DIFFERENT_CITY);
					res.setErrorMessage("被邀请号码跟您不属于同一城市");
				}
			}
		}
	}

	// ******************************************************* 以下是check的业务逻辑
	// *******************************************************
	/**
	 * 将用户信息设置到参数中
	 * 
	 * @param user1
	 * @param user2
	 * @return 相同城市 true 不同城市 false
	 */
	private boolean isSameCity(UserInfo user1, UserInfo user2) {
		boolean isSameCity = false;
		String city1 = user1.getDdrCity();
		String city2 = user2.getDdrCity();
		if (city1 != null && city1.length() > 0) {
			if (city1.equals(city2)) {
				isSameCity = true;
			}
		}

		return isSameCity;
	}

	/**
	 * 判断亲情号码组合是否已经加满
	 * 
	 * @param list
	 * @return
	 */
	private boolean isRelationFull(List<RelationNum> list) {
		List<RelationNum> relationList = list;
		List<RelationNum> newRelationList = new ArrayList<RelationNum>();
		// 过滤掉重复的号码
		for (RelationNum relation : relationList) {
			String phoneNum = relation.getPhoneNum();

			boolean isAdd = true;
			for (RelationNum newRelation : newRelationList) {
				String newPhoneNum = newRelation.getPhoneNum();

				if (newPhoneNum.equals(phoneNum)) {
					isAdd = false;
				}
			}

			if (isAdd) {
				newRelationList.add(relation);
			}
		}

		boolean isRelationFull = false;

		if (RELATION_MAX == newRelationList.size()) {
			isRelationFull = true;
		}
		return isRelationFull;
	}

	/**
	 * 判断是否开通了亲情号码组合 预约开通相当于开通
	 * 
	 * @param mobile
	 * @param relationNumList
	 * @return
	 */
	private boolean isOpen(String mobile, List<RelationNum> relationNumList) {
		boolean isOpen = false;

		if (relationNumList != null && relationNumList.size() > 0) {
			isOpen = true;
		} else {
			isOpen = isBookOpen(mobile, relationNumList);
		}
		return isOpen;
	}

	/**
	 * mobile是否预约开通
	 * 
	 * @param mobile
	 * @param list
	 * @return true:是 false:不是
	 */
	private boolean isBookOpen(String mobile, List<RelationNum> list) {
		boolean isBookOpen = false;

		if (list != null && list.size() > 0) {
			for (RelationNum relation : list) {
				if (relation.getPhoneNum().equals(mobile)) {
					// flag 1、在用 0、预约
					int flag = relation.getFlag();
					if (flag == 0) {
						String beginDate = relation.getBeginDate();
						if (beginDate != null && beginDate.length() > 0) {
							long year = Long.parseLong(beginDate
									.substring(0, 4));
							long month = Long.parseLong(beginDate.substring(5,
									7));
							long day = Long.parseLong(beginDate
									.substring(8, 10));

							long currentYear = Long.parseLong(DateTimeUtil
									.getTodayYear());
							long currentMonth = Long.parseLong(DateTimeUtil
									.getTodayMonth());
							long currentDay = Long.parseLong(DateTimeUtil
									.getTodayDay());

							if (year > currentYear) {
								isBookOpen = true;
							} else if (month > currentMonth) {
								isBookOpen = true;
							} else if (day > currentDay) {
								isBookOpen = true;
							}
						}
					}
				}
			}
		}

		return isBookOpen;
	}

	/**
	 * mobile是否预约关闭
	 * 
	 * @param mobile
	 * @param list
	 * @return true:是 false:不是
	 */
	private boolean isBookClose(String mobile, List<RelationNum> list) {
		boolean isBookClose = false;

		if (list != null && list.size() > 0) {
			for (RelationNum relation : list) {
				if (null != relation.getEndDate()
						&& !"".equals(relation.getEndDate())) {
					isBookClose = true;
				}
			}
		}

		return isBookClose;
	}

	/**
	 * 该客户的亲情号码组合是否预约在月底关闭，且不在下月开通
	 * 
	 * @param mobile
	 * @param list
	 * @return true:是 false:不是
	 */
	private boolean isBookNextMonthClose(String mobile, List<RelationNum> list) {
		boolean isBookNextMonthClose = false;

		if (list != null && list.size() > 0) {
			for (RelationNum relation : list) {
				if (relation.getPhoneNum().equals(mobile)) {
					String endDate = relation.getEndDate(); // 亲情号码如果关闭一定是月底生效
					if (endDate != null && endDate.length() > 0) {
						isBookNextMonthClose = true;
					}

					String beginDate = relation.getBeginDate();
					long month = Long.parseLong(beginDate.substring(4, 6));
					long currentMonth = Long.parseLong(DateTimeUtil
							.getTodayMonth());

					if (month == (currentMonth + 1)) { // 开通日期为下个月，那么为预约下月开通
						isBookNextMonthClose = false;
						break;
					}
				}
			}
		}

		return isBookNextMonthClose;
	}

	/**
	 * 该城市是否允许创建亲情号码组合
	 * 
	 * @param ddrCity
	 * @return true: 允许 false：不允许
	 */
	private boolean isCreateRelationAllowed(String ddrCity, String brand) {
		boolean isCreateRelationAllowed = false;
		List<ChildBusinessInfo> list = wellFormedDAO
				.getBusinessInfoMapByAreaNum(ddrCity, RELATION_CODE, brand);
		if (list != null && !list.isEmpty()) {
			isCreateRelationAllowed = true;
		}
		return isCreateRelationAllowed;
	}

	/**
	 * 后台配置是否允许客户加入该套餐的亲情号码组合
	 * 
	 * @param ddrCity
	 *            地市编码
	 * @param setMeal
	 *            申请加入的套餐
	 * @return true: 允许 false：不允许
	 */
	private boolean isJoinRelationAllowed(String ddrCity, String setMeal,
			String brand, boolean isNewRelation) {
		// TODO 这里给定的是亲情号码组合中可以开通的或可以加入的列表
		boolean isJoinRelationAllowed = false;
		List<ChildBusinessInfo> list = wellFormedDAO
				.getBusinessInfoMapByAreaNum(ddrCity, RELATION_CODE, brand);

		if (isNewRelation) { // 新建圈子
			for (ChildBusinessInfo info : list) {
				String childBizCode = info.getBusinessNum();
				String childBizType = info.getType();

				if (childBizCode.equals(setMeal)) {
					if (OPER_TYPE_NEW_RELATION_ALLOWED_OPEN
							.equals(childBizType) // 新建圈子
							|| OPER_TYPE_ALL_RELATION_ALLOWED_OPEN
									.equals(childBizType) // 新建既有都允许开通
					) {
						isJoinRelationAllowed = true;
						break;
					}
				}
			}
		} else { // 既有圈子
			for (ChildBusinessInfo info : list) {
				String childBizCode = info.getBusinessNum();
				String childBizType = info.getType();

				if (childBizCode.equals(setMeal)) {
					if (OPER_TYPE_EXIST_RELATION_ALLOWED_OPEN
							.equals(childBizType) // 既有圈子
							|| OPER_TYPE_ALL_RELATION_ALLOWED_OPEN
									.equals(childBizType) // 新建既有都允许开通
					) {
						isJoinRelationAllowed = true;
						break;
					}
				}
			}

		}
		return isJoinRelationAllowed;
	}

	/**
	 * 根据地市查询亲情号码中可开通的业务
	 * 
	 * @param ddrCity
	 *            城市编码
	 * @return
	 */
	private List<RelationPkg> getSetMealList(String ddrCity, String brand) {
		List<ChildBusinessInfo> infoList = this.wellFormedDAO
				.getBusinessInfoMapByAreaNum(ddrCity, RELATION_CODE, brand);
		List<ChildBusinessInfo> newList = new ArrayList<ChildBusinessInfo>();

		// 去重
		ChildBusinessInfo s1 = null;
		ChildBusinessInfo s2 = null;
		Map temMap = new HashMap();
		for (int i = 0; i < infoList.size(); i++) {
			s1 = infoList.get(i);
			temMap.put(s1.getBusinessNum(), infoList.get(i));
		}

		Iterator it = temMap.keySet().iterator();

		while (it.hasNext()) {

			String key;

			key = (String) it.next();
			newList.add((ChildBusinessInfo) temMap.get(key));

		}

		List<RelationPkg> list = new ArrayList<RelationPkg>();

		for (ChildBusinessInfo info : newList) {
			String childBizName = info.getBusinessName();
			String childBizCode = info.getBusinessNum();

			RelationPkg r = new RelationPkg();
			r.setRelationId(childBizCode);
			r.setRelationName(childBizName);
			list.add(r);
		}

		return list;
	}

	/**
	 * 获取用户开通亲情号码组合的列表
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	private Result getSubMobileRelationInfo(String accessId,
			ServiceConfig config, List<RequestParameter> params) {
		Result res = new Result();
		res.getRes().setResultCode("0");
		res.getRes().setErrorMessage("");

		// 被邀请人号码
		String subNum = "";

		try {
			for (RequestParameter par : params) {
				if ("subNum".equals(par.getParameterName())) {
					subNum = String.valueOf(par.getParameterValue());
					break;
				}
			}

			if (subNum != null && !"".equals(subNum)) {
				for (RequestParameter par : params) {
					if ("phoneNum".equals(par.getParameterName())) {
						par.setParameterValue(subNum);
						res = this
								.getUserRelationInfo(accessId, config, params);
						break;
					}
				}
			}

		} catch (Exception e) {
			logger.error(e, e);
		}

		return res;
	}

	// *******************************************************
	// 以下是获取用户已经开通的亲情号码组合列表
	// *******************************************************
	/**
	 * 将用户信息设置到参数中
	 * 
	 * @param user
	 * @param params
	 * @return
	 */
	private List<RequestParameter> setUserInfo(UserInfo user,
			List<RequestParameter> params) {
		user.getDdrCity();
		for (RequestParameter para : params) {
			if ("context_ddr_city".equals(para.getParameterName())) {
				para.setParameterValue(user.getDdrCity());
			} else if ("context_route_type".equals(para.getParameterName())) {
				para.setParameterValue(user.getRouteType());
			} else if ("context_user_id".equals(para.getParameterName())) {
				para.setParameterValue(user.getUserId());
			} else if ("context_route_value".equals(para.getParameterName())) { // 查询亲情号码的列表
				para.setParameterValue(user.getMobile());
			}
		}

		return params;
	}

	/**
	 * 根据手机号码查询对应用户的信息
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param mobile
	 * @return
	 */
	private UserInfo getUserInfoByNumber(String accessId, ServiceConfig config,
			QRY050029Result res, String mobile) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		Element content = null;
		RequestParameter r = null;
		ErrorMapping errDt = null;

		UserInfo user = new UserInfo();
		user.setMobile(mobile);

		List<RequestParameter> params = new ArrayList<RequestParameter>();
		try {
			r = new RequestParameter();
			r.setParameterName("dbi_month_pr_number");
			r.setParameterValue(this.config.getTodayChar6());
			params.add(r);

			r = new RequestParameter();
			r.setParameterName("cdr_reduce_total");
			r.setParameterValue("1");
			params.add(r);

			r = new RequestParameter();
			r.setParameterName("context_loginiplock_login_ip");
			r.setParameterValue("127.0.0.1");
			params.add(r);

			r = new RequestParameter();
			r.setParameterName("a_bg_bill_month");
			r.setParameterValue("0");
			params.add(r);

			r = new RequestParameter();
			r.setParameterName("phoneNum");
			r.setParameterValue(mobile);
			params.add(r);

			// *********
			r = new RequestParameter();
			r.setParameterName("context_route_type");
			r.setParameterValue("2");
			params.add(r);

			r = new RequestParameter();
			r.setParameterName("context_route_value");
			r.setParameterValue(mobile);
			params.add(r);

			r = new RequestParameter();
			r.setParameterName("context_login_msisdn");
			r.setParameterValue(mobile);
			params.add(r);

			// context_route_type

			reqXml = this.bossTeletextUtil.mergeTeletext("cc_cgetusercust_69",
					params);
			logger.debug(" ====== 查询用户信息 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, "cc_cgetusercust_69", this
								.generateCity(params)));
				logger.debug(" ====== 查询用户信息 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(
						root, "response"), "resp_code");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050029",
							"cc_cgetusercust_69", resp_code);
					if (null != errDt) {
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				content = this.config.getElement(root, "content");
				Element userInfo = this.config.getElement(content, "user_info");
				if (!"".equals(this.config.getChildText(userInfo, "user_city"))) {
					user.setDdrCity(this.config.getChildText(userInfo,
							"user_city"));
				}
				if (!"".equals(this.config.getChildText(userInfo, "user_id"))) {
					user.setUserId(this.config
							.getChildText(userInfo, "user_id"));
				}
				if (!"".equals(this.config.getChildText(userInfo,
						"user_brand_id"))) {
					user.setBrand(this.config.getChildText(userInfo,
							"user_brand_id"));
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		return user;
	}

	/**
	 * 获取用户开通亲情号码组合的列表
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @return
	 */
	private Result getUserRelationInfo(String accessId, ServiceConfig config,
			List<RequestParameter> params) {
		Result res = new Result();
		res.getRes().setResultCode("0");
		res.getRes().setErrorMessage("");

		// 当前登录人号码
		String phoneNum = "";
		// 被邀请人号码
		String subNum = "";

		try {
			for (RequestParameter par : params) {
				if ("phoneNum".equals(par.getParameterName())) {
					phoneNum = String.valueOf(par.getParameterValue());
				}

				if ("subNum".equals(par.getParameterName())) {
					subNum = String.valueOf(par.getParameterValue());
				}
			}

			// 查询用户加入的亲情组合套餐信息
			List<GetMsngSubPackDT> groupList = this.getRelationGroupPckInfo(
					accessId, config, params, res);
			if (null != groupList && groupList.size() > 0) {
				// 套餐类型编码
				String relationCode = RELATION_1;

				for (GetMsngSubPackDT packDt : groupList) {
					if (Long.parseLong(this.config.getDistanceDT(packDt
							.getPackage_use_date(), this.config
							.getTodayChar14(), "s")) >= 0) {
						String relationName = "";
						String pkgageCode = packDt.getPackage_code();
						if (this.map.get(pkgageCode) == null) {
							relationName = "1元功能费";
						} else {
							relationName = map.get(pkgageCode);
							relationCode = mapCode.get(pkgageCode);
						}
						// 查询亲情号码优惠情况
						this.getAcAGetZoneInfo(accessId, config, params, res,
								packDt.getPackage_code(), relationName);
					}
				}

				// 查询亲情号码组合
				this.queryRelationGroup(accessId, config, params, res, "3",
						relationCode);

				// 判断用户是否预约关闭亲情号码组合
				if (null != res.getRelationNum() && null != res.getPkgUse()) {
					// 查询预约另一个亲情号码组合
					if (this.isBookOpenRelationNumber(phoneNum, res
							.getRelationNum())) {
						// 查询亲情号码组合
						this.queryRelationGroup(accessId, config, params, res,
								"0", relationCode);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}

		// 过滤号码和日期重复的信息
		mobileFilter(res);
		return res;
	}

	/**
	 * 过滤号码和日期重复的信息
	 * 
	 * @param res
	 * @return
	 */
	private void mobileFilter(Result res) {
		List<RelationNum> relationList = res.getRelationNum();
		List<RelationNum> newRelationList = new ArrayList<RelationNum>();
		for (RelationNum relation : relationList) {
			String beginDate = relation.getBeginDate() == null ? "" : relation
					.getBeginDate();
			String endDate = relation.getEndDate() == null ? "" : relation
					.getEndDate();
			String phoneNum = relation.getPhoneNum();

			boolean isAdd = true;
			for (RelationNum newRelation : newRelationList) {
				String newBeginDate = newRelation.getBeginDate() == null ? ""
						: newRelation.getBeginDate();
				String newEndDate = newRelation.getEndDate() == null ? ""
						: newRelation.getEndDate();
				String newPhoneNum = newRelation.getPhoneNum();

				if (newBeginDate.equals(beginDate)
						&& newPhoneNum.equals(phoneNum)
						&& endDate.equals(newEndDate)) {
					isAdd = false;
				}
			}

			if (isAdd) {
				newRelationList.add(relation);
			}
		}

		res.setRelationNum(newRelationList);
	}

	/**
	 * 判断用户是否预约关闭亲情号码组合
	 * 
	 * @param number
	 * @param list
	 * @return
	 */
	private boolean isBookOpenRelationNumber(String number, List list) {
		try {
			if (null != list && list.size() > 0) {
				for (Object obj : list) {
					if (obj instanceof RelationNum) {
						RelationNum dt = (RelationNum) obj;
						if (number.equals(dt.getPhoneNum())
								&& null != dt.getEndDate()
								&& !"".equals(dt.getEndDate())) {
							return true;
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex, ex);
			return false;
		}

		return false;
	}

	/**
	 * 动感套餐优惠查询
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 * @param pkgName
	 */
	private void getAcAGetZoneInfo(String accessId, ServiceConfig config,
			List<RequestParameter> params, Result res,
			String acrelation_revision, String pkgName) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		Element content = null;
		RequestParameter r = null;
		PkgUse pkg = null;
		List<PkgUse> list = null;
		ErrorMapping errDt = null;

		try {
			r = new RequestParameter();
			r.setParameterName("dbi_month_pr_number");
			r.setParameterValue(this.config.getTodayChar6());
			params.add(r);
			r = new RequestParameter();
			r.setParameterName("cdr_reduce_total");
			r.setParameterValue("1");
			params.add(r);
			r = new RequestParameter();
			r.setParameterName("a_bg_bill_month");
			r.setParameterValue("0");
			params.add(r);
			r = new RequestParameter();
			r.setParameterName("acrelation_revision");
			r.setParameterValue(acrelation_revision);
			params.add(r);

			reqXml = this.bossTeletextUtil.mergeTeletext("ac_agetzoneinfo_518",
					params);
			logger.debug(" ====== 动感套餐优惠查询 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, "ac_agetzoneinfo_518", this
								.generateCity(params)));
				logger.debug(" ====== 动感套餐优惠查询 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(
						root, "response"), "resp_code");
				res.getRes()
						.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050001",
							"ac_agetzoneinfo_518", resp_code);
					if (null != errDt) {
						res.getRes().setErrorCode(errDt.getLiErrCode());
						res.getRes().setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				list = new ArrayList();
				content = this.config.getElement(root, "content");
				// 短信
				if (!"".equals(this.config.getChildText(content,
						"arecord_count"))) {
					pkg = new PkgUse();
					pkg.setName(pkgName);
					pkg.setFlag(2);
					pkg.setUse(this.config.getChildText(content,
							"arecord_count"));
					pkg.setRemain(this.config.getChildText(content, "mms_len"));
					pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse())
							+ Integer.parseInt(pkg.getRemain())));
					list.add(pkg);
				}
				// IP电话
				if (!"".equals(this.config.getChildText(content, "data_down"))) {
					pkg = new PkgUse();
					pkg.setName(pkgName);
					pkg.setFlag(10);
					pkg.setUse(this.config.getChildText(content, "data_down"));
					pkg
							.setRemain(this.config.getChildText(content,
									"fax_time"));
					pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse())
							+ Integer.parseInt(pkg.getRemain())));
					list.add(pkg);
				}
				// 本地通话
				if (!"".equals(this.config.getChildText(content, "data_up"))) {
					pkg = new PkgUse();
					pkg.setName(pkgName);
					pkg.setFlag(9);
					pkg.setUse(this.config.getChildText(content, "data_up"));
					pkg
							.setRemain(this.config.getChildText(content,
									"rec_time"));
					pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse())
							+ Integer.parseInt(pkg.getRemain())));
					list.add(pkg);
				}
				// 彩信
				if (!"".equals(this.config.getChildText(content,
						"acctbkitem_istransferable"))) {
					pkg = new PkgUse();
					pkg.setName(pkgName);
					pkg.setFlag(9);
					pkg.setUse(this.config.getChildText(content,
							"acctbkitem_istransferable"));
					pkg.setRemain(this.config.getChildText(content,
							"acctbkitem_usage_type"));
					pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse())
							+ Integer.parseInt(pkg.getRemain())));
					list.add(pkg);
				}
				// GPRS
				if (!"".equals(this.config.getChildText(content,
						"acctbkitem_default_flag"))) {
					pkg = new PkgUse();
					pkg.setName(pkgName);
					pkg.setFlag(9);
					pkg.setUse(this.config.getChildText(content,
							"acctbkitem_default_flag"));
					pkg.setRemain(this.config.getChildText(content,
							"acctbkitem_invprn_flag"));
					pkg.setTotal(String.valueOf(Integer.parseInt(pkg.getUse())
							+ Integer.parseInt(pkg.getRemain())));
					list.add(pkg);
				}
			}
			res.setPkgUse(list);
		} catch (Exception e) {
			logger.error(e, e);
		}
	}

	/**
	 * 查询亲情号码组合
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	private void queryRelationGroup(String accessId, ServiceConfig config,
			List<RequestParameter> params, Result res, String id_type,
			String relationCode) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		RelationNum dt = null;
		RequestParameter r = null;
		List<RelationNum> list = null;
		boolean idType = true;
		ErrorMapping errDt = null;

		try {
			for (RequestParameter p : params) {
				if ("id_type".equals(p.getParameterName())) {
					idType = false;
					p.setParameterValue(id_type);
				}
			}
			if (idType) {
				r = new RequestParameter();
				r.setParameterName("id_type");
				r.setParameterValue(id_type);
				params.add(r);
			}

			reqXml = this.bossTeletextUtil.mergeTeletext(
					"cc_cgetrelationlst_550", params);
			logger.debug(" ====== 查询亲情号码组合 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, "cc_cgetrelationlst_550", this
								.generateCity(params)));
				logger.debug(" ====== 查询亲情号码组合 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(
						root, "response"), "resp_code");
				res.getRes()
						.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050029",
							"cc_cgetrelationlst_550", resp_code);
					if (null != errDt) {
						res.getRes().setErrorCode(errDt.getLiErrCode());
						res.getRes().setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				if (null != res.getRelationNum()
						&& res.getRelationNum().size() > 0) {
					list = res.getRelationNum();
				} else {
					list = new ArrayList();
				}

				List list_size = this.config.getContentList(root, "list_size");
				if (null != list_size && list_size.size() > 0) {
					for (int i = 0; i < list_size.size(); i++) {
						dt = new RelationNum();
						dt.setUserName(this.config.getChildText(
								(Element) list_size.get(i),
								"msnuser_main_user_id"));
						dt.setPhoneNum(this.config.getChildText(
								(Element) list_size.get(i),
								"msnuser_sub_msisdn"));
						dt.setBeginDate(this.config.dateToString(this.config
								.stringToDate(this.config.getChildText(
										(Element) list_size.get(i),
										"user_apply_date"), "yyyyMMddHHmmss"),
								"yyyyMMdd"));
						dt.setEndDate(this.config.dateToString(this.config
								.stringToDate(
										this.config.getChildText(
												(Element) list_size.get(i),
												"end_date"), "yyyyMMddHHmmss"),
								"yyyyMMdd"));
						if ("3".equals(id_type)) {
							dt.setFlag(1); // 在用
						} else {
							dt.setFlag(0); // 预约
						}
						list.add(dt);
						dt.setOfferType(relationCode);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		res.setRelationNum(list);
	}

	/**
	 * 查询用户加入的亲情组合套餐信息 cc_cgetmsngsubpack_352
	 * 
	 * @param accessId
	 * @param config
	 * @param params
	 * @param res
	 */
	private List<GetMsngSubPackDT> getRelationGroupPckInfo(String accessId,
			ServiceConfig config, List<RequestParameter> params, Result res) {
		String reqXml = "";
		String rspXml = "";
		String resp_code = "";
		Element root = null;
		GetMsngSubPackDT dt = null;
		List<GetMsngSubPackDT> list = null;
		String usingPkgCode = "";
		String useState = "";
		String canOpenPkgCode = "";
		ErrorMapping errDt = null;

		try {
			reqXml = this.bossTeletextUtil.mergeTeletext(
					"cc_cgetmsngsubpack_352", params);
			logger.debug(" ====== 查询用户加入的亲情组合套餐信息 发送报文 ====== \n" + reqXml);
			if (null != reqXml && !"".equals(reqXml)) {
				rspXml = (String) this.remote.callRemote(new StringTeletext(
						reqXml, accessId, "cc_cgetmsngsubpack_352", this
								.generateCity(params)));
				logger.debug(" ====== 查询用户加入的亲情组合套餐信息 接收报文 ====== \n" + rspXml);
			}
			if (null != rspXml && !"".equals(rspXml)) {
				root = this.config.getElement(rspXml);
				resp_code = this.config.getChildText(this.config.getElement(
						root, "response"), "resp_code");
				res.getRes()
						.setResultCode("0000".equals(resp_code) ? "0" : "1");
				if (!"0000".equals(resp_code)) {
					errDt = this.wellFormedDAO.transBossErrCode("QRY050001",
							"cc_cgetmsngsubpack_352", resp_code);
					if (null != errDt) {
						res.getRes().setErrorCode(errDt.getLiErrCode());
						res.getRes().setErrorMessage(errDt.getLiErrMsg());
					}
				}
			}
			if (null != resp_code && "0000".equals(resp_code)) {
				List userList = this.config.getContentList(root,
						"package_user_id");
				if (null != userList && userList.size() > 0) {
					list = new ArrayList();
					for (int i = 0; i < userList.size(); i++) {
						Element packDt = this.config.getElement(
								(Element) userList.get(i), "cplanpackagedt");
						if (null != packDt) {
							if ("1035".equals(this.config.getChildText(packDt,
									"package_type"))) {
								String package_code = this.config.getChildText(
										packDt, "package_code");
								if ("4778".equals(package_code)
										|| "4779".equals(package_code)
										|| "4907".equals(package_code)) {
									dt = new GetMsngSubPackDT();
									dt.setPackage_user_id(this.config
											.getChildText(packDt,
													"package_user_id"));
									dt.setPackage_type(this.config
											.getChildText(packDt,
													"package_type"));
									dt.setPackage_code(this.config
											.getChildText(packDt,
													"package_code"));
									dt
											.setPackage_use_date(this.config
													.dateToString(
															this.config
																	.stringToDate(
																			this.config
																					.getChildText(
																							packDt,
																							"package_use_date"),
																			"yyyyMMddHHmmss"),
															"yyyyMMdd"));
									dt
											.setPackage_end_date(this.config
													.dateToString(
															this.config
																	.stringToDate(
																			this.config
																					.getChildText(
																							packDt,
																							"package_end_date"),
																			"yyyyMMddHHmmss"),
															"yyyyMMdd"));
									dt.setPackage_state(this.config
											.getChildText(packDt,
													"package_state"));
									dt.setPackage_change_date(this.config
											.getChildText(packDt,
													"package_change_date"));
									dt.setPackage_history_srl(this.config
											.getChildText(packDt,
													"package_history_srl"));
									dt.setPackage_apply_date(this.config
											.getChildText(packDt,
													"package_apply_date"));
									dt.setPackage_level(this.config
											.getChildText(packDt,
													"package_level"));

									// 正在使用亲情号码组合
									if (Long.parseLong(this.config
											.getDistanceDT(dt
													.getPackage_use_date(),
													this.config
															.getTodayChar14(),
													"s")) >= 0) {
										usingPkgCode = dt.getPackage_code();
										if (dt.getPackage_end_date().length() == 0) {
											canOpenPkgCode = dt
													.getPackage_code();
											useState = "10";
										} else {
											useState = useState == "13" ? "12"
													: "11";
										}
									} else {
										// 预约亲情号码组合
										canOpenPkgCode = dt.getPackage_code();
										useState = useState == "11" ? "12"
												: "13";
									}

									list.add(dt);
								}
							}
						}
					}
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setUsingPkgCode(usingPkgCode);
						list.get(i).setCanOpenPkgCode(canOpenPkgCode);
						list.get(i).setUseState(useState);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e, e);
		}
		return list;
	}

	public class Result {
		private QRY050029Result res = new QRY050029Result();

		private List<RelationNum> relationNum = new ArrayList<RelationNum>();

		private List<PkgUse> pkgUse = new ArrayList<PkgUse>();

		public void setRelationNum(List<RelationNum> relationNum) {
			this.relationNum = relationNum;
		}

		public List<RelationNum> getRelationNum() {
			return this.relationNum;
		}

		public void setPkgUse(List<PkgUse> pkgUse) {
			this.pkgUse = pkgUse;
		}

		public List<PkgUse> getPkgUse() {
			return this.pkgUse;
		}

		public QRY050029Result getRes() {
			return res;
		}

		public void setRes(QRY050029Result res) {
			this.res = res;
		}

	}

	public class RelationNum {
		private String userName;

		private String phoneNum;

		private String brand;

		private String beginDate;

		private String endDate;

		private int mainCard;

		private String offerType;

		/**
		 * 1、在用 0、预约
		 */
		private int flag;

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserName() {
			return this.userName;
		}

		public void setPhoneNum(String phoneNum) {
			this.phoneNum = phoneNum;
		}

		public String getPhoneNum() {
			return this.phoneNum;
		}

		public void setBrand(String brand) {
			this.brand = brand;
		}

		public String getBrand() {
			return this.brand;
		}

		public void setBeginDate(String beginDate) {
			this.beginDate = beginDate;
		}

		public String getBeginDate() {
			return this.beginDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public String getEndDate() {
			return this.endDate;
		}

		public void setMainCard(int mainCard) {
			this.mainCard = mainCard;
		}

		public int getMainCard() {
			return this.mainCard;
		}

		public void setOfferType(String offerType) {
			this.offerType = offerType;
		}

		public String getOfferType() {
			return this.offerType;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public int getFlag() {
			return this.flag;
		}

	}

	public class PkgUse {
		private String name;

		private String total;

		private String use;

		private String remain;

		private int flag;

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getTotal() {
			return this.total;
		}

		public void setUse(String use) {
			this.use = use;
		}

		public String getUse() {
			return this.use;
		}

		public void setRemain(String remain) {
			this.remain = remain;
		}

		public String getRemain() {
			return this.remain;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public int getFlag() {
			return this.flag;
		}

	}

	private class UserInfo {
		private String mobile;

		private String routeType = "2";

		private String ddrCity;

		private String userId;

		private String brand;

		public String getBrand() {
			return brand;
		}

		public void setBrand(String brand) {
			this.brand = brand;
		}

		public String getRouteType() {
			return routeType;
		}

		public String getDdrCity() {
			return ddrCity;
		}

		public void setDdrCity(String ddrCity) {
			this.ddrCity = ddrCity;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

	}
}