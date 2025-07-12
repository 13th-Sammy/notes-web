import {post} from './api.js'

const username=localStorage.getItem("username");
if(!username) {
    window.location.href="index.html"
}

const logOutBtn=document.getElementById("logOut");
const noteOverlay=document.getElementById("noteOverlay");
const notePanel=document.getElementById("notePanel");
const addNoteBtn=document.getElementById("addNote");
const delNoteBtn=document.getElementById("delNote");
const saveNoteBtn=document.getElementById("saveNote");
const notesContainer=document.getElementById("notesContainer");
const closePanelBtn=document.getElementById("closePanel");

let isEditing=false;
let origTitle=null;
let deleteMode=false;

function showNotePanel() {
    document.getElementById("noteTitle").value="";
    document.getElementById("noteContent").value="";
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

    card.dataset.title=title;

    const titleElem=document.createElement("h3");
    titleElem.textContent=title;

    const contentElem=document.createElement("p");
    contentElem.textContent=content;

    card.appendChild(titleElem);
    card.appendChild(contentElem);

    card.addEventListener("click", function() {
        isEditing=true;
        origTitle=title;

        notePanel.style.display="block";
        document.getElementById("noteTitle").value=title;
        document.getElementById("noteContent").value=content;
    });

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

function clearNotes() {
    notesContainer.innerHTML="";
}

function preventBackAfterLogout() {
    history.pushState(null, "", location.href);
    window.onpopstate=function () {
        history.go(1);
    };
}

function logOut() {
    localStorage.clear();
    preventBackAfterLogout();
    window.location.href="index.html";
}

getNotes();

addNoteBtn.addEventListener("click", showNotePanel);

closePanelBtn.addEventListener("click", hideNotePanel);

saveNoteBtn.addEventListener("click", async function() {
    const title=document.getElementById("noteTitle").value.trim();
    const content=document.getElementById("noteContent").value.trim();

    if(!title || !content) {
        alert("Title and Content cannot be empty");
        return;
    }

    if(isEditing) {
        try {
            const response=await post("/updateNote", {username, oldTitle: origTitle, newTitle: title, newContent: content});
            if(response.success) {
                hideNotePanel();
                isEditing=false;
                origTitle=null;
                clearNotes();
                getNotes();
            }
            else {
                alert(response.message || "Update Note Failed");
            }
        } catch (e) {
            alert("Error: "+e?.message||e);
        }
    } 
    else {
        addNote(title, content);
        hideNotePanel();
    }
});

delNoteBtn.addEventListener("click", function() {
    deleteMode=!deleteMode;

    const cards=document.querySelectorAll(".note-card");
    cards.forEach(card => {
        let del=card.querySelector(".delete-cross");
        if(deleteMode && !del) {
            const cross=document.createElement("span");
            cross.className="delete-cross";
            cross.textContent="❌";
            cross.style.position="absolute";
            cross.style.top="5px";
            cross.style.right="10px";
            cross.style.cursor="pointer";

            cross.addEventListener("click", async (e) => {
                e.stopPropagation();

                const title=card.dataset.title;
                try{
                    const response=await post("/deleteNote", {username, title});
                    if(response.success) {
                        card.remove();
                    }
                    else {
                        alert(response.message || "Delete Note Failed");
                    }
                } catch (e) {
                    alert("Error: "+e?.message||e);   
                }
            });
            card.appendChild(cross);
        }
        else {
            if (!deleteMode && del) del.remove();
        }
    });
});

logOutBtn.addEventListener("click", logOut);