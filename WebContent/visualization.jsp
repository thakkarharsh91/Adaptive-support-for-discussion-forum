<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Stack Underflow - Index</title>
<link rel="shortcut icon" href="images/sulogo.jpg">

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="css/sb-admin-2.css" rel="stylesheet">

<link href="css/customStyle.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="css/font-awesome.min.css" rel="stylesheet" type="text/css">

</head>
<script>

	var data =  [
			<c:forEach items="${map}" var="entry">
			{"letter":"${entry.key}", "frequency":"${entry.value}"},
			</c:forEach>
	];
	
	var dataPoint = [
		<c:forEach items="${piemap}" var="entry">
			{y:"${entry.value}", legendText:"${entry.value}", indexLabel:"${entry.value}"},
		</c:forEach>
	                 ];
	
</script>
<body>
	<div id="wrapper">
		<div id="custpage_header">
			<img src="images/stackunderflow.png" class="img-responsive" alt="Stack Underflow"></a>
		</div>
		<!-- Navigation -->
		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0; background-color: #262626;">
			<div class="navbar-header">
			</div>
			<!-- /.navbar-header -->

			<ul class="nav navbar-top-links navbar-right">
				<!-- /.dropdown -->


				<a class="dropdown-toggle"
					href="${pageContext.request.contextPath}/home"><i
					class="fa fa-home"></i> Home</a>
				<%if (session.getAttribute("EMAIL")!= null) {%>
				<a class="dropdown-toggle"
				href="${pageContext.request.contextPath}/visualize"><i
				class="fa fa-home"></i> Visualize Trend</a>
				<%}%>
				<li class="dropdown"><a class="dropdown-toggle"
					data-toggle="dropdown" href="#"> <i class="fa fa-user fa-fw"></i>
						<i class="fa fa-caret-down"></i>
				</a>
					<ul class="dropdown-menu dropdown-user">
						<%if (session.getAttribute("EMAIL")== null) {%>
						<li><a href="login.jsp"><i class="fa fa-sign-out fa-fw"></i>Login</a></li>
							<%} else {%>
						<li style="text-align:center">${EMAIL}</li>
						<li class="divider"></li>
						<li><a href="${pageContext.request.contextPath}/UpdateServlet"><i class="fa fa-user fa-fw"></i> Update Profile</a></li>
						<li class="divider"></li>
						<li><a href="<%=request.getContextPath()%>/LogoutServlet"><i
								class="fa fa-sign-out fa-fw"></i>Logout</a> <%}%></li>
					</ul> <!-- /.dropdown-user -->
				</li>
				<!-- /.dropdown -->
			</ul>
			<!-- /.navbar-top-links -->

		</nav>
		
	<h1>Your search trend</h1>

	<jsp:include page="IntentionTrend.html" />

		<!-- jQuery -->
	<script src="js/jquery.min.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>

	<!-- Custom Theme JavaScript -->
	<script src="js/sb-admin-2.js"></script>

</body>
</html>