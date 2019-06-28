package edu.uestc.dao;

import edu.uestc.domain.SeckillUser;
import org.apache.ibatis.annotations.*;

import java.nio.Buffer;

/**
 * 秒杀用户表seckill_user的SQl Mapper
 */
@Mapper
public interface SeckillUserDao {
    /**
     * 根据id查询秒杀用户信息
     *
     * @param id
     * @return
     */
    @Select("SELECT * FROM seckill_user WHERE id=#{id}")
    SeckillUser getById(@Param("id") Long id);

    /**
     *
     * @param updatedUser
     */
    @Update("UPDATE seckill_user SET password=#{password} WHERE id=#{id}")
    void updatePassword(SeckillUser updatedUser);

    @Insert("insert into seckill_user (id,nickname,password,salt) " +
            "values (#{id},#{nickname},#{password},#{salt})")
    void insertUser(SeckillUser user);

}
