package com.kh.spring.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.spring.board.model.dao.BoardDao;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.BoardImg;
import com.kh.spring.board.model.vo.BoardType;
import com.kh.spring.common.Utils;
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
		//System.out.println(pi);
		//System.out.println(list);
		
		//return list;
	}
	
	// 검색 게시글 목록 조회 서비스 구현
	public void selectBoardList(Map<String, Object> paramMap, Map<String, Object> map) {
		int listCount = boardDao.selectBoardListCount(paramMap);
		int pageLimit = 10;
		int boardLimit = 5;
		PageInfo pi = paginateion.getPageInfo(listCount, (int)paramMap.get("currentPage"), pageLimit, boardLimit);
		
		ArrayList<Board> list = boardDao.selectBoardList(pi, paramMap);
		
		map.put("pi", pi);
		map.put("list", list);
	}
	
	// 게시글 상세 조회 구현 서비스
	public Board selectBoardDetail(int boardNo) {
		return boardDao.selectBoardDetail(boardNo);
	}
	
	// 조회수 증가 서비스
	public int updateReadCount(int boardNo) {
		return boardDao.updateReadCount(boardNo);
	}
	
	/*
	 * 게시글 삽입 + 이미지 삽입
	 * 
	 * Spring에서 트랜잭션을 처리하는 방법
	 * 
	 * 예외가 발생하면 자동으로 rollback을 수행함
	 * service, service 내부의 코드 수행 시점에서 에러 발생 시 자동 rollback이 된다.
	 * 
	 * 방법 1) aop를 활용하는 방법(현재는 안됨)
	 * 
	 * 방법 2) @Transactional 어노테이션 활용하여 선언적 트랜잭션처리
	 * 		  -> RuntimeException에 대한 처리를 기본값으로 가진다.
	 * 
	 * rollbackFor : rollback을 수행하기 위한 예외의 종류를 작성하는 부분
	 */
	@Transactional(rollbackFor = {Exception.class}) // 모든 종류의 예외가 발생하면 rollback 시키겠다고 선언
	public int insertBoard(Board b, List<MultipartFile> list, String webPath, String serverFolderPath) throws Exception {
		
		// 1) 게시글 등록
		//    게시글 등록 후 해당 게시글의 pk값을 반환받을 예정 ==> boardNo
		int boardNo = boardDao.insertBoard(b);
		
		if (boardNo > 0 && list != null) {
			// 2) 이미지 사입 코드 추가
			// list -> 실제 파일이 담겨있는 리스트
			List<BoardImg> boardImageList = new ArrayList();
			// boardImgeList : db에 등록한 데이터를 모아놓은 컬렉션
			
			List<String> renameList =  new ArrayList();
			// renameList : 변경된 파일명을 저장할 리스트
			
			// list에서 담겨있는 파일 정보 중 실제로 업로드 된 파일만 분류하기
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getSize() > 0) { // i번째 요소에 업로드 된 이미지가 존재하는지 체크
					// 변경된 파일명 renameList에 저장 할 예정
					String changeName = Utils.saveFile(list.get(i), serverFolderPath);
					renameList.add(changeName);
					
					// BoardImg 객체를 생성해서 필드에 값을 세팅한 후 boardImgeList에 추가할 예정
					BoardImg img = new BoardImg();
					
					img.setRefBno(boardNo); // 등록한 게시글 번호
					img.setImgLevel(i); // 이미지 순서
					img.setOriginName(list.get(i).getOriginalFilename()); // 원본 파일명
					img.setChangeName(changeName);
					
					boardImageList.add(img);
				}
			}
			// 분류 작업 완료 후 boardImgaeList가 비어있는가 ? -> 등록한 이미지가 없음
			//                                비어있지 않다 ? -> 등록한 이미지가 있음
			if (!boardImageList.isEmpty()) {
				int result = boardDao.insertBoardImgList(boardImageList);
				if (result == boardImageList.size()) {
					// 삽입된 행의 개수와 업로드 된 이미지 수가 같은경우
					
					// 서버에 이미지 저장(Utils.savePath에서 선 작업함)
				} else { // 이미지 삽입 실패 시, 강제로 예외 발생시켜서 rollback 시키기
					throw new Exception("이미지 등록 시 예외 발생함");
				}
			}
		}
		return boardNo;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public int updateBoard(Board b, List<MultipartFile> list, String webPath, String serverFolderPath, String deleteList) throws Exception {
		// 1) 게시글 업데이트
		int result = boardDao.updateBoard(b); // 게시글 수정
		
		if (result > 0) {
			// 2) 업로드 된 이미지만 분류하는 작업 수행
			List<BoardImg> boardImgList = new ArrayList();
			List<String> reNameList = new ArrayList();
			
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getSize() > 0) {
						// 변경된 파일명 renameList에 저장 할 예정
						String changeName = Utils.saveFile(list.get(i), serverFolderPath);
						reNameList.add(changeName);
						
						// BoardImg 객체를 생성해서 필드에 값을 세팅한 후 boardImgeList에 추가할 예정
						BoardImg img = new BoardImg();
						
						img.setRefBno(b.getBoardNo()); // 등록한 게시글 번호
						img.setImgLevel(i); // 이미지 순서
						img.setOriginName(list.get(i).getOriginalFilename()); // 원본 파일명
						img.setChangeName(changeName);
						
						boardImgList.add(img);
					}
				}
				
				// 4) x버튼 눌렀을 때 해당 이미지를 db에서 삭제
				if (deleteList != null && !deleteList.equals("")) {
					Map<String, Object> map = new HashMap();
					map.put("boardNo", b.getBoardNo());
					map.put("deleteList", deleteList);
					
					result = boardDao.deleteBoardImage(map); // 게시글 수정에서 이미지 삭제
				}
				
				// 5) db에서 삭제에 성공했다면
				if (result > 0) {
					// boardImg 객체 하나하나 업데이트
					for (BoardImg img : boardImgList) {
						result = boardDao.updateBoardImg(img); // 게시글에서 이미지 추가하거나 수정
						// 결괏값 1 -> 수정 작업이 잘 이루어졌다 => 기존에 이미지가 있었다.
						// 결괏값 0 -> 수정 작업이 실패함 => 애초에 이미지가 없었다.
						
						// 6) 결괏값이 0 => update에는 실패했고 실제로 db에는 올라가야 할 데이터이기 때문에 insert 시켜준다.
						if (result == 0) {
							result = boardDao.insertBoardImg(img);
							// -> 값을 하나씩 대입해서 삽입하는 경우 결과가 0이 나올 수 없다.(단, 예외는 발생할 수 있음)
						}
					}
				}
				
				// 7) 업로드된 이미지가 있다면 서버에 저장하기(Utils로 대체함)
			}
		}
		return result;
	}
	
	public List<String> selectFileList() {
		return boardDao.selectFileList();
	}
	
	
}
