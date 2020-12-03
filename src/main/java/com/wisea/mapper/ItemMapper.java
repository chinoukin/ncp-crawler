package com.wisea.mapper;

import com.wisea.entity.ItemDetail;
import com.wisea.entity.ItemIndex;
import com.wisea.entity.ItemIndexPage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ItemMapper {

    // 查询已处理的页码
    @Select("select id,page_num,status from item_index_page where status = '1'")
    List<ItemIndexPage> findTreatedPages();

    @Select("select id,page_num,status from item_index_page where page_num = #{pageNum}")
    ItemIndexPage findPage(Integer pageNum);

    @Insert("insert into item_index_page(id,page_num,status) values(#{id},#{pageNum},#{status})")
    int insertItemIndexPage(ItemIndexPage itemIndexPage);

    @Update("update item_index_page set page_num = #{pageNum}, status = #{status} where id = #{id}")
    int updateItemIndexPage(ItemIndexPage itemIndexPage);

    @Delete("delete from item_index_page where id = #{id}")
    int deleteItemIndexPage(ItemIndexPage itemIndexPage);


    int batchInsertItemIndex(List<ItemIndex> list);

    int batchUpdateItemIndex(List<ItemIndex> list);

    @Select("select count(1) from item_index where status = '1'")
    long findTreatedItemIndexMaxRow();

    @Select("select id,item_id,item_country,status from item_index order by item_id limit #{startRow},#{count}")
    List<ItemIndex> findItemIndexList(Long startRow, Integer count);


    int batchInsertItemDetail(List<ItemDetail> list);

}
