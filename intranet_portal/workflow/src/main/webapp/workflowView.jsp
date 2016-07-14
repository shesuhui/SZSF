<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%
	String contextPath = request.getContextPath();
%>
<script type="text/javascript" src="<%=contextPath %>/js/workflow.js"></script>
<link rel="stylesheet" href="<%=contextPath %>/css/style.css" />
<script type="text/javascript" src="<%=contextPath %>/js/jquery.js"></script>
<script type="text/javascript" src="<%=contextPath %>/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="<%=contextPath %>/css/jquery.dataTables.min.css" />

<style>
.tabsmenu li.active{
	border: 1px #BCE066 solid;
	background-color: #E8FBBC;
	font-weight: bold;
	border-bottom-color: #E8FBBC;
}
.tabsmenu li{
	width: 29.5%;
	float:left;
	border: 1px #CCD4D6 solid;
	padding: 3px 8px;
	margin: 3px 0px 0px;
	color: #000;
	-moz-border-radius-topleft: 3px;
	-webkit-border-top-left-radius: 3px;
	-khtml-border-top-left-radius: 3px;
	border-top-left-radius: 3px;
	-moz-border-radius-topright: 3px;
	-webkit-border-top-right-radius: 3px;
	-khtml-border-top-right-radius: 3px;
	border-top-right-radius: 3px;
	background-color: #DEF0FE;
	cursor: pointer;
}
.tabsmenu li.a{
	border-top-right-radius: 0px;
	border-right:none;
}
.tabsmenu li.b{
	border-right:none;
	border-top-right-radius: 0px;
	border-top-left-radius: 0px;
}
.tabsmenu li.c{
	border-top-left-radius: 0px;
}
table.dataTable tbody th, table.dataTable tbody td {
	padding: 0px!important;
}
</style>
<script type="text/javascript">
$(function(){
	 listTable();
});
</script>
<div id="content" class="work-content">
	<div id="make-tab">
        <div id="tab" class="tabcontent">
			<div id="info"></div>
		  	<div>
			<table class="table-id">
				<thead>
					<tr id="thead" class="table-head">
					</tr>
				</thead>
		 		<tbody id="container">
		 	
				</tbody>
			</table>
		  	</div>
			<div class ="footer" style="width:100%;">
				<span id="total-record" class="total-record" ></span>
				<span id="page" class="page" ></span>
			</div>
        </div>
    </div>
</div>







