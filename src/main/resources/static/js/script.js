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

function editNote(noteId) {
    window.location.href = `/notes/editNote?noteId=${noteId}`;
}

function viewNote(noteId) {
    window.location.href = `/notes/viewNote?noteId=${noteId}`;
}

function showTagInput() {
    document.getElementById('tagInputContainer').style.display = 'block';
  }

async function submitTag(noteId) {
const tagName = document.getElementById('tagInput').value;

if (!tagName) {
    alert("Please enter a tag name.");
    return;
}

try {
    const response = await fetch('/notes/addTag', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ tagName, noteId }),
    });

    if (response.ok) {
        alert("Tag added successfully!");
        location.reload(); // Reload the page to show the new tag
    } else {
        alert("Failed to add tag.");
    }
} catch (error) {
    console.error("Error adding tag:", error);
    alert("An error occurred while adding the tag.");
}
}

// Function to open the modal and load notebooks
function openAddToNotebookModal(noteId) {
  document.getElementById("noteId").value = noteId;
  // First, fetch the notebooks (this can be done via an API call)
  fetch('/notebooks/userNotebooks') // Ensure this URL is correct
    .then(response => response.json())
    .then(notebooks => {
      const notebookSelect = document.getElementById('notebookSelect');
      notebookSelect.innerHTML = '';  // Clear any existing options

      // Populate the dropdown with notebooks
      notebooks.forEach(notebook => {
        const option = document.createElement('option');
        option.value = notebook.notebookId;  // Assuming 'id' is the notebook ID
        option.textContent = notebook.notebookTitle;  // Assuming 'title' is the notebook title
        notebookSelect.appendChild(option);
      });

      // Store the noteId to use when saving the note to the notebook
      document.getElementById('saveAddNoteButton').onclick = function() {
        const notebookId = notebookSelect.value;
        if (notebookId) {
          addNoteToNotebook(noteId, notebookId);
        }
      };

      // Show the modal
      var addToNotebookModal = new bootstrap.Modal(document.getElementById('addToNotebookModal'));
      addToNotebookModal.show();
    })
    .catch(error => {
      console.error('Error fetching notebooks:', error);
    });
}

// Function to add the note to the selected notebook
document.getElementById("saveAddNoteButton").addEventListener("click", async () => {
  const notebookId = document.getElementById("notebookSelect").value;
  const noteId = document.getElementById("noteId").value; // Pass the note ID dynamically

  if (!notebookId) {
    alert("Please select a notebook!");
    return;
  }

  try {
    const response = await fetch('/notebooks/addToNotebook', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ noteId: noteId, notebookId: notebookId }),
    });

    if (response.ok) {
      alert("Note successfully added to the notebook!");
      location.reload();
    } else {
      alert("Failed to add note to notebook. Please try again.");
    }
  } catch (error) {
    console.error("Error adding note to notebook:", error);
    alert("An error occurred. Please try again.");
  }
});


// Add an event listener for the forum link
document.getElementById('forum-link').addEventListener('click', function () {
    window.location.href = '/forum'; // Redirect to the forum page
});


