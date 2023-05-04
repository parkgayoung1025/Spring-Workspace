<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!--  공통적으로사용할 라이브러리 추가 -->
<!-- Jquey 라이브러리 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<!-- 부트스트랩에서 제공하있는 스타일 -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<!-- 부투스트랩에서 제공하고있는 스크립트 -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<!-- alertify -->
<script src="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/alertify.min.js"></script>
<!-- alertify css -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/alertify.min.css"/>
<!-- Default theme -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/default.min.css"/>
<!-- Semantic UI theme -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.13.1/build/css/themes/semantic.min.css"/>

<style>
div {box-sizing:border-box;}
#header {
	width: 80%;
	height: 100px;
	padding-top:20px;
	margin :auto;
	
	   /* sticky : 스크롤이 임계점(최상단)에 도달했을 때 화면에 스티커처럼 붙임
        - 평소에는 static(기본 position상태)
          임계점 도달 시 fixed(화면 특정 위치에 고정)

        * top, bottom, left, right 속성이 필수로 작성되어야 함
            -> 임계점 도달 시 어느 위치에 부착할지를 지정해야되기 때문에
    */
    position: sticky;
    top: 0; /* 최상단에 붙임*/
      background-color: white;
    border-bottom : 2px solid black;
    z-index: 10;
    
    
}
</style>
</head>



<body>

	<c:if test="${ not empty alertMsg }">
		<script>
			alertify.alert("서비스 요청 성공", '${alertMsg}' );
		</script>
		<c:remove var="alertMsg"/>
	</c:if>

	
	<div class="header">
<!-- 		<div id="header_1"> -->
			<div id="header_1_left">
			 <a href="<%= request.getContextPath() %>">
		  		<%--
		  		
		  		header.jsp파일처럼 를 별도의 jsp로 분리한 경우 , header.jsp를 포함(include)하고 있는 다른 jsp페이지에서 상대경로로 작성된 이미지의 경로가 일치하지 않는 문제가 발생할수 있음.
		    	따라서 지금처럼 분리시켜둔 jsp에 경로를 지정하는 경우 절대 경로를 이용할것
		    	
		    	contextPath 구하는 방법 
		  		1) /spring --> 하드코딩시 : 컨텍스트이름이 변경될경우 일일이 찾아서 수정해줘야하는 번거로움이 있다.
		   		2) <%=  request.getContextPath() %> --> 화면안에 자바코드가 혼합되어 프론트페이지가 지저분해지는 단점있따.
		    	3) ${pageContext.servletContext.contextPath } --> 자바코드를 사용하지 않고 el표현식을 활용하게될시 화면을 좀더 깔끔하게 꾸밀수 있찌만, contextPath를 구하기 위한 경로가 너무
		    												길어 작성하기 번거롭다.
		    			    	
		    	따라서 application scope에 최상위 주소를 간단히 부를 수 있는 형태로 저장할 예정!
		    	 --%>
		        <img src="<%=  request.getContextPath() %>/resources/images/top_logo.jpg">
		      	
		    </a>
<!-- 				<img src="https://www.iei.or.kr/resources/images/main/main_renewal/top_logo.jpg"/> -->
			</div>
			<div id="header_1_center">
				<div class="search-area">
		        <!-- form 내부 input 태그 값을 서버 또는 페이지로 전달, 기능구현 X -->
		        <form action="#" name="search-form">
		            <fieldset>
		
		                <!-- autocomplete="off" : 검색어 자동완성을 막음  -->
		                <input type="search" id="query" name="query" 
		                    autocomplete="off" placeholder="검색어를 입력해주세요.">
		
		                <!-- 검색 버튼 -->
		                    <button type="submit" id="search-btn" class="fa-solid fa-magnifying-glass"></button>  
		                </fieldset>
		            </form>
		        </div>
			</div>
			<div id="header_1_right">
				
            	<%-- if - else --%>
            		<%-- choose 내부에는 jsp 주석만 사용 --%>
            		
            		<%-- 로그인이 되어있지 않은 경우 --%>
       				<c:if test="${ empty sessionScope.loginUser}"> 
            			<a data-toggle="modal" data-target="#loginModal">로그인</a>
            			<span>|</span>
            			<a href="<%= request.getContextPath() %>/member/insert">회원가입</a>
            			<span>|</span>
	                    <a href="#">ID/PW 찾기</a>
            			<!-- 회원가입 / ID/PW 찾기 영역 -->

            		</c:if>
            		
			</div>			
			
			


	</div>
	<div class="nav">
	    <ul>
	    	<li><a href="#">HOME</a></li>
	    	<li><a href="#">공지사항</a></li>
	    	<li><a href="<%= request.getContextPath() %>/chat/chatRoomList">채팅</a></li>
	       <%--  <li><a href="<%= request.getContextPath() %>/board/list?type=1">공지사항</a></li>
	        <li><a href="<%= request.getContextPath() %>/board/list?type=2">자유 게시판</a></li>
	        <li><a href="<%= request.getContextPath() %>/board/list?type=3">질문 게시판</a></li> --%>	
			<c:forEach var="boardType" items="${boardTypeList}">
				<li><a href="<%= request.getContextPath() %>/board/list/${boardType.boardCd}">${boardType.boardName}</a></li>
			</c:forEach>
	    </ul>
	</div>




	<!-- 로그인 클릭시 뜨는 모달( 기존에 안보이다가 버튼클릭시 보임) -->
	<div class="modal fade" id="loginModal">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<!-- 모달 해더 -->
				<div class="modal-header">
					<h4 class="modal-title">Login</h4>
					<button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				<form action="<%= request.getContextPath() %>/member/login" method="post">
					<!--  모달 바디 -->
					<div class="modal-body">
						<input type="text" class="form-controll mb-2 mr-sm-2" placeholder="Enter ID" id="userId" name="userId" value="${cookie.saveId.value}" style="width:100%"> <br>
						<input type="password" class="form-controll mb-2 mr-sm-2" placeholder="Enter Password" id="userPwd" name="userPwd" style="width:100%">
					</div>
					
					<!-- 모달 푸터 -->
					<div class="modal-footer">
					<%-- 쿠키에 saveId가 있는 경우--%>
                        <c:if test="${ !empty cookie.saveId.value}">
                            <%-- chk 변수 생성(page scope)--%>
                            <c:set var="chk" value="checked"/>

                        </c:if>

	                    <label>
                               <!-- checked 속성 : radio/checkbox를 체크하는 속성 -->
	                        <input type="checkbox" name="saveId" ${chk}  id="saveId"> 아이디 저장
	                    </label>
						<button type="submit" class="btn btn-primary">로그인</button>
						<button type="button" class="btn btn-danger" data-dismiss="modal">취소</button>
					</div>
				</form>
			</div>
		</div>
	</div>





</body>
</html>