<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>KH 커뮤니티</title>
   
   <!-- 메인페이지에 적용할 css 템플릿 추가  -->
    <link rel="stylesheet" href="resources/css/main-style.css">
   <!-- fontawesome라이브러리추가 다양한 아이콘을 지원함.(EX) 검색용 돋보기 버튼) -->
    <script src="https://kit.fontawesome.com/a2e8ca0ae3.js" crossorigin="anonymous"></script>

</head>
<body>
    <div class="main">
        
        <!--
              * 경로 작성 시 최상위경로인 : /webapp  == / 에서부터 실제 파일이 존재하는 위치대로 작성
        -->
        <!-- 내부 접근 절대 경로 -->
        <jsp:include page="/WEB-INF/views/common/header.jsp" />


        <div class="content">
              <!-- 사이트에 유용한 기능을 추가하는 영역 직접 해보기!! -->
              <div class="content-1">
               <h3>회원 정보 조회</h3>

               <p>이메일을 입력 받아 일치하는 회원 정보를 출력</p>

               이메일 : <input type="text" id="in1">
               <button id="select1">조회</button>
               <div id="result1" style="height:150px"></div>

               <hr>

               <h3>회원 목록 조회</h3>

               <p>일정 시간 마다 비동기로 회원 목록(회원 번호, 이메일, 닉네임) 조회</p>

               <table border="1">
                   <thead>
                       <tr>
                           <th>회원 번호</th>
                           <th>이메일</th>
                           <th>닉네임</th>
                       </tr>
                   </thead>


                   <tbody id="memberList">

                       <tr>
                           <td>1</td>
                           <td>user01@kh.or.kr</td>
                           <td>유저일</td>
                       </tr>

                       <tr>
                           <td>2</td>
                           <td>user02@kh.or.kr</td>
                           <td>유저이</td>
                       </tr>

                       <tr>
                           <td>3</td>
                           <td>user03@kh.or.kr</td>
                           <td>유저삼</td>
                       </tr>

                   </tbody>
               </table>
               </div>
               <div class="content-2">
               <%-- 로그인이 되어있는 경우 d
                      회원가입 기능 수정후 다시오자.
                   --%>
                <c:if test="${!empty sessionScope.loginUser}"> 
                    
                    <div class="login-area">
                           <!-- 회원 프로필 이미지 -->
                       <a href="${contextPath}/member/myPage/profile">
                               
                               <c:if test="${empty loginUser.profileImage}">
                                   <img src="${contextPath}/resources/images/user.jpg" id="member-profile">
                               </c:if>

                               <c:if test="${!empty loginUser.profileImage}">
                                   <img src="${contextPath}${loginUser.profileImage}" id="member-profile">
                               </c:if>

                       </a>

                           <!-- 회원 정보 + 로그아웃 버튼 -->                                       
                           <div class="my-info">
                               <div>
<%--                                <a href="${contextPath}/member/myPage/info" id="nickname">루피</a> --%>
                                   <a href="${contextPath}/member/myPage/info" id="nickname">${empty loginUser.nickName ? '루피':loginUser.nickName}</a>

                                   <a href="${contextPath}/member/logout" id="logout-btn">로그아웃</a>
                               </div>

                               <p>
                                  <!-- alsrudals2022@naver.com -->
                                   ${empty loginUser.userId ? 'alsrudals93@naver.com':loginUser.userId}
                               </p>
                           </div>
                    </div>
                 
                 </c:if>
               </div>
        </div>

    </div>

    <!-- footer include -->
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <!-- jQuery 라이브러리 추가 -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
    
    <!-- main.js 연결 -->
    <script src="${contextPath}/resources/js/main/main.js"></script>

</body>
</html>