<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<!-- 	<link rel="icon" href="../../favicon.ico"> -->

<title>Welcome page</title>

<!-- Bootstrap core CSS -->
<link href="/resources/bootstrap-3.3.5-dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="/resources/css/jumbotron.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Showcase</a>
			</div>

			<div id="navbar" class="navbar-collapse collapse">
				<!-- Note: the 'action' attribute is set to '/login', a url which is automatically
			 	intercepted by spring-security that will try to authenticate the user -->
				<form class="navbar-form navbar-right" name='logoutForm'
					action="<c:url value='/logout' />" method='POST'>

					<a>${usernameLinkText}</a>

					<button type="submit" class="btn btn-success">Sign out</button>
					<!-- Remember to always put this hidden field inside the login form
						if the 'http' element of spring security config contains the 
						'csrf' child (see spring-security.xml)-->
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />
				</form>
				<!--/.navbar-collapse -->
			</div>
		</div>
	</nav>

	<div style="height: 20px;"></div>

	<div class="container">
		<div class="well">
			<div></div>
			<div class="row">
				<div class="col-md-4">
					<div>
						<span class="img-responsive img-rounded"
							style="background-image: url(&quot;resources/images/angry-toad.jpg&quot;);">
							<img class="img-responsive" alt=""
							src="resources/images/angry-toad.jpg" />
						</span>
						<p>Toad is not amused.</p>
					</div>
				</div>

				<div class="col-md-8">
					<h2>Just ranks</h2>
					<p>
						You can <a href="/user/ranks">review your ranks</a> or <a>create
							a new one</a>
					</p>
				</div>
			</div>

		</div>
	</div>

</body>