# tds-company-test

Clone this repo :arrow_down:

### Docker Instructions

Run tests: `./mvnw test` :test_tube:

Run project: `docker-compose up -d` :rocket:

Visualize data :chart_with_upwards_trend: :

  - Access Grafana - http://localhost:3000
  
  - Login - admin
  
  - Password - tds
  
  ![alt text](public/grafana1.png?raw=true "Grafana1")
  
  - Select url-shortener-app for app metrics.
  - Select JVM(Micrometer) for JVM metrics.

  ![alt text](public/grafana2.png?raw=true "Grafana1")
  
  - Select shortUrl variable from list
  
  ![alt text](public/grafana3.png?raw=true "Grafana1")
  
  - JVM Metrics


### Maven Wrapper Instructions

Run tests: `./mvnw test` :test_tube:

Run project: `./mvnw spring-boot:run` :rocket:

### Api Instructions

Create a short url:

```
POST - localhost:8081/api/v1/url/createUrl 

Body:
{
    "longUrl": "your_long_url"
}
```

Get short url statistics:

```
GET - localhost:8081/api/v1/statistics/{shortUrl}
```

### Api Docs

URL: http://localhost:8081/api/swagger-ui.html
