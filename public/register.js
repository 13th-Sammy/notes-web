import {post} from './api.js';

const okBtn=document.getElementById("ok");
let submitted=false;

async function register(event) {
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
        const response=await post("/register", {username, password});
        if(response.success) {
            window.location.href="registeredSuccess.html";
        }
        else {
            alert(response.message || "Registration failed");
        }
    } catch (e) {
        alert("Error: "+e?.message||e);
    }
}

["click, touchstart"].forEach(eventType => okBtn.addEventListener(eventType, register));