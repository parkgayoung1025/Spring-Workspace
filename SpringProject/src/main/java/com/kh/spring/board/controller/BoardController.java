package com.kh.spring.board.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.member.model.vo.Member;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;
	
	/*
	 * 게시글 목록 조회 서비스
	 * 
	 * @PathVariable : URL 경로에 포함되어 있는 값을 변수로 사용할 수 있게 하는 역할
	 * 				   => ★자동으로 request scope에 등록 된다. ==> jsp에서 ${value} el로 작성 가능하다.
	 * 
	 * PathVariable : 요청 자원을 식별하는 경우에 사용
	 * 
	 * QueryString : 정렬, 검색 등 필터링 옵션이 있을 경우 사용
	 */
	@GetMapping("/list/{boardCode}")
	public String boardList(@PathVariable("boardCode") String boardCode,
							@RequestParam(value = "cpage", defaultValue = "1") int currentPage,
							Model model,
							@RequestParam Map<String, Object> paramMap) {
		// 검색 요청이 있었다면, paramMap 안에는 keyword, condition이 들어가 있을 것
		//System.out.println(paramMap);
		Map<String, Object> map = new HashMap();
		
		// 게시글 목록 조회 서비스 호출 시 작업 내용
		// 1) 게시판 이름 조회
		
		// 검색 요청을 하지 않은 경우
		if (paramMap.get("condition") == null) {
			boardService.selectBoardList(currentPage, boardCode, map);
		} else {
			// 검색 요청을 한 경우
			// 검색 조건을 추가한 상태로 게시글 셀렉트
			paramMap.put("currentPage", currentPage);
			paramMap.put("boardCode", boardCode);
			boardService.selectBoardList(paramMap, map);
		} // map은 list, pi 담겨있고 paramMap은 검색 요청 기록이 담겨있음
		
		model.addAttribute("map", map);
		
		return "board/boardListView";
	}
	
	// 게시글 상세 조회
	@GetMapping("/detail/{boardCode}/{boardNo}")
	public String boardDetail(@PathVariable("boardCode") String boardCode,
							  @PathVariable("boardNo") int boardNo,
							  @RequestParam(value="cpage", required = false, defaultValue = "1") int cp,
							  Model model,
							  HttpSession session,
							  HttpServletRequest req,
							  HttpServletResponse resp) {
		// 게시글 상세 조회 서비스 호출
		Board detail = boardService.selectBoardDetail(boardNo);
		
		// 조회수 중복 증가 방지 코드(본인 글은 조회수 증가 안되게) -> 쿠키를 활용할 예정
		if (detail != null) { // 상세 조회 성공
			Member loginUser = (Member)session.getAttribute("loginUser");
			
			int memberNo = 0;
			
			if (loginUser != null) { // 로그인 한 상태
				memberNo = loginUser.getUserNo();
			}
			
			// 글쓴이와 현재 상세 보기 요청을 한 클라이언트가 같지 않을 경우에만 조회수 증가 서비스 호출
			if (Integer.parseInt(detail.getBoardWriter()) != memberNo) {
				Cookie cookie = null;
				
				Cookie[] cArr = req.getCookies(); // 쿠키 얻어보기
				
				if (cArr != null && cArr.length > 0) { // 얻어온 쿠키가 있을 경우
					
					for (Cookie c : cArr) {
						if (c.getName().equals("readBoardNo")) {
							cookie = c;
						}
					}
				}
				int result = 0;
				if (cookie == null) { // 기존에 readBoardNo라는 이름의 쿠키가 없던 경우
					cookie = new Cookie("readBoardNo", boardNo + "");
					result = boardService.updateReadCount(boardNo); // 조회수 증가 서비스 호출
				} else {
					// readBoardNo가 이미 존재할 경우 -> 쿠키에 저장된 값 뒤쪽에 현재 조회된 게시글 번호를 추가
					// 							     단, 기존 쿠키 값 중에 중복되는 번호가 없어야 한다.
					String temp [] = cookie.getValue().split("/"); // 기존 value
					
					// "readBoardNo" / "1/2/3/4/5/6/7/8/9"
					List<String> list = Arrays.asList(temp); // 배열 --> List로 변환시켜주는 함수
					
					// List.indexOf(Object) : List에서 Object와 일치하는 부분의 인덱스를 반환
					//						  일치하는 부분이 없으면 -1을 반환
					if (list.indexOf(boardNo+"") == -1) { // 즉, 기본 값에 같은 글번호가 없다면
						cookie.setValue(cookie.getValue() + "/" + boardNo);
						result = boardService.updateReadCount(boardNo); // 조회수 증가 서비스 호출
					}
				}
				if (result > 0) {
					cookie.setPath(req.getContextPath());
					cookie.setMaxAge(60*60*1); // 1시간
					resp.addCookie(cookie);
				}
			}
		}
		model.addAttribute("b", detail);
		return "board/boardDetailView";
	}
	
	@GetMapping("/enrollForm/{boardCode}")
	public String boardEnrollForm(@PathVariable("boardCode") String boardCode,
								  Model model,
								  @RequestParam(value = "mode", defaultValue = "insert", required = false) String mode,
								  @RequestParam(value = "bno", defaultValue = "0", required = false) int bno) {
		if (mode.equals("update")) {
			// 수정하기 페이지 요청
			// 선택한 게시판 정보 조회 후 이동
			Board b = boardService.selectBoardDetail(bno);
			
			model.addAttribute("b", b);
		}
		
		if (boardCode.equals("N")) {
			return "board/boardEnrollForm";
		} else {
			return "board/boardPictureEnrollForm";
		}
	}
	
	@PostMapping("/insert/{boardCode}")
	public String insertBoard(Board b,
							  @RequestParam(value = "mode", required = false, defaultValue = "insert") String mode,
							  @RequestParam(value = "images", required = false) List<MultipartFile> imgList, // 업로드용 이미지 파일
							  @RequestParam(value = "upfile", required = false) MultipartFile upfile, // 첨부파일
							  @PathVariable("boardCode") String boardCode,
							  HttpSession session,
							  Model model,
							  @RequestParam(value = "deleteList", required = false) String deleteList) {
		// 사진 게시판들 이미지를 저장할 저장 경로 얻어오기
		String webPath = "/resources/images/boardT/";
		String serverFolderPath = session.getServletContext().getRealPath(webPath);
		b.setBoardCd(boardCode);
		
		int result = 0;
		
		if (mode.equals("insert")) {
			// db에 Board 테이블에 데이터 추가
			try {
				result = boardService.insertBoard(b, imgList, webPath, serverFolderPath);
			} catch(Exception e) {
				System.out.println("에러 발생");
			}
		} else {
			// 게시글 수정 서비스 호출
			// b객체 안에 boardNo가 들어간 상태일 것
			try {
				result = boardService.updateBoard(b, imgList, webPath, serverFolderPath, deleteList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// 첨부파일 업로드 -> Board 테이블 안에 ORIGIN_NAME, CHANGE_NAME
		
		if (result > 0) { // 성공적으로 추가 시
			session.setAttribute("alertMsg", "게시글 작성에 성공하셨습니다.");
			return "redirect:../list/"+boardCode; // spring/board/list/+boardCode
		} else { // errorPage 포워딩
			model.addAttribute("errorMsg", "게시글 작성 실패");
			return "common/errorPage";
		}
	}
}
