<%@tag description="Head" pageEncoding="UTF-8" isELIgnored="false" import="javax.servlet.jsp.PageContext,com.base2.kagura.example.javascript.*" %>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="description" content="" />
<meta name="author" content="" />
<!-- Le styles -->
<link href="${pageContext.request.contextPath}/lib/bootstrap/css/bootstrap.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/lib/bootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/lib/kagura.css" rel="stylesheet" />
<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/lib/html5shiv.js"></script>
<![endif]-->
<script src="${pageContext.request.contextPath}/lib/spin.min.js"></script>
<script language="JavaScript">
    var server_base = "<%= Utils.serverPath(request) %>";
</script>