# Simple Java Web App

A simple Spring Boot TODO web application using in-memory storage.

## Requirements

- Java 21
- Docker, optional

## Run locally

`./gradlew bootRun`

Open: [http://localhost:8080/](http://localhost:8080/)

## Build Docker image

`docker build -t simple-java-web-app .`

## Run Docker container

`docker run --rm -p 8080:8080 simple-java-web-app`

The container runs the JVM with `-XX:MaxRAMPercentage=75.0` so the heap sizes to
the container's memory limit (important on the small ECS task below).

## Deploy to AWS

This app is deployed to **AWS ECS (EC2 launch type, t3.micro, free-tier)** behind a
**static Elastic IP** — no load balancer. The infrastructure (VPC, ECR, ECS cluster,
EC2 capacity, Elastic IP) is defined in a separate CDK repo:
**`simple-java-web-app-infra`**. Deploy that repo first — it creates the ECR
repository this app pushes to.

Region: `ap-south-1`. ECR repo: `simple-java-web-app`. Image tag: `latest`.
Image URI: `<ACCOUNT_ID>.dkr.ecr.ap-south-1.amazonaws.com/simple-java-web-app:latest`.

### Automated deployment (CI/CD)

`.github/workflows/build-and-deploy.yml` runs on every push to `main` (and via
manual dispatch). It runs the tests, builds the image, pushes it to ECR as
`latest`, then forces a new ECS deployment so the service pulls the new image:

```bash
aws ecs update-service --cluster simple-java-web-app-cluster \
  --service simple-java-web-app-service --force-new-deployment --region ap-south-1
```

Authentication uses **GitHub OIDC** assuming an AWS IAM role (no long-lived keys).
Configure these in the repo's **Settings → Secrets and variables → Actions**:

| Name | Kind | Value |
|------|------|-------|
| `AWS_ROLE_ARN` | secret | ARN of the OIDC deploy role (ECR push + `ecs:UpdateService`) |
| `AWS_REGION` | variable | `ap-south-1` |
| `ECR_REPOSITORY` | variable | `simple-java-web-app` |
| `ECS_CLUSTER` | variable | `simple-java-web-app-cluster` |
| `ECS_SERVICE` | variable | `simple-java-web-app-service` |

See the `simple-java-web-app-infra` README for the one-time setup (CDK bootstrap,
GitHub OIDC provider, deploy roles) and the first-image push runbook.

Once deployed, the app is reachable at `http://<ELASTIC_IP>:8080/` (the Elastic IP
is a CDK stack output).