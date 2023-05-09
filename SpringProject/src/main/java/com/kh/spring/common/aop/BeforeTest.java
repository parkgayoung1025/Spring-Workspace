package com.kh.spring.common.aop;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kh.spring.member.model.vo.Member;

@Component
@Aspect
public class BeforeTest {

	private Logger logger = LoggerFactory.getLogger(BeforeTest.class);
	
	//사용자가 사용하고 있는 핸드폰 종류
	static String logMP[] = {"iphone", "ipad", "android", "blacberry", "opera mobi"};
	
	// joinpoint : advice가 적용될 수 있는 예비 후보
	
	// JoinPoint 인터페치스 : advice가 실제로 적용되는 클래스(Target Object(*Impl*))의 정보, 전달되는 매개 변수
	//					   메서드, 반환값, 예외 등의 정보를 얻을 수 있는 메서드를 제공
	
	@Before("CommonPointcut.implPointcut()")
	public void logService(JoinPoint jp) { // JoinPoint 인터페이스는 항상 첫 번째 매개 변수로 작성되어야 한다.
		
		StringBuilder sb = new StringBuilder();
		sb.append("====================================================================\n");
		
		// start : 실행된 클래스명 - 실행된 메서드(매개 변수)
		// JoinPoint : getTarget() -> AOP가 적용된 객체를 반환(XXXserviceImpl)
		sb.append("start : " + jp.getTarget().getClass().getSimpleName() + " - "); // 패키지명을 제외한 클래스명
		
		// JoinPoint : getSingnature() : 수행되는 메서드 정보를 반환
		sb.append(jp.getSignature().getName());
		
		// JoinPoint : getArgs() : 메서드 호출 시 전달된 매개 변수
		sb.append("("+Arrays.toString(jp.getArgs())+")\n");
		
		// ip : web, mobile https://xxx...
		// HttpServletRequest 객체를 얻어오기
		// AOP 실행 시점에 예외 발생 시 -> 예외 처리해 줘야 함
		
		try {
			HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			
			String currentDevice = "web";
			String logUA = req.getHeader("user-agent").toLowerCase();
			for (String device : logMP) {
				if (logUA.indexOf(device) > -1) {
					currentDevice = "mobile";
					break;
				}
			}
			
			// http+domain+port+uri
			sb.append("ip : " + currentDevice + getIp(req) + " " + (req.isSecure() ? "https" : "http") + "://" +
					  req.getServerName() + req.getServerPort() + req.getRequestURI());
			
			// 접속자 아이디
			// userId : 유저 아이디
			Member loginUser = (Member)req.getSession().getAttribute("loginUser");
			if (loginUser != null) {
				sb.append("\nuserId : " + loginUser.getUserId());
			}
		} catch(Exception e) {
			sb.append(e.getMessage());
			sb.append("예외 발생");
		}
		
		logger.info(sb.toString());
	}
	
	public String getIp(HttpServletRequest request){
	       String ip = request.getHeader("X-Forwarded-For");
	   
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	         ip = request.getHeader("Proxy-Client-IP");
	      }
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	         ip = request.getHeader("WL-Proxy-Client-IP");
	      }
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	         ip = request.getHeader("HTTP_CLIENT_IP");
	      }
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	         ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	      }
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	         ip = request.getRemoteAddr();
	      }
	      return ip;
	   }
}
