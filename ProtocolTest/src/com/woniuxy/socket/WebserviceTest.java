package com.woniuxy.socket;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;

import com.woniuxy.http.HttpRequest;

public class WebserviceTest {

	public static void main(String[] args) {
		WebserviceTest wt =  new WebserviceTest();
		
//        wt.queryIpByHttp();
//        wt.queryPhoneNumByHttp();
		wt.queryIpByAxis2();
	}
	//直接使用标准的post请求查询IP地址归属地
	public void queryIpByHttp() {
		String postUrl = "http://ws.webxml.com.cn/WebServices/"
				+ "IpAddressSearchWebService.asmx/getCountryCityByIp";
		String postParam ="theIpAddress=120.123.11.123";
		com.woniuxy.http.HttpRequest hr = new HttpRequest();
		String resp = hr.sendPost(postUrl, postParam);
		System.out.println(resp);
	}
	//直接使用标准的post请求查询IP地址归属地
		public void queryPhoneNumByHttp() {
			String postUrl = "http://ws.webxml.com.cn/WebServices/"
					+ "MobileCodeWS.asmx/getMobileCodeInfo";
			String postParam ="mobileCode=15249151473&userID=";
			com.woniuxy.http.HttpRequest hr = new HttpRequest();
			String resp = hr.sendPost(postUrl, postParam);
			System.out.println(resp);
		}
		//使用Axis2进行远程调用
		public void queryIpByAxis2() {
			try {
				ServiceClient sc = new ServiceClient();
				Options options = new Options();
				options.setProperty(HTTPConstants.CHUNKED, false);
				String url = "http://ws.webxml.com.cn/WebServices/"
						+ "IpAddressSearchWebService.asmx/getCountryCityByIp";
				EndpointReference end = new EndpointReference(url);
				options.setTo(end);
				//改Action的内容参考WSDL文件中的soapAction的属性值
				options.setAction("http://WebXml.com.cn/getCountryCityByIp");
				sc.setOptions(options);
				
				OMFactory fac = OMAbstractFactory.getOMFactory();
				//该命名的内容参考WSDL文件中的targetNamespace
				OMNamespace omNs = fac.createOMNamespace("http://WebXml.com.cn/", "");
				//明确方法名，方法的参数名及参数对应的值
				OMElement method = fac.createOMElement("getCountryCityByIp", omNs);
				OMElement value = fac.createOMElement("theIpAddress", omNs);
				value.setText("129.105.1.26");
				method.addChild(value);
				OMElement res = sc.sendReceive(method);
				System.out.println(res);
			} catch (AxisFault e) {
				// Axis2将异常封装为一个AxisFault进行抛出。任何类型的异常Axis2都会对其进行封装
				e.printStackTrace();
			}
					
					
					
					
					
					
					
					
					
		}

}
