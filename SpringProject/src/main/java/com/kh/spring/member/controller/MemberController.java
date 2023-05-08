package com.kh.spring.member.controller;

import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.service.MemberServiceImpl;
import com.kh.spring.member.model.vo.Member;

@Controller // 생성된 bean객체가 Controller임을 명시 + bean 등록
@RequestMapping("/member") // localhost:8081/spring/member 이하의 url 요청을 처리하는 컨트롤러
@SessionAttributes({"loginUser"}) // 로그인, 회원가입 기능 완료 후 실행될 코드
public class MemberController extends QuartzJobBean {
	
	//private MemberService ms = new MemberServiceImpl();
	// 기존 객체 생성 방식. 서비스가 동시에 많은 횟수의 요청이 들어오면 그만큼의 객체가 생성됨
	// 객체 간의 결합도가 올라감
	
	// Spring의 DI(Dependency Injection) => 객체를 스프링에서 생성해서 주입을 해 주는 개념
	// new 연산자를 쓰지 않고 필드 선언만 한 후 @Autowried라는 어노테이션을 붙여서 내가 필요로 하는 객체를 스프링 컨테이너로부터 주입받을 수 있음
	
	/*
	 * Autowired를 통한 객체 주입 방법
	 * 1. 필드 방식 의존성 주입
	 * 2. 생성자 방식 의존성 주입
	 * 3. setter 방식 의존성 주입
	 */
	
	/*
	 * 1) 필드 방식 의존성
	 *    필드 방식 의존성 주입 장점 : 이해하기 편하다, 사용하기 편하다.
	 *    필드 방식 의존성 주입 단점 : 1) 순환 의존성 문제가 발생할 수 있다.
	 *                           2) 무분별한 주입 시 의존관계 확인이 어렵다.
	 */
	
	// 필드 방식 의존성 주입
	//@Autowired // bean으로 등록된 객체 중 타입이 같거나, 상속 관계인 bean을 자동으로 주입 해 주는 역할
	private MemberService memberService;
	
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	/*
	 * 2) 생성자 방식 의존성 주입(권장하는 방법)
	 *    생성자를 통한 의존성 주입 : 생성자에 매개 변수로 참조할 클래스를 인자로 받아 필드에 매핑시킴
	 *    
	 *    장점 : 1. 현재 클래스에서 내가 주입시킬 객체들을 모아서 관리할 수 있기 때문에 한 눈에 알아보기 편함
	 *          2. 코드 분석과 테스트에 유리하며, 객체 주입 시 가장 권장하는 방법
	 */
	
	// 생성자 방식 의존성 주입
	@Autowired // 생성자가 하나뿐이라면 생략 가능(필드 방식 의존성 주입 포함). 여러 개라면 반드시 Autowired 어노테이션을 추가해야 함
	public MemberController(MemberService memberService, BCryptPasswordEncoder bcryptPasswordEncoder) {
		this.memberService = memberService;
		this.bcryptPasswordEncoder = bcryptPasswordEncoder;
	}
	
	public MemberController() {
		
	}
	
	/*
	 *  3) setter 방식 의존성 주입
	 *  setter 주입 방식 : setter 메서드로 bean을 주입 받는 방식
	 *  
	 *  생성자에 너무 많은 의존성을 주입하게 되면 알아보기 힘들다는 단점이 있어서 보완하기 위해 사용
	 *  
	 *  필요할 때 마다 의존성을 주입 받아서 사용할 때, 즉, 의존성 주입이 필수가 아닌 선택 사항일 때 사용
	 */
	
	@Autowired
	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}
	
	/*
	 * 스프링에는 Argument Resolver라는 매개 변수를 유연하게 처리해 주는 해결사 클래스가 내장되어 있음
	 * Argument Resolver : 클라이언트가 전달한 파라미터 중 매개 변수의 조건과 일치하는 파라미터가 있다면 해당 객체를 매개 변수로 바인딩해준다.
	 * 
	 * 스프링에서 parameter(요청 시 전달값)을 받는 방법
	 * 
	 * 1. HttpServletRequest를 이용해서 전달 받기(jsp 프로젝트에서 하던 방식 그대로임)
	 *    해당 메소드의 매개 변수로 HttpServeltRequest를 작성해 놓으면 스프링 컨테이너(정확히는 Argument Resolver)가 해당 메소드를 호출할 때 자동으로 request 객체를 생성해서 매개 변수로 주입해 준다.
	 */
//	@RequestMapping("/login")
//	public String loginMember(HttpServletRequest request) {
//		String userId = request.getParameter("userId");
//		String userPwd = request.getParameter("userPwd");
//		
//		System.out.println(userId + userPwd);
//		
//		return "main";
//	}
	
	/*
	 * 2. @RequestParam 어노테이션을 이용하는 방법. request.getParameter("키")로 뽑는 역할을 대신 수행해 주는 어노테이션
	 *    jsp에서 작성했던 input 태그의 name 속성값을 value로 입력해 주면 알아서 매개 변수로 담아온다
	 *    
	 *    만약 넘어온 값이 비어있다면 defaultValue로 기본값 설정 가능
	 *    
	 *    사용 가능 속성  
	 *    - value : input 태그의 name 속성값으로 다른 속성을 작성하지 않은 경우 기본값으로 활용된다.
	 *              @RequestParam("url"), @RequestParam(value="url")
	 *            
	 *    - required : 입력된 name 속성값이 필수적으로 파라미터에 포함되어야 하는 지를 지정(required=true(기본값))
	 *    			   required = true일 때 파라미터가 없으면 400 에러를 반환(잘못된 요청(bad-request))
	 *    			   required = false일 때 파라미터가 없으면 그냥 null값이 들어감
	 *    
	 *    - defaultValue : required가 false인 상태에서 파라미터가 존재하지 않을 경우의 값을 지정
	 */
//	@RequestMapping("/login")
//	public String loginMember(@RequestParam(value="userId", defaultValue="!!!") String userId,
//							  @RequestParam(value="userPwd") String userPwd) {
//		
//		System.out.println(userId + userPwd);
//		
//		return "main";
//	}
	
	/*
	 * 3. @RequestParam 어노테이션을 생략하는 방법
	 *    단, 매개 변수명을 jsp의 name 속성값(요청 시 전달한 키 값)과 동일하게 작성해 줘야 한다.
	 *    또한, 위에서 작성했던 나머지 속성들 사용 불가
	 */
//	@RequestMapping("/login")
//	public String loginMember(String userId, String userPwd) {
//		
//		System.out.println(userId + userPwd);
//		
//		return "main";
//	}
	
	/*
	 * 4. @ModelAttribute 어노테이션을 사용하는 방법
	 * 
	 * [작성법]
	 * @ModelAttribute VO타입 변수명
	 * 매개 변수로 @ModelAttribute 사용 시 파라미터 중 name 속성값이 vo 클래스의 필드와 일치하면 해당 vo 클래스의 필드에 값을 셋팅함
	 * 
	 * [작동 원리]
	 * 스프링 컨테이너가 해당 객체를 (기본 생성자)로 생성 후 내부적으로 (setter 메서드)를 찾아서 요청 시 전달한 값 중 name 값과 일치하는 필드에 name 속성값을 담아줌
	 * 따라서 @ModelAttribute를 사용하기 위해선 기본 생성자와 + setter 메서드는 반드시 필요함
	 * 
	 * @ModelAttribute 생략 시 해당 객체를 커맨드 객체라고 부른다.
	 */
//	@RequestMapping("/login")
//	public String loginMember(/* @ModelAttribute */ Member m, Model model) {
//		
//		// 요청 처리 후 응답 데이터를 담고 응답 페이지로 포워딩 또는 url 재요청하는 방법
//		// 1) Model 객체 이용
//		// 포워딩 할 응답뷰로 전달하고자 하는 데이터를 맵 형식으로 담을 수 있는 영역 -> Model객체(requestScope)
//		// Model : 데이터를 맵 형식(key:value) 형태로 담아 전달하는 용도의 객체
//		// request, session을 대체하는 객체
//		
//		// model 클래스 안의 addAttribute()메서드를 이용하는 방법
//		model.addAttribute("errorMsg", "오류발생"); // == request.setAttribute("errorMsg", "오류발생");
//		
//		// Model의 기본 scope는 request scope임. 단, session scope로 변환하고 싶은 경우
//		// 클래스 레벨로 @SessionAttributes를 작성하면 된다. // 25번째 줄에 작성되어있음
//		model.addAttribute("loginMember", m); // == request.getSession().setAttribute ... // 25번째 줄에 작성되어있음
//		
//		System.out.println(m);
//		
//		return "main";
//	}
	
	// 2) ModelAndVies 객체 이용
	
	// ModelAndView에서 Model은 데이터를 key-value 담을 수 있는 Model 객체를 의미함
	// View는 응답뷰에 대한 정보를 담을 수 있다. 이 때는 리턴 타입이 String이 아닌 modelAndView로 전달해야 함.
	// Model + View가 결합된 형태의 객체 Model 객체는 단독 사용이 가능하지만 View는 불가능 함
	// mv.addObject("errorMsg", "로그인 실패");
	// mv.setViewName("common/errorPage");
	// return mv;
	
	/*
	 * RequestMapping : 클라이언트의 요청(url)에 맞는 클래스 or 메서드를 연결시켜주는 어노테이션
	 * 					해당 어노테이션이 붙은 클래스/메소드를 스프링 컨테이너가 HandlerMapping으로 등록해둔다.
	 * HandlerMapping : 사용자가 지정한 url 정보들을 스프링 컨테이너가 따로 모아둔 저장소
	 * 					-> 클래스 레벨에서 사용된 경우 : 공통 주소로 활용됨(만약 현재 클래스의 공통 주소인 member로 요청이 들어오면
	 * 															 현재 MemberController가 직접 요청 처리를 하게 됨)
	 * 					-> 메서드 레벨에서 사용된 경우 : 공통 주소 외 나머지 주소
	 * 					   ex) localhost:8081/spring/member(공통 주소, 클래스 레벨에서 선언)/login(그 외 주소, 메소드 레벨에서 선언)
	 * 						   단, 클래스 레벨에 @RequestMapping이 존재하지 않는다면 메서드 레벨에서 단독으로 요청을 처리한다(jsp 프로젝트에서 하던 방식)
	 * 
	 * [작성법]
	 * 1) @RequestMapping("url") -> url 주소만 붙이는 경우 요청 방식(get/post)과 관계없이 일치하는 url에 대해 요청 처리한다.
	 * 2) @RequestMapping(value="url", method = RequestMethod.GET/POST) => 일치하는 url + 요청 방식을 함께 검사하여 요청 처리한다.
	 * 
	 * 클래스 레벨을 선언하지 않아도 되지만 관리상 선언하는게 좋음
	 * ex) member/enroll 뒤에 메서드 레벨 이름이 겹칠 수 있어서 클래스 레벨로 구분지어야 되기 때문에 
	 *     board/enroll
	 * 
	 * 단, 일반적으로 메서드 레벨에서는 GET/POST 방식을 구분할 때
	 * @GetMapping("url"), @PostMapping("url")을 사용하는 게 일반적임
	 */
	@PostMapping("/login")
	public String loginMember(Model model, Member m,
									HttpSession session,
									RedirectAttributes ra,
									HttpServletResponse resp,
									HttpServletRequest req,
									@RequestParam(value="saveId", required = false) String saveId) {
		// 암호화 전 로그인 요청 처리
//		Member loginUser = memberService.loginMember(m);
//		
//		if (loginUser == null) {
//			mv.addObject("errorMsg", "로그인 실패");
//			
//			mv.setViewName("common/errorPage");
//		} else {
//			session.setAttribute("loginUser", loginUser);
//			
//			mv.setViewName("redirect:/"); // 메인 페이지로 url 재요청
//			// response.sendRedirect(request.getContextPath()); 와 동일
//		}
		
		// 암호화 후 로그인 요청 처리
		/*
		 * 기존에 평문이 디비에 등록되어 있었기 때문에 아이디랑 비밀번호를 같이 입력받아 조회하는 형태로 작업했음
		 * 암호화 작업을 하면 입력받는 비밀번호는 평문이지만 디비에 등록된 비밀번호는 암호문이기 때문에 비교 시 무조건 다르게 나옴
		 * 아이디로 먼저 회원 정보 조회 후 회원이 있으면 비밀번호 암호문 비교 메소드를 이용해서 일치하는지 확인
		 */
//		if (true) {
//			throw new RuntimeException(); // 예외 강제 발생
//		}
		Member loginUser = memberService.loginMember(m);
		// loginUser : 아이디 + 비밀번호로 조회한 회원정보 --> 아이디로만 조회
		// loginUser안의 userPwd : 암호화된 비밀번호
		// m안의 userPwd : 암호화 되지 않은 평문 비밀번호
		
		// BCryprtPasswordEncoder 객체의 메소드 중 matches 사용
		// matches(평문, 암호문)을 작성하면 내부적으로 복호화 작업이 이루어져서 일치 여부를 boolean값으로 반환(true 일치, false 불일치)
		
		if (loginUser != null && bcryptPasswordEncoder.matches(m.getUserPwd(), loginUser.getUserPwd())) {
			// 두 조건 모두 만족 시 로그인 성공
			model.addAttribute("loginUser", loginUser);
			//mv.addObject("loginUser", loginUser); // mv로 추가 시 에러 발생
			//session.setAttribute("loginUser", loginUser);
			
			session.setAttribute("alertMsg", "로그인 성공");
			
			// 로그인 성공 시 아이디값을 저장하고 있는 쿠키 생성(유효시간 1년)
			Cookie cookie = new Cookie("saveId", loginUser.getUserId());
			
			if (saveId != null) { // 아이디 저장 체크 시
				cookie.setMaxAge(60 * 60 * 24 * 365); // 초단위 지정(1년)
			} else { // 아이디 저장 체크 x
				cookie.setMaxAge(0); // 유효시간 0초 -> 생성되자마자 소멸
			}
			
			cookie.setPath("/spring");
			
			// 쿠키를 응답 시 클라이언트에게 전달
			resp.addCookie(cookie);
			
			//mv.setViewName("redirect:/");
			return "redirect:/";
		} else {
			ra.addFlashAttribute("errorMsg", "아이디 또는 비밀번호가 일치하지 않습니다.");
			
			// redirect의 특징 -> request에 데이터를 저장할 수 없다.
			// redirect 시 잠깐 데이터를 sessionScope에 보관
			// redirect 완료 후 다시 requestScope 이관 ==> 페이지 재요청 시에도 request 스코프에 데이터를 유지 가능
			// : redirect(페이지 재요청)시에도 request scope로 세팅된 데이터가 유지될 수 있도록 하는 방법을 spring에서 제공해줌
			// RedirectAttributes 객체(컨트롤러의 매개변수로 작성하면 Argument Resolver가 넣어줌)
			//mv.setViewName("redirect:/");
			return "redirect:/";
		}
		//return mv;
	}
	
	@GetMapping("/insert") // /sprig/member/insert     header.jsp -> <a href="<%= request.getContextPath() %>/member/insert">회원가입</a>
	public String enrollForm() {
		return "member/memberEnrollForm";
	}
	
	/*
	 * 1. memberService 호출해서 inserMember 메서드 실행 => db에 새 회원정보 등록
	 * 
	 * 2. 멤버 테이블에 회원가입 등록 성공했다면 alertMsg <-- 회원가입 성공 메세지 보내기(세션)
	 * 	  멤버 테이블에 회원등록 실패했다면 에러 페이지로 메세지 담아서 보내기 <-- 회원가입 실패 메세지(리퀘스트)
	 */
	@PostMapping("/insert")
	public String insertMember(Member m, HttpSession session, Model model) {
		
		//System.out.println("암호화 전 비밀번호 : " + m.getUserPwd());
		
		// 암호화 작업
		String encPwd = bcryptPasswordEncoder.encode(m.getUserPwd());
		
		// 암호화 된 pwd를 m의 userPwd 다시 대입
		m.setUserPwd(encPwd);
		
		//System.out.println("암호화 후 비밀번호 : " + m.getUserPwd());
		
		// 1. memberService 호출해서 insertMember 메서드 실행 후 db에 회원 객체 등록
		int result = memberService.insertMember(m);
		
		/*
		 * 2. 멤버테이블에 회원가입 등록 성공했다면 alertMsg(session)
		 * 						  실패했다면 errorMsg(request)
		 */
		String url = "";
		if (result > 0) { // 성공 시 - 메인페이지로
			session.setAttribute("alertMsg", "회원가입");
			url = "redirect:/";
		} else { // 실패 - 에러 페이지로
			model.addAttribute("errorMsg", "회원가입 실패");
			url = "common/errorPage";
		}
		
		return url;
	}
	
	@GetMapping("/logout")
	public String logoutMember(HttpSession session,
							   SessionStatus status) {
		
		// 로그아웃 기능 : session안에 저장된 login 정보를 날리는 게 곧 로그아웃
		// @SessionAttributes를 이용해서 session scope에 배치된 데이터는 일반적인 방법으로는 없앨 수 없음
		// SessionStatus라는 별도의 객체를 이용해야만 없앨 수 있음
		
		//session.invalidate(); // 기존 세션 방식으로는 안된다.
		status.setComplete(); // 세션이 할 일이 완료되면 없어짐
		
		return "redirect:/";
	}
	
	@ResponseBody // 반환되는 값이 forward/redirect 경로가 아닌 값 그 자체임을 의미(ajax 시 사용)
	@PostMapping("/selectOne")
	public String selectOne(String input) {
		
		Member m = new Member();
		m.setUserId(input);
		
		Member searchMember = memberService.loginMember(m);
		
		// JSON : 자바스크립트 객체 표기법으로 작성된 "문자열" 형태의 객체
		
		// GSON 라이브러리 : JSON을 보다 쉽게 다루기 위한 google에서 배포한 라이브러리
		return new Gson().toJson(searchMember);
	}
	
	@ResponseBody
	@GetMapping("/selectAll")
	public String selectAll() {
		
		ArrayList<Member> list = memberService.selectAll();
		
		return new Gson().toJson(list);
	}
	
	/*
	 * 스프링 예외 처리 방법(3가지, 중복 사용 가능)
	 * 
	 * 1순위) 메서드 별로 예외 처리(try/catch, throws)
	 * 
	 * 2순위) 하나의 컨트롤러에서 발생하는 예외를 싹 모아서 처리 -> @ExceptionHandler
	 * 
	 * 3순위) 웹 애플리케이션 전역에서 발생하는 예외를 다 모아서 처리 -> @ControllerAdvice
	 */
	
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {
		e.printStackTrace();
		
		model.addAttribute("errorMsg", "서비스 이용 중 문제가 발생했습니다.");
		model.addAttribute("e", e);
		
		return "common/errorPage";
	}
	
	public int count = 0;
	
	// 고정 방식(spring-scheduler)
	@Scheduled(fixedDelay = 1000)
	public void test() {
		//System.out.println("1초 마다 출력하기"+ count++);
	}
	
	// crontab 방식
	public void testCron() {
		//System.out.println("크론 테스트");
	}
	
	public void testQuartz() {
		//System.out.println("콰츠 테스트");
	}
	
	/*
	 * 회원 정보 확인 스케줄러
	 * 매일 오전 1시에 모든 사용자의 정보를 검색하여 사용자가 비밀번호를 안 바꾼지 3개월이 지났다면
	 * Member 테이블의 changePwd의 값을 y로 변경
	 * 
	 * 로그인을 할 때 changePwd의 값이 Y라면 비밀번호 변경 페이지로 이동(이건 안 함)
	 */
	@Override // public class MemberController extends QuartzJobBean 에서 extends QuartzJobBean을 하고 @Override를 하면 오버라이드를 한 메서드에 QuartzJobBean이 적용이 됨
	public void executeInternal(JobExecutionContext context) throws JobExecutionException {
		memberService.updateMemberChangePwd();
	}
	
}
