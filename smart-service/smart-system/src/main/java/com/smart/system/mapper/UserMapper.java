package com.smart.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.system.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}