package com.kosta.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.bank.dto.Account;
import com.kosta.bank.service.AccountService;

@Controller
public class AccountController {

	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main() {
		return "main";
	}

	@RequestMapping(value = "/makeaccount", method = RequestMethod.GET)
	public String makeAccount() {
		return "makeaccount";

	}

	@RequestMapping(value = "/makeaccount", method = RequestMethod.POST)
	public String makeAccount(@ModelAttribute Account acc, Model model) {
		try {
			accountService.makeAccount(acc);
			Account sacc = accountService.accountInfo(acc.getId());
			model.addAttribute("acc", sacc);
			return "accountinfo";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("err", "계좌개설 실패");
			return "error";
		}
	}

	@RequestMapping(value = "/accountinfo", method = RequestMethod.GET)
	public String accountInfo() {
		return "accountinfoform";
	}

	@RequestMapping(value = "/accountinfo", method = RequestMethod.POST)
	public String accountInfo(@RequestParam("id") String id, Model model) {
		try {
			Account acc = accountService.accountInfo(id);
			model.addAttribute("acc", acc);
			return "accountinfo";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("err", "계좌조회 실패");
			return "error";
		}
	}

	@RequestMapping(value = "/allaccountinfo", method = RequestMethod.GET)
	public String allAcouuntInfo(Model model) {
		try {
			model.addAttribute("accs", accountService.allAccountInfo());
			return "allaccountinfo";
		} catch (Exception e) {
			model.addAttribute("err", "전체계좌 조회 실패");
			return "error";

		}
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	public String deposit() {
		return "deposit";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	public ModelAndView deposit(@RequestParam("id") String id, @RequestParam("money") Integer money) {
		ModelAndView mav = new ModelAndView();
		try {
			accountService.deposit(id, money);
			Account acc = accountService.accountInfo(id);
			mav.addObject("acc", acc);
			mav.setViewName("accountinfo");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", e.getMessage());
			mav.setViewName("error");
		}
		return mav; // setdispatcher 어쩌고랑 비슷
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdraw() {
		return "withdraw";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.POST)
	public ModelAndView withdraw(@RequestParam("id") String id, @RequestParam("money") Integer money) {
		ModelAndView mav = new ModelAndView();
		try {
			accountService.withdraw(id, money);
			Account acc = accountService.accountInfo(id);
			mav.addObject("acc", acc);
			mav.setViewName("accountinfo");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
}
