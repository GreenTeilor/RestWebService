<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home</title>
    <jsp:include page="dependencies.jsp"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="style/common.css" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp"/>
<c:if test="${sessionScope.user != null}">
    <jsp:include page="info.jsp"/>
</c:if>
<div class="container-fluid">
    <div class="row">
        <c:forEach items="${categories}" var="item">
            <div class="col d-flex justify-content-center">
                <div class="card"
                     style="width: 22rem; margin: 20px 0 20px 0; background-color: #dee2e6 !important; border-radius: 40px;">
                    <a href="categories/${item.getName()}"><img src="${item.getImagePath()}"
                                                                class="card-img-top"
                                                                style="height: 25rem; border-radius: 40px 40px 0 0;"
                                                                alt="..."></a>
                    <div class="card-body" style="text-align: center">
                        <h2 class="card-title">${item.getName()}</h2>
                        <a href="categories/${item.getName()}" class="btn btn-primary">Перейти</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
