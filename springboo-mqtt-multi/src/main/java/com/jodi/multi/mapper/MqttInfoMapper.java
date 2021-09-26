package com.jodi.multi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jodi.multi.entity.MqttInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MqttInfoMapper extends BaseMapper<MqttInfo> {
}
