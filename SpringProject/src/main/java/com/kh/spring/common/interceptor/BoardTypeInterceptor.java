package com.kh.spring.common.interceptor;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.BoardType;

public class BoardTypeInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private BoardService boardService;
	
	@Override // 전 처리할 메서드 작성
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		
		// application scope에 boardTypeList가 있는지 체크, 없을 경우 이를 조회하는 boardService 메서드 호출 후 결과를 세팅
		
		// application scope 객체 얻어 오기
		ServletContext application = request.getServletContext();
		
		if (application.getAttribute("boardTypeList") == null) {
			ArrayList<BoardType> list = boardService.selectBoardTypeList();
			
			application.setAttribute("boardTypeList", list);
		}
		
		return true;
	}
	
	@Override // 후 처리할 메서드 작성
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		System.out.println("후처리 실행");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		System.out.println("view 처리 완료 후 실행");
	}
}
