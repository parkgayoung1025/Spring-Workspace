package com.kh.spring.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.BoardType;
import com.kh.spring.common.model.vo.PageInfo;
import com.kh.spring.common.template.Pagination;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardDao boardDao;
	
	@Autowired
	private Pagination paginateion;

	public ArrayList<BoardType> selectBoardTypeList(){
		return boardDao.selectBoardTypeList();
	}
	
	public void selectBoardList(int cp, String boardCode, Map<String, Object> map) {
		// 2) 페이지네이션 객체 생성
		// 3) 게시글 목록 조회
		int listCount = boardDao.selectBoardListCount(boardCode);
		int pageLimit = 10;
		int boardLimit = 5;
		PageInfo pi = paginateion.getPageInfo(listCount, cp, pageLimit, boardLimit);
		
		ArrayList<Board> list = boardDao.selectBoardList(pi, boardCode);
		map.put("pi", pi);
		map.put("list", list);
		
		//return list;
	}
	
	// 게시글 상세 조회 구현 서비스
	public Board selectBoardDetail(int boardNo) {
		return boardDao.selectBoardDetail(boardNo);
	}
	
	// 조회수 증가 서비스
	public int updateReadCount(int boardNo) {
		return boardDao.updateReadCount(boardNo);
	}
}
