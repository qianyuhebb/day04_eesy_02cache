package com.itheima.test;

import com.itheima.dao.IUserDao;
import com.itheima.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
public class SecondLevelCacheTest {

    private InputStream in;
    private  SqlSessionFactory factory;

    @Before//用于在测试方法执行之前执行
    public void init()throws Exception{
        //1.读取配置文件，生成字节输入流
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.获取SqlSessionFactory
        factory = new SqlSessionFactoryBuilder().build(in);

    }

    @After//用于在测试方法执行之后执行
    public void destroy()throws Exception{
        in.close();
    }

    /**
     * 测试二级缓存
     *  二级缓存使用步骤：1.主配置文件需要配置，支持二级缓存
     *                      <settings>
     *                           <setting name="cacheEnabled" value="true"/>
     *                      </settings>
     *                2.映射配置文件userdao.xml  需要配置支持二级缓存
     *
     *                             <cache/>
     *
     *
     *     <select id="findById" parameterType="INT" resultType="user" useCache="true">
     *         select * from user where id = #{uid}
     *     </select>
     *                3.配置当前操作支持二级缓存
     *
     *
     */
    @Test
    public void testFirstLevelCache(){
        SqlSession sqlSession1 = factory.openSession();
        IUserDao dao1 = sqlSession1.getMapper(IUserDao.class);
        User user1 = dao1.findById(41);
        System.out.println(user1);
        sqlSession1.close();//一级缓存消失

        SqlSession sqlSession2 = factory.openSession();
        IUserDao dao2 = sqlSession2.getMapper(IUserDao.class);
        User user2 = dao2.findById(41);
        System.out.println(user2);
        sqlSession2.close();
         //由于二级缓存里面存的是数据，不是对象，所以即使是使用了二级缓存，sout依然为false
        System.out.println(user1 == user2);

    }


}
