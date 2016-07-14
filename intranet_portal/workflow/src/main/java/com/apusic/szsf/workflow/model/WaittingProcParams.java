package com.apusic.szsf.workflow.model;

import java.io.Serializable;

public class WaittingProcParams implements Serializable{
	
	private String userAccount;
	private int  type;
	private int pageNum;
	private int pageSize;
	private String categoryIds;
	private String formsetIds;
	
	public void setUserAccount(String userAccount){
		this.userAccount=userAccount;
	}
	
	public String getUserAccount(){
		return this.userAccount;
	}
	
	public void setType(int type){
		this.type=type;
	}
	
	public int getType(){
		return this.type;
	}
	
	public void setPageNum(int pageNum){
		this.pageNum=pageNum;
	}
	
	public int getPageNum(){
		return this.pageNum;
	}
	
	public void setPageSize(int pageSize){
		this.pageSize=pageSize;
	}
	
	public int getPageSize(){
		return this.pageSize;
	}

	public void setCategoryIds(String categoryIds){
		this.categoryIds=categoryIds;
	}
	
	public String getCategoryIds(){
		return this.categoryIds;
	}
	
	public void setFormsetIds(String formsetIds){
		this.formsetIds=formsetIds;
	}
	
	public String getFormsetIds(){
		return this.formsetIds;
	}

}
