package com.wisea.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LinkDictMapper {

    @Select("select name from link_dict")
    List<String> findLinkNameList();
}
