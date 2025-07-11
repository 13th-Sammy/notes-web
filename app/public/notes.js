import {post} from './api.js'

const username=localStorage.getItem("username");
if(!username) {
    window.location.href="login.html"
}

const logOutBtn=document.getElementById("logOut");
const noteOverlay=document.getElementById("noteOverlay");
const notePanel=document.getElementById("notePanel");
const addNoteBtn=document.getElementById("addNote");
const saveNoteBtn=document.getElementById("saveNote");
const notesContainer=document.getElementById("notesContainer");

function showNotePanel() {
    notePanel.style.display="block";
    noteOverlay.style.display="block";
}

function hideNotePanel() {
    notePanel.style.display="none";
    noteOverlay.style.display="none";
}

function addNote(title, content) {

}

function renderNoteCard(title, content) {
    const card=document.createElement("div");
    card.className="note-card";

    const titleElem=document.createElement("h3");
    titleElem.textContent=title;

    const contentElem=document.createElement("p");
    contentElem.textContent=content;

    card.appendChild(titleElem);
    card.appendChild(contentElem);
    notesContainer.appendChild(card);
}

function logOut() {
    localStorage.removeItem("username");
    window.location.href="login.html";
}

addNoteBtn.addEventListener("click", showNotePanel);
addNoteBtn.addEventListener("touchstart", showNotePanel);

saveNoteBtn.addEventListener("click", function() {
    const title=document.getElementById("noteTitle").value.trim();
    const content=document.getElementById("noteContent").value.trim();
    addNote(title, content);
    hideNotePanel();
});
saveNoteBtn.addEventListener("touchstart", function() {
    const title=document.getElementById("noteTitle").value.trim();
    const content=document.getElementById("noteContent").value.trim();
    addNote(title, content);
    hideNotePanel();
});

logOutBtn.addEventListener("click", logOut);
logOutBtn.addEventListener("touchstart", logOut);