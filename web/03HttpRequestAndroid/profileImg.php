<?php

  header('content-type: text/html; charset=utf-8');   
  $connect= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
  mysqli_query($connect, "SET NAMES UTF8");

  
  //title, contents, img, pname, pkey를 param으로 받음
  $id = $_POST['id'];
  $file1= $_FILES['imgPath'];


  
    //이미지 파일을 영구보관하기 위해
    //이미지 파일의 세부정보 얻어오기
      $srcName1 = $file1['name'];
      $tmpName1 = $file1['tmp_name'];
    

      if(empty($srcName1)){
        $dstName1 = "null";
      }else{
        $dstName1= "uploads/".date('Ymd_his').$srcName1;
        $result1=move_uploaded_file($tmpName1, $dstName1);
      }
    

   //글 작성 시간 변수
    $now= date('Y-m-d H:i:s');

//profile_img테이블에 아이디와 이미지경로 삽입
    $sql="select id from profile_img where id = '" .$id. "'";
    $result = mysqli_query($connect, $sql);
    $rowCnt= mysqli_num_rows($result);
    

      if($rowCnt>=1){
        $sql = "update profile_img set imgPath = '$dstName1' where id = '$id'";
        $result = mysqli_query($connect, $sql);
     
      }else{
        $sql="insert into profile_img (id, imgPath) values ('$id', '$dstName1')";
        $result = mysqli_query($connect, $sql);
      }
      echo $dstName1;


  
        

//UPDATE 테이블명 SET 변경할컬럼 = 변경값 WHERE 컬럼구분값;

   mysqli_close($connect);
    
?>