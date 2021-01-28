var username;
var id;

window.setInterval(function(){
    refresh();
  }, 5000);

console.log('aqui');

//funçao que executa quando o body da pagina html e carregado
function onload() {
    console.log("onload working");
    getUsers();
    getMensagens();

}

//apos o utilizador se registar remove o texto de aviso e o botao de registo e faz aparecer o input para enviar mensagens e o botao para enviar
function display() {
    //var x = document.getElementById("sendmsg");
    //var y = document.getElementById("sendButton");
    var z = document.getElementById("labelRegistar");
    var w = document.getElementById("registar");
    if (z.style.display === "none") {
        z.style.display = "block";
    } else {
        z.style.display = "none";
    }
    if (w.style.display === "none") {
        w.style.display = "block";
    } else {
        w.style.display = "none";
    }
    document.getElementById("rowMsg").innerHTML = `<div class="col-sm-10">
    <input type="text" id="sendmsg" class="form-control">
  </div>
  <div class="col-sm-2">
    <button name="enviar" id="sendButton" type="button" class="btn btn-primary" onclick="sendMensagem()">enviar</button>
  </div>`;
}

//envia o username para o servidor para este ficar registo
function setUser() {
    console.log("javascript");
    var data = {};
    if (document.getElementById("username").value != "") {
        username = document.getElementById("username").value;
        document.getElementById("control").innerHTML = ``;
        data.username = document.getElementById("username").value;
        fetch("/postUser", {
            headers: { 'Content-Type': 'application/json' },
            method: 'POST',
            body: JSON.stringify(data)
        }).then(function (result) {
            return result.text();
        }).then(function (data) {
            if (data.includes("sucess")) {
                console.log(data);
                display();
                setId(data);
                getUsers();
                userSettings(username);
            }
        });
    } else {
        document.getElementById("control").innerHTML = `<p>Para se registar tem que inserir um username</p>`;
    }
}

// function getUser() {
//     fetch("/getUser", {
//         method: 'GET',
//     }).then(function (result){
//         return result.text();
//     }).then(function(data){
//         for(var i = 0; i < data.length; i++){
//             console.log(data[i]);
//         }
//     });
// }

//regista o id do utlizador em questao neste ficheiro
function setId(data) {
    id = data.substring(6);
}

//funçao que vai buscar os utilizadores registados
function getUsers() {
    fetch("/getUsers", {
        method: 'GET',
    }).then(function (result) {
        return result.text();
    }).then(function (data) {
        var newstring = "";
        for (var i = 0; i < data.length; i++) {
            if (data[i] !== "[" && data[i] !== "]") {
                newstring += data[i];
            }
        }
        setPresencas(newstring.split(", "));
    });
}

function getMensagens() {
    document.getElementById("cardbody").innerHTML = "";
    fetch("/getMensagens", {
        method: 'GET',
    }).then(function (result) {
        return result.text();
    }).then(function (data) {
        console.log("lista de mensagens: " + data);
        var newstring = "";
        if(data != "[]"){
        for (var i = 0; i < data.length; i++) {
            if (data[i] !== "[" && data[i] !== "]") {
                newstring += data[i];
            }
        }
        var separ = newstring.split(", ");
        for (var d = 0; d < separ.length; d++) {
            var final = separ[d].split("/");
            var localId = final[0];
            var localCorpo  = final[1];
            var localHora  = final[2];
            var localUsername = final[3];
            console.log("locald " + localId);
            console.log("lcoal corpo " + localCorpo)
            console.log("local hora " + localHora);
            console.log("local user " + localUsername);
            if(localId == id){
                document.getElementById("cardbody").innerHTML += `<div class="container bg-primary">
                <span class="time-left text-light">${localHora} ${localUsername}</span>
                <p class="text-light">${localCorpo}</p>
              </div>`
            }else{
                document.getElementById("cardbody").innerHTML += `<div class="container bg-light">
                <span class="time-left">${localHora} ${localUsername}</span>
                <p>${localCorpo}</p>
              </div>`
            }

        }
    }
    });
}

//faz os utilizadores registados aparecerem no card "lista de presenças"
function setPresencas(users) {
    document.getElementById("listapresencas").innerHTML = "";
    for (var i = 0; i < users.length; i++) {
        document.getElementById("listapresencas").innerHTML += `<p>${users[i]}</p>`;
    }
}

function sendMensagem() {
    var data = {};
    if (document.getElementById("sendmsg").value != "") {
        data.idUser =id;
        data.mensagem = "/" + document.getElementById("sendmsg").value;
        data.username = "/" + username;
        var d = new Date();
        data.hora = "/" + d.getHours() + ":" + d.getMinutes();
        fetch("/postMensagem", {
            headers: { 'Content-Type': 'application/json' },
            method: 'POST',
            body: JSON.stringify(data)
        }).then(function (result) {
            return result.text();
        }).then(function (data) {
            if (data === "success") {
                console.log(data);
                document.getElementById("sendmsg").value = "";
                //getMensagens();
            }
        });
    }
}

function refresh(){
    getUsers();
    getMensagens();
}

function userSettings(username){
    document.getElementById("usersettings").innerHTML = `
    <span class="mr-2 d-none d-lg-inline text-gray-600 small">${username}</span>`
}