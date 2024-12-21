function handleAddTask(event) {
    event.preventDefault();

    const description = document.getElementById('task-description').value;
    const deadline = document.getElementById('task-deadline').value;
    const status = document.getElementById('task-status').value;
    const contactId = document.getElementById('contact-id').value;

    const creatingTaskData = {
        description: description,
        status: status,
        deadline: deadline,
        contactId: contactId
    };

    fetch('/tasks', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(creatingTaskData),
    })
        .then(response => {
            if (response.ok) {
                alert('Task created successfully!');
                window.location.href = '/main-page';
            } else {
                alert('Failed to create task. Please check the form values.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred. Please try again later.');
        });
}

document.getElementById('createTaskForm').addEventListener('submit', handleAddTask);