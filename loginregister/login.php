<?php

require "conn.php";

$number=$_POST["number"];




if($conn){

		
		
	$sqlCheckNumber= "SELECT*FROM users_tabletest WHERE  number LIKE $number";
		$numberQuery=mysqli_query($conn,$sqlCheckNumber);
	
			
	
	if(mysqli_num_rows($numberQuery)>0){
		$sqlLogin= "SELECT*FROM users_tabletest WHERE  number LIKE $number";
		$loginQuery=mysqli_query($conn,$sqlLogin);
		if(mysqli_num_rows($loginQuery)>0){
			echo"Login Success";
			

		 }else{
			echo "This phone number  is not register.Please register.";
		}
	
	
	
	}else{
	echo"Wrong Number Or you Need to Register";
}

}else{
	echo"Connection error";
}


?>