# Game Store REST API

A backend RESTful API built with Java 21 and Spring Boot that simulates an online game store. The system provides stateless, token-based authentication (JWT) with role-based access control (RBAC), offering separate functionalities for users and administrators. A key feature of this project is the event-driven payment integration using the Stripe API and Webhooks.

## 🚀 Features

### User Capabilities
* **Authentication & Security:** Registration and login using JWT (JSON Web Tokens).
* **Catalog:** Browsing available games in the store.
* **Shopping Cart:** Adding and managing games in a personal shopping cart.
* **Checkout & Payments:** Placing orders and processing secure payments via **Stripe Checkout**.
* **Order Tracking:** Viewing order history and dynamically updated order statuses.

### Administrator Capabilities
* **Inventory Management:** CRUD operations (Adding, editing, and removing games) from the store catalog.
* **Order Management:** Viewing customer orders and managing order fulfillment.

### System & Architecture Highlights
* **Stateless Security:** Fully stateless session management using Spring Security and JWT.
* **Event-Driven Payments:** Asynchronous order status updates (e.g., `NEW` → `PAID`) handled securely via **Stripe Webhooks** (`checkout.session.completed` events).
* **Cloud Ready:** Containerized using Docker and configured for seamless deployment on cloud platforms (e.g., Render).

## 🛠️ Technologies & Tools

* **Core:** Java 21, Spring Boot 3
* **Security:** Spring Security, JWT (io.jsonwebtoken)
* **Database:** PostgreSQL, Spring Data JPA, Hibernate
* **3rd Party APIs:** Stripe API (Stripe Java SDK)
* **Build & Deployment:** Maven, Docker
* **Testing:** Postman (API endpoint testing)

## 🎯 Project Goal

The primary purpose of this project is to demonstrate the development of a robust, modern backend architecture. It showcases the ability to implement secure authentication, handle complex relational database mappings (Cart, Items, Orders), and integrate external financial services using industry-standard event-driven patterns (Webhooks) rather than synchronous client-side updates.
