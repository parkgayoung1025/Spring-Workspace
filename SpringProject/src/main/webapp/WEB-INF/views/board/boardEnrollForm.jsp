<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
						<td><input type="text" id="title" class="form-control" name="boardTitle" required value="${b.boardTitle }"></td>
					</tr>
					<tr>
						<th><label for="writer">작성자</label></th>
						<td><input type="text" id="writer" class="form-control" value="${loginUser.userNo }" name="boardWriter" readonly></td>
					</tr>
					<tr>
						<th><label for="content">내용</label></th>
						<td><textarea id="content" style="resize:none;" rows ="10" class="form-control" name="boardContent" required="required">${b.boardContent }</textarea></td>
					</tr>
				</table>
				<br>
				<input type="hidden" name="mode" value="${param.mode}"/>
				<input type="hidden" name="boardNo" value="${empty b.boardNo ? 0 : b.boardNo}" />
				<div align="center">
					
					<button type="submit" class="btn btn-primary">등록하기</button>
					
					<button type="reset"  class="btn btn-danger">취소하기</button>
				</div>				
			</form>
		</div>
	</div>
















	<jsp:include page="../common/footer.jsp"/>
	
	
</body>
</html>