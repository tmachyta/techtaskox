function handleAddContact(event) {
    event.preventDefault();

    // Отримання значень з форми
    const name = document.getElementById('contact-name').value;
    const lastName = document.getElementById('contact-lastname').value;
    const email = document.getElementById('contact-email').value;
    const phone = document.getElementById('contact-phone').value;
    const clientId = document.getElementById('client-id').value;

    // Створення об'єкта для відправки
    const creatingContactData = {
        name: name,
        lastName: lastName,
        email: email,
        phone: phone,
        clientId: clientId
    };

    // Відправка даних на сервер через fetch
    fetch('/contacts', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(creatingContactData),
    })
        .then(response => {
            if (response.ok) {
                alert('Contact created successfully!');
                window.location.href = '/main-page'; // Перехід на головну сторінку після успішного додавання
            } else {
                alert('Failed to create contact. Please check the form values.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred. Please try again later.');
        });
}

// Додавання обробника події для форми
document.getElementById('createContactForm').addEventListener('submit', handleAddContact);
