package com.kh.spring.board.model.service;

import java.util.List;

import com.kh.spring.board.model.vo.Reply;

public interface ReplyService {

	// 댓글 등록
	int insertReply(Reply reply); // 인터페이스는 추상 메서드라 퍼블릭이나 그런 거 저절로 붙어서 작성 안 해도 됨
	
	// 댓글 목록 조회
	List<Reply> selectReplyList(int bno);
	
	// 댓글 수정
	int updateReply(Reply reply);
	
	// 댓글 삭제
	int deleteReply(int replyNo);
	
}
