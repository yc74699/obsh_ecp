package com.xwtech.xwecp.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.xwtech.xwecp.dao.WellFormedDAO;
import com.xwtech.xwecp.teletext.BossTeletextUtil;

import junit.framework.TestCase;

public class BossTeletextTest extends TestCase {

    public ApplicationContext ctx;
    public BossTeletextUtil bossTele;
   
   

    protected void setUp() throws Exception {
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
    }

    public void testGetTeletextTemplate() {
        bossTele.getTeletextTemplate("L10056");
    }

}
