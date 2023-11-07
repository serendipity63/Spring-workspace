package com.kosta.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.api.dto.UserInfo;
import com.kosta.api.service.KakaoLoginService;

@Controller
public class KakaoLoginController {

	@Autowired
	private KakaoLoginService kakaoLoginService;

	@RequestMapping(value = "/kakao", method = RequestMethod.GET)
	public String kakao() {
		return "kakaologin";
	}

	@RequestMapping(value = "/kakaologin", method = RequestMethod.GET)
	public ModelAndView kakaoLogin(@RequestParam("code") String code) {
		ModelAndView mav = new ModelAndView();
		try {
			UserInfo userInfo = kakaoLoginService.kakaoLogin(code);
			mav.addObject("userInfo", userInfo);
			mav.setViewName("userinfo");

		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "카카오 로그인 실패");
			mav.setViewName("error");
		}
		return mav;
	}

}
