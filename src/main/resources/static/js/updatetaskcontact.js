document.getElementById('update-task-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const taskId = document.getElementById('task-id').value;
    const contactId = document.getElementById('contact-id').value;

    if (taskId && contactId) {
        const updatedTask = {
            contactId: contactId
        };

        fetch(`/tasks/contact/${taskId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedTask),
        })
            .then(response => {
                if (response.ok) {
                    response.json().then(data => {
                        const newMessageRow = document.createElement('tr');
                        newMessageRow.innerHTML = `<td><strong>Task ID ${data.id}:</strong> Assigned to Contact ID ${data.contactId}</td>`;
                        document.getElementById('task-id').value = '';
                        document.getElementById('contact-id').value = '';
                    });
                } else {
                    alert('Error updating task with contact ID');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error updating task with contact ID');
            });
    } else {
        alert('Please enter Task ID and Contact ID.');
    }
});
