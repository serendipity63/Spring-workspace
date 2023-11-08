<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<html>
<head>
<title>UserInfo</title>
</head>
<body>

	<!-- id, nickname, email, gender, name, mobile, age, birthday -->
	<div class="container mt-3">
		<h2>네이버 사용자 정보</h2>
		<table class="table">
			<tbody>
				<tr>
					<th>ID</th>
					<td>${userInfo.id}</td>
				</tr>
				<tr>
					<th>Nickname</th>
					<td>${userInfo.nickname}</td>
				</tr>
				<tr>
					<th>email</th>
					<td>${userInfo.email}</td>
				</tr>
				<tr>
					<th>gender</th>
					<td>${userInfo.gender}</td>
				</tr>
				<tr>
					<th>name</th>
					<td>${userInfo.name}</td>
				</tr>
				<tr>
					<th>age</th>
					<td>${userInfo.age}</td>
				</tr>
				<tr>
					<th>mobile</th>
					<td>${userInfo.mobile}</td>
				</tr>
				<tr>
					<th>birthday</th>
					<td>${userInfo.birthday}</td>
				</tr>
			</tbody>
		</table>
	</div>

</body>
</html>