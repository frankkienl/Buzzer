<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://www.gstatic.com/firebasejs/5.5.8/firebase.js"></script>
    <script>
        // Initialize Firebase
        var config = {
            apiKey: "EXAMPLE",
            authDomain: "EXAMPLE.firebaseapp.com",
            databaseURL: "https://EXAMPLE.firebaseio.com",
            projectId: "EXAMPLE",
            storageBucket: "EXAMPLE.appspot.com",
            messagingSenderId: "EXAMPLE"
        };
        firebase.initializeApp(config);
    </script>

    <script src="https://www.gstatic.com/firebasejs/5.5.8/firebase-firestore.js"></script>
</head>
<body>
<h2>BUZZER</h2>
<a href="buzzer.apk">Download apk</a><br />
<div id="stuff">

</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        // The Firebase SDK is initialized
        const firestore = firebase.firestore();
        const settings = {timestampsInSnapshots: true};
        firestore.settings(settings);

        var stuff = "";
        let devices = firestore
            .collection('buzzer').get().then(function (querySnapshot) {
                querySnapshot.forEach(function (doc) {
                    // doc.data() is never undefined for query doc snapshots
                    console.log(doc.id, " => ", doc.data());
                    stuff += "<li>" + doc.data().name + " - <a href=\"gcm.php?token=" + doc.data().token + "\">" + doc.data().name + "</a>";
                });
                document.getElementById('stuff').innerHTML = "<ul>" + stuff + "</ul>";
            });
    });
</script>

</body>
</html>