package com.ucap.web.cm.webapp.filter;

import com.ucap.account.model.ExplicitUser;
import com.ucap.account.model.SessionUser;
import com.ucap.account.model.User;
import com.ucap.account.service.UserManager;
import com.ucap.base.model.system.ExplicitMenu;
import com.ucap.business.db.PageData;
import com.ucap.business.server.ExtendedServerConfig;
import com.ucap.system.service.MenuManager;
import com.ucap.utils.formatString.Validator;
import com.ucap.web.cm.webapp.listener.OnlineUsers;
import com.ucap.web.cm.webapp.util.RequestUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class LoginFilter implements Filter {
	protected FilterConfig filterConfig;
	private String encoding;
	private static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
	protected String redirectUrl;
	protected String noFiltedUrlsStr;
	private List<String> noFiltedUrls;
	boolean enable_cas_sso_funation = false;

	private String getUserUrl = null;

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpservletrequest = (HttpServletRequest) request;
		HttpServletResponse httpservletresponse = (HttpServletResponse) response;
		if (this.encoding == null) {
			this.encoding = "UTF-8";
		}
		request.setCharacterEncoding(this.encoding);
		String userId = (String) httpservletrequest.getSession().getAttribute("userId");

		String url = httpservletrequest.getRequestURI();

		if (Validator.isNotNull(userId)) {
			SessionUser sUser = OnlineUsers.getInstance().getUser(userId);
			if ((sUser != null) && ("777".equals(sUser.getStatus()))) {
				httpservletrequest.setAttribute("no_session", Boolean.valueOf(true));

				httpservletresponse.getWriter().write(
						"<script>alert('对不起，您已经断开链接!');window.top.location.href='" + this.redirectUrl + "';</script>");
				return;
			}
			if (sUser != null) {
				if (!sUser.getSessionId().equalsIgnoreCase(httpservletrequest.getSession().getId())) {
					httpservletrequest.getSession().invalidate();
					if (this.redirectUrl.contains("logout"))
						httpservletresponse.getWriter().write("<script>alert('对不起，您已经断开链接!');window.top.location.href='"
								+ this.redirectUrl + "';</script>");
					else {
						httpservletresponse.sendRedirect(this.redirectUrl);
					}

					return;
				}
				executeFilter(request, response, chain);
				return;
			}

			executeFilter(request, response, chain);
			return;
		}

		if (isNotFilterUrl(url)) {
			executeFilter(request, response, chain);
			return;
		}

		// if (this.enable_cas_sso_funation) {
		// Object object =
		// httpservletrequest.getSession().getAttribute("_const_cas_assertion_");
		// if (object != null) {
		// Assertion assertion = (Assertion) object;
		// String loginName = assertion.getPrincipal().getName();
		WebApplicationContext wct = WebApplicationContextUtils
				.getWebApplicationContext(httpservletrequest.getSession().getServletContext());

		UserManager userManager = (UserManager) wct.getBean("userManager");
		String loginName = "admin";

		MultiThreadedHttpClient httpClient = new MultiThreadedHttpClient();
		String accessToken = httpservletrequest.getParameter("accesstoken");
		if (!StringUtils.isBlank(accessToken)) {
			String responseBody = httpClient.executeHttpGet(this.getUserUrl + accessToken);

			System.out.println(responseBody);

			User user = userManager.getUserByLoginId(loginName);

			if (!Validator.isEmpty(user)) {
				setUserInformationInSession(user, httpservletrequest, wct);
				executeFilter(request, response, chain);

				OnlineUsers.captureUser((HttpServletRequest) request, user);
				return;
			}
		}

		// }

		// }

		httpservletrequest.setAttribute("no_session", Boolean.valueOf(true));

		httpservletresponse.getWriter().write(
				"<script>alert('当前会话已经失效，请您重新登录!');window.top.location.href='" + this.redirectUrl + "';</script>");
	}

	private void executeFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpservletrequest = (HttpServletRequest) request;
		HttpServletResponse httpservletresponse = (HttpServletResponse) response;
		putInformationOfClientToMDC(httpservletrequest);
		chain.doFilter(httpservletrequest, httpservletresponse);
	}

	private void putInformationOfClientToMDC(HttpServletRequest httpservletrequest) {
		ExplicitUser explicitCurrentUser = (ExplicitUser) httpservletrequest.getSession()
				.getAttribute("explicitCurrentUser");

		if (!Validator.isEmpty(explicitCurrentUser)) {
			MDC.put("explicitCurrentUser", explicitCurrentUser);
		}
		String currentTimezoneId = (String) httpservletrequest.getSession().getAttribute("TimeZone");

		if (Validator.isNotNull(currentTimezoneId))
			MDC.put("currentTimezoneId", currentTimezoneId);
	}

	private void setUserInformationInSession(User user, HttpServletRequest request, WebApplicationContext wct) {
		String userId = user.getUserId();
		setUserIdInSession(userId, request);
		setUserLoginIdSession(user.getLoginId(), request);
		setUserNameSession(user.getName(), request);
		setTimezoneInSession(request);
		List menus = getMenu(userId, request, wct);
		setViewMenusInSession(menus, request);
		setExplicitMenusInSession(menus, request);
		setExplicitUserInSession(user, request, wct);
		setUrlInSession(getUrlsByMenus(menus), request);
	}

	private void setUserIdInSession(String userId, HttpServletRequest request) {
		request.getSession().setAttribute("userId", userId);
		String ip = RequestUtil.getIP(request);
		request.getSession().setAttribute(userId, ip);
	}

	private void setUserLoginIdSession(String loginId, HttpServletRequest request) {
		request.getSession().setAttribute("loginId", loginId);
	}

	private void setUserNameSession(String userName, HttpServletRequest request) {
		request.getSession().setAttribute("userName", userName);
	}

	private List<ExplicitMenu> getMenu(String userId, HttpServletRequest request, WebApplicationContext wct) {
		if (Validator.isNull(userId))
			return null;
		PageData pageData = new PageData();
		pageData.setSortFieldName("SORT_VALUE");
		pageData.setSortByAD("ASC");
		MenuManager menuManager = (MenuManager) wct.getBean("menuManager");
		menuManager.setPageParams(pageData, "SORT_VALUE", "ASC");
		List menus = menuManager.getMenus(null, null, false, userId);
		return menus;
	}

	private void setViewMenusInSession(List<ExplicitMenu> explicitMenus, HttpServletRequest request) {
		if (!Validator.isEmpty(explicitMenus)) {
			List viewMenus = getViewMenus(explicitMenus);
			request.getSession().setAttribute("viewMenus", viewMenus);
		}
	}

	private List<ExplicitMenu> getViewMenus(List<ExplicitMenu> explicitMenus) {
		List viewMenus = new ArrayList();
		if (!Validator.isEmpty(explicitMenus)) {
			for (int i = 0; i < explicitMenus.size(); i++) {
				ExplicitMenu explicitMenu = (ExplicitMenu) explicitMenus.get(i);
				String status = explicitMenu.getStatus();
				if ("3".equals(status)) {
					viewMenus.add(explicitMenu);
				}
			}
		}
		return viewMenus;
	}

	private void setExplicitMenusInSession(List<ExplicitMenu> explicitMenus, HttpServletRequest request) {
		if (!Validator.isEmpty(explicitMenus)) {
			request.getSession().setAttribute("explicitMenus", explicitMenus);
		}
	}

	private void setExplicitUserInSession(User user, HttpServletRequest request, WebApplicationContext wct) {
		String userId = user.getUserId();
		UserManager userManager = (UserManager) wct.getBean("userManager");
		ExplicitUser eu = userManager.getExplicitUserById(userId);
		request.getSession().setAttribute("explicitCurrentUser", eu);
	}

	private void setUrlInSession(List<String> urls, HttpServletRequest request) {
		if (!Validator.isEmpty(urls))
			request.getSession().setAttribute("localUrls", urls);
	}

	private List<String> getUrlsByMenus(List<ExplicitMenu> menus) {
		if (Validator.isEmpty(menus))
			return null;
		List urls = new ArrayList();
		for (ExplicitMenu explicitMenu : menus) {
			urls.add(explicitMenu.getUrl());
		}
		return urls;
	}

	private void setTimezoneInSession(HttpServletRequest request) {
		String timeZoneId = (String) request.getSession().getAttribute("TimeZone");
		if (Validator.isNull(timeZoneId))
			timeZoneId = "GMT+8:00";
		request.getSession().setAttribute("TimeZone", timeZoneId);
	}

	private boolean isNotFilterUrl(String url) {
		boolean isNotFilterUrl = false;
		List list = getNoFiltedUrls();
		for (int i = 0; i < list.size(); i++) {
			String temp = (String) list.get(i);
			if (url.indexOf(temp) >= 0) {
				isNotFilterUrl = true;
				break;
			}
			isNotFilterUrl = false;
		}

		return isNotFilterUrl;
	}

	private List<String> getNoFiltedUrls() {
		if (!Validator.isEmpty(this.noFiltedUrls))
			return this.noFiltedUrls;
		List list = null;
		if (Validator.isNotNull(this.noFiltedUrlsStr)) {
			String[] urls = this.noFiltedUrlsStr.split(";");

			if ((Validator.isEmpty(urls)) || (urls.length == 0)) {
				list = getDefaultNoFiltedUrls();
			} else {
				list = new ArrayList(urls.length);
				for (int i = 0; i < urls.length; i++) {
					String url = urls[i];
					if (Validator.isNull(url)) {
						continue;
					}
					list.add(url.trim());
				}
			}
		} else {
			list = getDefaultNoFiltedUrls();
		}
		this.noFiltedUrls = list;
		return list;
	}

	private List<String> getDefaultNoFiltedUrls() {
		List list = new ArrayList();
		list.add("login_login.action");
		list.add("yqUrl_login.action");
		list.add("getRandomImages_login.action");
		list.add("validateCode_login.action");
		list.add("validateUserByLoginId_login.action");
		list.add("validatePwd_login.action");
		list.add("checkAccountIsNotExpired_login.action");
		list.add("checkPwdIsNotExpired_login.action");
		list.add("ajaxValidateLogin_login.action");
		list.add("preview_resource.action");
		list.add("getWebsiteAndChannelNotLoginTree_tree.action");
		list.add("viewCommentListEntry_comment.action");
		list.add("insertCommentEntry_comment.action");
		list.add("insertComment_comment.action");
		list.add("insertCommentWindow_comment.action");
		list.add("insertCommentWindowEntry_comment.action");
		list.add("insertCitationEntry_comment.action");
		list.add("insertCitation_comment.action");
		list.add("viewAnswers_answerComment.action");
		list.add("add_answerComment.action");
		list.add("save_answerComment.action");
		return list;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		if (filterConfig.getInitParameter("redirectUrl") != null)
			this.redirectUrl = filterConfig.getInitParameter("redirectUrl");
		this.encoding = filterConfig.getInitParameter("encoding");
		if (filterConfig.getInitParameter("noFiltedUrlsStr") != null) {
			this.noFiltedUrlsStr = filterConfig.getInitParameter("noFiltedUrlsStr");
		}
		this.enable_cas_sso_funation = ExtendedServerConfig.getInstance().getBoolProperty("enable_cas_sso_funation");
		if (filterConfig.getInitParameter("getUserUrl") != null)
			this.getUserUrl = filterConfig.getInitParameter("getUserUrl");
	}
}