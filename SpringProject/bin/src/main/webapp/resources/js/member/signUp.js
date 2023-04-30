/* signUp.js
   필수기능 
   * 다음주소 api 연동
   * 아이디값 중복체크기능 -> 버튼따로 만들지 않고, keyup이벤트로 유효성검사 진행할 예정
   
   선택기능
   닉네임 유효성검사,
   비밀번호 유효성검사,
   아이디 유효성검사,
   전화번호 유효성검사

 */
// 유효성 검사 여부를 기록할 객체 생성
const checkObj = { 
    "userId"     : true, 
    "userPw"        : false,
    "userPwConfirm" : false,
    "nickName"  : false,
    "phone"       : false,
};

// 아이디 유효성 검사
/*const userId = document.getElementById("userId");
const emailMessage = document.querySelector("#emailMessage");
userId.addEventListener("input", function(){

    // 입력이 되지 않은 경우
    if( userId.value.length == 0 ){
        emailMessage.innerText = "아이디로 사용할 이메일을 입력해주세요.";
        emailMessage.classList.remove("confirm", "error");

        checkObj.userId = false; // 유효 X 기록
        return;
    }
    // 입력된 경우
    const regExp = /^[\w\-\_]{4,}@[\w\-\_]+(\.\w+){1,3}$/; 

    if( regExp.test(userId.value) ){ // 유효한 경우
        $.ajax({
            url : "idCheck",   
            //  필수 속성 url
            // 현재 주소 : /spring/member/insert
            // 상대 경로 : /spring/member/ +  idCheck
            data : { "userId" : userId.value },
            type : "GET", // 데이터 전달 방식 type
            success : function(result){
                if(result == 1){ // 중복 O
                    emailMessage.innerText = "이미 사용중인 아이디(이메일) 입니다.";
                    emailMessage.classList.add("error");
                    emailMessage.classList.remove("confirm");
                    checkObj.userId = false; // 유효 X 기록

                } else { // 중복 X
                    emailMessage.innerText = "사용 가능한 아이디(이메일) 입니다.";
                    emailMessage.classList.add("confirm");
                    emailMessage.classList.remove("error");
                    checkObj.userId = true; // 유효 O 기록
                }
            },
            
            error : function(req, status, error){
                console.log(req.responseText);
            }
        });
    }else{
        emailMessage.innerText = "이메일 형식이 유효하지 않습니다.";
        emailMessage.classList.add("error");
        emailMessage.classList.remove("confirm");

        checkObj.userId = false; // 유효 X 기록
    }
});*/

// 비밀번호 유효성 검사
const userPwd = document.getElementById("userPwd");
const userPwdConfirm = document.getElementById("userPwdConfirm");
const pwMessage = document.getElementById("pwMessage");

userPwd.addEventListener("input", function(){

    if(userPwd.value.length == 0){
        pwMessage.innerText = "영어, 숫자, 특수문자(!,@,#,-,_) 6~30글자 사이로 작성해주세요.";
        pwMessage.classList.remove("confirm", "error");

        checkObj.userPwd = false; // 유효 X 기록
        return;
    }

    const regExp = /^[\w!@#_-]{6,30}$/;

    if( regExp.test(userPwd.value) ){ // 비밀번호 유효

        checkObj.userPwd = true; // 유효 O 기록

        if(userPwdConfirm.value.length == 0){ // 비밀번호 유효, 확인 작성 X
            pwMessage.innerText = "유효한 비밀번호 형식입니다.";
            pwMessage.classList.add("confirm");
            pwMessage.classList.remove("error");
        
        } else { // 비밀번호 유효, 확인 작성 O
            checkPw(); // 비밀번호 일치 검사 함수 호출()
        }

    }else{
        pwMessage.innerText = "비밀번호 형식이 유효하지 않습니다.";
        pwMessage.classList.add("error");
        pwMessage.classList.remove("confirm");

        checkObj.userPwd = false; // 유효 X 기록
    }
});

userPwdConfirm.addEventListener("input", checkPw );
// -> 이벤트가 발생 되었을 때 정의된 함수를 호출하겠다


function checkPw(){ // 비밀번호 일치 검사
    // 비밀번호 / 비밀번호 확인이 같을 경우
    if(userPwd.value == userPwdConfirm.value){
        pwMessage.innerText = "비밀번호가 일치합니다.";
        pwMessage.classList.add("confirm");
        pwMessage.classList.remove("error");

        checkObj.userPwdConfirm = true; // 유효 O 기록

    } else{
        pwMessage.innerText = "비밀번호가 일치하지 않습니다.";
        pwMessage.classList.add("error");
        pwMessage.classList.remove("confirm");

        checkObj.userPwdConfirm = false; // 유효 X 기록
    }
}

// 회원가입 버튼 클릭 시 유효성 검사가 완료 되었는지 확인하는 함수
function signUpValidate(){

    // checkObj에 있는 모든 속성을 반복 접근하여
    // false가 하나라도 있는 경우에는 form태그 기본 이벤트 제거

    let str;

    for( let key  in checkObj ){ // 객체용 향상된 for문

        // 현재 접근 중인 key의 value가 false인 경우
        if( !checkObj[key] ){ 
            switch(key){
            case "userId":     str="아이디가"; break;
            case "userPw":        str="비밀번호가"; break;    
            case "userPwConfirm": str="비밀번호 확인이"; break;
            case "nickName":  str="닉네임이"; break;
            case "phone":       str="전화번호가"; break;
            }

            str += " 유효하지 않습니다.";

            alert(str);

            document.getElementById(key).focus();
            
            return false; // form태그 기본 이벤트 제거
        }
    }

    return true; // form태그 기본 이벤트 수행

}

function sample4_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var roadAddr = data.roadAddress; // 도로명 주소 변수
           

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            document.getElementById('sample4_postcode').value = data.zonecode;
            document.getElementById("sample4_roadAddress").value = roadAddr;

        }
    }).open();
}