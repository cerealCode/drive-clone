// app.js
document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const uploadForm = document.getElementById('upload-form');
    const loginContainer = document.getElementById('login-container');
    const registerContainer = document.getElementById('register-container');
    const dashboard = document.getElementById('dashboard');
    const fileList = document.getElementById('file-list');
    const logoutBtn = document.getElementById('logout');

    // Show register form
    document.getElementById('show-register').addEventListener('click', function () {
        loginContainer.classList.add('hidden');
        registerContainer.classList.remove('hidden');
    });

    // Show login form
    document.getElementById('show-login').addEventListener('click', function () {
        registerContainer.classList.add('hidden');
        loginContainer.classList.remove('hidden');
    });

    // Login form submission
    loginForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        
        // Perform AJAX login request (Example)
        // Simulating a successful login
        if (username === 'user' && password === 'pass') {
            loginContainer.classList.add('hidden');
            dashboard.classList.remove('hidden');
            loadFiles();
        } else {
            alert('Invalid login credentials');
        }
    });

    // Register form submission
    registerForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const username = document.getElementById('reg-username').value;
        const password = document.getElementById('reg-password').value;

        // Perform AJAX registration request (Example)
        alert('Registration successful!');
        registerContainer.classList.add('hidden');
        loginContainer.classList.remove('hidden');
    });

    // Upload form submission
    uploadForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const file = document.getElementById('file-upload').files[0];
        const fileName = document.getElementById('file-name').value;

        // Perform AJAX upload request (Example)
        const listItem = document.createElement('li');
        listItem.textContent = fileName;
        fileList.appendChild(listItem);
        alert('File uploaded successfully!');
    });

    // Logout
    logoutBtn.addEventListener('click', function () {
        dashboard.classList.add('hidden');
        loginContainer.classList.remove('hidden');
    });

    // Load files (Example)
    function loadFiles() {
        fileList.innerHTML = '';
        const files = ['file1.txt', 'file2.jpg', 'file3.pdf'];
        files.forEach(file => {
            const listItem = document.createElement('li');
            listItem.textContent = file;
            fileList.appendChild(listItem);
        });
    }
});
