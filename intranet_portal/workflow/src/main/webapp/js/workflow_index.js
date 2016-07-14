/*设置标题提示有多少需要处理的记录*/
function setTite(NS,totalRecord){
	
	/* var obj = $("#content_"+NS).parents(".aps-portlet-decorator-daylight").find(".aps-portlet-header .aps-portlet-header-content .aps-portlet-title"); */
	var obj = $("#content_"+NS).parents(".aps-portlet-container").find(".aps-portlet-header .aps-portlet-title");
	var title = obj.html();
	var count = obj.find(".count-title").html();
	if(count==null){
		obj.html(title+"(<span class='count-title'>"+totalRecord+"</span>)");
	}else{
		obj.find(".count-title").html(totalRecord);
	}
}

//设置更多链接
function setMoreText(NS){
	var header = $("#content_"+NS).parents(".aps-portlet-container").find(".aps-portlet-header");
	var more = $("#content_"+NS).parents(".aps-portlet-container").find(".aps-portlet-header .more-data").html();
	var moreUrl = window[NS+'_WORK_MORE_URL'];
	if(more==null)
		header.append('<div class="more-data"><a href="'+moreUrl+'">更多>></a></div>');
}

/*第一次请求所有的系统*/
function list(ns,pageNum){
	
	var infoSel = "#info"+ns;
	var containerSel = "#container"+ns;
	var threadSel = "#thead"+ns;
	var pageSel = "#page"+ns;
	var totalRecordSel = "#total-record"+ns;
	
	$.ajax({
		url: window[ns+'_WORK_URL'].listURL,
		data: {pageNum: pageNum},
		dataType: 'json',
		type: 'post',
		beforeSend:function(){			
			$(containerSel).empty();$(threadSel).empty();$(pageSel).empty();$(totalRecordSel).empty();		
			$(infoSel).html("<h4>数据正在加载中...</h4>");
		},
		success: function(data){
			
			$('#container'+ns).empty();
			$("#info"+ns).html("");
			viewListData(ns,data[0]);//由于现在只有一个系统直接访问第一个系统的数据显示								
						
		}
	});
}


/*显示所有的业务系统的数据*/
function viewListData(ns,data){
	
	var flowinfo = data;
	var totalRecord = flowinfo.totalRecord;
	var infoSel = "#info"+ns;
	var containerSel = "#container"+ns;
	var threadSel = "#thead"+ns;
	if(flowinfo.responseCode==0){
		/*等于0表示从接口获取数据失败*/
		$(infoSel).append('<h4>'+flowinfo.responseText+'</h4>');
	}
	else
	{
		if(flowinfo.infoList.length!=0){
			
			viewTable(ns,threadSel,containerSel,flowinfo);
			
		}else{
			if("${type}"=="1"){
				$(infoSel).append('<h4>没有待办的公文</h4>');
			}
			if("${type}"=="2"){
				$(infoSel).append('<h4>没有待阅的公文</h4>');
			}
		}
	}
		
				
	
	//设置显示总的待阅或待办数
	setTite(ns,totalRecord);
}



/*显示table*/
function viewTable(ns,threadSel,containerSel,flowinfo){
	$(containerSel).html("");
	
	var size = parseInt(flowinfo.totalRecord);
	var viewNum = parseInt(window[ns+'_WORK_VIEW_NUM']);

	if(size>viewNum){
		size = viewNum;
		setMoreText(ns);
	}
	for(var k=0;k<size;k++){
		var d = flowinfo.infoList[k];
		$(containerSel).append('<li class="tr_'+k+'">'
				+'<span class="rank'+k+ns+' num">'+(k+1)+'</span>'
				+'<span class="work-title"><a target="_blank" title="'+d.title+'" href="'+window[ns+'_DOMAIN']+'/missive/processing/workitem/Form.do?action=docInstEditView&workitemId='+d.workItemId+'&docInstId='+d.docInstId+'">'+d.title+'</a></span>'
			
				+'</li>');

		

																
	
	}
}

