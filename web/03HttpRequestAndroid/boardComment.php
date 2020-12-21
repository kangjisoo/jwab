<?php

  header('content-type: text/html; charset=utf-8');   
  $connect= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
  mysqli_query($connect, "SET NAMES UTF8");

  
  //title, contents, img, pname, pkey를 param으로 받음
  $id = $_POST['id'];
  $comment= $_POST['contents'];
  $pname = $_POST['pname'];
  $boardTitle = $_POST['name'];
  $boardDate = $_POST['boardDate'];
  $profileImg = $_POST['profileImg'];

  
   //글 작성 시간 변수
    $now= date('Y-m-d H:i:s');

     $sql="select id from comment where id = '" .$id. "'";
    $result = mysqli_query($connect, $sql);
    $rowCnt= mysqli_num_rows($result);
    
    if($rowCnt>=1){
      for($i=0; $i<$rowCnt; $i++){
        $sql = "update comment set profileImg = '$profileImg' where id = '$id'";
        $result = mysqli_query($connect, $sql);
    }
  }
   

  //선택된 프로젝트에 제목, 내용, 이미지경로, 날짜 저장됨
    $sql="insert into comment (id, profileImg, contents, date, projectName, boardTitle, boardDate) values ('$id', '$profileImg','$comment','$now','$pname','$boardTitle','$boardDate')";
    
    $result = mysqli_query($connect, $sql);
    
      if($result) echo "insert success \n";
    else echo "\ninsert fail \n";


   mysqli_close($connect);
    
?>