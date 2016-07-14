
package com.apusic.szsf.workflow.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apusic.szsf.workflow.model.FlowInfo;
import com.apusic.szsf.workflow.model.WaittingProcParams;
import com.apusic.szsf.workflow.service.WorkFlowService;

/**
 * 待办消息的controller
 * @author ssh
 * @version  0.0.1
 *
 */
@Controller
@RequestMapping("/")
public class WaittingProcController {
	
	@Autowired
	private WorkFlowService workFlowService;
	
	private static final Logger  logger=LoggerFactory.getLogger(WaittingProcController.class);

	@RequestMapping("/pagelist.do")
	@ResponseBody
	public List<FlowInfo>  getPageList(@RequestBody() WaittingProcParams params){
		List<FlowInfo> flowInfoList=null;
		try {
			flowInfoList = workFlowService.getFlowInfoTest(params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flowInfoList;
	}

}
