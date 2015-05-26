package com.xwtech.xwecp.util;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ResponseXmlFormatUtil{
   public static void main(String[] args) {
		String string  ="<?xml version=\"1.0\" encoding=\"GBK\" ?><operation_out><process_code>cc_smallpay_qrydetail</process_code><sysfunc_id>76</sysfunc_id><response_time>20140325103310</response_time><request_source>102010</request_source><request_seq>0_6</request_seq><request_type /><content><ret_code></ret_code><ret_msg></ret_msg><business_info><business_id>101401210081388728</business_id><crm_businessid>22100000482188</crm_businessid><payfee>-4200</payfee><pantform_date>20140121165050</pantform_date><status_date>2014-01-21 16:47:50</status_date><status>1</status><error_code>1</error_code><error_desc></error_desc><merchant_info>0026</merchant_info></business_info><business_info><business_id>101401220081841571</business_id><crm_businessid>22100000484196</crm_businessid><payfee>-1000</payfee><pantform_date>20140122141236</pantform_date><status_date>2014-01-22 14:09:35</status_date><status>1</status><error_code>1</error_code><error_desc></error_desc><merchant_info>0026</merchant_info></business_info><business_info><business_id>101401230082330002</business_id><crm_businessid>22100000486221</crm_businessid><payfee>-27500</payfee><pantform_date>20140123142848</pantform_date><status_date>2014-01-23 14:25:47</status_date><status>1</status><error_code>1</error_code><error_desc></error_desc><merchant_info>0026</merchant_info></business_info><business_info><business_id>101401240082878495</business_id><crm_businessid>22100000496726</crm_businessid><payfee>1000</payfee><pantform_date></pantform_date><status_date>2014-01-24 15:22:08</status_date><status>1</status><error_code>1</error_code><error_desc></error_desc><merchant_info></merchant_info></business_info><business_info><business_id>101401240082880673</business_id><crm_businessid>22100000496741</crm_businessid><payfee>27500</payfee><pantform_date></pantform_date><status_date>2014-01-24 15:27:25</status_date><status>1</status><error_code>1</error_code><error_desc></error_desc><merchant_info></merchant_info></business_info></content><response><resp_result>0</resp_result><resp_code>0000</resp_code><resp_desc><![CDATA[]]></resp_desc></response></operation_out>";
		System.out.println(format(string));;
		string = string.replace("\"", "'");
		System.out.println(string);
   }
    public static String format(String str)  {
    	//str = str.replace("\"", "'");
        StringReader in=null;
        StringWriter out=null;
        String string3 = str;
        if(str.indexOf("对应的业务解析方式失败") > -1)
        {
        	System.out.println(str);
        	return str;
        }
        else if(str.indexOf("为空 节点") > -1)
        {
        	System.out.println(str);
        	return str;
        }
        else if(str.indexOf("<?xml version=") == -1)
        {
        	System.out.println(str);
        	return str;
        }
        else
        {
            try{
                SAXReader reader=new SAXReader();
                //创建一个串的字符输入流
                in=new StringReader(str);
                Document doc=reader.read(in);
                //创建输出格式
                OutputFormat format = new OutputFormat("    ",true,"GBK");
    			format.setNewlines(true);
    			format.setTrimText(true);
                //formate.setEncoding("GBK");
                //创建输出
                out=new StringWriter();
                //创建输出流
                XMLWriter writer=new XMLWriter(out,format);
                //输出格式化的串到目标中,格式化后的串保存在out中。
                writer.write(doc);
                String string2 = out.toString();
                int beginIndex = string2.indexOf("?>")+2;
        		int endIndex = string2.indexOf("<operation_out>");
        		 string3 = string2.replace(string2.subSequence(beginIndex, endIndex), "\r\n");
            } catch (IOException ioe){
            	ioe.printStackTrace();
            } catch (DocumentException de){
            	de.printStackTrace();
            } finally{
                //关闭流
                quietClose(in);
                quietClose(out);
            }
         
            return string3;
        }
      }    
    
   
    public static void quietClose(Reader reader){
        try{
            if(reader!=null){
                reader.close();
            }
        } catch(IOException ioe){
        }

    }
    
    
    public static void quietClose(Writer writer){
        try{
            if(writer!=null){
                writer.close();
            }
        } catch(IOException ioe){
        	ioe.printStackTrace();
        }
    }   

}