var stompClient = null;

$(document).ready(function() {
    console.log("Index page is ready");

    connect();

});

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/task-status', function (message) {
            showNotification(JSON.parse(message.body).content);
        });
    });
}


function showNotification(message) {
    $("#messages").append("<tr><td style='color: black;'>" + message + "</td></tr>");
    console.log("Notification received: " + message);
}
