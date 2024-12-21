document.getElementById('update-contact-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const contactId = document.getElementById('contact-id').value;
    const clientId = document.getElementById('client-id').value;

    if (contactId && clientId) {
        const updatedContact = {
            clientId: clientId
        };

        fetch(`/contacts/client/${contactId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedContact),
        })
            .then(response => {
                if (response.ok) {
                    response.json().then(data => {
                        window.location.href = '/contactlist.html';
                    });
                } else {
                    alert('Error updating contact details');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error updating contact details');
            });
    } else {
        alert('Please enter Contact ID and Client ID.');
    }
});
