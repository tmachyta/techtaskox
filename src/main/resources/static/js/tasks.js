function handleGetTasks() {
    fetch('/tasks', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => response.json())
        .then(tasks => {
            const tasksList = document.getElementById('tasks-list');
            tasksList.innerHTML = '';

            tasks.forEach(task => {
                const taskRow = document.createElement('tr');
                taskRow.innerHTML = `
                    <td>${task.description}</td>
                    <td>${task.deadline}</td>
                    <td>${task.status}</td>
                    <td>
                    <button class="edit-btn" onclick="window.location.href = ''">Edit</button>
                    <button class="delete-btn" onclick="handleDeleteTask(${task.id})">Delete</button>
                    </td>
                `;
                tasksList.appendChild(taskRow);
            });
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while fetching tasks.');
        });
}

function handleDeleteTask(taskId) {
    fetch(`/tasks/${taskId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.ok) {
                handleGetTasks();
            } else {
                alert('Failed to delete task');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while deleting the task.');
        });
}

window.addEventListener('DOMContentLoaded', handleGetTasks);


function handleUpdateTask(event) {
    event.preventDefault();

    const taskId = document.getElementById('task-id').value;
    const description = document.getElementById('task-description').value;
    const deadline = document.getElementById('task-deadline').value;
    const status = document.getElementById('task-status').value;
    const contactId = document.getElementById('contact-id').value;

    const updatingTaskData = {
        description: description,
        status: status,
        deadline: deadline,
        contactId: contactId
    };

    fetch(`/tasks/${taskId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatingTaskData),
    })
        .then(response => {
            if (response.ok) {
                alert('Task updated successfully!');
                window.location.href = '/main-page';
            } else {
                alert('Failed to update task. Please check the form values.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred. Please try again later.');
        });
}

document.getElementById('updateTaskForm').addEventListener('submit', handleUpdateTask);

