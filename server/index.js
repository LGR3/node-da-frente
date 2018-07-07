const WebSocketServer = require('websocket').server;
const http = require('http');
const publish = require('./publisher')
const subscribe = require('./subscriber').subscribe
const utm = require('utm')
const server = http.createServer(function (request, response) {
    // process HTTP request. Since we're writing just WebSockets
    // server we don't have to implement anything.
});

server.listen(8080, function () { });

const wsServer = new WebSocketServer({
    httpServer: server
});

wsServer.on('request', function (request) {
    var connection = request.accept(null, request.origin);
    connection.on('message', function (message) {
        console.log(message)
        message = message.utf8Data
        publish('bus', message) //call 3333, after getAllBus return [ '3333' ]

        subscribe(message, (_row) => {
            console.log(_row)
            const row = _row.split(';');
            const _utm = utm.toLatLon(row[6], row[7], 25, 'l');
            row[6] = _utm.latitude;
            row[7] = _utm.longitude;
            connection.send(row.join(';'));
        })
    });
});


