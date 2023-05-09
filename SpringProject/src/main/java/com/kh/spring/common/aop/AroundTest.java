package com.kh.spring.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(4)
public class AroundTest {

	private Logger logger = LoggerFactory.getLogger(AroundTest.class);
	
	// @Around : Before + After가 합쳐진 어노테이션
	@Around("CommonPointcut.implPointcut()")
	public Object checkRuuningTime(ProceedingJoinPoint jp) throws Throwable {
		
		// ProceedingJoinPoint 인터페이스 : 전/후 처리 관련된 기능을 제공. TrgetObject의 값을 얻어올 수 있는 메서드도 함께 제공
		
		// proceed() 메서드 호출 전 : @Before 용도로 사용할 advice 작성
		// proceed() 메서드 효출 후 : @After 용도로 사용할 advice 작성
		// 메서드의 마지막에 proceed()의 반환값을 리턴해 줘야 함
		
		// before 시작
		long start = System.currentTimeMillis(); // 시스템 서버 시간을 ms 단위로 나타낸 값
		
		// before 끝
		
		Object obj = jp.proceed(); // 중간 지점
		System.out.println("obj : " +obj);
		// after 시작
		long end = System.currentTimeMillis();
		
		logger.info("Running Time : {} ms", (end-start));
		
		// after 끝
		
		return obj;
	}
}
