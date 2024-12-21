function handleGetContacts() {
    fetch('/contacts', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => response.json())
        .then(contacts => {
            const contactsList = document.getElementById('contacts-list');
            contactsList.innerHTML = '';

            contacts.forEach(contact => {
                const contactRow = document.createElement('tr');
                contactRow.innerHTML = `
                    <td>${contact.name}</td>
                    <td>${contact.email}</td>
                    <td>${contact.phone}</td>
                    <td>
                        <button class="delete-btn" onclick="handleDeleteContact(${contact.id})">Edit</button>
                    </td>
                `;
                contactsList.appendChild(contactRow);
            });
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while fetching contacts.');
        });
}

function handleDeleteContact(contactId) {
    fetch(`/contacts/${contactId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then(response => {
            if (response.ok) {
                handleGetContacts();
            } else {
                alert('Failed to delete contact');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while deleting the contact.');
        });
}

window.addEventListener('DOMContentLoaded', handleGetContacts);
