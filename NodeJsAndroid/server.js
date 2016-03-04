var io = require('socket.io');
var server = io.listen(7070);

var macbook_socket = undefined;

var user;

server.sockets.on('connection', function(socket) {

    socket.on("chat", function(data) {


        console.log("received message " + data.name1);
        user = data.name1;

        socket.emit("helo", {msg: "welcome " + user});
    });

});



