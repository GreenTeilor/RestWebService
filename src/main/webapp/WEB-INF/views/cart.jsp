<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Корзина</title>
    <jsp:include page="dependencies.jsp"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/style/common.css"/>" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container-fluid">
    <div class="row">
        <c:forEach items="${cart.getProducts()}" var="product">
            <div class="col d-flex justify-content-center">
                <div class="card" style="width: 22rem; margin: 20px; background-color: #dee2e6">
                    <a href="<c:url value="/products/${product.getId()}"/>"><img
                            src="<c:url value="/${product.getImagePath()}"/>"
                            class="card-img-top"
                            style="height: 25rem;"
                            alt="..."></a>
                    <div class="card-body" style="text-align: center">
                        <h2 class="card-title">${product.getName()}</h2>
                        <p class="card-text">Цена: <fmt:formatNumber value="${product.getPrice()}"
                                                                     type="currency"/><br></p>
                        <a href="<c:url value="/cart/removeProduct/${product.getId()}"/>"
                           class="btn btn-primary btn-rounded">Удалить</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<p style="text-align: center">
    <a href="<c:url value="/cart/makeOrder"/>" class="btn btn-primary btn-rounded"
       style="font-size: 1.5rem; margin: 10px">Оформить заказ</a>
    <a href="<c:url value="/cart/clear"/>" class="btn btn-primary btn-rounded" style="font-size: 1.5rem; margin: 10px">Очистить</a>
    <a style="color: ${color}">${status}</a>
</p>
</body>
</html>
