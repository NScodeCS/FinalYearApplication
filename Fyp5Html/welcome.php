<?php

   require_once "config.php";

?>
<html">
   
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


   <body>
      <h1>Emergency Units</h1> 
      <h2><a href = "logout.php">Sign Out</a></h2>
      <form action = "userresults.php" method = "post">
                <label>Search for user Location and pictures  :</label>
                <input type = "text" name = "number" id="number" class = "box"/><br /><br />
                <input type = "submit" value = " Submit "/><br />

             </form>



