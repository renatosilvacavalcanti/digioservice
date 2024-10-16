# Digioservice

## Description
Digioservice is a Spring Boot application designed to manage and analyze customer purchases. It provides APIs for retrieving purchase records, identifying loyal customers, and recommending wine based on customer buying habits.

## Features
- **GET /compras**: Retrieve a list of purchases, sorted by total value.
- **GET /maior-compra/{ano}**: Get the largest purchase in the specified year.
- **GET /clientes-fieis**: Return the top 3 most loyal customers.
- **GET /recomendacao/cliente/{cpf}/{tipo}**: Recommend wine based on customer’s most frequent purchases.

## Technologies Used
- Java 17
- Spring Boot
- Maven
- RestTemplate

## Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/renatosilvacavalcanti/digioservice.git
