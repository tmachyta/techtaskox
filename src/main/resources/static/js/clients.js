function handleGetClients() {
    fetch('/clients', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => response.json())
        .then(clients => {
            const clientList = document.getElementById('clients-list');
            clientList.innerHTML = '';

            clients.forEach(client => {
                const clientRow = document.createElement('tr');
                clientRow.innerHTML = `
                    <td>${client.name}</td>
                    <td>${client.field}</td>
                    <td>${client.address}</td>
                    <td>
                    <button class="delete-btn" onclick="handleDeleteClient(${client.id})">Delete</button>
                    </td>
                `;
                clientList.appendChild(clientRow);
            });
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while fetching clients.');
        });
}

window.addEventListener('DOMContentLoaded', handleGetClients);

function handleDeleteClient(clientId) {
    fetch(`/clients/${clientId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.ok) {
                handleGetClients();
            } else {
                alert('Failed to delete client');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while deleting client.');
        });
}

