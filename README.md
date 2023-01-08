# Getting Started

### Run services locally

#### Pre-request

- Java >17
- Docker

#### Procedure

1. Start up RabbitMQ and Grafana stack.

Go to `infra` directory
`cd infra`

Run up RabbitMQ and Grafana
`docker-compose up`

2. Run applications

Use IDE to start up each sevices.

Maven's `spring-boot:run` command is unavailable for some reasons.

3. Play with applications!

- Swagger UI of BFF
  - http://localhost:9000/swagger-ui.html
- Grafana UI
  - http://localhost:3000

### Run on k8s

#### Pre-request

- Java >17
- kubernetes environment (such as minikube, Docker Desktop, etc)
- kubectl
- Helm

#### Procedure

1. Start up k8s and set environments (Only for minikube. If you choose to use k8s in Docker Desktop, skip this section)

Start minikube with enough resources
`minikube start --cpus=4 --memory=4096`

Set environment variables to use minikube's image repository
`eval $(minikube docker-env)`

2. Build images

Go to `services` directory
`cd services`

Build application images
`sh build-image.sh`

3. Install middlewares on k8s

Go to `k8s` directory
`cd k8s`

Create namespace (optional)
`kubectl create ns spring-store-2022`

Add Helm repository
`helm repo add grafana https://grafana.github.io/helm-charts`

Install Tempo (if you use namespace, add `-n spring-store-2022`)
`helm install tempo grafana/tempo -f helm/tempo-config.yml`

Install Grafana, Prometheus, Loki, Promtail (if you use namespace, add `-n spring-store-2022`)
`helm install loki-stack grafana/loki-stack -f helm/loki-stack-config.yml`

Note: Loki takes a few minutes to starts up causing connection errors until then.

4. Deploy applications to k8s

Deploy applications (if you use namespace, add `-n spring-store-2022`)
`kubectl apply -f ./deploy`

5. Set port forwardings

If you are using namespace, keep in mind to add `-n spring-store-2022`.

Set port forward to Grafana
`kubectl port-forward svc/loki-stack-grafana 3000:80`

If you're using minikube, set port forward to BFF (Unecessary for k8s on Docker Desktop)
`kubectl port-forward svc/bff-svc 9000:9000`

6. Play with applications!

- Swagger UI of BFF
  - http://localhost:9000/swagger-ui.html
- Grafana UI
  - http://localhost:3000
