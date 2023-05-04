package com.kh.spring.chat.model.service;

import java.util.List;

import com.kh.spring.chat.model.vo.ChatMessage;
import com.kh.spring.chat.model.vo.ChatRoom;
import com.kh.spring.chat.model.vo.ChatRoomJoin;

public interface ChatService {

	List<ChatRoom> selectChatRoomList();
	
	int openChatRoom(ChatRoom cr);
	
	List<ChatMessage> joinChatRoom(ChatRoomJoin crj);
	
	int insertMessage(ChatMessage cm);
	
	int exitChatRoom(ChatRoomJoin crj);
}
