document.getElementById('update-client-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const clientId = document.getElementById('client-id').value;
    const clientName = document.getElementById('client-name').value;
    const clientField = document.getElementById('client-field').value;
    const clientAddress = document.getElementById('client-address').value;

    if (clientId && clientName && clientField && clientAddress) {
        const updatedClient = {
            name: clientName,
            field: clientField,
            address: clientAddress
        };

        fetch(`/clients/${clientId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedClient),
        })
            .then(response => {
                if (response.ok) {
                    response.json().then(data => {
                        window.location.href = '/clientslist.html';
                    });
                } else {
                    alert('Error updating client details');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Error updating client details');
            });
    } else {
        alert('Please enter Client ID, Name, Field, and Address.');
    }
});
