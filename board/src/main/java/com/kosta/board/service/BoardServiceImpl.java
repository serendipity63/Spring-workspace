package com.kosta.board.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.board.dao.BoardDao;
import com.kosta.board.dto.Board;
import com.kosta.board.dto.FileVo;
import com.kosta.board.dto.PageInfo;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;

	@Override
	public List<Board> boardListByPage(PageInfo pageInfo) throws Exception {
		int boardCount = boardDao.selectBoardCount();
		System.out.println(boardCount);
		if (boardCount == 0)
			return null;
		int allPage = (int) Math.ceil((double) boardCount / 10);
		int startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		int endPage = Math.min(startPage + 10 - 1, allPage);
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		if (pageInfo.getCurPage() > allPage)
			pageInfo.setCurPage(allPage);
		int row = (pageInfo.getCurPage() - 1) * 10 + 1;
		return boardDao.selectBoardList(row - 1);
	}

	@Override
	public List<Board> boardSearchListByPage(String type, String keyword, PageInfo pageInfo) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("type", type);
		param.put("keyword", keyword);
		int searchCount = boardDao.searchBoardCount(param);
		if (searchCount == 0)
			return null;

		int allPage = (int) Math.ceil((double) searchCount / 10);
		int startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		int endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		if (pageInfo.getCurPage() > allPage)
			pageInfo.setCurPage(allPage);
		int row = (pageInfo.getCurPage() - 1) * 10 + 1;
		param.put("row", row - 1);
		return boardDao.searchBoardList(param);
	}
	// 어디로 넘겨주는거지?

	@Override
	public Board writeBoard(Board board, MultipartFile file) throws Exception {
		if (file != null && !file.isEmpty()) { // 앞에서 false이면 뒤까지 가지도 않음 그래서 null 먼저 해야함
			String dir = "c:/jisu/upload/";
			FileVo fileVo = new FileVo();
			fileVo.setDirectory(dir);
			fileVo.setName(file.getOriginalFilename());
			fileVo.setSize(file.getSize());
			fileVo.setContenttype(file.getContentType());
			fileVo.setData(file.getBytes());
			boardDao.insertFile(fileVo);
			Integer num = fileVo.getNum();

			File uploadFile = new File(dir + num);
			file.transferTo(uploadFile); // 이 2줄이 파일업로드
			board.setFileurl(num + "");
		}
		boardDao.insertBoard(board);
		return boardDao.selectBoard(board.getNum());
	}

	@Override
	public void fileView(Integer num, OutputStream out) throws Exception {
		try {
			FileVo fileVo = boardDao.selectFile(num); // outputstream에다 복사하는거임
			// FileCopyUtils.copy(fileVo.getData(), out); // 테이블에서 읽어온 데이터를 내려보주는거

			FileInputStream fis = new FileInputStream(fileVo.getDirectory() + num); // upload 폴더에서 읽어오는거
			FileCopyUtils.copy(fis, out); // 디렉토리랑 넘버랑 활용하는거?
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Board boardDetail(Integer num) throws Exception {
		boardDao.updateBoardViewCount(num);
		return boardDao.selectBoard(num);
	}

	@Override
	public Board modifyBoard(Board board, MultipartFile file) throws Exception {
		if (file != null && !file.isEmpty()) { // file이 null 이 아니고 file이 empty가 아니면
			// 1. 파일정보 DB에 추가
			String dir = "c:/jisu/upload/"; // 파일 업로드 처리
			FileVo fileVo = new FileVo();
			fileVo.setDirectory(dir);
			fileVo.setName(file.getOriginalFilename());
			fileVo.setSize(file.getSize());
			fileVo.setContenttype(file.getContentType());
			fileVo.setData(file.getBytes());
			boardDao.insertFile(fileVo);

			// 2. upload 폴더에 파일 업로드
			// File uploadFile = new File(dir + num); 이건 왜 아닐까?
			File uploadFile = new File(dir + fileVo.getNum());
			file.transferTo(uploadFile); // 이 2줄이 파일업로드

			// 3. 기존 파일번호 삭제 위해 받아놓기
			Integer deleteFileNum = null;
			if (board.getFileurl() != null && !board.getFileurl().trim().equals("")) {
				deleteFileNum = Integer.parseInt(board.getFileurl());
			}
			// 4. 파일번호를 board fileUrl에 복사 & board update
			board.setFileurl(fileVo.getNum() + ""); // 백업받고
			boardDao.updateBoard(board); // update하고 나서 delete을 한다

			// 5. board fileUrl에 해당하는 파일 번호를 파일 테이블에서 삭제
			if (deleteFileNum != null) {
				boardDao.deleteFile(deleteFileNum); // 45 바뀌면 foreign key error남
			}
		} else {
			boardDao.updateBoard(board); // 파일이 없을때 업데이트
		}
		// 5. board테이블에 추가
		return boardDao.selectBoard(board.getNum());
	}

	@Override
	public void removeBoard(Integer num) throws Exception {
		Board board = boardDao.selectBoard(num);
		if (board != null) {
			if (board.getFileurl() != null) {
				boardDao.deleteFile(Integer.parseInt(board.getFileurl()));
			}
			boardDao.deleteBoard(num);
		}
	}

	@Override
	public Boolean isBoardLike(String userId, Integer boardNum) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("id", userId);
		param.put("num", boardNum);
		Integer likeNum = boardDao.selectBoardLike(param);
		return likeNum == null ? false : true;
	}

	@Override
	public Boolean selectBoardLike(String userId, Integer boardNum) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("id", userId);
		param.put("num", boardNum);
		Integer likeNum = boardDao.selectBoardLike(param);
		if (likeNum == null) {
			boardDao.insertBoardLike(param);
			boardDao.plusBoardLikeCount(boardNum);
			return true;
		} else {
			boardDao.deleteBoardLike(param);
			boardDao.minusBoardLikeCount(boardNum);
			return false;
		}
	}

}
