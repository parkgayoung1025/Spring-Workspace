<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<style>
			/* 프로필 */
		.board-writer > img{
		    width: 40px;
		    height: 40px;
		}
		
		/* 닉네임 */
		.board-writer > span{
		    font-weight: bold;
		    margin-left : 10px;
		}
	</style>
	<link rel="stylesheet" href="${contextPath}/resources/css/main-style.css">
</head>
<body>
	
	<jsp:include page="../common/header.jsp"/>

	<div class="content">
		<br><br>
		<div class="innerOuter">
			<h2>게시글 상세보기</h2>
			<br>
			
			<button id="listBtn" class="btn btn-secondary" style="float: right;" >목록으로</button>
			<br><br>
			
			<table id="contentArea" align="center" class="table">
				<tr>
					<th width="100">제목</th>
					<td colspan="3">${b.boardTitle }</td>
				</tr>
				<tr>
					<th>  
						작성자
                    </th>
					<td  class="board-writer">
						<c:if test="${empty detail.profileImage}">
	                        <!-- 프로필 이미지가 없는 경우 -->
	                        <img src="${contextPath}/resources/images/user.jpg">
	                    </c:if>
	
	                    <c:if test="${ !empty detail.profileImage}">
	                         <!-- 프로필 이미지가 있는 경우 -->
	                        <img src="${contextPath}/resources/images/user.jpg">
	                        <%-- <img src="${contextPath}${detail.profileImage}"> --%>
	                    </c:if>
	                    ${b.nickName}
					</td>
					<th>
						작성일
					</th>
					<td>
						${b.createDate}
					</td>
				</tr>
				<c:if test="${!empty b.imgList }">
					<c:forEach var="i" begin="0" end="${fn:length(b.imgList) -1 }">
						<tr>
							<th>이미지${i+1}</th>
							<td colspan="3">
							<img src="${contextPath}/resources/images/boardT/${b.imgList[i].changeName}">
							<a href ="${contextPath}/resources/images/boardT/${b.imgList[i].changeName}"
							download="${b.imgList[i].changeName}">다운로드</a>
							</td>
						</tr>						
					</c:forEach>
				</c:if>
				<tr>
					<th>내용</th>
					<td>					
					</td>
					<th>
						조회수
					</th>
					<td>
						${b.count }
					</td>
				</tr>
				<tr>
					<td colspan="4"><p style="height:150px;">${b.boardContent }</p></td>
				</tr>
			</table>
			<br>
			
			<div align="center">
				<!-- 수정하기, 삭제하기 버튼은 이글이 본인이 작성한 글일 경우에만 보여져야한다. -->
				<a class="btn btn-primary" href="${contextPath }/board/enrollForm/${boardCode}?mode=update&bno=${b.boardNo}">수정하기</a>
				<a class="btn btn-danger" href="">삭제하기</a>
			</div>
			<br><br>
			
			<!-- 댓글등록기능 -->
			<table id="replyArea" class="table" align="center">
				<thead>
					<tr>
						<th colspan="2">
							<textarea class="form-control" name="replyContent" id="replyContent" rows="2" cols="55" 
							style="resize:none; width:100%;"></textarea>
						</th>
						<th style="vertical-align: middle;"><button class="btn btn-secondary" onclick="insertReply();">등록하기</button></th> 
					</tr>
					<tr>
						<td colspan="3">댓글(<span id="rcount">3</span>)</td>
					</tr>
				</thead>
				<tbody>
					<!--  스크립트 구문으로 댓글 추가 -->
					
					
				</tbody>
			</table>
				<script>
					
					$(function(){
						selectReplyList();
						
						//setInterval(selectReplyList , 100);
					});
					
					// 댓글 목록 조회
					function selectReplyList(){
						$.ajax({
							url : '${contextPath}/reply/selectReplyList',
							data : {bno : '${b.boardNo}'},
							dataType : 'json',
							success: function(result){
								console.log(result);
								let html ="";
								for(let reply of result){
									html += "<tr>"
									html +=	"<td>"+reply.replyWriter +"</td>"
									if (reply.userNo == '${loginUser.userNo}') {
										html +=	"<td><textarea id='replyContent"+reply.replyNo+"'>"+reply.replyContent +"</textarea></td>"
									} else {
										html += "<td>"+reply.replyContent+"</td>";
									}
									html +=	"<td>"+reply.createDate +"</td>"
									if (reply.userNo == '${loginUser.userNo}') {
										html += "<td><button onclick='updateReply("+reply.replyNo+");'>수정</button>"+
										"<button onclick='deleteReply("+reply.replyNo+");'>삭제</button>"+"</td>";
									}
								    html += "</tr>";
								}
								$("#replyArea tbody").html(html);
								$("#rcount").html(result.length);
							},error : function(req,sts,err){
								console.log(req);
								console.log(sts);
								console.log(err);
							} 
						})
					}
					
					function insertReply(){
						
						$.ajax({
							url: "${contextPath}/reply/insert",
							data : {
								refBno : '${b.boardNo}',
								replyContent: $("#replyContent").val(),
								replyWriter : '${loginUser.userNo}'
							},
							type : 'POST',
							success : function (result){
								if(result == "1"){
									alertify.alert("서비스 요청 성공", '댓글 등록 성공' );
								}else{
									alertify.alert("서비스 요청 실패", '댓글 등록 실패' );
								}
								selectReplyList();
							},
							complete : function(){
								$("#replyContent").val("");
							}
							
						})
						
					}
					
					function deleteReply(replyNo) {
						$.ajax({
							url : "${contextPath}/reply/delete",
							data : {replyNo},
							success : function(result) {
								if (result == 1) {
									alert("삭제되었습니다.");
								} else {
									alert("삭제에 실패했습니다.");
								}
								selectReplyList();
							}
						})
					}
					
					function updateReply(replyNo) {
						$.ajax({
							url : "${contextPath}/reply/update",
							data : {replyNo,
									replyContent : $("#replyContent" + replyNo).val()},
							type : 'POST',
							success : function(result) {
								if (result == 1) {
									alert("댓글이 수정되었습니다.");
								} else {
									alert("댓글 수정에 실패했습니다.");
								}
								selectReplyList();
							}
						})
					}
					
				</script>
		</div>
	</div>















	<jsp:include page="../common/footer.jsp"/>
</body>
</html>