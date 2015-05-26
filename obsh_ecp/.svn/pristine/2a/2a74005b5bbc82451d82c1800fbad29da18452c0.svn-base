package com.xwtech.xwecp.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.xwtech.xwecp.dao.service.ServiceConfigDAOImpl;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.config.ServiceConfigAnalyzer;
//import com.xwtech.xwecp.web.action.ServiceConfigAction;

public class ServiceConfigActionTest {

    private static final Logger logger = Logger
            .getLogger(ServiceConfigActionTest.class);
    public ApplicationContext ctx;
    public ServiceConfigDAOImpl serviceConfigDaoImpl;
    //public ServiceConfigAction action;
   

    protected void setUp() throws Exception {

    }

    @Test
    public void testDefaultHandle() {

        String paths[] = { "/WebRoot/WEB-INF/cfg/applicationContext-xwecp.xml",
                "/WebRoot/WEB-INF/cfg/applicationContext-datasource.xml",
                "/WebRoot/WEB-INF/cfg/controllers.xml",
                "/WebRoot/WEB-INF/cfg/applicationContext-initialization.xml" };
        try {
            ctx = new FileSystemXmlApplicationContext(paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        serviceConfigDaoImpl = (ServiceConfigDAOImpl) ctx
                .getBean("serviceConfigDAO");

    //    jdbcDao = (JdbcTemplate) ctx.getBean("jdbcTemplate");

  //      serviceConfigDaoImpl.setJdbcDao(jdbcDao);
        // action=(ServiceConfigAction)ctx.getBean("/serviceConfig.do");
        // String filePath = "D:\\meta15.xml";
        String filePath = "D:\\apache-tomcat-5.5.27\\webapps\\xwecp/WEB-INF/meta/meta15.xml";
        StringBuffer buf = null;
        BufferedReader breader = null;
        String tmpXml = null;
        try {
            breader = new BufferedReader(new InputStreamReader(
                    new FileInputStream((filePath)), Charset.forName("utf-8")));

            buf = new StringBuffer();

            while (breader.ready())
                buf.append((char) breader.read());

            breader.close();
            tmpXml = buf.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("读取文件失败");
            return;
        }

        ServiceConfig serviceConfig = null;
        try {
           
            serviceConfig = new ServiceConfigAnalyzer().readFromFile(new File(
                    filePath));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("解析模板出现错误");
            return;
        }
        serviceConfigDaoImpl.addServiceConfig(tmpXml, serviceConfig);
    }

}
