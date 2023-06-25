package com.smart.sso.server.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.smart.sso.server.enums.TrueFalseEnum;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 角色
 * 
 * @author Joe
 */
@TableName("sys_role")
public class Role extends PersistentObject {

	private static final long serialVersionUID = 564115576254799088L;

	/** 名称 */
	private String name;
	/** 排序 */
	private Integer sort;
	/** 描述 */
	private String description;
	/** 是否启用 */
	private Boolean isEnable;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return this.sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsEnable() {
		return this.isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}
	
	@Transient
	public String getIsEnableStr() {
		return (isEnable != null && isEnable) ? TrueFalseEnum.TRUE.getLabel() : TrueFalseEnum.FALSE.getLabel();
	}
}
