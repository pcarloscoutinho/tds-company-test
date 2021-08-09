# tds-company-test

Clone this repo :arrow_down:

### Docker Instructions

Run tests: `./mvnw test` :test_tube:

Run project: `docker-compose up -d` :rocket:

Visualize data :chart_with_upwards_trend: :

  - Access Grafana - http://localhost:3000
  
  - Login - admin
  
  - Password - tds
  
  ![alt text](public/grafana1.jpg)
  
  - Select url-shortener-app for app metrics.
  - Select JVM(Micrometer) for JVM metrics.


### Maven Wrapper Instructions

Run tests: `./mvnw test` :test_tube:

Run project: ./mvnw spring-boot:run :rocket:

### Api Instructions

Create a short url:

```
POST - localhost:8081/api/v1/url/createUrl 

Body:
{
    "longUrl": "you_long_url"
}
```

Get short url statistics:

```
GET - localhost:8081/api/v1/statistics/{shortUrl}
```

### Api Docs

URL: http://localhost:8081/api/swagger-ui.html
