document.addEventListener('DOMContentLoaded', function () {
    // Element references
    const loginForm = document.getElementById('login-form'); // Reference to the login form
    const registerForm = document.getElementById('register-form'); // Reference to the registration form
    const uploadForm = document.getElementById('upload-form'); // Reference to the upload form
    const loginContainer = document.getElementById('login-container'); // Reference to the login container div
    const registerContainer = document.getElementById('register-container'); // Reference to the registration container div
    const dashboard = document.getElementById('dashboard'); // Reference to the dashboard container div
    const fileList = document.getElementById('file-list'); // Reference to the file list ul element
    const folderList = document.getElementById('folder-list'); // Reference to the folder list ul element
    const logoutBtn = document.getElementById('logout'); // Reference to the logout button
    const createFolderButton = document.getElementById('create-folder'); // Reference to the "Create Folder" button
    let currentFolderId = null; // Stores the ID of the currently selected folder

    console.log("Page loaded. Setting up event listeners...");

    // Show the registration form and hide the login form
    document.getElementById('show-register')?.addEventListener('click', function () {
        loginContainer?.classList.add('hidden'); // Hide the login container
        registerContainer?.classList.remove('hidden'); // Show the registration container
    });

    // Show the login form and hide the registration form
    document.getElementById('show-login')?.addEventListener('click', function () {
        registerContainer?.classList.add('hidden'); // Hide the registration container
        loginContainer?.classList.remove('hidden'); // Show the login container
    });

    // Handle login form submission
    loginForm?.addEventListener('submit', function (e) {
        e.preventDefault(); // Prevent the default form submission
        const username = document.getElementById('username').value; // Get the username value
        const password = document.getElementById('password').value; // Get the password value

        // Check if both username and password are provided
        if (username && password) {
            // Send a POST request to the server for login
            fetch('/perform_login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`, // Encode form data
            })
            .then(response => {
                if (response.ok) {
                    console.log("Login successful");
                    window.location.href = '/dashboard'; // Redirect to the dashboard after successful login
                } else {
                    alert('Invalid login credentials'); // Show an error if login fails
                }
            })
            .catch(() => {
                alert('An error occurred during login.'); // Show a general error if the request fails
            });
        } else {
            alert('Please enter both username and password'); // Alert if either field is empty
        }
    });

    // Handle registration form submission
    registerForm?.addEventListener('submit', function (e) {
        e.preventDefault(); // Prevent the default form submission
        const username = document.getElementById('reg-username').value; // Get the registration username
        const password = document.getElementById('reg-password').value; // Get the registration password

        // Send a POST request to the server to register a new user
        fetch('/api/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }), // Send username and password as JSON
        })
            .then(response => {
                if (response.ok) {
                    alert('Registration successful!');
                    registerContainer?.classList.add('hidden'); // Hide the registration form
                    loginContainer?.classList.remove('hidden'); // Show the login form
                } else {
                    return response.json().then(error => {
                        alert('Registration failed: ' + error.message); // Show an error if registration fails
                    });
                }
            })
            .catch(() => {
                alert('An error occurred during registration.'); // Show a general error if the request fails
            });
    });

    // Handle file upload form submission
    uploadForm?.addEventListener('submit', function (e) {
        e.preventDefault(); // Prevent the default form submission
        const file = document.getElementById('file-upload').files[0]; // Get the selected file
        const fileName = document.getElementById('file-name').value; // Get the provided file name

        // Ensure that a file, name, and folder are selected
        if (!file || !fileName || !currentFolderId) {
            alert('Please select a file, provide a name, and select a folder.');
            return;
        }

        const formData = new FormData();
        formData.append('file', file); // Append the file to the FormData
        formData.append('fileName', fileName); // Append the file name to the FormData
        formData.append('folderId', currentFolderId); // Append the folder ID to the FormData

        // Send a POST request to upload the file
        fetch('/api/files/upload', {
            method: 'POST',
            body: formData, // Send the FormData
        })
            .then(response => {
                if (response.ok) {
                    loadFiles(currentFolderId);  // Reload the files after successful upload
                    alert('File uploaded successfully!');
                } else {
                    alert('File upload failed.'); // Show an error if the upload fails
                }
            })
            .catch(() => {
                alert('An error occurred during file upload.'); // Show a general error if the request fails
            });
    });

    // Handle logout button click
    logoutBtn?.addEventListener('click', function () {
        fetch('/logout', { method: 'POST' }) // Send a POST request to logout
            .then(() => {
                window.location.href = '/login'; // Redirect to the login page after logout
            })
            .catch(() => alert('An error occurred during logout.')); // Show an error if the logout fails
    });

    // Load the folders from the server
    function loadFolders() {
        fetch('/api/folders') // Send a GET request to retrieve folders
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok " + response.statusText); // Handle HTTP errors
                }
                return response.json();
            })
            .then(folders => {
                if (!folderList) return;

                folderList.innerHTML = '';  // Clear the current folder list

                if (folders.length === 0) {
                    folderList.innerHTML = '<li>No folders found</li>'; // Display a message if no folders are found
                } else {
                    folders.forEach(folder => {
                        const folderItem = document.createElement('li');
                        folderItem.innerHTML = `<i class="fa fa-folder"></i> ${folder.name}`; // Display each folder
                        folderItem.addEventListener('click', () => {
                            currentFolderId = folder.id; // Set the current folder ID
                            loadFiles(folder.id); // Load files for the selected folder
                        });
                        folderList.appendChild(folderItem); // Add the folder to the folder list
                    });
                }
            })
            .catch(() => alert('An error occurred while loading folders.')); // Show an error if the request fails
    }

    // Load the files from the server for the current folder
    function loadFiles(folderId) {
        fetch(`/api/files?folderId=${folderId}`) // Send a GET request to retrieve files for the specified folder
            .then(response => response.json())
            .then(files => {
                fileList.innerHTML = ''; // Clear the current file list
                if (files.length === 0) {
                    fileList.innerHTML = '<li>No files found</li>'; // Display a message if no files are found
                } else {
                    files.forEach(file => {
                        const fileItem = document.createElement('li');
                        fileItem.innerHTML = `<a href="/api/files/download/${file.id}">${file.name}</a>`; // Display each file
                        fileList.appendChild(fileItem); // Add the file to the file list
                    });
                }
            })
            .catch(() => alert('An error occurred while loading files.')); // Show an error if the request fails
    }

    // Create a new folder
createFolderButton?.addEventListener('click', function () {
    const folderName = prompt("Enter folder name:");
    if (!folderName) return;

    fetch('/api/folders/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: folderName }), // <-- Potential issue here TODO
    })
    .then(response => {
        if (response.ok) {
            loadFolders();
        } else {
            alert('Failed to create folder');
        }
    })
    .catch(() => alert('An error occurred while creating the folder.'));
});


    // Initial load of folders if the user is on the dashboard
    if (dashboard && !dashboard.classList.contains('hidden')) {
        loadFolders(); // Load folders if the dashboard is visible
    }

    // Redirect to the dashboard after a delay on the welcome page
    if (document.querySelector('.container h1 span')) {
        setTimeout(() => {
            window.location.href = '/dashboard'; // Redirect to the dashboard after 2 seconds
        }, 2000);
    }

    // Redirect to dashboard after a delay on the welcome page
    if (document.querySelector('.container h1 span')) {
        setTimeout(() => {
            window.location.href = '/dashboard';
        }, 2000);
    }
});
