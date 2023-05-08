package com.kh.spring.common.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component // 런타임 시 ★필요한 위치에 코드를 끼워 넣을 수★ 있도록 bean으로 등록시켜주기
@Aspect // 공통 관심사가 작성된 클래스임을 명시
		// * 공통 관심사 : 특정 흐름 사이에 끼여서 수행할 코드
		// Aspect 어노테이션이 붙은 클래스에는 실행할 코드(advice)와 pointCut이 정의되어 있어야 한다.
		// advice(끼어들어서 실제로 수행할 메서드, 코드)
		// @pointCut(acvice가 끼어들어서 수행될 클래스, 메서드 위치 등을 정의)이 작성되어 있어야 한다.
public class Test {

	private Logger logger = LoggerFactory.getLogger(Test.class);
	
	// joinPoint : 클라이언트가 호출하는 모든 비즈니스 메서드. advice가 적용될 수 있는 예비 후보
	//			   ex) 클래스의 인스턴스 생성 시점, 메서드 호출 시점, 예외 발생 등 전부
	// PointCut : joinpoint들 중에서 "실제로" advice가 끼어들어서 실행될 지점을 선택
	
	/*
	 * PointCut 작성 방법
	 * 
	 * @PointCut("execution([접근 제한자] 반환형 패키지+클래스명.메서드명([매개 변수]))")
	 * 
	 * PointCut 표현식
	 * * : 모든 | 아무값
	 * .. : 하위 | 아래(하위 패키지) | 0개 이상의 매개 변수
	 * 
	 * @Before : PointCut에 지정된 메서드가 수행되기 "전"에 advice 수행을 명시하는 어노테이션
	 * 
	 *  com.kh.spring.board 패키지 아래에 있는 Impl로 끝나는 클래스의 모든 메서드에(매개 변수와 상관없이) 포인트컷 지정
	 */
	@Before("execution(* com.kh.spring.board..*Impl*.*(..))")
	public void start() { // 서비스 수행 전에 실행되는 메서드(advice)
		logger.info("===============서비스 시작됨===============");
	}
	
	//After : PointCut에 지정된 메서드가 수행된 후, advice 수행을 하라고 지시하는 어노테이션
	
	@After("execution(* com.kh.spring.board..*Impl*.*(..))")
	public void end() {
		logger.info("===============서비스 끝===============");
	}
}
