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

async function getNotes() {
    try {
        const response=await post("/getNotes", {username});

        if(response.success && response.notes) {
            response.notes.forEach(note => {
                renderNoteCard(note.title, note.content)
            });
        }
        else {
            alert(response.message || "Failed to load notes");
        }
    } catch (e) {
        alert("Error: "+e?.message||e);
    }
}

async function addNote(title, content) {
    try {
        const response=await post("/addNote", {username, title, content});

        if(response.success) {
            renderNoteCard(title, content);
        }
        else {
            alert(response.message || "Failed to add note");
        }
    } catch (e) {
        alert("Error: "+e?.message||e);
    }
}

function logOut() {
    localStorage.removeItem("username");
    window.location.href="login.html";
}

getNotes();

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