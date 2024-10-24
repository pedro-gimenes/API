const url = "http//localhost:8080/task/user/1";

function HideLoader() {
    document.getElementById("loading").style.display = "none";
}

function show(tasks) {
    let tab = `<thead>
            <th scope="col">#</th>
            <th scope="col">Description</th>
        </thead>`;

    for (let task of tasks) {
        tab += `
            <tr>
                <td scope="row">${task.id}</td>
                <td>${task.description}</td>
            </tr>
        `;
    }

    document.getElementById("tasks").innerHTML = tab;
}

async function getTasks() {
    let Key = "Authorization";
    const response = await fetch(tasksEndPoint, {
        method: "GET",
        headers: new Headers({
            Authorization: localStorage.getItem(Key),
        })
    });

    var data = await response.json();
    console.log(data);
    if(response) HideLoader();
    show(data);
    }
    document.addEventListener("DOMContentLoaded", function (event) {
        if(!localStorage.getItem("Authorization"))
            window.location = "/view/login.html";
    });

    getTasks();