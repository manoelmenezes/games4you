const stompClient = new StompJs.Client({
    brokerURL: 'ws://34.44.12.137:8080/gs-guide-websocket'
});



function createGame() {
    // Define the data to send
    const payload = { 
        playerId: $("#playerId").val(),
    };

    $.ajax({
        url: 'http://34.44.12.137:8080/games',
        type: 'POST',
        contentType: 'application/json; charset=utf-8', // Tells the server you are sending JSON
        dataType: 'json',                               // Tells jQuery you expect JSON back
        data: JSON.stringify(payload),
        success: function(response) {
            console.log("Success:", response);
            game = JSON.parse(response);
            showGame(game);
            $("#gameId").val(game.id);
            console.log("Game:", response);
            console.log("Status:", status);
            connect();
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
    $("#game").html("");
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
    destination = '/game/' + $("#gameId").val() + '/player/' + $("#playerId").val();
    stompClient.publish({
        destination: destination,
        body: JSON.stringify({'gameId': $("#gameId").val()})
    });
}

function showGame(game) {
    $("#game").append("<tr><td>" + game + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#send" ).click(() => sendMove());
    $( "#createGame" ).click(() => createGame());
});