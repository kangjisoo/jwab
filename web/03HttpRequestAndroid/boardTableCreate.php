<?php

  header('content-type: text/html; charset=utf-8'); 
  
  $connect= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
  mysqli_query($connect, "SET NAMES UTF8");
  
  $projectTitle = $_POST['pname'];
  $projectKey = $_POST['pkey'];
  echo "projectName: ".$projectTitle."\n";
  echo "projectKey: ".$projectKey;

  $sql = "\ncreate table ".$projectTitle."_".$projectKey."(title char(50), contents char(100), img char(150), date datetime(20), no int(10))\n";
  $result = mysqli_query($connect, $sql);
  echo $sql;

  echo "\nbordTableCreate.php END";
  return 0;

?>