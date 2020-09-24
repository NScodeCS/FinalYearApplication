<?php
require "conn.php";

$user=$_POST["textuser"];
$image=$_POST["image"];
	
if($conn){
	
$sql_register="INSERT INTO userpictures (number, imagefile)VALUES('$user','$image')";

	if(mysqli_query($conn,$sql_register)){
			echo "Succesfully Uploaded your image";
	}
	else{echo "Your Image was not able to be uploaded";

}
}
else{
echo	"connection error";
}



?>