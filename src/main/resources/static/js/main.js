document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const uploadForm = document.getElementById('upload-form');
    const loginContainer = document.getElementById('login-container');
    const registerContainer = document.getElementById('register-container');
    const dashboard = document.getElementById('dashboard');
    const fileList = document.getElementById('file-list');
    const logoutBtn = document.getElementById('logout');

    // Log page load
    console.log("Page loaded. Setting up event listeners...");

    // Show register form
    document.getElementById('show-register').addEventListener('click', function () {
        console.log("Show register form clicked");
        loginContainer.classList.add('hidden');
        registerContainer.classList.remove('hidden');
    });

    // Show login form
    document.getElementById('show-login').addEventListener('click', function () {
        console.log("Show login form clicked");
        registerContainer.classList.add('hidden');
        loginContainer.classList.remove('hidden');
    });

    // Login form submission
    loginForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        console.log("Login attempt with username:", username);

        // Simulated login check (replace this with actual AJAX request to backend if necessary)
        if (username && password) {
            console.log("Submitting login request");
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
                        loginContainer.classList.add('hidden');
                        dashboard.classList.remove('hidden');
                        loadFiles();
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

    // Register form submission
    registerForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const username = document.getElementById('reg-username').value;
        const password = document.getElementById('reg-password').value;

        console.log("Register attempt with username:", username);

        // Perform AJAX registration request
        fetch('/api/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ username: username, password: password }),
        })
            .then(response => {
                if (response.ok) {
                    console.log("Registration successful for user:", username);
                    alert('Registration successful!');
                    registerContainer.classList.add('hidden');
                    loginContainer.classList.remove('hidden');
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

    // Upload form submission
    uploadForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const file = document.getElementById('file-upload').files[0];
        const fileName = document.getElementById('file-name').value;

        if (!file || !fileName) {
            alert('Please select a file and provide a name.');
            return;
        }

        console.log("File upload attempt with file:", fileName);

        const formData = new FormData();
        formData.append('file', file);
        formData.append('fileName', fileName);

        fetch('/api/file/upload', {
            method: 'POST',
            body: formData,
        })
            .then(response => {
                if (response.ok) {
                    console.log("File uploaded successfully");
                    loadFiles();  // Reload files to include the newly uploaded one
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

    // Logout
    logoutBtn.addEventListener('click', function () {
        console.log("Logout clicked");
        dashboard.classList.add('hidden');
        loginContainer.classList.remove('hidden');
    });

    // Load files from the server and display them
    function loadFiles() {
        console.log("Loading files...");
        fileList.innerHTML = '';
        fetch('/api/file/list')
            .then(response => response.json())
            .then(files => {
                files.forEach(file => {
                    const listItem = document.createElement('li');
                    const downloadLink = document.createElement('a');
                    downloadLink.textContent = file.fileName;
                    downloadLink.href = `/api/file/download/${file.id}`;
                    downloadLink.download = file.fileName;
                    listItem.appendChild(downloadLink);
                    fileList.appendChild(listItem);
                });
            })
            .catch(error => {
                console.error("Error loading files:", error);
                alert('An error occurred while loading files.');
            });
    }
});
