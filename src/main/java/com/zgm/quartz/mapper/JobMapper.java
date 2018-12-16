package com.zgm.quartz.mapper;

import com.zgm.quartz.entity.Job;
import com.zgm.quartz.entity.JobExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface JobMapper {
    long countByExample(JobExample example);

    int deleteByExample(JobExample example);

    int insert(Job record);

    int insertSelective(Job record);

    List<Job> selectByExample(JobExample example);

    int updateByExampleSelective(@Param("record") Job record, @Param("example") JobExample example);

    int updateByExample(@Param("record") Job record, @Param("example") JobExample example);
}