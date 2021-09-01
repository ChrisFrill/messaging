# Messaging Application
A simple messaging application written in Java with Springt WebFlux that lets you send and read messages from Redis. The sent messages are also broadcasted via WebSockets. 

## Usage
The messaging application can be reached at port 8080.
### POST /messages
Send new message to Redis in format rejecting invalid messages:
```json
{
  "content": "abrakadabra",
  "timestamp": "2018-10-09 00:12:12+0100"
}
```
### GET /messages
Returns all messages persisted in Redis with the longest palindrome size calculated from the given message content. Only alphabetic characters count.
```json

[
  {
    "content": "abrakadabra",
    "timestamp": "2018-10-08 23:12:12+0000",
    "longest_palindrome_size": 3
  }
]
```
### Subscribe to WebSocket
Clients can subscribe to `ws://localhost:8080/message` endpoint to recieve messages.

## Configuration Options
The following configuration options should be set by providing environment variables to the server application:
* `REDIS_HOST` - HTTP host of Redis. Defaults to redis.
* `REDIS_PORT` - HTTP port of Redis. Defaults to 6379.

## Demo
### Startup
The application can be started by issuing the following command from the root directory of the project:
```bash
docker-compose up
```
The command will start up a Redis instance (port 6379), the server for the API and WebSocket (port 8080), and a client application (port 8081) which can send and subscribe to messages.

If the services should be exposed on different ports the `docker-compose.yaml` file should be modified accordingly. 

### Client application
The composition contains a default client that can be reached at port 8081 by default.
For date formatting [luxon](https://github.com/moment/luxon) is used.
