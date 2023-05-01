package com.kh.spring.board.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.BoardType;
import com.kh.spring.common.model.vo.PageInfo;

@Repository
public class BoardDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public ArrayList<BoardType> selectBoardTypeList() {
		return (ArrayList)sqlSession.selectList("boardMapper.selectBoardTypeList");
	}
	
	public int selectBoardListCount(String boardCode) {
		return sqlSession.selectOne("boardMapper.selectBoardListCount", boardCode);
	}
	
	public ArrayList<Board> selectBoardList(PageInfo pi, String boardCode) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		int limit = pi.getBoardLimit();
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return (ArrayList)sqlSession.selectList("boardMapper.selectBoardList", boardCode, rowBounds);
	}
	
	public Board selectBoardDetail(int boardNo) {
		return sqlSession.selectOne("boardMapper.selectBoardDetail", boardNo);
	}
	
	public int updateReadCount(int boardNo) {
		return sqlSession.update("boardMapper.updateReadCount", boardNo);
	}
}
