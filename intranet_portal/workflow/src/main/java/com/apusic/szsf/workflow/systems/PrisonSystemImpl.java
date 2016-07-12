package com.apusic.szsf.workflow.systems;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.apusic.szsf.workflow.model.FlowInfo;
import com.apusic.szsf.workflow.model.WaittingProcParams;
import com.apusic.webservice.axis.sfj.OaWorkFlow;
import com.apusic.webservice.axis.sfj.OaWorkFlowLocator;
import com.apusic.webservice.axis.sfj.OaWorkFlowSoap;

public class PrisonSystemImpl extends AbstractSystemImp implements SystemInterface {

	public FlowInfo getFlowInfoTest(WaittingProcParams params) throws Exception {

		String userAccount = params.getUserAccount();
		int type = params.getType();
		int pageNum = params.getPageNum();
		int pageSize = params.getPageSize();

		String categoryIds = params.getCategoryIds();
		String formsetIds = params.getFormsetIds();

		FlowInfo flowInfo = new FlowInfo();
		try {

			String testFileName = null;
			if (type == 1) {
				testFileName = "test1.xml";
			} else if (type == 2) {
				testFileName = "test4.xml";
			}
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(testFileName);
			StringBuilder sb = new StringBuilder();
			String line = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			String xml = sb.toString();

			flowInfo = xmlToFlowInfo(xml);
			return flowInfo;

		} catch (Exception e) {
			throw e;
		}

	}

	public FlowInfo getFlowInfo(WaittingProcParams params) {

		String userAccount = params.getUserAccount();
		int type = params.getType();
		int pageNum = params.getPageNum();
		int pageSize = params.getPageSize();

		String categoryIds = params.getCategoryIds();
		String formsetIds = params.getFormsetIds();

		FlowInfo flowInfo = new FlowInfo();

		try {
			String flowWService_address = webServiceAddress;
			OaWorkFlow flow = new OaWorkFlowLocator(flowWService_address);
			OaWorkFlowSoap flowService = flow.getflowWService();
			String resultXml = flowService.getWaittingFlowInfos(userAccount, type, 0, pageNum, pageSize);
			flowInfo = xmlToFlowInfo(resultXml);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 当请求的页数大于总的页数时，实际返回的是最后一页的数据，所以这里判断设为空（后面再优化改进判断的地方）
		if (pageNum != flowInfo.getPageNum() && flowInfo.getPageNum() != 0) {
			flowInfo = null;
		}
		return flowInfo;
	}

}
