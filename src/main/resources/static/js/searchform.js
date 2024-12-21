document.getElementById('search-client-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const clientName = document.getElementById('client-name').value;

    if (clientName) {
        window.location.href = `/clients/search?name=${clientName}`;
    } else {
        alert('Please enter a valid client name.');
    }
});

document.getElementById('search-contact-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const contactName = document.getElementById('contact-name').value;

    if (contactName) {
        window.location.href = `/contacts/search?name=${contactName}`;
    } else {
        alert('Please enter a valid contact name.');
    }
});