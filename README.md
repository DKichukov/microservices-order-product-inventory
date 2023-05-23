# microservices-order-product-inventory
Spring Boot project consists of products, orders and inventory microservises for orders processing. Product is used for Product REST operations, order is for placing orders and inventory to control products availability.
To be able to place order it shlould have enough quantities of and if quantity reach zero product will be deleted.

## Technologies Used
This project was built using:
- Spring Boot
- Maven
- Spring Data JPA
- Eureka server
- Open Fign client
- MySQL
- Flyway migration

## API Endpoints
Eureka serever: http://localhost:8761
- Orders:       http://localhost:8081/api/v1/orders
- Inventory:    http://localhost:8082/api/v1/inventories
- Products:     http://localhost:8083/api/v1/products

