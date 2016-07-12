<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查阅卷宗</title>
</head>
<body>
	<div>
		
	    <h1>查阅人：${applyUser} }</h1>

		<object classid="clsid27CDB6E-AE6D-11cf-96B8-444553540000"
			codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0"
			width="373" height="166" align="center">
			<param name="movie" value="images/move.swf">
			<param name="quality" value="high">
			<param name="wmode" value="transparent">
			<!--这里代码可使Flash背景透明 -->
			<embed src="${pdfPath}" width="373" height="166" align="center"
				quality="high"
				pluginspage="http://www.macromedia.com/go/getflashplayer"
				type="application/x-shockwave-flash">
			</embed>
		</object>

	</div>

</body>
</html>