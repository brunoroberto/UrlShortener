# Url Shortener

This is a simple URL shortener developed using Spring Boot

API deployed on Heroku => https://cryptic-sierra-19162.herokuapp.com/

## Build
```code
./gradlew build
```

## Run 
```code
./java -jar UrlShortener-0.0.1-SNAPSHOT
```

After that you can access using http://localhost:8080

Note: To change the server port check the resources/application.properties file

## Endpoints

### POST /

Request Payload:

```code
{
    "url": "http://youtube.com",
    "expireTimeInHours": 1
}
```

Creates a short url which expires during the hours defined in `expireTimeInHours`.

Note: The maximum expiry time is 48 hours. Check the resources/application.properties file.

Response Payload:

```code
{
    "urlID": "mp7",
    "expireOn": "2020-08-23T18:38:13.676"
}
```

### GET/{urlID}

The urlID is found in the response payload from the POST request. This endpoint will then redirect to the correct url.

## Cleaning up expired urls

There is a cleanup task that is executed according to the settings `urlShortener.cleanUpScheduleTime` in 
resources/application.properties file. This task is responsible to delete all expired short urls.