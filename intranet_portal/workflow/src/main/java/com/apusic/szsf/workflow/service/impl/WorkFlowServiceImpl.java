package com.apusic.szsf.workflow.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.apusic.szsf.workflow.model.FlowInfo;
import com.apusic.szsf.workflow.model.WaittingProcParams;
import com.apusic.szsf.workflow.service.WorkFlowService;
import com.apusic.szsf.workflow.systems.SystemInterface;

public class WorkFlowServiceImpl implements WorkFlowService{

	private List<SystemInterface> systems;

	
	//获取模拟数据
	public List<FlowInfo> getFlowInfoTest(WaittingProcParams params) throws Exception {
	
		List<FlowInfo> flowInfoList = new ArrayList();
		int systemSize = systems.size();

		for(SystemInterface si:systems){
			FlowInfo flowInfo = si.getFlowInfoTest(params);
			flowInfoList.add(flowInfo);
		}
		
		return flowInfoList;
	}

	//获取多个系统
	public List<FlowInfo> getFlowInfoList(WaittingProcParams params) {
		
		List<FlowInfo> flowInfoList = new ArrayList();
		
		for(SystemInterface si:systems){
			FlowInfo flowInfo = si.getFlowInfo(params);
			if(flowInfo!=null)
				flowInfoList.add(flowInfo);
		}
		
		return flowInfoList;
	}

	//获取�?��系统的数�?
	public FlowInfo getFlowInfo(WaittingProcParams params,String systemName) {
	
		FlowInfo flowInfo = new FlowInfo();
		
		for(SystemInterface si:systems){
			String sys = si.getSystemName();
			if(sys.equals(systemName)){
				flowInfo = si.getFlowInfo(params);
			}
			
		}
		return flowInfo;
	}
	
	
	public void setSystems(List<SystemInterface> systems) {
		this.systems = systems;
	}

	

	


	
	
}
