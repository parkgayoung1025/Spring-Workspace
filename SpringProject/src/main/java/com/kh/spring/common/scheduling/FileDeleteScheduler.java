package com.kh.spring.common.scheduling;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.BoardImg;

@Component
public class FileDeleteScheduler {

	private Logger logger = LoggerFactory.getLogger(FileDeleteScheduler.class);
	
	@Autowired
	private ServletContext application;
	
	@Autowired
	private BoardService service;
	
	// 1. BOARD_IMG 테이블 안에 있는 이미지 목록들을 모두 조회
	// 2. images/boardT 디텍토리 안에 있는 이미지들과 대조하여
	// 3. 일치하지 않는 이미지 파일들을 삭제(db에는 없는 데이터인데 boardT 안에는 존재하는 경우)
	// 4. 우선 5초 간격으로 테스트 후, 정상적으로 작동한다면 매달 1일 정시에 실행되는 스케줄러로 만들기
	
	@Scheduled(cron = "0 0 0 1 * *")
	public void deleteFile() {
		logger.info("파일 삭제 시작");
		
		// 1) board_img 테이블 안에 있는 모든 파일 목록들 조회
		List<String> list = service.selectFileList(); // <string>인 이유는 boardImg 테이블 안에서 changeName만 필요하기 때문, <boardImg>로 해도 상관은 없음
		
		// 2) images/boardT 폴더 아래에 존재하는 모든 이미지 파일 목록 조회(File 클래스 활용)
		File path = new File(application.getRealPath("/resources/images/boardT"));
		
		// path가 참조하고 있는 폴더에 들어가서 모든 파일을 File 배열로 얻어오기
		File[] files = path.listFiles();
		
		// 배열을 컬렉션(리스트)으로 바꾸기
		List<File> fileList = Arrays.asList(files);
		
		logger.info(list.toString());
		logger.info(fileList.toString());
		
		// 3) 두 목록을 비교해서 일치하지 않는 파일 삭제(삭제 시 File 클래스의 delete() 활용)
		if (!list.isEmpty()) {
			// server : C:\Spring-Workspace\SpringProject\src\main\webapp\resources\images\boardT\2023050212385418102.jpg
			// list : 2023050212152147331.jpg
			for (File serverFile : fileList) {
				String fileName = serverFile.getName(); // 파일명만 얻어오는 메서드
				
				// 방법 1
//				boolean isTrue = false;
//				for (String str : list) {
//					if (str.equals(fileName)) {
//						isTrue = true;
//					}
//				}
//				if (!isTrue) {
//					serverFile.delete();
//				}
				
				// 방법 2 indexOf or contains 이용
				// List.indexOf(value) : List에 value값과 같은 값이 있으면 인덱스를 반환 / 없으면 -1을 반환
				if (list.indexOf(fileName) == -1) {
					// select 해 온 db 목록에는 없는데 실제 웹서버에는 저장된 파일인 경우
					serverFile.delete();
					logger.info(fileName+"파일 삭제함");
				}
			}
		}
		logger.info("서버 파일 삭제 작업 끝");
	}
}
