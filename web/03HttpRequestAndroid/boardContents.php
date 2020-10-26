<?php

  header('content-type: text/html; charset=utf-8');   
  $connect= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
  mysqli_query($connect, "SET NAMES UTF8");

  
  //title, contents, img, pname, pkey를 param으로 받음
  $name= $_POST['name'];
  $contents= $_POST['contents'];
  $file= $_FILES['img'];
  $pname = $_POST['pname'];
  $pkey = $_POST['pkey'];

  
    //이미지 파일을 영구보관하기 위해
    //이미지 파일의 세부정보 얻어오기
    $srcName= $file['name'];
    $tmpName= $file['tmp_name']; //php 파일을 받으면 임시저장소에 넣는다. 그곳이 tmp
  


   //임시 저장소 이미지를 원하는 폴더로 이동
    //ftp://jwab.dothome.co.kr/html/Android/uploads/  여기에 저장됨
    $dstName= "uploads/".date('Ymd_his').$srcName;
    $result=move_uploaded_file($tmpName, $dstName);
    if($result){
        echo "upload success\n";
    }else{
        echo "upload fail\n";
    }
 
  
   //글 작성 시간 변수
    $now= date('Y-m-d H:i:s');


    
    
  // mysqli_select_db($connect, $dbname);
  // // 세션 시작
  // session_start();
  
  
  //선택된 프로젝트에 제목, 내용, 이미지경로, 날짜 저장됨
    $sql="insert into ".$pname."_".$pkey."(title, contents, img, date) values('$name','$contents','$dstName','$now')";
    echo $sql;
    $result = mysqli_query($connect, $sql);
    
      if($result) echo "insert success \n";
    else echo "insert fail \n";
 
   mysqli_close($connect);
    
?>