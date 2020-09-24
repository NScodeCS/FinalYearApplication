<?php
$number=$_POST["number"];
require_once "config.php";


$sqlAdress= mysqli_query($config,"SELECT Address, Latitute, test From users_tabletest WHERE number LIKE $number");
if($sqlAdress == null)
{echo "User not found make sure you are searching for the correct user: " .$number;

}
else{
$row = mysqli_fetch_array($sqlAdress);

//echo $row['Address'] .$row['Latitute'] .$row['test'];
$latitute = substr($row['Latitute'], 10);
$long = substr($row['test'], 11);

}

?>
<div id="map" ></div>
<html>
<head>
<meta charset="utf-8">
    <meta name= "viewport" content="initial-scale=1.0, user-scalable=no">

    <style>
       /* Set the size of the div element that contains the map */
      #map_canvas {
        height: 900px;  
        width: 100%;  
       }
    </style>
  </head>
  <body>
    <h3>User <?php echo $number; ?> location and pictures</h3>
    
    <form action = "welcome.php" method = "post">           
                <input type = "submit" value = " New Search "/><br />

             </form>

    <!--The div element for the map -->
    <div id="map_canvas"></div>
    <script>
  
// Initialize and add the map
function initMap() {
  var map;
var global_markers = [];  
var lat= <?php echo $latitute?>;
var long =<?php echo $long;?>;
var user = <?php echo $number?>;
var markers = [[lat, long, user]];
var position = [lat,long];
var service;

var infowindow = new google.maps.InfoWindow({});

function initialize() {
    geocoder = new google.maps.Geocoder();
    var latlng = new google.maps.LatLng(35.095192,33.203430 );
    var myOptions = {
        zoom: 10,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
    addMarker();
    





var service;

var iconBase = 'https://maps.google.com/mapfiles/kml/shapes/';


  var pyrmont = new google.maps.LatLng(lat,long);

  

  var request = {
    location: pyrmont,
    radius: '30',
    query: 'hospital'
    
    
  };

  service = new google.maps.places.PlacesService(map);
  service.textSearch(request, callback);


function callback(results, status) {
  if (status == google.maps.places.PlacesServiceStatus.OK) {
    for (var i = 0; i < results.length; i++) {
      var place = results[i];
      createMarker(results[i]);
    }
  }
}

function createMarker(place) {

new google.maps.Marker({
    position: place.geometry.location,
    map: map,
  
    icon:"http://maps.google.com/mapfiles/kml/shapes/hospitals.png"
});
}

}
function addMarker() {
    for (var i = 0; i < markers.length; i++) {
      
        var lat = parseFloat(markers[i][0]);
        var lng = parseFloat(markers[i][1]);
        var trailhead_name = markers[i][2];

        var myLatlng = new google.maps.LatLng(lat, lng);

        var contentString = "<html><body><div><p><h2>" + trailhead_name + "</h2></p></div></body></html>";

        var marker = new google.maps.Marker({
            zoom: 4,
            position: myLatlng,
            map: map,
            icon:"http://maps.google.com/mapfiles/kml/paddle/grn-blank.png",
            title: "Coordinates: " + lat + " , " + lng + " | Trailhead name: " + user,
            label: ""+user,
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

    <script 
    src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA0bUypVv1SHzTAMNV9wUQVSSEyWIw7vBg&callback=initMap&libraries=places">
    
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
