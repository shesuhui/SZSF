package com.apusic.szsf.workflow.model;

import java.util.Date;

public class Info {

	private String systemName;//数据来源系统名称
	
	private String docInstId;//公文实例id
	private String workItemId;//当前工作项id
	private String title;//流程实例标题
	private Date receiveTime;//接收时间
	private String rank;//紧�?程度
	private String formsetName;//公文种类
	private String currentStep;//当前步骤
	private Date completeTime;//完成时间
	

	
	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
	public String getDocInstId() {
		return docInstId;
	}
	public void setDocInstId(String docInstId) {
		this.docInstId = docInstId;
	}
	public String getWorkItemId() {
		return workItemId;
	}
	public void setWorkItemId(String workItemId) {
		this.workItemId = workItemId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getFormsetName() {
		return formsetName;
	}
	public void setFormsetName(String formsetName) {
		this.formsetName = formsetName;
	}
	public String getCurrentStep() {
		return currentStep;
	}
	public void setCurrentStep(String currentStep) {
		this.currentStep = currentStep;
	}
	public Date getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}
	
	
	
}
