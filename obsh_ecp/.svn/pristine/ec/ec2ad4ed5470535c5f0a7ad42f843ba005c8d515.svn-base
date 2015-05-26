package com.xwtech.xwecp.service.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ServiceConfigAnalyzer {
	private static final Logger logger = Logger.getLogger(ServiceConfigAnalyzer.class);

	public ServiceConfig read(String xml) throws InvalidConfigException {
		StringReader sr = new StringReader(xml);
		return this.read(sr);
	}

	public ServiceConfig read(Reader reader) throws InvalidConfigException {
		try {
			ServiceConfig config = new ServiceConfig();
			SAXReader saxBuilder = new SAXReader();
			Document doc = saxBuilder.read(reader);

			Element root = doc.getRootElement();
			String namespace = root.elementText("namespace");
			String name = root.elementText("name");
			String cmd = root.elementText("cmd");
			String chineseName = root.elementText("chinese-name");
			String description = root.elementText("description");
			config.setChineseName(chineseName);
			config.setCmd(cmd);
			config.setDescription(description);
			config.setName(name);
			config.setNamespace(namespace);
			config.setDescription(description);

			Element input = root.element("input");
			Element output = root.element("output");
			Element impl = root.element("impl");
			Element extension = root.element("extension");
			if ( input == null ) {
				throw new InvalidConfigException("逻辑接口输入未配置!");
			}
			else {
				ServiceInput serviceInput = new ServiceInput();
				List params = input.elements("param");
				for (int i = 0; i < params.size(); i++) {
					Element el = (Element) (params.get(i));
					ServiceInputParameter sParameter = new ServiceInputParameter();
					sParameter.setClassName(el.attributeValue("class"));
					sParameter.setDataType(el.attributeValue("data-type"));
					String sIndex = el.attributeValue("index");
					if ( sIndex != null ) {
						sParameter.setIndex(Integer.parseInt(sIndex));
						if ( sParameter.getIndex() < 1 ) { throw new InvalidConfigException("逻辑接口输入参数配置错误, 参数索引值必须从1开始!"); }
					}
					sParameter.setName(el.attributeValue("name"));
					sParameter.setRegular(el.attributeValue("regular"));
					serviceInput.getParams().add(sParameter);
				}
				config.setInput(serviceInput);
			}
			if ( output == null ) {
				throw new InvalidConfigException("逻辑接口输出未配置!");
			}
			else {
				ServiceOutput serviceOutput = new ServiceOutput();
				List outputFields = output.elements("field");
				for (int i = 0; i < outputFields.size(); i++) {
					Element el = (Element) (outputFields.get(i));
					ServiceOutputField oField = new ServiceOutputField();
					oField.setClassName(el.attributeValue("class"));
					oField.setDataType(el.attributeValue("data-type"));
					oField.setDefaultValue(el.attributeValue("default"));
					oField.setName(el.attributeValue("name"));
					serviceOutput.getOutputFields().add(oField);
				}
				config.setOutput(serviceOutput);
			}
			if ( impl == null ) {
				throw new InvalidConfigException("逻辑接口实现未配置!");
			}
			else {
				ServiceImplementation sImpl = new ServiceImplementation();
				String sDirect = impl.attributeValue("direct-impl");
				sImpl.setDirectImpl(Boolean.valueOf(sDirect));
				sImpl.setImplClass(impl.attributeValue("impl-class"));

				List teletextMapping = impl.element("boss-teletext-mapping").elements("boss-teletext");

				for (int i = 0; i < teletextMapping.size(); i++) {
					TeletextMapping m = new TeletextMapping();
					Element el = (Element) (teletextMapping.get(i));
					m.setParamName(el.attributeValue("param-name"));
					m.setMatch(el.attributeValue("match"));
					m.setTeletextTemplate(el.attributeValue("template"));
					m.setResolverClass(el.attributeValue("resolver"));
					m.setDirectImplClass(el.attributeValue("direct-impl"));

					/***
					 * 2011-11-15增加result-mapping-id 字段
					 */
					m.setResultMappingId(el.attributeValue("result-mapping-id"));
					/***
					 * end
					 */

					sImpl.getTeletextMapping().add(m);
				}
				List resultMapping = impl.elements("result-mapping");
				for (int i = 0; i < resultMapping.size(); i++) {
					Element mapping = (Element) (resultMapping.get(i));
					String id = mapping.attributeValue("template");

					/***
					 * 2011-11-15增加result-mapping-id 字段
					 */
					String resultMappingId = mapping.attributeValue("id");
					//如果存在result-mapping-id 則使用其作為map的key
					if ( resultMappingId != null && !"".equals(resultMappingId.trim()) ) {
						id = resultMappingId;
					}
					/***
					 * end
					 */

					List map = mapping.elements("mapping");
					List<ServiceResultMapping> serviceResultMapping = new ArrayList<ServiceResultMapping>();
					for (int j = 0; j < map.size(); j++) {
						ServiceResultMapping m = new ServiceResultMapping();
						Element el = (Element) (map.get(j));
						m.setName(el.attributeValue("name"));
						m.setExpression(el.attributeValue("expression"));
						m.setValueMapStr(el.attributeValue("value-map"));
						List subMapping = el.elements("mapping");
						recusiveAnalyzeResultMapping(m, subMapping);
						serviceResultMapping.add(m);
					}
					sImpl.getResultMapping().put(id, serviceResultMapping);
				}
				config.setImpl(sImpl);
			}
			if ( extension == null ) {
				throw new InvalidConfigException("逻辑接口扩展信息未配置, 如果没有扩展配置请保留该节点!");
			}
			else {
				ServiceExtension sExt = new ServiceExtension();
				List classes = extension.elements("class");
				for (int i = 0; i < classes.size(); i++) {

					Element el = (Element) (classes.get(i));
					ExtensionClassInfo cInfo = new ExtensionClassInfo();
					cInfo.setClassName(el.attributeValue("name"));
					List fields = el.elements("field");
					for (int j = 0; j < fields.size(); j++) {
						BeanFieldInfo bInfo = new BeanFieldInfo();
						Element bEl = (Element) (fields.get(j));
						bInfo.setClassName(bEl.attributeValue("class"));
						bInfo.setDataType(bEl.attributeValue("data-type"));
						bInfo.setName(bEl.attributeValue("name"));
						cInfo.getFields().add(bInfo);
					}
					sExt.getExtensions().add(cInfo);
				}
				config.setExtension(sExt);
			}

			config.initOutputFieldInfoMap();

			return config;
		}
		catch (Exception e) {
			throw new InvalidConfigException(e);
		}
	}

	public ServiceConfig readFromFile(File configFile) throws InvalidConfigException {
		if ( configFile == null ) { return null; }
		InputStreamReader fr = null;
		try {
			fr = new InputStreamReader(new FileInputStream(configFile), Charset.forName("utf-8"));
			return this.read(fr);
		}
		catch (FileNotFoundException e) {
			logger.error("逻辑接口配置失败[" + configFile.getAbsolutePath() + "]!");
			throw new InvalidConfigException(e);
		}
		finally {
			if ( fr != null ) {
				try {
					fr.close();
				}
				catch (IOException e) {
					logger.error(e, e);
				}
			}
		}
	}

	private void recusiveAnalyzeResultMapping(ServiceResultMapping parent, List resultMapping) {
		if ( resultMapping == null || resultMapping.size() == 0 ) { return; }
		for (int i = 0; i < resultMapping.size(); i++) {
			ServiceResultMapping m = new ServiceResultMapping();
			Element el = (Element) (resultMapping.get(i));
			m.setName(el.attributeValue("name"));
			m.setExpression(el.attributeValue("expression"));
			m.setValueMapStr(el.attributeValue("value-map"));
			List subMapping = el.elements("mapping");
			recusiveAnalyzeResultMapping(m, subMapping);
			parent.getChildResultMapping().add(m);
		}
	}

	public boolean save(ServiceConfig config) {
		return false;
	}

	@SuppressWarnings("unchecked")
	public List<ServiceConfig> readFromFolder(File folder) throws InvalidConfigException {
		try {
			List<ServiceConfig> list = new ArrayList<ServiceConfig>();
			Collection<File> configFiles = FileUtils.listFiles(folder, new IOFileFilter() {
				public boolean accept(File f, String s) {
					String fileName = f.getName();
					return fileName.startsWith("meta") && fileName.endsWith("xml");
				}

				public boolean accept(File f) {
					String fileName = f.getName();
					return fileName.startsWith("meta") && fileName.endsWith("xml");
				}

			}, null);
			for (File configFile : configFiles) {
				list.add(this.readFromFile(configFile));
			}
			return list;
		}
		catch (Exception e) {
			throw new InvalidConfigException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> readFolder(File folder) throws InvalidConfigException {
		try {
			List<String> list = new ArrayList<String>();
			Collection<File> configFiles = FileUtils.listFiles(folder, new IOFileFilter() {
				public boolean accept(File f, String s) {
					String fileName = f.getName();
					return fileName.startsWith("meta") && fileName.endsWith("xml");
				}

				public boolean accept(File f) {
					String fileName = f.getName();
					return fileName.startsWith("meta") && fileName.endsWith("xml");
				}

			}, null);
			for (File configFile : configFiles) {
				list.add(configFile.getAbsolutePath());
			}
			return list;
		}
		catch (Exception e) {
			throw new InvalidConfigException(e);
		}
	}

	private boolean validate(ServiceConfig config) {
		if ( config != null ) {
			List<ExtensionClassInfo> extensions = config.getExtension().getExtensions();
			List<String> extClasses = new ArrayList<String>();
			for (ExtensionClassInfo extension : extensions) {
				String className = extension.getClassName();
				if ( !extClasses.contains(className) )
					extClasses.add(className);
			}
			// 校验输入参数中的自定义class是否都已配置
			List<ServiceInputParameter> inputParams = config.getInput().getParams();
			for (ServiceInputParameter inputParam : inputParams) {
				String dataType = inputParam.getDataType();
				if ( dataType.equals("list") || dataType.equals("class") ) {
					if ( !extClasses.contains(inputParam.getClassName()) ) {
						logger.error("Class:" + inputParam.getClassName() + "未配置！");
						return false;
					}
				}
			}

			// 校验输出参数中的自定义class是否都已配置
			List<ServiceOutputField> outputFields = config.getOutput().getOutputFields();
			for (ServiceOutputField outputField : outputFields) {
				String dataType = outputField.getDataType();
				if ( dataType.equals("list") || dataType.equals("class") ) {
					if ( !extClasses.contains(outputField.getClassName()) ) {
						logger.error("Class:" + outputField.getClassName() + "未配置！");
						return false;
					}
				}
			}
			return true;
		}
		else {
			return false;
		}
	}
}
