package com.xwtech.xwecp.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.xwtech.xwecp.dao.service.ServiceConfigDAOImpl;
import com.xwtech.xwecp.teletext.BossTeletextUtil;


import junit.framework.TestCase;

public class ServiceConfigDAOImplTest extends TestCase {

    public ApplicationContext ctx;
    public ServiceConfigDAOImpl serviceConfigDaoImpl;
   
    //public JdbcTemplate jdbcDao;
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
        serviceConfigDaoImpl = (ServiceConfigDAOImpl) ctx.getBean("serviceConfigDAO");

         //jdbcDao = (JdbcTemplate) ctx.getBean("jdbcTemplate");
      
 //       serviceConfigDaoImpl.setJdbcDao(jdbcDao);
    }

    public void testGetServiceConfig() {
        serviceConfigDaoImpl.getServiceConfig("L10056");
    }

//    public void testAddServiceConfig() {
//       //  serviceConfigDaoImpl.addServiceConfig("D:\\meta15.xml");
//    }
}
