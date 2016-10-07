<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Stack Underflow - Register</title>
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
    function validate() {
        var pw = document.getElementById('password').value;
        var cpw = document.getElementById('confirmpassword').value;
        
        if(pw!=cpw){
        	alert("Both the passwords should match");
        	return false;
        }
      }
    
    function reset() {
    	document.getElementById("registerform").reset();
      }
</script>
<body>
	<form action="RegisterServlet" name="registerform">
		<div id="wrapper">
			<div id="custpage_header">
				<img src="images/stackunderflow.png" class="img-responsive"
					alt="Stack Underflow"></a>
			</div>
			<!-- Navigation -->
			<nav class="navbar navbar-default navbar-static-top"
				role="navigation"
				style="margin-bottom: 0; background-color: #262626;">
				<div class="navbar-header"></div>
				<!-- /.navbar-header -->

				<ul class="nav navbar-top-links navbar-right">
					<!-- /.dropdown -->
					<a class="dropdown-toggle"
						href="${pageContext.request.contextPath}/home"><i
						class="fa fa-home"></i> Home</a>
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
				</li>				</ul>
				<!-- /.navbar-top-links -->

			</nav>

			<div id="page-wrapper">
				<div class="row">
					<br>
					<div class="col-lg-6">
						<div class="panel panel-default">
							<div class="panel-heading">Registration Form</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-lg-12">
										<form role="form">
											<div class="form-group">
												<%if(request.getAttribute("errormessage")!=null){ %>
												<label><%=request.getAttribute("errormessage") %></label>
												<%if(request.getAttribute("errormessage").equals("Registration successful")){ %>
												Please click <a href="login.jsp">here</a> to continue!
												<%} %>
												<%} %>
											</div>
											<div class="form-group">
												<label>Email</label> <input type="email"
													class="form-control" id='email' name='email' required>
											</div>
											<div class="form-group">
												<label>Password</label> <input type="password"
													class="form-control" id='password' name='password' required>
											</div>
											<div class="form-group">
												<label>Re-Enter Password</label> <input type="password"
													class="form-control" id='confirmpassword' required>
											</div>
											<div class="form-group">
												<label>First Name</label> <input class="form-control"
													id='firstname' name='firstname' required>
											</div>
											<div class="form-group">
												<label>Last Name</label> <input class="form-control"
													id='lastname' name='lastname' required>

											</div>
											<div class="form-group">
												<label>Areas of Interest</label> <select multiple
													class="form-control" id='interest' name='interest' required>
													<option value="0">Java</option>
													<option value="725">Polymorphism</option>
													<option value="726">Overloading</option>
													<option value="271">Collections</option>
													<option value="265">Substring</option>
													<option value="57">Generics</option>
													<option value="87">Enums</option>
													<option value="94">Methods</option>
													<option value="146">String</option>
													<option value="145">Serialization</option>
													<option value="24">Spring</option>
													<option value="5">Android</option>
													<option value="44">Multithreading</option>
													<option value="150">Mysql</option>
													<option value="158">Apache</option>
												</select>
											</div>
											<button type="submit" class="btn btn-default"
												onclick=" return validate()">Submit</button>
											<button type="reset" class="btn btn-default"
												onclick=" return reset()">Reset</button>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>

		<!-- Bootstrap Core JavaScript -->
		<script src="js/bootstrap.min.js"></script>

		<!-- Custom Theme JavaScript -->
		<script src="js/sb-admin-2.js"></script>
	</form>
</body>

</html>
