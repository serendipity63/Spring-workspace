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

<div class="container mt-3">
  <h2>${userInfo.nickname }</h2>
  <p>${userInfo.gender }</p>
  <div class="card" style="width:400px">
    <img class="card-img-top" src="${userInfo.profileImage }" alt="Card image" style="width:100%">
    <div class="card-body">
      <h4 class="card-title">${userInfo.nickname }</h4>
      <p class="card-text">${userInfo.email }</p>
      <a href="#" class="btn btn-primary">자세히..</a>
    </div>
  </div>
  <br>
  </div>



</body>
</html>