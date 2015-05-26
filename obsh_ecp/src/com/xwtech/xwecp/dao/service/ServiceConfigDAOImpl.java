package com.xwtech.xwecp.dao.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.lob.LobHandler;

import com.xwtech.xwecp.dao.BaseDAO;
import com.xwtech.xwecp.service.ServiceInfo;
import com.xwtech.xwecp.service.config.DataTypeConstants;
import com.xwtech.xwecp.service.config.InvalidConfigException;
import com.xwtech.xwecp.service.config.ServiceConfig;
import com.xwtech.xwecp.service.config.ServiceConfigAnalyzer;
import com.xwtech.xwecp.service.config.ServiceExtension;
import com.xwtech.xwecp.service.config.ServiceImplementation;
import com.xwtech.xwecp.service.config.ServiceInput;
import com.xwtech.xwecp.service.config.ServiceInputParameter;
import com.xwtech.xwecp.service.config.ServiceOutput;
import com.xwtech.xwecp.service.config.ServiceOutputField;
import com.xwtech.xwecp.service.config.ServiceResultMapping;
import com.xwtech.xwecp.service.config.TeletextMapping;

public class ServiceConfigDAOImpl extends BaseDAO implements IServiceConfigDAO 
{
    private static final Logger logger = Logger
            .getLogger(ServiceConfigDAOImpl.class);
    private LobHandler lobHandler;

    public LobHandler getLobHandler() {
        return lobHandler;
    }

    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    public ServiceConfig getServiceConfig(String cmd) {

//        if (true) {
//            try {
//                return new ServiceConfigAnalyzer().readFromFile(new File(
//                        "D:\\MyProjects\\网营产品化\\meta\\meta.xml"));
//            } catch (InvalidConfigException e) {
//                logger.error(e, e);
//                return null;
//            }
//        }
        String configTmp = "";
        ServiceConfig config = null;
        Map mapResult = null;
        configTmp = (String) getCache().get("F_LI_ALL_TMP_KEY_" + cmd);
        if (configTmp == null || ("").equals(configTmp)) {
            String sql = "select  tlf.F_LI_ALL_TMP  from T_LINTERFACE_DA tlf where tlf.F_LI_NUMBER='"
                    + cmd + "' and tlf.F_LI_STATE = 1"; // 如果被禁用会导致查询不到.

            try {
                mapResult = (Map) this.getJdbcTemplate().queryForObject(sql,
                        new RowMapper() {
                            public Object mapRow(ResultSet rs, int rowNum)
                                    throws SQLException {
                                Map<String, String> mapRs = new HashMap<String, String>();
                                ResultSetMetaData metaData = rs.getMetaData();

                                for (int num = 1; num <= metaData
                                        .getColumnCount(); num++) {
                                    if (metaData.getColumnType(num) == Types.CLOB) {

                                        mapRs.put(metaData.getColumnName(num)
                                                .toLowerCase(), lobHandler
                                                .getClobAsString(rs, num));
                                    }
                                }
                                return mapRs;
                            }
                        });
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                logger.error("操作数据库出现错误！！！");
                return null;
            }
            configTmp = (String) mapResult.get("f_li_all_tmp");
            getCache().add("F_LI_ALL_TMP_KEY_" + cmd, configTmp);
        }

        if (!"".equals(configTmp) && configTmp != null) {

            try {
                config = new ServiceConfigAnalyzer().read(configTmp);

            } catch (InvalidConfigException e) {
                logger.error(e.getMessage(), e);
                logger.error("解析模板配置出现错误");
            }

        }

        return config;
    }

    protected ServiceConfig getSimulatedService_DirectiveImplementation(
            String cmd) {
        ServiceConfig config = new ServiceConfig();
        ServiceInput input = new ServiceInput();
        ServiceOutput output = new ServiceOutput();
        ServiceImplementation impl = new ServiceImplementation();
        ServiceExtension ext = new ServiceExtension();

        config.setCmd(cmd);
        config.setName("login");
        config.setNamespace("com.xwtech.xwecp.service.logic");
        config.setChineseName("用户登陆");
        config.setDescription("用户登陆 - 测试配置");
        config.setInput(input);
        config.setOutput(output);
        config.setImpl(impl);
        config.setExtension(ext);

        ServiceInputParameter mobile = new ServiceInputParameter();
        mobile.setName("mobile");
        mobile.setDataType(DataTypeConstants.STRING);
        mobile.setRegular("");
        ServiceInputParameter pwd = new ServiceInputParameter();
        pwd.setName("password");
        pwd.setDataType(DataTypeConstants.STRING);
        pwd.setRegular("");
        input.getParams().add(mobile);
        input.getParams().add(pwd);

        ServiceOutputField balance = new ServiceOutputField();
        balance.setDataType(DataTypeConstants.INT);
        balance.setName("balance");

        ServiceOutputField custName = new ServiceOutputField();
        custName.setDataType(DataTypeConstants.STRING);
        custName.setName("custName");

        output.getOutputFields().add(balance);
        output.getOutputFields().add(custName);

        impl.setDirectImpl(true);
        impl.setImplClass("com.xwtech.xwecp.test.ServiceImplTest");

        ServiceResultMapping balanceMapping = new ServiceResultMapping();
        balanceMapping.setName("balance");
        balanceMapping.setExpression("xpath(/test/balance/text())");

        ServiceResultMapping custNameMapping = new ServiceResultMapping();
        custNameMapping.setName("custName");
        custNameMapping.setExpression("xpath(/test/custName/text())");

        //impl.getResultMapping().add(balanceMapping);
        //impl.getResultMapping().add(custNameMapping);

        return config;
    }

    protected ServiceConfig getSimulatedService_Adapting(String cmd) {
        ServiceConfig config = new ServiceConfig();
        ServiceInput input = new ServiceInput();
        ServiceOutput output = new ServiceOutput();
        ServiceImplementation impl = new ServiceImplementation();
        ServiceExtension ext = new ServiceExtension();

        config.setCmd(cmd);
        config.setName("login");
        config.setNamespace("com.xwtech.xwecp.service.logic");
        config.setChineseName("用户登陆");
        config.setDescription("用户登陆 - 测试配置");
        config.setInput(input);
        config.setOutput(output);
        config.setImpl(impl);
        config.setExtension(ext);

        ServiceInputParameter mobile = new ServiceInputParameter();
        mobile.setName("mobile");
        mobile.setDataType(DataTypeConstants.STRING);
        mobile.setRegular("");
        ServiceInputParameter pwd = new ServiceInputParameter();
        pwd.setName("password");
        pwd.setDataType(DataTypeConstants.STRING);
        pwd.setRegular("");
        input.getParams().add(mobile);
        input.getParams().add(pwd);

        ServiceOutputField balance = new ServiceOutputField();
        balance.setDataType(DataTypeConstants.INT);
        balance.setName("balance");

        ServiceOutputField custName = new ServiceOutputField();
        custName.setDataType(DataTypeConstants.STRING);
        custName.setName("custName");

        output.getOutputFields().add(balance);
        output.getOutputFields().add(custName);

        impl.setDirectImpl(false);
        TeletextMapping teletextMapping = new TeletextMapping();
        teletextMapping.setParamName("mobile");
        teletextMapping.setMatch("*");
        teletextMapping.setTeletextTemplate("10110");
        teletextMapping
                .setResolverClass("com.xwtech.xwecp.test.TeletextResolverTest");

        ServiceResultMapping balanceMapping = new ServiceResultMapping();
        balanceMapping.setName("balance");
        balanceMapping.setExpression("xpath(/test/balance/text())");

        ServiceResultMapping custNameMapping = new ServiceResultMapping();
        custNameMapping.setName("custName");
        custNameMapping.setExpression("xpath(/test/custName/text())");

        //impl.getResultMapping().add(balanceMapping);
        //impl.getResultMapping().add(custNameMapping);

        return config;
    }

    public void addServiceConfig(String xml, ServiceConfig config) {
        String impClass = "";
        int numWay = 0;
        final ServiceConfig serviceConfig = config;
        final String tmpXml = xml;
        try {

            boolean direct = serviceConfig.getImpl().isDirectImpl();
            if (direct) {
                impClass = serviceConfig.getImpl().getImplClass();
                numWay = 1;
            } else {
                numWay = 0;
            }
            final int numImplWay = numWay;
            if (serviceConfig != null) {
                final String fimpClass = impClass;
                String delWhere = " where F_LI_NUMBER=?";

                String delSql = "delete from T_LINTERFACE_DA ";
                String delMxSql = "delete from T_LINTERFACE_IN_MX ";
                // 先删除数据库中cmd中相同的记录
                this.getJdbcTemplate().update(delMxSql + delWhere,
                        new PreparedStatementSetter() {
                            public void setValues(PreparedStatement ps)
                                    throws SQLException {
                                ps.setString(1, serviceConfig.getCmd());

                            }
                        });
                this.getJdbcTemplate().update(delSql + delWhere,
                        new PreparedStatementSetter() {
                            public void setValues(PreparedStatement ps)
                                    throws SQLException {
                                ps.setString(1, serviceConfig.getCmd());

                            }
                        });

                this
                        .getJdbcTemplate()
                        .update(
                                "insert into T_LINTERFACE_DA values(?,?,?,?,?,'','',?,'',?,EMPTY_CLOB(),1,'')",
                                new PreparedStatementSetter() {
                                    public void setValues(PreparedStatement ps)
                                            throws SQLException {
                                        ps.setString(1, serviceConfig.getCmd());
                                        ps
                                                .setString(2, serviceConfig
                                                        .getChineseName());
                                        ps.setString(3, serviceConfig
                                                .getDescription());
                                        ps.setString(4, serviceConfig
                                                .getName());
                                        ps.setString(5, serviceConfig
                                                .getNamespace());
                                        ps.setInt(6, numImplWay);
                                        ps.setString(7, fimpClass);

                                    }
                                });

                String sql = "update T_LINTERFACE_DA set F_LI_ALL_TMP=? where F_LI_NUMBER='"
                        + serviceConfig.getCmd() + "'";
                SqlLobValue[] values = new SqlLobValue[1];
                values[0] = new SqlLobValue(tmpXml);
                updateForClob(sql, values);

                // //////////////////////////////
                List list = serviceConfig.getInput().getParams();
                for (int i = 0; i < list.size(); i++) {
                    final ServiceInputParameter inParameter = (ServiceInputParameter) list
                            .get(i);
                    this
                            .getJdbcTemplate()
                            .update(
                                    "insert into T_LINTERFACE_IN_MX values(?,?,?,?,?,?,?)",
                                    new PreparedStatementSetter() {
                                        public void setValues(
                                                PreparedStatement ps)
                                                throws SQLException {
                                            ps.setString(1, serviceConfig
                                                    .getCmd());
                                            ps.setString(2, inParameter
                                                    .getName());
                                            ps.setString(3, inParameter
                                                    .getDataType());
                                            ps.setString(4, inParameter
                                                    .getClassName());
                                            ps.setString(5, inParameter
                                                    .getDefaultValue());
                                            ps
                                                    .setInt(6, inParameter
                                                            .getIndex());

                                            ps.setString(7, inParameter
                                                    .getRegular());

                                        }
                                    });
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.error("操作数据库失败！！！");
            return;
        }

    }
}
