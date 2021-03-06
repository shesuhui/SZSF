package com.apusic.szsf.workflow.systems;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.apusic.szsf.workflow.model.FlowInfo;
import com.apusic.szsf.workflow.model.Info;

public abstract class AbstractSystemImp implements SystemInterface {

	protected String webServiceAddress;// 服务器地址
	protected String systemName;// 数据来源系统名称

	public AbstractSystemImp() {
		super();
	}

	protected FlowInfo xmlToFlowInfo(String resultXml)
			throws ParserConfigurationException, SAXException, IOException, ParseException {

		FlowInfo flowInfo = new FlowInfo();
		List<Info> infoList = new ArrayList();

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(resultXml)));
		Element root = document.getDocumentElement();

		// 获取ResponseCode节点信息
		NodeList resList = root.getElementsByTagName("ResponseCode");
		Element ResponseCode = (Element) resList.item(0);
		int responseCode = Integer.parseInt(ResponseCode.getTextContent());
		flowInfo.setResponseCode(responseCode);

		// 获取ResponseText节点信息
		resList = root.getElementsByTagName("ResponseText");
		Element ResponseText = (Element) resList.item(0);
		flowInfo.setResponseText(ResponseText.getTextContent());

		if (responseCode != 0) {
			// 得到responseData节点
			NodeList nodeList = root.getElementsByTagName("ResponseData");
			Element ResponseData = (Element) nodeList.item(0);

			// 得到info-list节点
			NodeList info_lList = ResponseData.getElementsByTagName("info-list");
			Element info_listElement = (Element) info_lList.item(0);

			// 得到info-list的子节点
			NodeList info_ListChild = info_listElement.getChildNodes();

			// 遍历info-list的子节点并把页面信息存入PageInfo类中
			for (int i = 0; i < info_ListChild.getLength(); i++) {
				Node node = info_ListChild.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element personNode = (Element) node;
					if (personNode.getNodeName().equals("total-page")) {
						flowInfo.setTotalPage(Integer.parseInt(personNode.getTextContent()));
					} else if (personNode.getNodeName().equals("total-record")) {
						flowInfo.setTotalRecord(Integer.parseInt(personNode.getTextContent()));
					} else if (personNode.getNodeName().equals("page-num")) {
						flowInfo.setPageNum(Integer.parseInt(personNode.getTextContent()));
					} else if (personNode.getNodeName().equals("page-size")) {
						flowInfo.setPageSize(Integer.parseInt(personNode.getTextContent()));
					}
				}
			}

			// 读取<info>，并存入pageInfo
			NodeList infoNodeList = info_listElement.getElementsByTagName("info");
			for (int j = 0; j < infoNodeList.getLength(); j++) {
				// 这是info节点的数�?
				Info info = new Info();
				Element infoElement = (Element) infoNodeList.item(j);
				NodeList infoChildList = infoElement.getChildNodes();
				for (int k = 0; k < infoChildList.getLength(); k++) {
					// 这是info节点的子节点的数�?
					Node node = infoChildList.item(k);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;
						if (element.getNodeName().equals("docInstId")) {
							info.setDocInstId(element.getTextContent());
						} else if (element.getNodeName().equals("workItemId")) {
							info.setWorkItemId(element.getTextContent());
						} else if (element.getNodeName().equals("title")) {
							info.setTitle(element.getTextContent());
						} else if (element.getNodeName().equals("receive-time")) {
							String time = element.getTextContent();
							info.setReceiveTime(simpleDateFormat.parse(time));
						} else if (element.getNodeName().equals("rank")) {
							info.setRank(element.getTextContent());
						} else if (element.getNodeName().equals("formset-name")) {
							info.setFormsetName(element.getTextContent());
						} else if (element.getNodeName().equals("current-step")) {
							info.setCurrentStep(element.getTextContent());
						} else if (element.getNodeName().equals("complete-time")) {
							String time = element.getTextContent();
							if (time == "") {
								info.setCompleteTime(null);
							} else {
								info.setCompleteTime(simpleDateFormat.parse(time));
							}

						}
					}
				}
				info.setSystemName(getSystemName());
				infoList.add(info);
			}
			flowInfo.setInfoList(infoList);
		}

		flowInfo.setSystemName(getSystemName()); // 设置系统名称
		return flowInfo;
	}

	public void setWebServiceAddress(String webServiceAddress) {
		this.webServiceAddress = webServiceAddress;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemName() {
		return systemName;
	}

}