import {post} from './api.js'

const username=localStorage.getItem("username");
if(!username) {
    window.location.href="login.html"
}

function logOut() {
    localStorage.removeItem("username");
    window.location.href="login.html";
}

const logOutBtn=document.getElementById("logOut");
logOutBtn.addEventListener("click", logOut);
logOutBtn.addEventListener("touchstart", logOut);