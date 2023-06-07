const http = require('http');

const server = http.createServer().listen(6060);

server.on('request', (req, res) => {
    console.log('Your request arrived.');
    res.write("HostName: " + process.env.HOSTNAME + "\n");
    res.end();
});

server.on('connection', (socket) => {
    console.log("Your Connected.");
});
