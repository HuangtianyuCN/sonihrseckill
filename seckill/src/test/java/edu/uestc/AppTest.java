package edu.uestc;

import static org.junit.Assert.assertTrue;

import edu.uestc.redis.KeyPrefix;
import edu.uestc.redis.RedisService;
import edu.uestc.redis.UserKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
    @Autowired
    ApplicationContext ioc;
    @Autowired
    RedisService redisService;
    @Test
    public void testJedis(){
        System.out.println(ioc.containsBean("jedisPool"));
    }
    @Test
    public void testRedisSetAndGet(){
        KeyPrefix prefix = new UserKey("test");
        redisService.set(prefix,"testRedisSetAndGet",1);
        System.out.println(redisService.get(prefix,"testRedisSetAndGet",int.class));
    }
}
