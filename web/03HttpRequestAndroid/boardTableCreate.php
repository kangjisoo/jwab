<?php
  header('content-type: text/html; charset=utf-8'); 
  //dbCon.php 의 server address/id/pw 참조
  // include('dbCon.php');
  
  $connect= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
  mysqli_query($connect, "SET NAMES UTF8");
  
  $projectTitle = $_POST['pname'];
  $projectKey = $_POST['pkey'];
  echo "projectName: ".$projectTitle."\n";
  echo "projectKey: ".$projectKey."\n";

  $sql = "create table ".$projectTitle."_".$projectKey."(title char(20), contents char(100), img1 char(50), img2 char(50), img3 char(50), img4 char(50), img5 char(50), date datetime(6), no int(10))";
  $result = mysqli_query($connect, $sql);
  echo $sql;

  echo "\nbordTableCreate.php END";
  mysqli_close($connect);

?>