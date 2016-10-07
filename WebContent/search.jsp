<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Stack Underflow - Search</title>
	<link rel="shortcut icon" href="images/sulogo.jpg">

    <!-- Bootstrap Core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="css/sb-admin-2.css" rel="stylesheet">
    
     <link href="css/customStyle.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css">
</head>

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

        <div id="page-wrapper">
            <div class="row">
				<br>
				<div class="col-lg-12">
					<h1 style="text-align: center">Search Content</h1>
					<br>
					<form method="post"
						action="${pageContext.request.contextPath}/question">
						<div class="input-group custom-search-form">
							<input type="text" name="searchitem" class="form-control"
								placeholder="Search..." required> <span
								class="input-group-btn">
								<button class="btn btn-default" type="submit"
									style="background-color: #9494b8;">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div>
					</form>
					<hr>
				</div>
			</div>
            <!-- /.row -->
            
            <div class="row">

                <div class="col-lg-8">
                <h4>Search Query :</h4> <p style="display:inline;">${questionText}<p><br>
                    <h4>Search Results:</h4><br>
                    <c:forEach items="${questions}" var="question">
	                    <div class="panel panel-default">
	                    	<div class="panel-heading">
	                            <!-- <a href="<c:url value="/answers?id=${question.id}&question=${question.title}&tags=${question.tags}" />">${question.title} ${question.tags}</a> -->
	                            <a href="<c:url value="/answers?id=${question.id}" />">${question.title}</a>
	                        </div>
	                        <div class="panel-body" id="questionbody_bar">
 	                            <p class="questionbody">${question.shortBody}</p>                         
	                        </div>
	                        <div class="panel-body" id="tag_bar">
	                             <c:forEach items="${question.tags}" var="tagname">
	 	                            <p class="question">${tagname}</p>                         
	                             </c:forEach>
	                        </div>
	                    </div>
                    </c:forEach>
                </div>
                <%if (session.getAttribute("EMAIL")!= null) {%>
				<div class="col-lg-4" id="side-box">
					<div class="panel panel-default">
						<div class="panel-heading" style="background-color: #1aa3ff">
							<i class="fa fa-bell fa-fw"></i> Recommendations
						</div>
						<!-- /.panel-heading -->
						<div class="panel-body">
							<c:forEach items="${filteredques}" var="relatedquestion">
								<div class="list-group">
									<a class="list-group-item" href="<c:url value="/answers?id=${relatedquestion.id}&question=${relatedquestion.title}&tags=${relatedquestion.tags}" />">${relatedquestion.title} </a>
								</div>
							</c:forEach>
						</div>
						<!-- /.list-group -->
					</div>
					<!-- /.panel-body -->
				</div>
				<%} %>
                <!-- /.panel -->
                </div>
            </div>
            <!-- /.row -->
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <!-- jQuery -->
    <script src="js/jquery.min.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="js/bootstrap.min.js"></script>

    <!-- Custom Theme JavaScript -->
    <script src="js/sb-admin-2.js"></script>

</body>

</html>
