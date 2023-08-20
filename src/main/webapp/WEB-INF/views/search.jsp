<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Поиск</title>
    <jsp:include page="dependencies.jsp"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value="/style/search.css"/>" rel="stylesheet">
    <link href="<c:url value="/style/common.css"/>" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp"/>
<form class="well form-inline search-form" method="POST" action="<c:url value="/search"/>">
    <input type="text" name="keyWords" id="keyWords" class="span3" placeholder="Поиск">
    <button type="submit" class="btn btn-primary">Найти</button>
</form>
<div class="container-fluid">
    <div class="row filter-and-content">
        <div class="col-md-5 d-flex justify-content-center align-items-center">
            <div class="filter">
                <div class="items-group main-text">
                    Фильтр
                </div>
                <form method="POST" action="#">
                    <div class="items-group">
                        <div>
                            <label for="categories">Категория</label>
                        </div>
                        <select id="categories" name="categories">
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.getName()}">${category.getName()}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="items-group">
                        <div>
                            Цена
                        </div>
                        <input type="text" placeholder="От">
                        <input type="text" placeholder="До">
                        <div>
                            <button class="btn btn-primary">Применить</button>
                        </div>
                    </div>
                </form>

            </div>
        </div>
        <div class="col-md-7 d-flex justify-content-center align-items-center">
            <div class="content">
                <div class="items-group main-text">
                    Найденные товары
                </div>
                <div class="products">
                    <c:forEach items="${products}" var="product">
                        <div style="display: inline-block;">
                            <div class="card" style="width: 15rem; margin: 10px; background-color: #dee2e6">
                                <a href="<c:url value="/products/${product.getId()}"/>"><img
                                        src="<c:url value="/${product.getImagePath()}"/>"
                                        class="card-img-top"
                                        style="height: 17rem;"
                                        alt="..."></a>
                                <div class="card-body" style="text-align: center">
                                    <h2 class="card-title" style="font-size: 1rem;">${product.getName()}</h2>
                                    <p class="card-text">Цена: <fmt:formatNumber value="${product.getPrice()}"
                                                                                 type="currency"/><br></p>
                                    <a href="<c:url value="/products/${product.getId()}"/>" class="btn btn-primary">Посмотреть</a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <nav class="pagination-nav">
                    <ul class="pagination">
                        <li class="page-item"><a class="page-link" href="<c:url value="/search/prev"/>"><<</a></li>
                        <li class="page-item"><a class="page-link" href="<c:url value="/search/1"/>">1</a></li>
                        <li class="page-item"><a class="page-link" href="<c:url value="/search/2"/>">2</a></li>
                        <li class="page-item"><a class="page-link" href="<c:url value="/search/3"/>">3</a></li>
                        <li class="page-item"><a class="page-link" href="<c:url value="/search/next"/>">>></a></li>
                    </ul>
                </nav>
            </div>
        </div>
    </div>
</div>

</body>
</html>
