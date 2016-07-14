package com.apusic.szsf.workflow.systems;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.apusic.szsf.workflow.model.FlowInfo;
import com.apusic.szsf.workflow.model.WaittingProcParams;

public class SystemImpl3 extends AbstractSystemImp implements SystemInterface{

	
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
				testFileName = "test5.xml";
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
			
			flowInfo=this.xmlToFlowInfo(xml);
			return flowInfo;

		} catch (Exception e) {
            throw e;
		}
	}

	@Override
	 public FlowInfo getFlowInfo(WaittingProcParams params) {
		// TODO Auto-generated method stub
		return null;
	}

}
