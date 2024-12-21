function handleLogin(event) {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    const loginData = {
        email: username,
        password: password
    };

    fetch('/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData),
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/main-page';
            } else {
                alert('Login failed! Please check your credentials.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function handleRegistration(event) {
    event.preventDefault();

    const email = document.getElementById('register-email').value;
    const password = document.getElementById('register-password').value;
    const repeatPassword = document.getElementById('repeat-password').value;
    const firstName = document.getElementById('first-name').value;
    const lastName = document.getElementById('last-name').value;

    if (password !== repeatPassword) {
        alert('Passwords do not match!');
        return;
    }

    const registrationData = {
        email: email,
        password: password,
        repeatPassword: repeatPassword,
        firstName: firstName,
        lastName: lastName
    };

    fetch('/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(registrationData),
    })
        .then(response => {
            if (response.ok) {
                alert('Registration successful!');
                window.location.href = '/auth/login-registration';
            } else {
                alert('Registration failed! Please check the form values.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

document.getElementById('loginForm').addEventListener('submit', handleLogin);

document.getElementById('registrationForm').addEventListener('submit', handleRegistration);