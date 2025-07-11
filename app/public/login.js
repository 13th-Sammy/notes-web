import {post} from './api.js';

const okBtn=document.getElementById("ok");
let submitted=false;

async function login(event) {
    event.preventDefault();
    if(submitted) return;
    submitted=true;
    setTimeout(() => submitted = false, 1000);

    const username=document.getElementById("uname").value.trim();
    const password=document.getElementById("pword").value.trim();

    if(!username || !password) {
        alert("Username and Password cannot be empty");
        return;
    }

    try {
        const response = await post("/login", {username, password});
        if(response.success) {
            localStorage.setItem("username", username);
            window.location.href="notes.html";
        }
        else {
            alert(response.message || "Login Failed");
        }
    } catch (e) {
        alert("Error: "+e?.message||e);
    }
}

okBtn.addEventListener("click", login);
okBtn.addEventListener("touchstart", login);