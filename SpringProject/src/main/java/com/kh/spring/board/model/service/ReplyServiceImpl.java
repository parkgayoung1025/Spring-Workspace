package com.kh.spring.board.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.board.model.dao.ReplyDao;
import com.kh.spring.board.model.vo.Reply;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyDao dao;
	
	public int insertReply(Reply reply) {
		return dao.insertReply(reply);
	}
	
	public List<Reply> selectReplyList(int bno) {
		return dao.selectReplyList(bno);
	}
	
	public int deleteReply(int replyNo) {
		return dao.deleteReply(replyNo);
	}
	
	public int updateReply(Reply reply) {
		return dao.updateReply(reply);
	}
}

