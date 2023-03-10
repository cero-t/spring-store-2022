# Spring Microservice Application Example (2022)

## What's this?

- Microservice application example (sorry but headless)
- Built with Spring Boot 3.0 and partially with Spring Cloud Stream
- Runnable both locally (with docker-compose) and on k8s
- Monitored with Grafana, Loki, Tempo with Prometheus and Promtail

Services look like:
```mermaid
flowchart LR
  BFF -->|View catalog| ItemService
  BFF -->|Operate cart| CartService
  CartService -->|View items| ItemService
  CartService -->|Check stocks| StockService
  BFF -->|Order| OrderService
  OrderService -->|Keep stock| StockService
  OrderService -->|Process order| M((Messaging))
  M -.->|Queue| M
  M -.->|Settle payment| PaymentService
  M -.->|Delivery| DeliveryService
```

Monitoring infrastructure looks like:
```mermaid
flowchart LR
  APPs -->|Metrics| Prometheus
  APPs -->|Logs| Loki
  APPs -->|Traces| Tempo
  Prometheus --- G(Grafana: vizualize)
  Loki --- G
  Tempo --- G
```

## Run services locally

### Pre-request

- Java >17
- Docker

### Procedure

#### 1. Start up RabbitMQ and Grafana stack.

Go to `docker` directory
```
cd docker
```

Run up RabbitMQ and Grafana stack
```
docker-compose up -d
```

#### 2. Run applications

Use IDE to start up each sevices.

Maven's `spring-boot:run` command is unavailable for some reasons.

#### 3. Play with applications!

- Swagger UI of BFF
  - http://localhost:9000/swagger-ui.html

1. catalog-controller: GET `/catalog`
    - You can get items and prices, images, etc.
2. cart-controller: POST `/cart`
    - You can create your cart and get `cartId`.
3. cart-controller: POST `/cart/{cartId}`
    - You can add item to your cart.
    - `itemId` should match to one of the ids in `/catalog`.
4. cart-controller: GET `/cart/{cartId}`
    - You can check items and total amount in your cart.
5. order-controller: POST `/order`
    - You can order your item virtually! Don't worry, any card payment or e-mail sending do not happen.
    - `cardExpire` must be `MM/yy` formart year and month.
    - `cartId` must match the id obtained by POST `/cart`.

#### 4. How to use store application

Example of `/order` Post body.
```json
{
  "name": "Shin Tanimoto",
  "address": "Tokyo",
  "telephone": "0123456789",
  "mailAddress": "hello@example.com",
  "cardNumber": "0000111122223333",
  "cardExpire": "12/24",
  "cardName": "Shin Tanimoto",
  "cartId": "1"
}
```

- Grafana UI
  - http://localhost:3000


#### 5. Finish applications

1. Stop each services from your IDE

2. Stop RabbitMQ and Grafana stack

```
docker-compose down
```

## Run on k8s

### Pre-request

- Java >17
- kubernetes environment (such as minikube, Docker Desktop, etc)
- kubectl
- Helm

### Procedure

#### 1. Start up k8s and set environments (Only for minikube. If you choose to use k8s in Docker Desktop, skip this section)

Start minikube with enough resources
```
minikube start --cpus=4 --memory=4096
```

Set environment variables to use minikube's image repository
```
eval $(minikube docker-env)
```

#### 2. Build images

Go to `services` directory
```
cd services
```

Build application images
```
sh build-image.sh
```

#### 3. Install middlewares on k8s

Go to `k8s` directory
```
cd k8s
```

Create namespace (optional)
```
kubectl create ns spring-store-2022
```

Add Helm repository
```
helm repo add grafana https://grafana.github.io/helm-charts
```

Install Tempo (if you use namespace, add `-n spring-store-2022`)
```
helm install tempo grafana/tempo -f helm/tempo-config.yml
```

Install Grafana, Prometheus, Loki, Promtail (if you use namespace, add `-n spring-store-2022`)
```
helm install loki-stack grafana/loki-stack -f helm/loki-stack-config.yml
```

Note: Loki takes a few minutes to starts up causing connection errors until then.

#### 4. Deploy applications to k8s

Deploy applications (if you use namespace, add `-n spring-store-2022`)
```
kubectl apply -f ./deploy
```

#### 5. Set port forwardings

If you are using namespace, keep in mind to add `-n spring-store-2022`.

Set port forward to Grafana
```
kubectl port-forward svc/loki-stack-grafana 3000:80
```

If you're using minikube, set port forward to BFF (Unecessary for k8s on Docker Desktop)
```
kubectl port-forward svc/bff-svc 9000:9000
```

#### 6. Play with applications!

- Swagger UI of BFF
  - http://localhost:9000/swagger-ui.html
- Grafana UI
  - http://localhost:3000

See [3. Play with applications!](#3-play-with-applications) for application instruction.

#### 7. Finish applications

Undeploy applications
```
kubectl delete -f ./deploy
```

Uninstall middlewares
```
helm uninstall loki-stack
```
```
helm uninstall tempo
```

Delete namespace (optional)
```
kubectl delete ns spring-store-2022
```
