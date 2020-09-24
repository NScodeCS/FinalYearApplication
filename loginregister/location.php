<?php
require "conn.php";

$user=$_POST["textuser"];
$Latitude=$_POST["txtLatLong"];
$Address=$_POST['txtaddress'];
$Longitute=$_POST["txtlong"];


$sql = "update users_tabletest set Address = '$Address' where number = '$user' ";
$sqltest = "update users_tabletest set Latitute = '$Latitude'  where number = '$user' ";
$sqltestlong = "update users_tabletest set test = '$Longitute'  where number = '$user' ";

$sqltestactivate = mysqli_query($conn,$sqltest);
$sqltestlongtitute = mysqli_query($conn,$sqltestlong);

if(mysqli_query($conn,$sql)){
	if(mysqli_query($conn,$sqltest)){
	echo "Succesfully Uploaded Your Location";
	}else{
		echo"Fail"; 
	}
	
}else{
	echo"Connection error" .mysqli_error($conn);
}




?>