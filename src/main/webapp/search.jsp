<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SQL Injection Demo - Search Product</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
<div class="container container-large">
    <%
        if ((session.getAttribute("username") == null) || (session.getAttribute("username") == "")) {
    %>
    <p>You are not logged in</p>
    <a href="index.jsp">Please Login</a>
    <%} else {
    %>
    <p class="welcome">Welcome <%=session.getAttribute("username")%> 👋</p>
    <h1>Search Product</h1>
    <form action="search" method="get">
        <input type="text" name="product_name" placeholder="Enter Product Name" required />
        <input type="submit" value="Search" class="btn-green" />
    </form>
    <%
        }
    %>
</div>
</body>
</html>
