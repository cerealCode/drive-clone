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
    const createFolderButton = document.getElementById('create-folder');
    let currentFolderId = null;

    console.log("Page loaded. Setting up event listeners...");

    // Show register form
    document.getElementById('show-register')?.addEventListener('click', function () {
        loginContainer?.classList.add('hidden');
        registerContainer?.classList.remove('hidden');
    });

    // Show login form
    document.getElementById('show-login')?.addEventListener('click', function () {
        registerContainer?.classList.add('hidden');
        loginContainer?.classList.remove('hidden');
    });

    // Login form submission
    loginForm?.addEventListener('submit', function (e) {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

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
                    loadFolders(); // Load folders after successful login
                } else {
                    alert('Invalid login credentials');
                }
            })
            .catch(() => {
                alert('An error occurred during login.');
            });
        } else {
            alert('Please enter both username and password');
        }
    });


    // Register form submission
    registerForm?.addEventListener('submit', function (e) {
        e.preventDefault();
        const username = document.getElementById('reg-username').value;
        const password = document.getElementById('reg-password').value;

        fetch('/api/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username, password }),
        })
            .then(response => {
                if (response.ok) {
                    alert('Registration successful!');
                    registerContainer?.classList.add('hidden');
                    loginContainer?.classList.remove('hidden');
                } else {
                    return response.json().then(error => {
                        alert('Registration failed: ' + error.message);
                    });
                }
            })
            .catch(() => {
                alert('An error occurred during registration.');
            });
    });

    // Upload form submission
    uploadForm?.addEventListener('submit', function (e) {
        e.preventDefault();
        const file = document.getElementById('file-upload').files[0];
        const fileName = document.getElementById('file-name').value;

        if (!file || !fileName || !currentFolderId) {
            alert('Please select a file, provide a name, and select a folder.');
            return;
        }

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
                    loadFiles(currentFolderId);  // Reload files to include the newly uploaded one
                    alert('File uploaded successfully!');
                } else {
                    alert('File upload failed.');
                }
            })
            .catch(() => {
                alert('An error occurred during file upload.');
            });
    });

    // Logout
    logoutBtn?.addEventListener('click', function () {
        fetch('/logout', { method: 'POST' })
            .then(() => {
                dashboard?.classList.add('hidden');
                loginContainer?.classList.remove('hidden');
            })
            .catch(() => alert('An error occurred during logout.'));
    });

    // Load folders from the server
    function loadFolders() {
        fetch('/api/folders')
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok " + response.statusText);
                }
                return response.json();
            })
            .then(folders => {
                if (!folderList) return;

                folderList.innerHTML = '';  // Clear existing content

                if (folders.length === 0) {
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
            .catch(() => alert('An error occurred while loading folders.'));
    }

    // Load files from the server for the current folder
    function loadFiles(folderId) {
        fetch(`/api/files?folderId=${folderId}`)
            .then(response => response.json())
            .then(files => {
                fileList.innerHTML = '';
                if (files.length === 0) {
                    fileList.innerHTML = '<li>No files found</li>';
                } else {
                    files.forEach(file => {
                        const fileItem = document.createElement('li');
                        fileItem.innerHTML = `<a href="/api/files/download/${file.id}">${file.name}</a>`;
                        fileList.appendChild(fileItem);
                    });
                }
            })
            .catch(() => alert('An error occurred while loading files.'));
    }

    // Create a new folder
    createFolderButton?.addEventListener('click', function () {
        const folderName = prompt("Enter folder name:");
        if (!folderName) return;

        fetch('/api/folders/create', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: folderName }),
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

    // Initial load of folders if user is on the dashboard
    if (dashboard && !dashboard.classList.contains('hidden')) {
        loadFolders();
    }

    // Redirect to dashboard after a delay on the welcome page
    if (document.querySelector('.container h1 span')) {
        setTimeout(() => {
            window.location.href = '/dashboard';
        }, 2000);
    }
});
