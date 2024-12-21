document.getElementById('find-client-form').addEventListener('submit', function(event) {
    event.preventDefault();  // Запобігаємо стандартній поведінці форми

    const clientId = document.getElementById('client-id').value;

    if (clientId) {
        fetch(`/clients/${clientId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Client not found');
                }
                return response.json();
            })
            .then(data => {
                const clientDetailsElement = document.getElementById('client-details');
                clientDetailsElement.innerHTML = `
                    <h2>Client Details</h2>
                    <p><strong>Name:</strong> ${data.name}</p>
                    <p><strong>Email:</strong> ${data.email}</p>
                    <p><strong>Phone:</strong> ${data.phone}</p>
                    <p><strong>Status:</strong> ${data.status}</p>
                    <p><strong>Address:</strong> ${data.address}</p>
                `;
            })
            .catch(error => {
                const clientDetailsElement = document.getElementById('client-details');
                clientDetailsElement.innerHTML = `<p>Error: ${error.message}</p>`;
            });
    } else {
        alert('Please enter a valid client ID.');
    }
});
