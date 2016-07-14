package com.apusic.szsf.workflow.systems;

import java.util.Map;

import com.apusic.szsf.workflow.model.FlowInfo;
import com.apusic.szsf.workflow.model.WaittingProcParams;

public interface SystemInterface {
	
	public String getSystemName();

	public FlowInfo getFlowInfoTest(WaittingProcParams params) throws Exception;

	public FlowInfo getFlowInfo(WaittingProcParams params);

}
