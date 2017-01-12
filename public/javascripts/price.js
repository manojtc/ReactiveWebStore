function loadPrice(doc) {
    jQuery.get("http://localhost:9000/rnd/rxbat", function(response) {
        doc.getElementById("price").value = parseFloat(response)
    }).fail(function(e) {
        alert('Wops! We were not able to call http://localhost:9000/rnd/rxba. Error: ' + e.statusText);
    });
}
