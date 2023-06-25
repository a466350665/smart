package com.smart.sso.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.smart.sso.server.service.impl.BaseServiceImpl;
import com.smart.sso.server.dao.OfficeDao;
import com.smart.sso.server.model.Office;
import com.smart.sso.server.service.OfficeService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Component("officeService")
public class OfficeServiceImpl extends BaseServiceImpl<OfficeDao, Office> implements OfficeService {

    @Override
    @Transactional(readOnly = false)
    public void enable(Boolean isEnable, List<Integer> idList) {
        selectByIds(idList).forEach(t -> {
            t.setIsEnable(isEnable);
            updateById(t);
        });
    }

    private List<Office> selectByIds(List<Integer> idList){
        LambdaQueryWrapper<Office> wrapper =  Wrappers.lambdaQuery();
        wrapper.in(Office::getId, idList);
        return list(wrapper);
    }

	@Override
	public List<Office> selectList(Boolean isEnable, Boolean isParent, Integer currentId, String prefix) {
		List<Office> list = selectList(isEnable, isParent, currentId);
		if (!StringUtils.isEmpty(prefix)) {
			List<Office> dataList = Lists.newArrayList();
			for (Office office : list) {
				if (office.getParentId() == null) {
					dataList.add(office);
					buildTree(office.getId(), list, dataList, prefix, prefix);
				}
			}
			list = dataList;
		}
		return list;
	}
	
	private void buildTree(Integer officeId, List<Office> list, List<Office> dataList, String currentPrefix, String prefix){  
        List<Office> subList = getSubList(officeId, list, currentPrefix);
        if (!subList.isEmpty()) {  
            for (Office office : subList) {
            	dataList.add(office);
                buildTree(office.getId(), list, dataList, prefix + currentPrefix, prefix);  
            }  
        }   
    }  
      
    private List<Office> getSubList(Integer officeId, List<Office> list, String currentPrefix){  
        List<Office> children = Lists.newArrayList();
        for (Office child : list) {
            if (officeId.equals(child.getParentId())) {
            	child.setName(currentPrefix + child.getName());
                children.add(child);  
            }  
        }  
        return children;  
    }

	@Override
	public List<Integer> selectIdListByParentId(Integer parentId) {
		if (parentId == null){
            return Collections.emptyList();
        }
		List<Integer> idList = Lists.newArrayList();
		idList.add(parentId);
		List<Office> list = selectList(true, null, null);
		if (!CollectionUtils.isEmpty(list)) {
			buildTree(parentId, list, idList);
		}
		return idList;
	}
	
	private void buildTree(Integer officeId, List<Office> list, List<Integer> idList){
        List<Office> subList = getSubList(officeId, list, "");
        if (!subList.isEmpty()) {  
            for (Office office : subList) {
            	idList.add(office.getId());
                buildTree(office.getId(), list, idList);  
            }  
        }   
    } 
	
    private List<Office> selectList(Boolean isEnable, Boolean isParent, Integer currentId) {
        LambdaQueryWrapper<Office> wrapper =  Wrappers.lambdaQuery();
        wrapper.eq(isEnable != null, Office::getIsEnable, isEnable);
        wrapper.isNull(isParent != null && isParent, Office::getParentId);
        wrapper.ne(currentId != null, Office::getId, currentId);
        return list(wrapper);
    }
}
