<?php



require "config.php";

$admin=$_POST["username"];
 $password=$_POST["password"];


if($config){
 

 $sqlCheckNumber= "SELECT*FROM adminwebapp WHERE  Admin LIKE $admin and password =$password";
 $numberQuery=mysqli_query($config,$sqlCheckNumber);

    

if(mysqli_num_rows($numberQuery)>0){
 $sqlLogin= "SELECT*FROM adminwebapp WHERE  Admin LIKE $admin and password =$password";
 $loginQuery=mysqli_query($config,$sqlLogin);
 if(mysqli_num_rows($loginQuery)>0){
    
   header("location: welcome.php");

  }
 }else{
    echo "Sorry wrong password or username.";



}

}else{
   echo "No database connection";
}


?>
