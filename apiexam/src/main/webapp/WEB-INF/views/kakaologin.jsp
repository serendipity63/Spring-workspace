<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<c:set var="path" value="${pageContext.request.contextPath }"/>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


<a href="https://kauth.kakao.com/oauth/authorize?client_id=25026a8d2b03657741ee2c7b317c8e4d&redirect_uri=http://localhost:8090/api/kakaologin&response_type=code&">
<img src="${path }/resources/img/kakao_login_medium_wide.png"/>	
</a>


</body>
</html>