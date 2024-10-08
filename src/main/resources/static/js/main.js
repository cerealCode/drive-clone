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

        console.log("File upload attempt with file:", fileName);

        // Perform AJAX upload request (Example)
        const listItem = document.createElement('li');
        listItem.textContent = fileName;
        fileList.appendChild(listItem);
        alert('File uploaded successfully!');
    });

    // Logout
    logoutBtn.addEventListener('click', function () {
        console.log("Logout clicked");
        dashboard.classList.add('hidden');
        loginContainer.classList.remove('hidden');
    });

    // Load files (Example)
    function loadFiles() {
        console.log("Loading files...");
        fileList.innerHTML = '';
        const files = ['file1.txt', 'file2.jpg', 'file3.pdf'];
        files.forEach(file => {
            const listItem = document.createElement('li');
            listItem.textContent = file;
            fileList.appendChild(listItem);
        });
    }
});
