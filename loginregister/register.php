<?php

require "conn.php";

$number=$_POST["number"];
$c_number=$_POST["confirmnumber"];



if($conn){
	
	if(strlen($number)> 8 || strlen($number)< 8 ){
		echo"Use a correct phone number";

	
	
	}if(($number) !== ($c_number)){
		echo "The numbers dont match. Make sure the numbers match and try again";
		
	}else{ 
		$sqlCheckNumber= "SELECT*FROM users_tabletest WHERE  number LIKE $number";
		$numberQuery=mysqli_query($conn,$sqlCheckNumber);
		
		$sqlCheckCNumber= "SELECT*FROM users_tabletest WHERE c_number LIKE $c_number";
		$CnumberQuery=mysqli_query($conn,$sqlCheckCNumber);
		
	
		
		if(mysqli_num_rows($numberQuery)>0){
			echo "This phone number  is already used. Use another number";
		}
	
		else{
			
				$sql_register="INSERT INTO users_tabletest (number, c_number)VALUES('$number','$c_number')";
			
		if(mysqli_query($conn,$sql_register)){
			echo "Succesfully Registered";
			
		}else{
			
		
			echo"Failed to Regisiter";
			
		}
		
		}
		
		
		
		
		
	}
	
	
	}else{
	echo "connection error";
	
}


?>