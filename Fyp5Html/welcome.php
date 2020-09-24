<?php

   require_once "config.php";

?>
<html>
<meta charset="utf-8">
    <meta name= "viewport" content="initial-scale=1.0, user-scalable=no">
   <head>
      <title> </title>
   </head>
   <style>
body {
  background-color: White;
  text-align: center;
  color: black;
  font-family: Arial, Helvetica, sans-serif;
}
</style>

<html>
   <body>
   



      <h1>Emergency Units</h1> 
      <h2><a href = "logout.php">Sign Out</a></h2>
      <form name="myform" action = "userresults.php" onsubmit="return ValidateForm()" method = "post" required>
                <label>Search for user Location and pictures  :</label>
                <input type = "text" name = "number" id="number" class = "box"/><br /><br />
                <input type = "submit" value = " Submit "/><br />



             </form>
             </html>

  

