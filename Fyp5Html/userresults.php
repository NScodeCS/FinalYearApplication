<?php
$number=$_POST["number"];
require_once "config.php";


$sqlAdress= mysqli_query($config,"SELECT Address, Latitute, test From users_tabletest WHERE number LIKE $number");

$row = mysqli_fetch_array($sqlAdress);

//echo $row['Address'] .$row['Latitute'] .$row['test'];
$latitute = substr($row['Latitute'], 10);
$long = substr($row['test'], 11);

?>

<div id="map" ></div>
<html>
<head>

    <style>
       /* Set the size of the div element that contains the map */
      #map_canvas {
        height: 500px;  /* The height is 400 pixels */
        width: 100%;  /* The width is the width of the web page */
       }
    </style>
  </head>
  <body>
    <h3>User <?php echo $number; ?> location and pictures</h3>
    <!--The div element for the map -->
    <div id="map_canvas"></div>
    <script>
    var map;
// Initialize and add the map
function initMap() {

var global_markers = [];  
var lat= <?php echo $latitute?>;
var long =<?php echo $long;?>;
var user = <?php echo $number?>;
var markers = [[lat, long, user]];
var position = [[lat,long]];

var infowindow = new google.maps.InfoWindow({});

function initialize() {
    geocoder = new google.maps.Geocoder();
    var latlng = new google.maps.LatLng(35.095192,33.203430 );
    var myOptions = {
        zoom: 9,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    addMarker();
    getNearbyPlaces(position);
    
}
function getNearbyPlaces() {
    let request = {
    location: position,
    rankBy: google.maps.places.RankBy.DISTANCE,
    keyword: 'hospital'
    };

    service = new google.maps.places.PlacesService(map);
    service.nearbySearch(request, nearbyCallback);
}

// Handle the results (up to 20) of the Nearby Search
function nearbyCallback(results, status) {
    if (status == google.maps.places.PlacesServiceStatus.OK) {
    addMarker(results);
    }
}

function createMarkers() {
    places.forEach(place => {
    let marker = new google.maps.Marker({
        position: place.geometry.location,
        map: map,
        title: place.name
    });

    });
  
   
}

function addMarker() {
    for (var i = 0; i < markers.length; i++) {
        // obtain the attribues of each marker
        var lat = parseFloat(markers[i][0]);
        var lng = parseFloat(markers[i][1]);
        var trailhead_name = markers[i][2];

        var myLatlng = new google.maps.LatLng(lat, lng);

        var contentString = "<html><body><div><p><h2>" + trailhead_name + "</h2></p></div></body></html>";

        var marker = new google.maps.Marker({
            position: myLatlng,
            map: map,
            title: "Coordinates: " + lat + " , " + lng + " | Trailhead name: " + user,
    
        });

        marker['infowindow'] = contentString;

        global_markers[i] = marker;
        
        google.maps.event.addListener(global_markers[i], 'click', function() {
            infowindow.setContent(this['infowindow']);
            infowindow.open(map, this);
        });
    }
}

window.onload = initialize;
}
    </script>
    <!--Load the API from the specified URL
    * The async attribute allows the browser to render the page while the API loads
    * The key parameter will contain your own API key (which is not needed for this tutorial)
    * The callback parameter executes the initMap() function
    -->
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?=AIzaSyDeaXNyQ1onKQkIGmfxmpEc5VsG7hbLit0&libraries=places&callback=initMap">
    </script>
  </body>

</body>

<body>
<center>
<table>
<thead>

<th>IMAGE</th>
<th>number</th>
<th>id</th>

</thead>
<?php
require_once "config.php";
$selectimages = "SELECT imagefile,imageid From userpictures WHERE $number=number; ";
$selected = mysqli_query($config,$selectimages);

while($row = mysqli_fetch_array($selected)){
?>
<tr>
<td><?php echo '<img src="data:imagefile/;base64,'.$row['imagefile'].'" alt="Image"style="width:600px; height:400px;">' ?></td>
<td><?php echo $row['imageid'] ?></td>

</tr>

<?php
}

?>

</table>

</center>

</body>

</html>