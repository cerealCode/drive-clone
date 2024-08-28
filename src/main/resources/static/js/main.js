document.addEventListener('DOMContentLoaded', function () {
    // Element references
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const uploadForm = document.getElementById('upload-form');
    const loginContainer = document.getElementById('login-container');
    const registerContainer = document.getElementById('register-container');
    const dashboard = document.getElementById('dashboard');
    const fileList = document.getElementById('file-list');
    const folderList = document.getElementById('folder-list');
    const logoutBtn = document.getElementById('logout');
    const uploadButton = document.getElementById('upload-file');
    const createFolderButton = document.getElementById('create-folder');
    let currentFolderId = null;

    // Log page load
    console.log("Page loaded. Setting up event listeners...");

    // Show register form
    const showRegisterLink = document.getElementById('show-register');
    if (showRegisterLink) {
        showRegisterLink.addEventListener('click', function () {
            console.log("Show register form clicked");
            loginContainer?.classList.add('hidden');
            registerContainer?.classList.remove('hidden');
        });
    }

    // Show login form
    const showLoginLink = document.getElementById('show-login');
    if (showLoginLink) {
        showLoginLink.addEventListener('click', function () {
            console.log("Show login form clicked");
            registerContainer?.classList.add('hidden');
            loginContainer?.classList.remove('hidden');
        });
    }

    // Login form submission
    if (loginForm) {
        loginForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            console.log("Login attempt with username:", username);

            if (username && password) {
                fetch('/perform_login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`,
                })
                    .then(response => {
                        if (response.ok) {
                            console.log("Login successful");
                            loginContainer?.classList.add('hidden');
                            dashboard?.classList.remove('hidden');
                            loadFolders(); // Load folders and files after successful login
                        } else {
                            console.error("Login failed, status:", response.status);
                            alert('Invalid login credentials');
                        }
                    })
                    .catch(error => {
                        console.error("Error during login:", error);
                        alert('An error occurred during login.');
                    });
            } else {
                console.log("Login failed: Missing username or password");
                alert('Please enter both username and password');
            }
        });
    }

    // Register form submission
    if (registerForm) {
        registerForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const username = document.getElementById('reg-username').value;
            const password = document.getElementById('reg-password').value;

            console.log("Register attempt with username:", username);

            fetch('/api/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            })
                .then(response => {
                    if (response.ok) {
                        console.log("Registration successful for user:", username);
                        alert('Registration successful!');
                        registerContainer?.classList.add('hidden');
                        loginContainer?.classList.remove('hidden');
                    } else {
                        return response.json().then(error => {
                            console.error("Registration failed:", error.message);
                            alert('Registration failed: ' + error.message);
                        });
                    }
                })
                .catch(error => {
                    console.error("Error during registration:", error);
                    alert('Registration failed: ' + error.message);
                });
        });
    }

    // Upload form submission
    if (uploadForm) {
        uploadForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const file = document.getElementById('file-upload').files[0];
            const fileName = document.getElementById('file-name').value;

            if (!file || !fileName || !currentFolderId) {
                alert('Please select a file, provide a name, and select a folder.');
                return;
            }

            console.log("File upload attempt with file:", fileName);

            const formData = new FormData();
            formData.append('file', file);
            formData.append('fileName', fileName);
            formData.append('folderId', currentFolderId);

            fetch('/api/files/upload', {
                method: 'POST',
                body: formData,
            })
                .then(response => {
                    if (response.ok) {
                        console.log("File uploaded successfully");
                        loadFiles(currentFolderId);  // Reload files to include the newly uploaded one
                        alert('File uploaded successfully!');
                    } else {
                        console.error("File upload failed, status:", response.status);
                        alert('File upload failed.');
                    }
                })
                .catch(error => {
                    console.error("Error during file upload:", error);
                    alert('An error occurred during file upload.');
                });
        });
    }

    // Logout
    if (logoutBtn) {
        logoutBtn.addEventListener('click', function () {
            console.log("Logout clicked");
            fetch('/logout', { method: 'POST' })
                .then(() => {
                    dashboard?.classList.add('hidden');
                    loginContainer?.classList.remove('hidden');
                })
                .catch(error => console.error("Error during logout:", error));
        });
    }

    // Load folders from the server
    function loadFolders() {
        console.log("Loading folders...");
        fetch('/api/folders')
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok " + response.statusText);
                }
                return response.json();
            })
            .then(folders => {
                console.log("Folders data received:", folders);
    
                if (!folderList) {
                    console.error("Folder list element is not found in the DOM.");
                    return;
                }
    
                folderList.innerHTML = '';  // Clear any existing content in the list
    
                if (folders.length === 0) {
                    console.log("No folders found for the user.");
                    folderList.innerHTML = '<li>No folders found</li>';
                } else {
                    folders.forEach(folder => {
                        const folderItem = document.createElement('li');
                        folderItem.innerHTML = `<i class="fa fa-folder"></i> ${folder.name}`;
                        folderItem.addEventListener('click', () => {
                            currentFolderId = folder.id;
                            loadFiles(folder.id);
                        });
                        folderList.appendChild(folderItem);
                    });
                }
            })
            .catch(error => {
                console.error("Error loading folders:", error);
                alert('An error occurred while loading folders.');
            });
    }
    

    // Load files from the server for the current folder
    function loadFiles(folderId) {
        console.log("Loading files for folder:", folderId);
        fetch(`/api/files?folderId=${folderId}`)
            .then(response => response.json())
            .then(files => {
                fileList.innerHTML = '';
                if (files.length === 0) {
                    console.log("No files found in this folder.");
                    // Optionally, you could display a message to the user here
                    return;
                }
                files.forEach(file => {
                    const fileItem = document.createElement('li');
                    fileItem.innerHTML = `<a href="/api/files/download/${file.id}">${file.name}</a>`;
                    fileList.appendChild(fileItem);
                });
            })
            .catch(error => {
                console.error("Error loading files:", error);
                alert('An error occurred while loading files.');
            });
    }

    // Create a new folder
    if (createFolderButton) {
        createFolderButton.addEventListener('click', function () {
            const folderName = prompt("Enter folder name:");

            if (!folderName) {
                return;
            }

            fetch('/api/folders/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ name: folderName }),
            })
                .then(response => {
                    if (response.ok) {
                        console.log("Folder created successfully");
                        loadFolders();
                    } else {
                        alert('Failed to create folder');
                    }
                })
                .catch(error => {
                    console.error("Error creating folder:", error);
                    alert('Failed to create folder');
                });
        });
    }

    // Initial load of folders if user is on the dashboard
    if (dashboard && !dashboard.classList.contains('hidden')) {
        loadFolders();
    }
});
