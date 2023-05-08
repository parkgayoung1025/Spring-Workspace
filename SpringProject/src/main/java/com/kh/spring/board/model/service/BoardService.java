package com.kh.spring.board.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.BoardImg;
import com.kh.spring.board.model.vo.BoardType;

public interface BoardService {

	public ArrayList<BoardType> selectBoardTypeList();
	
	public void selectBoardList(int currentPage, String boardCode, Map<String, Object> map);
	
	public void selectBoardList(Map<String, Object> paramMap, Map<String, Object> map);
	
	public Board selectBoardDetail(int boardNo);
	
	public int updateReadCount(int boardNo);
	
	public int insertBoard(Board b, List<MultipartFile> list, String webPath, String serverFolderPath) throws Exception;
	
	public int updateBoard(Board b, List<MultipartFile> list, String webPath, String serverFolderPath, String deleteList) throws Exception;

	public List<String> selectFileList();
}
