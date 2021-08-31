var ws = null;
var url = "ws://localhost:8080/message";

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('send').disabled = !connected;
}

function connect() {
    ws = new WebSocket(url);
    ws.onopen = function() {
        setConnected(true);
        log('Info: Connection Established.');
    };

    ws.onmessage = function(event) {
        log(event.data);
    };

    ws.onclose = function(event) {
        setConnected(false);
        log('Info: Closing Connection.');
    };
}

function disconnect() {
    if (ws != null) {
        ws.close();
        ws = null;
    }
    setConnected(false);
}

function log(message) {
    var console = document.getElementById('messages');
    var p = document.createElement('p');
    p.appendChild(document.createTextNode(message));
    console.appendChild(p);
}

async function postMessage(event) {
    event.preventDefault();
    var DateTime = luxon.DateTime;
    var currentDate = DateTime.now().toFormat("yyyy-MM-dd HH:mm:ssZZZ");
    console.log("Current date is: " + currentDate);

    var form = document.querySelector("#formElem");
    data = {
      content : form.querySelector('textarea[name="content"]').value,
      timestamp : currentDate,
    }

    let response = await fetch('http://localhost:8080/messages', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
    })

    let text = await response.text();
    console.log("Response message was: " + text);
}