
function handleAddClient(event) {
    event.preventDefault();

    const name = document.getElementById('client-name').value;
    const field = document.getElementById('client-industry').value;
    const address = document.getElementById('client-address').value;


    const creatingClientData = {
        name: name,
        field: field,
        address: address
    };

    fetch('/clients', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(creatingClientData),
    })
        .then(response => {
            if (response.ok) {
                alert('Creating client successful!');
                window.location.href = '/main-page';
            } else {
                alert('Creating failed! Please check the form values.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

document.getElementById('createClientForm').addEventListener('submit', handleAddClient);