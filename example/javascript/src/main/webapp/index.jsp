<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <t:head />
    <title>Sign in &middot; Kagura</title>
    <style type="text/css">
        body {
            padding-top: 40px;
            padding-bottom: 40px;
            background: #f5f5f5 url("loginback1.jpg") no-repeat fixed center bottom;
        }

        .form-signin {
            max-width: 300px;
            padding: 19px 29px 29px;
            margin: 0 auto 20px;
            background-color: #fff;
            border: 1px solid #e5e5e5;
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            border-radius: 5px;
            -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
            -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
            box-shadow: 0 1px 2px rgba(0,0,0,.05);
        }
        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }
        .form-signin input[type="text"],
        .form-signin input[type="password"] {
            font-size: 16px;
            height: auto;
            margin-bottom: 15px;
            padding: 7px 9px;
        }
        .container
        {
            background-color: rgba(255,255,255,0);
        }
        .hero-unit
        {
            background-color: rgba(255,255,255,.4);
            padding-bottom: 150px;
        }
    </style>

</head>

<body>
    <div class="container">
        <div class="hero-unit">
            <h1>Kagura</h1>
            <p>Reporting for your future</p>
            <p>
                <div class="form-signin" id="signinSection">
                    <h2 class="form-signin-heading">Please sign in</h2>
                    <input id="loginEmail" type="text" class="input-block-level" placeholder="Email address">
                    <input id="loginPassword" type="password" class="input-block-level" placeholder="Password">
                    <%--<label class="checkbox">--%>
                    <%--<input type="checkbox" value="remember-me"> Remember me--%>
                    <%--</label>--%>
                    <button class="btn btn-large btn-primary" type="submit" onclick="doLogin()">Sign in</button>
                </div>
            </p>
        </div>
    </div> <!-- /container -->
    <t:footer />
</body>
</html>
