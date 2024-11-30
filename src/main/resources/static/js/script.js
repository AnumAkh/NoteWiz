const hamBurger = document.querySelector(".toggle-btn");

hamBurger.addEventListener("click", function () {
  document.querySelector("#sidebar").classList.toggle("expand");
});

document.addEventListener('DOMContentLoaded', () => {
    const attachButton = document.getElementById('attachButton');
    const attachOptions = document.getElementById('attachOptions');

    if (attachButton && attachOptions) {
        attachButton.addEventListener('click', () => {
            attachOptions.style.display = attachOptions.style.display === 'none' ? 'block' : 'none';
        });
    } else {
        console.error("Element(s) not found: attachButton or attachOptions");
    }
});

document.getElementById('addNoteBtn').addEventListener('click', function() {
    window.location.href = '/addNote'; // Redirect to the new page with the form
});

let quill;
document.addEventListener("DOMContentLoaded", function () {
    quill = new Quill("#editor", {
        modules: {
            toolbar: [
                [{ header: [1, 2, false] }],
                ["bold", "italic", "underline"],
                ["image", "code-block"],
            ],
        },
        placeholder: "Compose your note...",
        theme: "snow",
    });
    console.log("Quill initialized:", quill);
});

// Save Note Button Click
document.getElementById("saveAddNoteButton").addEventListener("click", async () => {
    console.log("Save Note Button Clicked!");
    const noteTitle = document.getElementById("noteTitle").value; // Get note title
    const noteContent = quill.root.innerHTML; // Get content from Quill editor

    if (!noteTitle || !noteContent) {
        alert("Please fill in the note title and content.");
        return;
    }

    // Prepare the note data
    const noteData = {
        noteTitle: noteTitle,
        content: noteContent,
    };

    console.log("Note Data:", { title: noteTitle, content: noteContent });

    // Send data to the backend
    try {
        const response = await fetch("/notes/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json", // Set Content-Type to JSON
            },
            body: JSON.stringify(noteData),
        });

        if (response.ok) {
            alert("Note saved successfully!");
            window.location.href = "/home"; // Redirect to home or refresh
        } else {
            alert("Failed to save note.");
        }
    } catch (error) {
        console.error("Error saving note:", error);
        alert("An error occurred while saving the note.");
    }
});


function deleteNote(noteId) {
if (confirm("Are you sure you want to delete this note?")) {
  fetch('/notes/delete/' + noteId, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      'JSESSIONID': getCookie('JSESSIONID')
    }
  })
  .then(response => {
    if (response.ok) {
      alert('Note deleted successfully!');
      location.reload();
    } else {
      alert('Failed to delete the note.');
    }
  })
  .catch(error => {
    console.error('Error deleting the note:', error);
    alert('An error occurred while deleting the note.');
  });
}
}

function getCookie(name) {
const value = `; ${document.cookie}`;
const parts = value.split(`; ${name}=`);
if (parts.length === 2) return parts.pop().split(';').shift();
}

function editNote(noteId) {
    window.location.href = `/notes/editNote?noteId=${noteId}`;
}

