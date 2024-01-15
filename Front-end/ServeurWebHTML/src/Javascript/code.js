document.addEventListener('DOMContentLoaded', function () {
    // Appeler le serveur pour récupérer les données des articles
    fetch('/FormArticle', {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            createRowsFromJSON(data);
        })
        .catch(error => {
            console.error('Erreur lors de la récupération des données:', error);
        });

    // Ajouter un gestionnaire d'événements pour le bouton de mise à jour
    document.getElementById('updateButton').addEventListener('click', function () {
        updateData();
    });
});

function createRowsFromJSON(data) {
    console.log("je suis ici");
    const tableBody = document.getElementById('table');

    data.forEach(item => {
        const newRow = document.createElement('tr');
        newRow.innerHTML = `
            <td>${item.id}</td>
            <td>${item.nom}</td>
            <td>${item.UnitPrice}</td>
            <td>${item.quantite}</td>
        `;

        newRow.addEventListener('click', function () {
            changeForm(item.id, item.nom, item.UnitPrice, item.quantite, item.image);
        });

        tableBody.appendChild(newRow);
    });
}



function changeForm(id, nom, prix, quantite, image) {
    document.getElementById('id').value = id;
    document.getElementById('article').value = nom;
    document.getElementById('unitPrice').value = prix;
    document.getElementById('quantite').value = quantite;

    // Set the image source
    const imageElement = document.getElementById('selectedImage');
    imageElement.src = `/images/${image}`;
    imageElement.alt = nom;
}


function updateData() {
    // Récupérer les valeurs des champs
    const id = document.getElementById('id').value;
    const unitPrice = document.getElementById('unitPrice').value;
    const quantite = document.getElementById('quantite').value;

    // Construire l'objet de données à envoyer
    const updatedData = {
        id: id,
        price: unitPrice,
        quantity: quantite
    };

    // Envoyer la requête POST avec les données mises à jour
    fetch('/FormArticle', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedData)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erreur lors de la mise à jour des données');
            }
            return response.json();
        })
        .then(data => {
            // Gérer la réponse du serveur si nécessaire
            console.log('Données mises à jour avec succès', data);
        })
        .catch(error => {
            console.error('Erreur lors de la mise à jour des données:', error);
        });

    alert("Mise a jour effectué")

}
