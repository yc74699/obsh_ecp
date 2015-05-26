package com.xwtech.xwecp.service.logic.pojo;

import java.io.Serializable;

/**
 * 在线入网订单实名制图片更新接口
 * @author YangXQ
 * 2014-11-3
 */
public class Paralist implements Serializable
{
	
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;

    private String attribute;
    private String pic;
    private String Pic_edt;
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPic_edt() {
		return Pic_edt;
	}
	public void setPic_edt(String picEdt) {
		Pic_edt = picEdt;
	}
    
}