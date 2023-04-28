<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>KH-회원가입</title>

     <link rel="stylesheet" href="../resources/css/main-style.css">
     
     <link rel="stylesheet" href="../resources/css/signUp-style.css">
     <style>
     	
		#enroll-form table{margin:auto;}
		#enroll-form input{margin:5px;}
		#enroll-form input:focus-within{ border-bottom: 2px solid #455ba8;}
     </style>
</head>
<body>
    
    <!-- 메뉴바 -->	
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
	
    <div class="signUp-content">
    
     <!-- 회원가입 화면 전환 주소(GET)와 같은 주소로 
                실제 회원가입을 요청(POST)
                -> 요청 주소가 같아도 데이터 전달 방식이 다르면 중복 허용
                
                js, css 수정중
     -->
     
       <form action="insert" method="post" name="signUp-form" onsubmit="return signUpValidate()">
 
                 <label for="userId">
                     <span class="required">*</span> 아이디(이메일)
                 </label>
                 
                 <div class="signUp-input-area">
                     <input type="text" id="userId" name="userId"
                             placeholder="아이디" maxlength="30"
                             autocomplete="off" required>
 
                     <!-- autocomplete="off" : 자동완성 미사용 -->
                     <!-- required : 필수 작성 input 태그 -->
                 </div>
                 
                 <span class="signUp-message" id="emailMessage"></span>
                 
                 <label for="userPwd">
                     <span class="required">*</span> 비밀번호
                 </label>
                 
                 <div class="signUp-input-area">
                     <input type="text" id="userPwd" name="userPwd"
                             placeholder="비밀번호" maxlength="30">
                 </div>
 
                 <div class="signUp-input-area">
                     <input type="text" id="userPwdConfirm"
                             placeholder="비밀번호 확인" maxlength="30">
                 </div>
 
                 <span class="signUp-message" id="pwMessage">영어, 숫자, 특수문자(!,@,#,-,_) 6~30글자 사이로 작성해주세요.</span>
 
                 <label for="nickname">
                     <span class="required">*</span> 닉네임
                 </label>
                 
                 <div class="signUp-input-area">
                     <input type="text" id="nickName" name="nickName"
                             placeholder="닉네임" maxlength="10">
                 </div>
 
                 <span class="signUp-message" id="nicknameMessage">영어/숫자/한글 2~10글자 사이로 작성해주세요.</span>
 
 
 
                 <label for="phone">
                     <span class="required">*</span> 전화번호
                 </label>
                 
                 <div class="signUp-input-area">
                     <input type="text" id="phone" name="phone"
                             placeholder="(- 없이 숫자만 입력)" maxlength="11">
                 </div>
 
                 <span class="signUp-message" id="telMessage">전화번호를 입력해주세요.(- 제외)</span>
              
              	 
                 <label for="address">
                     주소
                 </label>
                 <!-- 다음 api 연동 -->
                 <div class="signUp-input-area">
                     <input type="text" id="sample4_postcode" name="address"
                             placeholder="우편번호" maxlength="6">
                     
                     <button type="button" onclick="sample4_execDaumPostcode()">검색</button>
                 </div>
 
                 <div class="signUp-input-area">
                     <input type="text" id="sample4_roadAddress" name="address" placeholder="도로명주소">
                 </div>
 
                 <div class="signUp-input-area">
                     <input type="text" id="sample4_detailAddress" name="address" placeholder="상세주소">
                 </div>
 
                 <button type="submit" id="signUp-btn">가입하기</button>
 
             </form>
	</div>
	
	
    <!-- 푸터바 -->
    <jsp:include page="../common/footer.jsp" />
	
	<!-- 다음 주소 API -->
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

    <!-- signUp.js 연결 -->
    <script src="${contextPath}/resources/js/member/signUp.js"></script>
    
</body>
</html>