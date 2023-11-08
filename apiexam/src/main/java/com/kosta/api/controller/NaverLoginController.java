package com.kosta.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.api.dto.UserInfo;
import com.kosta.api.service.NaverLoginService;

@Controller
public class NaverLoginController {

	@Autowired // 제발 빼먹지 말기
	private NaverLoginService naverLoginService;

	@Autowired
	private HttpSession session;

//	@RequestMapping(value = "/naver", method = RequestMethod.GET)
//	public String naver() {
//		return "naverlogin";
//	}

//	@RequestMapping(value = "/naverlogin", method = RequestMethod.GET)
//	public String naverLogin() {
//		return "callback";
//	}
	@RequestMapping(value = "/naverlogin", method = RequestMethod.GET)
	public ModelAndView naverLogin(HttpServletRequest request) {
//		public ModelAndView naverLogin(@RequestParam("code") String code, @RequestParam("state") String state) {
		ModelAndView mav = new ModelAndView();
		String code = request.getParameter("code");
		System.out.println("code:" + code);
		String state = request.getParameter("state");
		System.out.println("state:" + state);
		try {

			UserInfo userInfo = naverLoginService.naverLogin(code, state);
			session.setAttribute("userInfo", userInfo);
			mav.setViewName("naveruserinfo");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "네이버 로그인 실패");
			mav.setViewName("error");
		}
		return mav;
	}
}
