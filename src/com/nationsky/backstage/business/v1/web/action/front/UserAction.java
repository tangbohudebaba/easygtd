package com.nationsky.backstage.business.v1.web.action.front;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.nationsky.backstage.business.v1.bsc.ISecurityService;
import com.nationsky.backstage.business.v1.bsc.dao.po.User;
import com.nationsky.backstage.business.v1.web.SecurityUrls;
import com.nationsky.backstage.core.Factor;
import com.nationsky.backstage.core.I18n;
import com.nationsky.backstage.core.Factor.C;
import com.nationsky.backstage.core.web.action.BaseAction;
import com.nationsky.backstage.util.HashUtil;
import com.nationsky.backstage.util.ValidateUtil;

@Controller
@RequestMapping(value = "/manager")
public class UserAction extends BaseAction {

	@Autowired
	private ISecurityService securityService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request,HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		System.out.println("正在登陆"+ username+"\t"+password);
		
		if(ValidateUtil.isNull(username)){
			this.getMessageMap().put("username", I18n.FILED_REQUIRED);
		}
		
		if(ValidateUtil.isNull(password)){
			this.getMessageMap().put("password", I18n.FILED_REQUIRED);
		}
		
		if(!this.getMessageMap().isEmpty()){
			return this.getModelAndView(SecurityUrls.SECURITY_LOGIN);
		}
		
		User user = securityService.getUnique(User.class, Factor.create("name", C.Eq, username));
		if(user!=null){
			if(ValidateUtil.isEquals(HashUtil.MD5Hashing(user.getPassword()), HashUtil.MD5Hashing(password))){
				System.out.println("登陆成功");
				request.setAttribute("text", "登陆成功");
				return this.getModelAndView(SecurityUrls.TEXT);
			}
		}
		this.getMessageMap().put("error", "用户名或密码错误");
		return this.getModelAndView(SecurityUrls.SECURITY_LOGIN);
	}
}
