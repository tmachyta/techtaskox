document.getElementById('find-client-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const clientId = document.getElementById('client-id').value;

    if (clientId) {
        window.location.href = `/clients/${clientId}`;
    } else {
        alert('Please enter a valid client ID.');
    }
});

document.getElementById('find-contact-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const contactId = document.getElementById('contact-id').value;

    if (contactId) {
        window.location.href = `/contacts/${contactId}`;
    } else {
        alert('Please enter a valid contact ID.');
    }
});

document.getElementById('find-task-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const taskId = document.getElementById('task-id').value;

    if (taskId) {
        window.location.href = `/tasks/${taskId}`;
    } else {
        alert('Please enter a valid contact ID.');
    }
});

