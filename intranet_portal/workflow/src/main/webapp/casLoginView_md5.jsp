<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>登录</title>
<%response.setHeader("P3P","CP=CAO PSA OUR");%>

<link href="css/cas_login.css" rel="stylesheet" type="text/css" />
 <script type="text/javascript" src="js/jquery.js"></script>
 <script type="text/javascript" src="js/jquery.cookie.js"></script>
 <script type="text/javascript" src="js/jQuery.md5.js"></script>
<script type="text/javascript" src="/cas/bootstrap/js/bootstrap.min.js"></script>
<link type="text/css" rel="stylesheet" href="/cas/bootstrap/css/bootstrap.min.css"></link>
<style>
	body{
		background-color:#e8eff5;
	}
	.login-btn{
		width:100px;
		height:32px;
		line-height:32px;
		text-decoration:none;
		text-align:center;
		display:block;
		font-size:16px;
		color:#fff;
		background: url("login_btn.png") no-repeat 0px;
		float:left;
	}
	.login-btn:hover{
		color:yellow;
		text-decoration:none;
	}
    .iframe-input-pwd{
        width:100px;
    }
    #rememberLabel{
    	float:left;
    	margin-top:6px;
    	margin-left:5px;
    	margin-right:15px;
    	
    }
    #rememberMe{    	margin-top:10px;    	background-color:#63c045;    	color:#fff;    	float:left;    }        #remenberDiv{    	margin-top:20px;    }
</style>
<script type="text/javascript">
	$(function() {		
		$('.aps .aps-page input#username,.aps .aps-page input#password').focus(function(){
			$(this).addClass('focus');
		}).blur(function(){
			$(this).removeClass('focus');			});		
		$('#submit').click(function(){
			
		});
		$('input[type=text]').focus(function(){
			$('#respMsg').html('');		}).blur(function(){			$(this).val($.trim($(this).val()));	
		});
		if(top!=self){
			$('#ifram-login').css("display","block");
		}else{
			$('#login_main').css("display","block");
			$('body').addClass('login_bg');
		}

		$('.login_info input#username').focus();
           //页面加载判断是否有cookies存在；如果存在就存入相应的文本框； 
           if ($.cookie("username")!= null && $.cookie("username")!= "") { 
               $("#username").val($.cookie("username")); 
           } 
           if ($.cookie("password") != null && $.cookie("password") != "") { 
               $("#password").val($.cookie ("password")); 
           }            if ($.cookie("rememberMe") != null && $.cookie("rememberMe") != "") { 
        	   if($.cookie("rememberMe") == 'true'){
        		   $("#rememberMe").prop('checked',true); 
        	   }
           }            if ($.cookie("username")!= null && $.cookie("username")!= "") {                $("#username1").val($.cookie("username"));            }            if ($.cookie("password") != null && $.cookie("password") != "") {                $("#password1").val($.cookie ("password"));            }            if ($.cookie("rememberMe") != null && $.cookie("rememberMe") != "") {         	   if($.cookie("rememberMe") == 'true'){        		   $("#rememberMe1").prop('checked',true);         	   }           } 
	});

	function submitXXX(){ 	   chkFlag = $("#rememberMe").prop('checked');	   if (chkFlag) { 		   $.cookie("password", $("#password").val(), {expires: 14 }); 	   }else{		   $.cookie("password", null); 	   }       $.cookie("username", $("#username").val(), {expires: 14 });	   $.cookie("rememberMe", chkFlag,{expires: 14 });
	   //md5密码加密
	   raw=$("#password").val();
	   encrypt=md5(raw);
	   $("#password").val(encrypt);	   fm11.submit();
   }  	function windowSubmit(){	   chkFlag = $("#rememberMe1").prop('checked');	   if (chkFlag) { 		   $.cookie("password", $("#password1").val(), {expires: 14 }); 	   }else{		   $.cookie("password", null); 	   }       $.cookie("username", $("#username1").val(), {expires: 14 });	   $.cookie("rememberMe", chkFlag,{expires: 14 });
	   //md5密码加密
	   raw=$("#password1").val();
	   encrypt=md5(raw);
	   $("#password1").val(encrypt);	   aps-login-forms.submit();	} 
  	//关键字段加密
  	function md5(data){
  		salt='szsfj';
  		return $.md5(data+salt);
  	}		
</script>
 
</head>
<body style="color:inherit;">
	<div id="ifram-login" style="display:none;">	
	 <div class="iframe-inputs">
		<div class="input-div" style="margin: 30px 14px">
        <form:form method="post" id="fm11" cssClass="fm-v clearfix" commandName="${commandName}" htmlEscape="true">
			<div class="control-group">
			    <div class="controls">
				     <div class="input-prepend">
						<span class="add-on"><i class="icon-user"></i></span>
						<input  id="username" type="text" class="span3" name="username" placeholder="输入您的用戶名" value="" style="width: 170px;" >
					</div>
				</div>
                  </div>
			<div class="control-group">
			    <div class="controls">
				     <div class="input-prepend">
						<span class="add-on"><i class="icon-lock"></i></span>
						<input id="password" type="password" class="span3" name="password" placeholder="输入您的密码" style="width: 170px;" >                        <span id="respMsg" style="font-size: 14px;color: red;"><form:errors   cssClass="resp-error" path="*" id="status" element="div" /></span>
					</div>
				</div>
                  </div>
                  <div class="control-group">
			    <div class="controls" style="margin-left:5px">
                          <input type="hidden" name="lt" value="${flowExecutionKey}" />					
					<input type="hidden" name="_eventId" value="submit" />
					<input type="checkbox" id="rememberMe" name="rememberMe"/><label id="rememberLabel" for="rememberMe">记住密码</label>
					 <a class="login-btn" href="javascript:submitXXX();">登陆</a>
				</div>
			</div>
		</form:form>		
		</div>
	 </div>
	</div>
	<div class="login_main" style="display:none" id="login_main">
		<div class="inputs">	
			<div class="login_info">

				<form:form method="post" id="aps-login-forms" commandName="${commandName}" htmlEscape="true">
					<form:input id="username1" cssClass="focus input_txt"  cssStyle="width: 240px;" tabindex="1" accesskey="${userNameAccessKey}" path="username" autocomplete="false" htmlEscape="true"/>
					<form:password id="password1" tabindex="2" path="password"  cssStyle="width: 240px;" accesskey="${passwordAccessKey}" htmlEscape="true" autocomplete="off" cssClass="input_pwd"/>
					<span id="respMsg"><form:errors   cssClass="resp-error" path="*" id="status" element="div" /></span>
					<span class="password-service-warning"></span>
					<input type="hidden" name="lt" value="${flowExecutionKey}" />
					<div id="remenberDiv"><input type="checkbox" id="rememberMe1" name="rememberMe"/><label  for="rememberMe" style="display:inline">记住密码</label></div>					
					<input type="hidden" name="_eventId" value="submit" />					
					<input class="input_btn" type="submit" name="submit" id="submit" value=" " onclick="windowSubmit()" style="width: 320px;"/>
				</form:form>
				
			</div>
		</div>
	</div><div>	<iframe id="hiddencz" src="http://web.szsf:8888/apusic-ps-wcm/cz.jsp?logout=true" height="0px" width="0px" style="border: none;"></iframe></div>
</body>
</html>