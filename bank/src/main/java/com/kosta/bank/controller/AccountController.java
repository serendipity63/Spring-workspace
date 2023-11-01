package com.kosta.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String accountInfo(@RequestParam("id") String id) {
		try {
			accountService.accountInfo(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "accountinfo";
	}

	@RequestMapping(value = "/allaccountinfo", method = RequestMethod.GET)
	public String allAcouuntInfo() {
		return "allaccountinfo";
	}

	@RequestMapping(value = "/deposit", method = RequestMethod.GET)
	public String deposit() {
		return "deposit";
	}

	@RequestMapping(value = "/withdraw", method = RequestMethod.GET)
	public String withdraw() {
		return "withdraw";
	}

}
