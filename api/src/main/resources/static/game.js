// Dynamically uses whatever host/IP the browser used to load the page
const wsProtocol = window.location.protocol === 'https:' ? 'wss://' : 'ws://';
const brokerUrl = wsProtocol + window.location.host + '/gs-guide-websocket';
const stompClient = new StompJs.Client({
    brokerURL: brokerUrl,
});

const url = window.location.protocol + '//' + window.location.host + '/games';

function createGame() {
    // Define the data to send
    const payload = { 
        playerId: $("#playerId").val(),
    };

    $.ajax({
	url: url,
        type: 'POST',
        contentType: 'application/json; charset=utf-8', // Tells the server you are sending JSON
        dataType: 'json',                               // Tells jQuery you expect JSON back
        data: JSON.stringify(payload),
        success: function(response) {
            connect();
            console.log("Success:", response);
            game = response;
            showGame(game);
            $("#gameId").val(game.id);
        },
        error: function(xhr, status, error) {
            console.error("Error:", error);
        }
    });
}

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    topic = '/topic/messages/game/' +  $("#gameId").val() + '/player/' + $("#playerId").val();
    stompClient.subscribe(topic, (game) => {
        showGame(JSON.parse(game.body));
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMove() {
    destination = '/app/game/' + $("#gameId").val() + '/player/' + $("#playerId").val();
    stompClient.publish({
        destination: destination,
        body: JSON.stringify({'gameId': $("#gameId").val()})
    });
}

function showGame(game) {
    console.log('Game:', game);
    $("#game").append("<tr><td>" + JSON.stringify(game) + "</td></tr>");
    
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#send" ).click(() => sendMove());
    $( "#createGame" ).click(() => createGame());
});
