document.getElementById('update-contact-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const contactId = document.getElementById('contact-id').value;
    const contactName = document.getElementById('contact-name').value;
    const contactLastName = document.getElementById('contact-lastname').value;
    const contactEmail = document.getElementById('contact-email').value;
    const contactPhone = document.getElementById('contact-phone').value;

    if (contactId && contactName && contactLastName && contactEmail && contactPhone) {
        const updatedContact = {
            name: contactName,
            lastName: contactLastName,
            email: contactEmail,
            phone: contactPhone
        };

        fetch(`/contacts/${contactId}`, {
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
        alert('Please enter Contact ID, Name, Last Name, Email, and Phone.');
    }
});
