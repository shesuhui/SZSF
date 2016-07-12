package com.apusic.szsf.workflow.service;

import java.util.List;
import java.util.Map;

import com.apusic.szsf.workflow.model.FlowInfo;
import com.apusic.szsf.workflow.model.WaittingProcParams;

public interface WorkFlowService {

	// 模拟数据测试
	//public abstract FlowInfo getFlowInfoTest(Map<String,Object> args);

	public List<FlowInfo> getFlowInfoTest(WaittingProcParams params)throws Exception ;
	public List<FlowInfo> getFlowInfoList(WaittingProcParams params);
	public FlowInfo getFlowInfo(WaittingProcParams params, String systemName);

}