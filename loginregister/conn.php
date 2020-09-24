<?php
$db_name="users_database";
//$db_name="users_tabletest";
$username="root";
$password="";
$servername="127.0.0.1";

$conn=mysqli_connect($servername,$username,$password,$db_name);

if(!$conn){
	echo 'Connection error' . mysqli_connect_error();
}



?>