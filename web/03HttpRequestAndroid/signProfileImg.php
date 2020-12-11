<?php

  header('content-type: text/html; charset=utf-8');   
  $connect= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
  mysqli_query($connect, "SET NAMES UTF8");

  
  //회원가입시 프로필사진DB에 이미지경로가 null값이 들어가도록 함
  $id = $_POST['id'];


      $sql="insert into profile_img (id, imgPath) values ('$id', 'null')";
        $result = mysqli_query($connect, $sql);


   mysqli_close($connect);
    
?>