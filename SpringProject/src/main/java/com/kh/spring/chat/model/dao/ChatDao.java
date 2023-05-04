package com.kh.spring.chat.model.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kh.spring.chat.model.vo.ChatMessage;
import com.kh.spring.chat.model.vo.ChatRoom;
import com.kh.spring.chat.model.vo.ChatRoomJoin;

@Repository
public class ChatDao {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	public List<ChatRoom> selectChatRoomList() {
		return sqlSession.selectList("chattingMapper.selectChatRoomList");
	}
	
	public int openChatRoom(ChatRoom chatRoom) {
		int result = sqlSession.insert("chattingMapper.openChatRoom", chatRoom);
		
		if (result > 0) {
			return chatRoom.getChatRoomNo();
		} else {
			return result;
		}
	}
	
	// 채팅방 참여여부 확인
	public int joinCheck(ChatRoomJoin join) {
		return sqlSession.selectOne("chattingMapper.joinCheck", join);
	}
	
	// 채팅방 참여
	public void joinChatRoom(ChatRoomJoin join) {
		sqlSession.insert("chattingMapper.joinChatRoom", join);
	}
	
	// 채팅방 메시지 목록 조회
	public List<ChatMessage> selectChatMessage(int chatRoomNo) {
		return sqlSession.selectList("chattingMapper.selectChatMessage", chatRoomNo);
	}
	
	// 채팅 메시지 삽입
	public int insertMessage(ChatMessage chatMessage) {
		return sqlSession.insert("chattingMapper.insertMessage", chatMessage);
	}
	
	// 채팅방 나가기
	public int exitChatRoom(ChatRoomJoin join) {
		return sqlSession.delete("chattingMapper.exitChatRoom", join);
	}
	
	// 채팅방 인원수 확인
	public int countChatRoomMember(int chatRoomNo) {
		return sqlSession.selectOne("chattingMapper.countChatRoomMember", chatRoomNo);
	}
	
	// 채팅방 닫기
	public int closeChatRoom(int chatRoomNo) {
		return sqlSession.update("chattingMapper.closeChatRoom", chatRoomNo);
	}
	
	
}
