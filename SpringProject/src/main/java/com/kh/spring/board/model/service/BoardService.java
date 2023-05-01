package com.kh.spring.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.BoardType;

public interface BoardService {

	public ArrayList<BoardType> selectBoardTypeList();
	
	public void selectBoardList(int currentPage, String boardCode, Map<String, Object> map);
	
	public Board selectBoardDetail(int boardNo);
	
	public int updateReadCount(int boardNo);
}
