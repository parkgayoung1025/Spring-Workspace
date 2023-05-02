<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#enrollForm>table{width:100%;}
	#enrollForm>table *{margin:5px;}
</style>
<link rel="stylesheet" href="${contextPath}/resources/css/main-style.css">
</head>
<body>

	<jsp:include page="../common/header.jsp"/>

	<div class="content">
		<br><br>
		<div class="innerOuter">
			<h2>게시글 작성하기</h2>
			<br>
			
			<form id="enrollForm" action="${contextPath }/board/insert/${boardCode }" enctype="multipart/form-data" method="post">
				<table align="center">
					<tr>
						<th><label for="title">제목</label></th>
						<td><input type="text" id="title" class="form-control" name="boardTitle" value="${b.boardTitle }" required></td>
					</tr>
					<tr>
						<th><label for="writer">작성자</label></th>
						<td><input type="text" id="writer" class="form-control" value="${loginUser.userNo }" name="boardWriter" readonly></td>
					</tr>
					<tr>
						<th><label for="upfile">첨부파일</label></th>
						<td><input type="file" id="upfile" class="form-control" name="upfile">
							${b.originName }
							<input type="hidden" name="originName" value="${b.originName }"/>
							<input type="hidden" name="changeName" value="${b.changeName }"/>
						</td>
					</tr>
					<c:forEach var="boardImg" items="${b.imgList }">
						<c:choose>
							<c:when test="${boardImg.imgLevel == 0 }">
								<c:set var="img0" value="${contextPath}/resources/images/boardT/${boardImg.changeName }"/>
							</c:when>
							<c:when test="${boardImg.imgLevel == 1 }">
								<c:set var="img1" value="${contextPath}/resources/images/boardT/${boardImg.changeName }"/>
							</c:when>
							<c:when test="${boardImg.imgLevel == 2 }">
								<c:set var="img2" value="${contextPath}/resources/images/boardT/${boardImg.changeName }"/>
							</c:when>
							<c:when test="${boardImg.imgLevel == 3 }">
								<c:set var="img3" value="${contextPath}/resources/images/boardT/${boardImg.changeName }"/>
							</c:when>
						</c:choose>
					</c:forEach>
					<tr>
						<th><label  for="image">업로드 이미지1</label></th>
						<td>
						<img class="preview" src="${img0 }">
						<input type="file" name="images" class="form-control" accept="images/*" id="img1">
						<span class="delete-image">&times;</span>
						</td>
					</tr>
					<tr>
						<th><label  for="image">업로드 이미지2</label></th>
						<td>
						<img class="preview" src="${img1 }">
						<input type="file" name="images" class="form-control" accept="images/*" id="img2">
						<span class="delete-image">&times;</span>
					</tr>
					<tr>
						<th><label  for="image">업로드 이미지3</label></th>
						<td>
						<img class="preview" src="${img2 }">
						<input type="file" name="images" class="form-control" accept="images/*" id="img3">
						<span class="delete-image">&times;</span>
						</td>
					</tr>
					<tr>
						<th><label  for="image">업로드 이미지4</label></th>
						<td>
						<img class="preview" src="${img3 }">
						<input type="file" name="images" class="form-control" accept="images/*" id="img4">
						<span class="delete-image">&times;</span>
						</td>
					</tr>
					
					<tr>
						<th><label for="content">내용</label></th>
						<td><textarea id="content" style="resize:none;" rows ="10" class="form-control" name="boardContent" required="required">${b.boardContent }</textarea></td>
					</tr>
					
					
				</table>
				<br>
				<input type="hidden" name="mode" value="${param.mode }"/>
				<input type="hidden" name="boardNo" value="${empty b.boardNo ? 0 : b.boardNo }" />
				
				<!--  존재했떤 이미지가 제거되었음을 기록하는 input 태그
				     value값에 이미지의 "레벨"을 기록(x버튼 클릭했을때)
				     
				     delete from board_img
				     where ref_bno = ??
				     and img_level in (1,2,3)
				 -->
				<input type="hidden" name="deleteList" id="deleteList"  value=""/>
				
				<div align="center">
					<button type="submit" class="btn btn-primary">등록하기</button>
					<button type="reset"  class="btn btn-danger">취소하기</button>
				</div>				
			</form>
		</div>
	</div>
	<script>
		const deleteImage = document.getElementsByClassName("delete-image");
		const preview = document.getElementsByClassName("preview");
		const deleteList = document.getElementById("deleteList");
		
		const deleteSet = new Set();// 순서 x, 중복값 허용 x 
		
		
		for(let i =0; i<deleteImage.length; i++){
			// 미리보기 이름 삭제 , 프리뷰 삭제
			
			deleteImage[i].addEventListener("click", function(){
					
				//1. 미리보기 img가 있따면 삭제
				if(preview[i].getAttribute("src") != ""){
					
					//미리보기 삭제
					preview[i].removeAttribute("src");
					
					deleteSet.add(i);
					
					// Array.from(유사배열 | 컬렉션 ) : 배열로 변환해서 반환
					// 컨트롤러에서 1,2,3 이라는 문자열 같으로 반환받기위해
					deleteList.value = Array.from(deleteSet); 
				}
			});
		}
		
	
	</script>
	
	
	<jsp:include page="../common/footer.jsp"/>
	
	
</body>
</html>