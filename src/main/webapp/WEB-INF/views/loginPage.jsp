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

<title>Login page</title>

<!-- Bootstrap core CSS -->
<link href="/resources/bootstrap-3.3.5-dist/css/bootstrap.min.css"
	rel="stylesheet">

<!-- Custom styles for this template -->
<link href="/resources/css/signin.css" rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

	<div class="container">

		<c:if test="${errorMessage != null}">
			<div class="alert alert-danger" role="alert">
				${errorMessage}
			</div>
		</c:if>

		<form class="form-signin" name="loginForm" method="POST"
			action="<c:url value='/login' />">
			<h2 class="form-signin-heading">Please sign in</h2>
			<label for="inputEmail" class="sr-only">Email address</label> 
			<input
				type="text" name="username" id="inputUsername" class="form-control"
				placeholder="Username" required autofocus> </input> 
			<label
				for="inputPassword" class="sr-only">Password</label> 
			<input
				type="password" name="password" id="inputPassword" class="form-control"
				placeholder="Password" required> </input>
			<div class="checkbox">
				<label> 
					<input type="checkbox" value="remember-me" />
					Remember me
				</label>
			</div>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
				in</button>
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form>

	</div>
	<!-- /container -->


	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script src="/resources/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>