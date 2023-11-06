package com.kosta.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.api.dto.AnimalClinic;
import com.kosta.api.dto.PageInfo;
import com.kosta.api.service.SeoulApiService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class SeoulApiController {

	@Autowired
	private SeoulApiService seoulApiService;

	@RequestMapping(value = { "/clinic/{page}", "/clinic" }, method = RequestMethod.GET)
	public ModelAndView animalClinicList(@PathVariable(required = false) Integer page) {
		PageInfo pageInfo = new PageInfo();
		if (page != null) {
			pageInfo.setCurPage(page);
		} else {
			pageInfo.setCurPage(1);
		}

		ModelAndView mav = new ModelAndView();
		try {
			List<AnimalClinic> acList = seoulApiService.animalClinicList(pageInfo);
			mav.addObject("acList", acList);
			mav.addObject("pageInfo", pageInfo);
			mav.setViewName("animalclinic");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "서울시 동물병원 허가 정보 조회 실패");
			mav.setViewName("error");
		}

		return mav;
	}

}
