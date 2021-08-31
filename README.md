# Messaging Application
A simple messaging application that lets you send and read messages from Redis. The sent messages are also broadcasted via WebSockets. 

## Usage
### Startup
The application can be started by issuing:
```bash
docker-compose up
```
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
Clients can subscribe to `ws://{HOST}:{PORT}/message` endpoint to recieve messages.

## Configuration Options

The following configuration options should be set by providing environment variables to the application:
* `REDIS_HOST` - HTTP host of Redis. Defaults to redis.
* `REDIS_PORT` - HTTP port of Redis. Defaults to 6379.
