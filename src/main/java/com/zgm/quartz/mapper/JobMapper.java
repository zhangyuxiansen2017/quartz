package com.zgm.quartz.mapper;

import com.zgm.quartz.entity.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobMapper {
    Job selectById(Long id);
    int updateStatusById(@Param("id") Long id, @Param("status") Integer status);
    List<Job> selectAll();
}