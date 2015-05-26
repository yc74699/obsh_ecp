package com.xwtech.xwecp.test;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.memcached.IMemcachedManager;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

public class MemcachedTest extends TestCase {

    public ApplicationContext ctx;
    public BossTeletextUtil bossTele;

    public JdbcTemplate jdbcDao;

    public void testTeletext() throws Exception {

 
        String paths[] = { "/WebRoot/WEB-INF/cfg/applicationContext-xwecp.xml",
                "/WebRoot/WEB-INF/cfg/applicationContext-datasource.xml",
                "/WebRoot/WEB-INF/cfg/controllers.xml",
                "/WebRoot/WEB-INF/cfg/applicationContext-initialization.xml" };
        try {
            ctx = new FileSystemXmlApplicationContext(paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bossTele = (BossTeletextUtil) ctx.getBean("bossTeletextUtil");
    
        JdbcTemplate jdbcDao = (JdbcTemplate) ctx.getBean("jdbcTemplate");

   //     bossTele.getWellFormedDAO().setJdbcDao(jdbcDao);

        bossTele.getTeletextTemplate("tel001");

    }
}
