package by.teachmeskills.springbootproject.dto.converters;

import by.teachmeskills.springbootproject.dto.OrderDto;
import by.teachmeskills.springbootproject.dto.OrderProductDto;
import by.teachmeskills.springbootproject.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class OrdersProductsConverter {

    public List<OrderDto> toOrdersDto(List<OrderProductDto> ordersProductsDto) {
        Map<Integer, OrderDto> orders = new HashMap<>();
        if (ordersProductsDto != null) {
            ordersProductsDto.forEach(orderProduct -> {
                Integer orderId = orderProduct.getOrderId();
                if (!orders.containsKey(orderId)) {
                    orders.put(orderId, OrderDto.
                            builder().
                            date(orderProduct.getOrderDate()).
                            userId(orderProduct.getUserId()).
                            products(new ArrayList<>()).
                            price(orderProduct.getPrice()).
                            build());
                }
                orders.get(orderId).getProducts().add(ProductDto.
                        builder().
                        id(orderProduct.getId()).
                        name(orderProduct.getName()).
                        description(orderProduct.getDescription()).
                        imagePath(orderProduct.getImagePath()).
                        categoryName(orderProduct.getCategoryName()).
                        price(orderProduct.getPrice()).
                        build());
            });
            return orders.values().stream().toList();
        }
        return null;
    }

    public List<OrderProductDto> fromOrdersDto(List<OrderDto> ordersDto) {
        List<OrderProductDto> ordersProducts = new ArrayList<>();
        AtomicReference<Integer> i = new AtomicReference<>(0);
        if (ordersDto != null) {
            ordersDto.forEach(orderDto -> {
                orderDto.getProducts().forEach(productDto -> ordersProducts.add(OrderProductDto.
                        builder().
                        id(productDto.getId()).
                        name(productDto.getName()).
                        description(productDto.getDescription()).
                        imagePath(productDto.getImagePath()).
                        categoryName(productDto.getCategoryName()).
                        orderDate(orderDto.getDate()).
                        userId(orderDto.getUserId()).
                        orderId(i.get()).
                        price(productDto.getPrice()).
                        build()));
                i.set(i.get() + 1);
            });
            return ordersProducts;
        }
        return null;
    }
}
