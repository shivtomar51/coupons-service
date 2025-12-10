# Coupons Service

A Spring Boot REST API for managing and applying three different coupon types for an e-commerce platform:

* **Cart-wise coupons** (percentage discount applied when threshold is met)
* **Product-wise coupons** (percentage discount for specific products)
* **BxGy coupons** (Buy X Get Y free, with repetition limits)

This project demonstrates extensible coupon logic, clean handler-based architecture, and implementation of REST APIs for coupon CRUD operations along with discount evaluation and application.

---

## 🚀 Tech Stack

* **Java 17+**
* **Spring Boot 3.5.x**
* **Spring Web**
* **Spring Data JPA**
* **MySQL**
* **Lombok (optional)**
* **Maven**

---

## 📁 Project Structure

```
coupons-service/
├─ pom.xml
├─ README.md
├─ src/
│  ├─ main/java/com/monk/coupons/
│  │  ├─ controller/
│  │  ├─ service/
│  │  ├─ handler/
│  │  ├─ repository/
│  │  ├─ entity/
│  │  └─ dto/
│  └─ main/resources/application.properties
└─ target/
```

---

## 🧪 How to Run the Project

### 1. Create MySQL Database

```sql
CREATE DATABASE coupons_db;
```

### 2. Configure `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/coupons_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 3. Run Application

```
mvn spring-boot:run
```

Or run the generated JAR:

```
java -jar target/coupons-service.jar
```

Base URL: `http://localhost:8080`

---

## 📌 API Endpoints

### **1. Create a Coupon**

`POST /coupons`

### **2. Get All Coupons**

`GET /coupons`

### **3. Get Coupon by ID**

`GET /coupons/{id}`

### **4. Update Coupon**

`PUT /coupons/{id}`

### **5. Delete Coupon**

`DELETE /coupons/{id}`

### **6. Get Applicable Coupons for Cart**

`POST /coupons/applicable`

### **7. Apply Coupon to Cart**

`POST /coupons/apply/{id}`

---

## 🧾 Sample Coupon Payloads

### ✔ Cart-wise Coupon

```json
{
  "code": "B2G1",
  "type": "BXGY",
  "active": true,
  "cartWiseDetails": null,
  "productWiseDetails": null,
  "bxgyDetails": {
    "buyCount": 2,
    "buyProductIds": [1, 2],
    "getCount": 1,
    "getProductIds": [3],
    "repetitionLimit": 3
  }
}
```

### ✔ Product-wise Coupon

```json
{
  "code": "P20",
  "type": "PRODUCT_WISE",
  "active": true,
  "cartWiseDetails": null,
  "productWiseDetails": {
    "productId": 1,
    "percent": 20
  },
  "bxgyDetails": null
}
```

### ✔ BxGy Coupon

```json
{
  "code": "B2G1",
  "type": "BXGY",
  "active": true,
  "cartWiseDetails": null,
  "productWiseDetails": null,
  "bxgyDetails": {
    "buyCount": 2,
    "buyProductIds": [1, 2],
    "getCount": 1,
    "getProductIds": [3],
    "repetitionLimit": 3
  }
}
```

---

## 🛒 Sample Cart Payload

```json
{
  "items": [
    {"productId": 1, "quantity": 6, "price": 50},
    {"productId": 2, "quantity": 3, "price": 30},
    {"productId": 3, "quantity": 2, "price": 25}
  ]
}
```

---

## ✅ Implemented Features

* Cart-wise discount logic with threshold + proportional discount distribution
* Product-wise discount logic applied only to matching productId
* BxGy logic with:

  * Buy-product sets
  * Get-product sets
  * Repetition limit
  * Automatic free item discounting
* Coupon activation + validity checks
* Clean Handler architecture using strategy pattern

---

## ⚠️ Assumptions

* Product prices come from the cart, not a product database
* Only **one coupon** can be applied at a time
* Get-items for BxGy must exist in the cart
* No authentication implemented
* No coupon stacking

---

## ❌ Limitations

* No per-user coupon usage tracking
* No fixed-amount discounts (only percent currently)
* No prioritized coupon selection logic
* No admin UI to manage coupons

---

## 🧭 Future Improvements

* Add fixed amount coupon support
* Add global/user-based usage restrictions
* Add caching (Redis) for fast coupon lookup
* Introduce coupon stacking rules and priority
* Add product-price lookup from a dedicated service

---

## 📦 Packaging & Deployment

* Build JAR with:

```
mvn clean package
```

* Deploy to any JVM environment or containerize with Docker

---




# Age Prediction API Integration (3rd Party API Demo)

This module demonstrates how a third-party API is integrated into a Spring Boot application and how the response is stored into a database.

## 🔹 Use Case
- User enters a name (example: Shiv)
- Application calls a third-party API (Agify)
- Age data is received from the API
- The response is saved into the database
- The saved data is returned as the API response

## 🔹 Third-Party API Used
Agify API  
https://api.agify.io?name=Shiv

Sample Response:
```json
{
  "name": "shiv",
  "age": 64,
  "count": 12345
}


## 👨‍💻 Author

Shiv Tomar

Feel free to reach out for any queries!
