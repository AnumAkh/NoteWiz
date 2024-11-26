
// Sidebar toggle functionality
const hamBurger = document.querySelector(".toggle-btn");
if (hamBurger) {
    hamBurger.addEventListener("click", function () {
        const sidebar = document.querySelector("#sidebar");
        if (sidebar) {
            sidebar.classList.toggle("expand");
        }
    });
}

// Attach button functionality
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

// Add Note button functionality
document.getElementById('addNoteBtn').addEventListener('click', function() {
    window.location.href = '/addNote'; // Redirect to the new page with the form
});


// Quill editor initialization
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

const saveNoteBtn = document.getElementById("saveNoteBtn");
if (saveNoteBtn) {
    saveNoteBtn.addEventListener("click", async () => {
        console.log("Save Note Button Clicked!");
        const noteTitle = document.getElementById("noteTitle").value;
        const noteContent = quill?.root?.innerHTML;

        if (!noteTitle || !noteContent) {
            alert("Please fill in the note title and content.");
            return;
        }

        try {
            const response = await fetch("/notes/add", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ noteTitle, content: noteContent }),
            });

            if (response.ok) {
                alert("Note saved successfully!");
                window.location.href = "/home";
            } else {
                alert("Failed to save note.");
            }
        } catch (error) {
            console.error("Error saving note:", error);
            alert("An error occurred while saving the note.");
        }
    });
}

// Function to handle the Save Notebook button click
document.getElementById("saveNotebookButton").addEventListener("click", async () => {
  const title = document.getElementById("notebookTitle").value.trim();
  const description = document.getElementById("description").value.trim();

  if (!title) {
    alert("Notebook title is required!");
    return;
  }

  const notebookData = {
    notebookTitle: title,
    description: description,
    createdOn: new Date().toISOString(),
  };

  try {
    const response = await fetch('/create-notebooks', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(notebookData),
    });

    if (response.ok) {
      // Show success message and close modal
      document.getElementById("successMessage").style.display = "block";
      setTimeout(() => location.reload(), 2000);  // Refresh page to show the new notebook
      document.querySelector('#createNotebookModal .btn-close').click(); // Close the modal
    } else {
      alert("Failed to create notebook. Please try again.");
    }
  } catch (error) {
    console.error("Error creating notebook:", error);
    alert("An error occurred. Please try again.");
  }
});
