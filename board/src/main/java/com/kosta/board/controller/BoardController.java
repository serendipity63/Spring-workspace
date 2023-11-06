package com.kosta.board.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.board.dto.Board;
import com.kosta.board.dto.Member;
import com.kosta.board.dto.PageInfo;
import com.kosta.board.service.BoardService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;

	@Autowired
	private HttpSession session;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main() {
		return "main";
	}

	@RequestMapping(value = "/boardlist", method = RequestMethod.GET)
	public ModelAndView boardlist(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
		ModelAndView mav = new ModelAndView();
		try {
			PageInfo pageInfo = new PageInfo();
			System.out.println(page);
			pageInfo.setCurPage(page);
			List<Board> boardList = boardService.boardListByPage(pageInfo);
			mav.addObject("pageInfo", pageInfo);
			mav.addObject("boardList", boardList);
			mav.setViewName("boardlist");
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("error");
		}
		return mav;
	}

	@RequestMapping(value = "/boardwrite", method = RequestMethod.GET)
	public String boardWrite() {
		return "writeform";
	}

	@RequestMapping(value = "/boardwrite", method = RequestMethod.POST)
	public ModelAndView boardWrite(@ModelAttribute Board board, @RequestParam("file") MultipartFile file) {
		ModelAndView mav = new ModelAndView();
		try {
			Board writeboard = boardService.writeBoard(board, file);
			mav.addObject("board", writeboard);
			mav.setViewName("detailform");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "글 등록 오류");
			mav.setViewName("error");
		}
		return mav;
	}

	// view/image?filename=1 대신 view/image/1으로 가져올 수 있음
	@RequestMapping(value = "/image/{num}")
	@ResponseBody
	public void imageView(@PathVariable Integer num, HttpServletResponse response) {
		try {
			boardService.fileView(num, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/boarddetail/{num}/{page}", method = RequestMethod.GET)
	public ModelAndView boardDetail(@PathVariable Integer num, @PathVariable Integer page) {
		ModelAndView mav = new ModelAndView();
		try {
			Board board = boardService.boardDetail(num);
			mav.addObject("board", board);
			mav.addObject("page", page);
			Member user = (Member) session.getAttribute("user");
			if (user != null) {
				Boolean select = boardService.isBoardLike(user.getId(), num);
				mav.addObject("select", select);
			}
			mav.setViewName("detailform");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "글 상세 조회 오류");
			mav.setViewName("error");
		}
		return mav;
	}

	@RequestMapping(value = "/boardmodify/{num}/{page}", method = RequestMethod.GET)
	public ModelAndView boardModify(@PathVariable Integer num, @PathVariable Integer page) {
		ModelAndView mav = new ModelAndView();
		try {
			Board board = boardService.boardDetail(num);
			mav.addObject("board", board);
			mav.addObject("page", page);
			mav.setViewName("modify");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "글 상세 조회 오류");
			mav.setViewName("error");
		}
		return mav;
	}

	@RequestMapping(value = "/boardmodify", method = RequestMethod.POST)
	public ModelAndView boardModify(@ModelAttribute Board board, @RequestParam("file") MultipartFile file,
			@RequestParam("page") Integer page) {
		ModelAndView mav = new ModelAndView();
		try {
			Board mBoard = boardService.modifyBoard(board, file);
			mav.setViewName("redirect:/boarddetail/" + board.getNum() + "/" + page); // 수정 후 상세 후 리스트 유지
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "글 수정 오류");
			mav.setViewName("error");
		}
		return mav;
	}

	@RequestMapping(value = "/boarddelete/{num}/{page}", method = RequestMethod.GET)
	public String boardDelete(@PathVariable Integer num, @PathVariable Integer page) {
		try {
			boardService.removeBoard(num); // file 조회해서 url해당하는거 삭제하고
			return "redirect:/boardlist?page=" + page;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@RequestMapping(value = "/like", method = RequestMethod.POST)
	@ResponseBody // 반환되는게 data
	public String boardLike(@RequestParam("num") Integer num) {
		Member user = (Member) session.getAttribute("user");
		try {
			if (user == null)
				throw new Exception("로그인 필요");
			Boolean select = boardService.selectBoardLike(user.getId(), num);
			return String.valueOf(select);
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
	}

	@RequestMapping(value = "/boardsearch", method = RequestMethod.POST)
	public ModelAndView boardSearch(@RequestParam("page") Integer page, @RequestParam("type") String type,
			@RequestParam("keyword") String keyword) {
		ModelAndView mav = new ModelAndView();
		try {
			if (type.equals("all") || keyword == null || keyword.trim().equals("")) {
				mav.setViewName("redirect:/boardlist");
				return mav;
			}
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurPage(page);
			List<Board> boardList = boardService.boardSearchListByPage(type, keyword, pageInfo); // 객체를 파라미터로 넘기면 공유
			mav.addObject("pageInfo", pageInfo);
			mav.addObject("boardList", boardList);
			mav.addObject("type", type);
			mav.addObject("keyword", keyword);
			mav.setViewName("boardlist");
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("err", "게시글 검색 실패");
			mav.setViewName("error");
		}
		return mav;
	}
}
