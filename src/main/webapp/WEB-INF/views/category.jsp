<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>${categoryName}</title>
    <jsp:include page="dependencies.jsp"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/style/common.css"/>" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container-fluid">
    <div class="row">
        <c:forEach items="${categoryProducts}" var="product">
            <div class="col d-flex justify-content-center">
                <div class="card" style="width: 22rem; margin: 20px 0 20px 0; background-color: #dee2e6">
                    <a href="<c:url value="/products/${product.getId()}"/>"><img
                            src="<c:url value="/${product.getImagePath()}"/>" class="card-img-top"
                            style="height: 25rem;" alt="..."></a>
                    <div class="card-body" style="text-align: center">
                        <h2 class="card-title">${product.getName()}</h2>
                        <p class="card-text"><span style="color: green; font-size: 1.5rem;">Цена: <fmt:formatNumber
                                value="${product.getPrice()}"
                                type="currency"/></span><br>
                                ${product.getDescription()}</p>
                        <a href="<c:url value="/products/${product.getId()}"/>" class="btn btn-primary">Посмотреть</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
