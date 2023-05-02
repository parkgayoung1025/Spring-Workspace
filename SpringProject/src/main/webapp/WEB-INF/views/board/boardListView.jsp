<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pi" value="${map.pi}"/>
<c:set var="list" value="${map.list}"/>
<c:if test="${!empty param.condition}">
	<c:set var="sUrl" value="&condition=${param.condition}&keyword=${param.keyword}"/>
</c:if>
<c:forEach items="${boardTypeList}" var="bt">
	<c:if test="${bt.boardCd == boardCode}">
		<c:set var="boardNm" value="${bt.boardName}"/>
	</c:if>
</c:forEach>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${contextPath}/resources/css/main-style.css">
<style>
      #boardList {text-align:center;}
        #boardList>tbody>tr:hover {cursor:pointer;}

        #pagingArea {width:fit-content; margin:auto;}
        
        #searchForm {
            width:80%;
            margin:auto;
        }
        #searchForm>* {
            float:left;
            margin:5px;
        }
        .select {width:20%;}
        .text {width:53%;}
        .searchBtn {width:20%;}
        /* 썸네일 관련 스타일 */
      #boardList tr > td:nth-of-type(2){ /* 2번째 td(제목) */
          position: relative;
      }
        .list-thumbnail{
           max-width: 50px;
          max-height: 30px;
      
          position: absolute;
          left : -15px;
          top : 10px;
        }
</style>
</head>
<body>
	<jsp:include page="../common/header.jsp"/>
	
	<div class="content">
		<br><br>
		<div class="innerOuter" style="padding:5% 10%;">
			<h2>${boardNm}</h2>
			<br>
			<!-- 로그인 시에만 보이는 글쓰기 버튼 -->
			<c:if test="${not empty loginUser}">
				<a class="btn btn-secondary" style="float:right;" href="${contextPath}/board/enrollForm/${boardCode}">글쓰기</a>
			</c:if>
			<br><br>
			<table id="boardList" class="table table-hover" align="center">
				<thead>
					<tr>
						<th>글번호</th>
						<th>제목</th>
						<th>작성자</th>
						<th>조회수</th>
						<th>작성일</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty list}">
						<td colspan="5">게시글이 없습니다.</td>
					</c:if>
					<c:forEach items="${list}" var="b">
						<tr onclick="movePage(${b.boardNo});">
							<td class="bno">${b.boardNo}</td>
							<td>
								<c:if test="${!empty b.thumbnail}">
									<img class="list-thumbnail" src="${contextPath}${b.thumbnail}">
								</c:if>
								${b.boardTitle}
							</td>
							<td>${b.nickName}</td>
							<td>${b.count}</td>
							<td>${b.createDate}</td>
						</tr>
					</c:forEach>
					<tr onclick="movePage(1);">
						<td class="bno">555</td>
						<td>
							<img class="list-thumbnail" src="${contextPath}/resources/images/user.jpg">
							테스트
						</td>
						<td>테스트</td>
						<td>0</td>
						<td>2023.01.01</td>
					</tr>
				</tbody>
			</table>
			
			<script>
				function movePage(bno) {
					location.href = '${contextPath}/board/detail/${boardCode}/'+bno;
				}
			</script>
			
			<br>
			<!-- 페이지네이션 A태그에 사용될 공통 주소를 저장한 변수 -->
			<c:set var="url" value="${boardCode}?cpage="/>
			
			<!-- 페이지 이동기능 구현 -->
	        <div id="pagingArea">
	           <ul class="pagination">
	              <c:choose>
	                 <c:when test="${ pi.currentPage eq 1 }">
	                    <li class="page-item disabled"><a class="page-link" href="#">Previous</a></li>
	                 </c:when>
	                 <c:otherwise>
	                    <li class="page-item"><a class="page-link" href="${url}${pi.currentPage -1 }${sUrl}">Previous</a></li>
	                 </c:otherwise>               
	              </c:choose>
	               
	              <c:forEach var="item" begin="${pi.startPage }" end="${pi.endPage }">
	                 <li class="page-item"><a class="page-link" href="${url}${item }${sUrl}">${item }</a></li>
	              </c:forEach>
	               
	              <c:choose>
	                 <c:when test="${ pi.currentPage eq pi.maxPage }">
	                    <li class="page-item disabled"><a class="page-link" href="#">Next</a></li>
	                 </c:when>
	                 <c:otherwise>
	                    <li class="page-item"><a class="page-link" href="${url}${pi.currentPage + 1 }${sUrl}">Next</a></li>
	                 </c:otherwise>               
	              </c:choose>
	           </ul>
	        </div>
	        <br clear="both"><br>
	        <form id="searchForm" action="${boardCode}" method="get" align="center">
	        	<div class="select">
	        		<select class="custom=select" name="condition">
	        			<option value="writer" ${param.condition == 'writer' ? 'checked' : ''}>작성자</option>
	        			<option value="title" ${param.condition == 'title' ? 'checked' : ''}>제목</option>
	        			<option value="content" ${param.condition == 'content' ? 'checked' : ''}>내용</option>
	        			<option value="titleAndContent" ${param.condition == 'titleAndContent' ? 'checked' : ''}>제목+내용</option>
	        		</select>
	        	</div>
	        	<div class="text">
	        		<input type="text" class="form-control" name="keyword" value="${param.keyword}">
	        	</div>
	        	<button type="submit" class="searchBtn btn btn-secondary">검색</button>
	        </form>
		</div>
	</div>
	
	<jsp:include page="../common/footer.jsp"/>
</body>
</html>