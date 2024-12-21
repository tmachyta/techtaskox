document.getElementById('update-task-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const taskId = document.getElementById('task-id').value;
    const taskStatus = document.getElementById('task-status').value;

    if (taskId && taskStatus) {
        const updatedTask = {
            status: taskStatus
        };

        fetch(`/tasks/status/${taskId}`, {
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
                        newMessageRow.innerHTML = `<td><strong>Task ID ${data.id}:</strong> ${data.status}</td>`;
                        document.getElementById('task-id').value = '';
                        document.getElementById('task-status').value = 'OPEN';
                    });
                } else {
                    alert('Error updating task status');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error updating task status');
            });
    } else {
        alert('Please enter Task ID and select a status.');
    }
});
