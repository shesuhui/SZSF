/*设置标题提示有多少需要处理的记录*/
function setTite(totalRecord) {
	/*
	 * var obj =
	 * $("#content_"+NS).parents(".aps-portlet-decorator-daylight").find(".aps-portlet-header
	 * .aps-portlet-header-content .aps-portlet-title");
	 */
	var obj = $("#content_").parents(".aps-portlet-container").find(
			".aps-portlet-header .aps-portlet-title");
	var title = obj.html();
	var count = obj.find(".count-title").html();
	if (count == null) {
		obj.html(title + "(<span class='count-title'>" + totalRecord
				+ "</span>)");
	} else {
		obj.find(".count-title").html(totalRecord);
	}
}
function LZ(x) {
	return (x < 0 || x > 9 ? "" : "0") + x;
}
function formatDate(date, format) {
	format = format + "";
	var result = "";
	var i_format = 0;
	var c = "";
	var token = "";
	var y = date.getYear() + "";
	var M = date.getMonth() + 1;
	var d = date.getDate();
	var H = date.getHours();
	var m = date.getMinutes();
	var s = date.getSeconds();
	var value = {};
	if (y.length < 4) {
		y = "" + (y - 0 + 1900);
	}
	value["y"] = "" + y;
	value["yyyy"] = y;
	value["yy"] = y.substring(2, 4);
	value["M"] = M;
	value["MM"] = LZ(M);
	value["d"] = d;
	value["dd"] = LZ(d);
	value["H"] = H;
	value["HH"] = LZ(H);
	if (H == 0) {
		value["h"] = 12;
	} else if (H > 12) {
		value["h"] = H - 12;
	} else {
		value["h"] = H;
	}
	value["hh"] = LZ(value["h"]);
	if (H > 11) {
		value["a"] = "PM";
	} else {
		value["a"] = "AM";
	}
	value["m"] = m;
	value["mm"] = LZ(m);
	value["s"] = s;
	value["ss"] = LZ(s);
	while (i_format < format.length) {
		c = format.charAt(i_format);
		token = "";
		while ((format.charAt(i_format) == c) && (i_format < format.length)) {
			token += format.charAt(i_format++);
		}
		if (value[token] != null) {
			result += value[token];
		} else {
			result += token;
		}
	}
	return result;
}

window.workflow = {
	pageSize : 2
};
function listTable() {
	var infoSel = "#info";
	var containerSel = "#container";
	var threadSel = "#thead";
	var pageSel = "#page";
	var totalRecordSel = "#total-record";
	$.ajax({
		url : "pagelist.do",
		dataType : 'json',
		contentType: "application/json",
		data : '{"userAccount" : "",	"type" : "1","pageNum" : "","pageSize" : "","categoryIds" : "",	"formsetIds" : ""}',
		type : 'post',
		beforeSend : function() {
			$(containerSel).empty();
			$(threadSel).empty();
			$(pageSel).empty();
			$(totalRecordSel).empty();
			$(infoSel).html("<h4>数据正在加载中...</h4>");
		},
		success : function(data) {
			$('#container').empty();
			$("#info").html("");
			viewPagedListData(data.data[2]);
		}
	});
}

/**
 * ns,"#tab"+ns,data
 */
function viewPagedListData(data) {
	var totalRecord = data.totalRecord;
	var totalPage = Math.ceil(totalRecord / window.workflow.pageSize);
	window.workflow.totalPage = totalPage;
	var list = data.infoList;
	var pageNum = 1;
	var funcName = 'turnPagedPage';
	var infoSel = "#info";
	var containerSel = "#container";
	var threadSel = "#thead";
	if (totalRecord != 0) {
		$(containerSel).html("");
		$(threadSel)
				.append(
						'<th style="width:40%;padding: 0px 0px 0px 6px;">标题</th><th style="width:30%;padding: 0px;">当前步骤</th><th 	style="width:30%;padding: 0px;">接收时间</th><th style="display:none;">接收时间</th>');
		for (var k = 0; k < list.length; k++) {
			var d = list[k];
			if (d.systemName) {
			} else {
				d.systemName = '司法鉴定';
			}
			if (d.workItemId.indexOf('http') >= 0) {
				$(containerSel).append(
						'<tr class="tr_'
								+ k
								+ '">'
								+ '<td><span class="work-title"><a title="'
								+ d.title
								+ '" href="javascript:openWorkWindow2(\''
								+ d.workItemId
								+ '\',\''
								+ d.docInstId
								+ '\')">['
								+ d.systemName
								+ ']'
								+ d.title
								+ '</a></span></td>'
								+ '<td><span>'
								+ d.currentStep
								+ '</span></td>'
								+ '<td><span class="time">'
								+ formatDate(new Date(d.receiveTime),
										'yyyy/MM/dd HH:mm') + '</span></td>'
								+ '<td style="display:none;">' + d.receiveTime
								+ '</td>' + '</tr>');
			} else {
				$(containerSel).append(
						'<tr class="tr_'
								+ k
								+ '">'
								+ '<td><span class="work-title"><a title="'
								+ d.title
								+ '" href="javascript:openWorkWindow(\''
								+ d.workItemId
								+ '\',\''
								+ d.docInstId
								+ '\')">['
								+ d.systemName
								+ ']'
								+ d.title
								+ '</a></span></td>'
								+ '<td><span>'
								+ d.currentStep
								+ '</span></td>'
								+ '<td><span class="time">'
								+ formatDate(new Date(d.receiveTime),
										'yyyy/MM/dd HH:mm') + '</span></td>'
								+ '<td style="display:none;">' + d.receiveTime
								+ '</td>' + '</tr>');
			}
			/* 紧急程度判断 */
			var rank = '.rank' + k;
			if (d.rank == "特急") {
				$(rank).addClass("red");
			} else if (d.rank == "加急") {
				$(rank).addClass("green");
			}
		}
		$('#tab table').dataTable({
			"bFilter" : false,
			"order" : [ [ 3, "desc" ] ],
			"oLanguage" : {
				"sProcessing" : "正在加载中......",
				"sLengthMenu" : "",
				"sZeroRecords" : "没有数据！",
				"sEmptyTable" : "表中无数据存在！",
				"sInfo" : "共 _TOTAL_ 条记录",
				"sInfoEmpty" : "显示0到0条记录",
				"sInfoFiltered" : "数据表中共为 _MAX_ 条记录",
				"sSearch" : "搜索",
				"oPaginate" : {
					"sFirst" : "首页",
					"sPrevious" : "上一页",
					"sNext" : "下一页",
					"sLast" : "末页"
				}
			}
		});
	} else {
		if ("${type}" == "1") {
			$(infoSel).append('<h4>没有待办的公文</h4>');
		}
		if ("${type}" == "2") {
			$(infoSel).append('<h4>没有待阅的公文</h4>');
		}
	}
	setTite(totalRecord);
}

function turnPagedPage(page) {

}

/* 第一次请求所有的系统 */
function list(pageNum) {

	var infoSel = "#info";
	var containerSel = "#container";
	var threadSel = "#thead";
	var pageSel = "#page";
	var totalRecordSel = "#total-record";

	$.ajax({
		url : null,
		data : {
			pageNum : pageNum
		},
		dataType : 'json',
		type : 'post',
		beforeSend : function() {
			$(containerSel).empty();
			$(threadSel).empty();
			$(pageSel).empty();
			$(totalRecordSel).empty();
			$(infoSel).html("<h4>数据正在加载中...</h4>");
		},
		success : function(data) {

			$('#container').empty();
			$("#info").html("");

			viewListData("#tab", data);

		}
	});
}

/* 显示所有的业务系统的数据 */
function viewListData(belong, data) {
	var totalRecord = 0;
	// alert('${NS}');
	// alert(data.length);
	for (var j = 0; j < data.length; j++) {
		var flowinfo = data[j];
		var belongId = belong + (j + 1);// tabId
		var infoSel = belongId + " #info";
		var containerSel = belongId + " #container";
		var threadSel = belongId + " #thead";

		$("#link-tab" + (j + 1)).html(
				flowinfo.systemName ? flowinfo.systemName : "法律服务");
		if (flowinfo.responseCode == 0) {
			/* 等于0表示从接口获取数据失败 */
			$(infoSel).append(
					'<h4>' + flowinfo.systemName + ',' + flowinfo.responseText
							+ '</h4>');
			continue;
		} else {
			if (flowinfo.infoList.length != 0) {

				viewTable(threadSel, containerSel, flowinfo);

				/* 传入参数一次为总页数，分页导航显示页数，当前页 */
				createPage(flowinfo.totalPage, 4, flowinfo.pageNum,
						flowinfo.totalRecord, "turnPage", belongId,
						flowinfo.systemName);
			} else {
				if ("${type}" == "1") {
					$(infoSel).append('<h4>没有待办的公文</h4>');
				}
				if ("${type}" == "2") {
					$(infoSel).append('<h4>没有待阅的公文</h4>');
				}
			}
		}

		totalRecord += parseInt(flowinfo.totalRecord);

	}/* 遍历每个系统var j循环结束 */

	// 设置显示总的待阅或待办数
	setTite(totalRecord);
}

/* 翻页 */
function turnPage(belongId, systemName, pageNum) {

	var infoSel = belongId + " #info";
	var containerSel = belongId + " #container";
	var threadSel = belongId + " #thead";
	var pageSel = belongId + " #page";
	var totalRecordSel = belongId + " #total-record";

	$.ajax({
		url : null,
		data : {
			pageNum : pageNum,
			systemName : systemName
		},
		dataType : 'json',
		type : 'post',
		beforeSend : function() {
			$(containerSel).empty();
			$(threadSel).empty();
			$(pageSel).empty();
			$(totalRecordSel).empty();
			$(infoSel).html("<h4>数据正在加载中...</h4>");
		},
		success : function(data) {

			$(containerSel).empty();
			$(infoSel).html("");

			viewSystemData(belongId, data);

		}
	});
}

/* 单个系统翻页显示 */
function viewSystemData(belongId, data) {

	var flowinfo = data;
	var infoSel = belongId + " #info";
	var containerSel = belongId + " #container";
	var threadSel = belongId + " #thead";

	if (flowinfo.responseCode == 0) {
		/* 等于0表示从接口获取数据失败 */
		$(infoSel).append(
				'<h4>' + flowinfo.systemName + ',' + flowinfo.responseText
						+ '</h4>');
	} else {
		if (flowinfo.infoList.length != 0) {

			viewTable(threadSel, containerSel, flowinfo);

			/* 传入参数一次为总页数，分页导航显示页数，当前页 */
			createPage(flowinfo.totalPage, 4, flowinfo.pageNum,
					flowinfo.totalRecord, "turnPage", belongId,
					flowinfo.systemName);
		} else {
			if ("${type}" == "1") {
				$(infoSel).append('<h4>没有待办的公文</h4>');
			}
			if ("${type}" == "2") {
				$(infoSel).append('<h4>没有待阅的公文</h4>');
			}
		}
	}

}
function openWorkWindow(workItemId, docInstId) {
	var width = window.screen.availWidth - 10;
	var height = window.screen.availHeight - 50;
	var url = '/missive/processing/workitem/Form.do?action=docInstEditView&workitemId='
			+ workItemId + '&docInstId=' + docInstId;

	var workWin = window
			.open(
					url,
					"workWindow",
					"width="
							+ width
							+ ",height="
							+ height
							+ ",resizeable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,status=yes,directories=no,top=0,left=0");
}

function openWorkWindow2(workItemId, docInstId) {

	var width = window.screen.availWidth - 10;

	var height = window.screen.availHeight - 50;

	var url = workItemId;

	var workWin = window
			.open(
					url,
					"workWindow",
					"width="
							+ width
							+ ",height="
							+ height
							+ ",resizeable=yes,scrollbars=yes,toolbar=no,menubar=no,location=no,status=yes,directories=no,top=0,left=0");

}

/* 显示table */
function viewTable(threadSel, containerSel, flowinfo) {
	$(containerSel).html("");
	$(threadSel)
			.append(
					'<th style="width:50%">标题</th><th style="width:20%">当前步骤</th><th style="width:30%">接收时间</th>');

	for (var k = 0; k < flowinfo.infoList.length; k++) {
		var d = flowinfo.infoList[k];

		if (d.workItemId.indexOf('http') >= 0) {
			$(containerSel).append(
					'<tr class="tr_' + k + '">'

					+ '<td><span class="work-title"><a title="' + d.title
							+ '" href="javascript:openWorkWindow2(\''
							+ d.workItemId + '\',\'' + d.docInstId + '\')">'
							+ d.title + '</a></span></td>'

							+ '<td><span>' + d.currentStep + '</span></td>'

							+ '<td><span class="time">' + d.receiveTime
							+ '</span></td>'

							+ '</tr>');
		} else {
			$(containerSel).append(
					'<tr class="tr_' + k + '">'
							+ '<td><span class="work-title"><a title="'
							+ d.title + '" href="javascript:openWorkWindow(\''
							+ d.workItemId + '\',\'' + d.docInstId + '\')">'
							+ d.title + '</a></span></td>' + '<td><span>'
							+ d.currentStep + '</span></td>'
							+ '<td><span class="time">' + d.receiveTime
							+ '</span></td>' + '</tr>');
		}
		if (k % 2 != 0) {
			$(".tr_" + k).addClass("even");
		}

		/* 紧急程度判断 */
		var rank = '.rank' + k;
		if (d.rank == "特急") {
			$(rank).addClass("red");
		} else if (d.rank == "加急") {
			$(rank).addClass("green");
		}

	}
}

/* 翻页处理 */
function createPage(totalPage, viewPageNum, pageNum, totalRecord, funcName,
		belongId, systemName) {

	pageSel = belongId + " #page";
	totalRecordSel = belongId + " #total-record";

	$(pageSel).empty();

	var totalPageCount = totalPage; // 分页导航总页数
	var viewPageNum = viewPageNum; // 分页导航每页数
	var currentPageIndex = pageNum; // 分页导航当前页索引

	var lastPage = pageNum - 1;
	var nextPage = parseInt(pageNum) + 1;

	$(totalRecordSel).empty();
	$(totalRecordSel).append('共有 ' + totalRecord + ' 条记录');

	$(pageSel).append(
			'<span class="total-page">共 ' + totalPageCount + ' 页</span>');

	if (lastPage > 0) {
		$(pageSel).append(
				'<a href="' + 'javascript:void(0);"  onclick="' + funcName
						+ '(\'' + belongId + '\',\'' + systemName + '\','
						+ lastPage + ');"><上一页</a>');
	}

	var start = currentPageIndex - (Math.ceil(viewPageNum / 2) - 1);
	// 限制开始页数，每页数小于总页数时
	if (viewPageNum < totalPageCount) {
		if (start < 1) {
			start = 1;
		} else if (start + viewPageNum > totalPageCount) {
			start = totalPageCount - viewPageNum + 1;
		}
	} else {
		start = 1;
	}
	var end = start + viewPageNum - 1;
	// 限制结束页数，当结束页数大于总页数时
	if (end > totalPageCount) {
		end = totalPageCount;
	}
	var newNumberStr;
	for (var i = start; i <= end; i++) {
		newNumberStr = "";

		if (i == currentPageIndex) {
			newNumberStr = '<span class="a-selected">' + i + '</span>';
		} else {
			newNumberStr = '<a href="' + 'javascript:void(0);"  onclick="'
					+ funcName + '(\'' + belongId + '\',\'' + systemName
					+ '\',' + i + ');">' + i + '</a>';
		}
		$(pageSel).append(newNumberStr);
	}

	if (nextPage <= totalPageCount) {
		$(pageSel).append(
				'<a id="" href="' + 'javascript:void(0);"  onclick="'
						+ funcName + '(\'' + belongId + '\',\'' + systemName
						+ '\',' + nextPage + ');">下一页></a>');
	}

}