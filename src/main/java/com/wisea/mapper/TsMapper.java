package com.wisea.mapper;

import com.wisea.entity.FcProdType;
import com.wisea.entity.TsDetail;
import com.wisea.entity.TsDetailSqlParam;
import com.wisea.entity.TsIndex;
import com.wisea.entity.TsIndexPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TsMapper {

    // 查询已处理的页码
    @Select("select id,page_num,status from ts_index_page where status = '1'")
    List<TsIndexPage> findTreatedPages();

    @Select("select id,page_num,status from ts_index_page where page_num = #{pageNum}")
    TsIndexPage findPage(Integer pageNum);

    @Insert("insert into ts_index_page(id,page_num,status) values(#{id},#{pageNum},#{status})")
    int insertTsIndexPage(TsIndexPage tsIndexPage);

    @Update("update ts_index_page set page_num = #{pageNum}, status = #{status} where id = #{id}")
    int updateTsIndexPage(TsIndexPage tsIndexPage);

    @Delete("delete from ts_index_page where id = #{id}")
    int deleteTsIndexPage(TsIndexPage tsIndexPage);


    int batchInsertTsIndex(List<TsIndex> list);

    int batchUpdateTsIndex(List<TsIndex> list);

    @Select("select count(1) from ts_index where status = '1'")
    long findTreatedTsIndexMaxRow();

    @Select("select id,ts_id,ts_no,ts_validity,ts_name,ts_pub_dept,status from ts_index order by ts_id limit #{startRow},#{count}")
    List<TsIndex> findTsIndexList(Long startRow, Integer count);

    List<TsIndex> findSubTsIndexList(List<String> tsIds);


    int batchInsertTsDetail(List<TsDetail> list);


    @Select("select * from fc_prod_type where (length(names)-length(replace(names,'>',''))) = #{level}")
    List<FcProdType> findFcProdTypeList(int level);

    int batchUpdateTsDetail(TsDetailSqlParam tsDetailSqlParam);

}
