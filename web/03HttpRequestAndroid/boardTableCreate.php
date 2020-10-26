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

  $sql = "create table ".$projectTitle."_".$projectKey."(title char(50), contents char(100), img char(150), date datetime(6), no int(10))";
  $result = mysqli_query($connect, $sql);
  echo $sql;

  echo "\nbordTableCreate.php END";
  mysqli_close($connect);

?>