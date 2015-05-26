package com.xwtech.xwecp.teletext.funcs;

/**
 * 根据格式来匹配输出
 * 例如：输入为“1:0;2:1;|1”，则输出为“0”
 * @author 邵琪
 *
 */
public class ValueMap extends AbstractFunctionExecutor {

	public ValueMap() {
		super("value_map");
	}

	public String execute(String parameter) {
		//返回值
		String rtnValue = "";
		//输入参数类似：“1:0;2:1;|1”
		if(parameter != null && parameter.indexOf("|") > 0){//判断参数是否为正确格式
			//拆分成“1:0;2:1;”和“1”
			String[] parameters = parameter.split("\\|",2);
			//拆分“1:0;2:1;”
			String[] valueMapStrs = parameters[0].split(";");
			//输入值
			String inValue = parameters[1];
			for (int i = 0; i < valueMapStrs.length; i++) {
				if(valueMapStrs[i].trim().length() > 0){
					String[] valueMapStr = valueMapStrs[i].split(":",2);
					if(inValue.equals(valueMapStr[0])){
						//取值成功则返回匹配的值
						rtnValue = valueMapStr[1];
						break;
					}
				}
			}
		}
		return rtnValue;
	}

}
