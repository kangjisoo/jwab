<?php

  header('content-type: text/html; charset=utf-8');   
  $connect= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
  mysqli_query($connect, "SET NAMES UTF8");

  
  //title, contents, img, pname, pkey를 param으로 받음
  $id = $_POST['id'];
  $name= $_POST['name'];
  $contents= $_POST['contents'];
  $pname = $_POST['pname'];
  $pkey = $_POST['pkey'];
  $imgTest = $_POST['imgTest'];
  $profileImg = $_POST['profileImg'];


  $file1= $_FILES['img1'];
  $file2= $_FILES['img2'];
  $file3= $_FILES['img3'];
  $file4= $_FILES['img4'];
  $file5= $_FILES['img5'];


  
    //이미지 파일을 영구보관하기 위해
    //이미지 파일의 세부정보 얻어오기
      $srcName1 = $file1['name'];
      $tmpName1 = $file1['tmp_name'];
    
      $srcName2 = $file2['name'];
      $tmpName2 = $file2['tmp_name'];
      
      $srcName3 = $file3['name'];
      $tmpName3 = $file3['tmp_name'];

      $srcName3 = $file3['name'];
      $tmpName3 = $file3['tmp_name'];

      $srcName4 = $file4['name'];
      $tmpName4 = $file4['tmp_name'];

      $srcName5 = $file5['name'];
      $tmpName5 = $file5['tmp_name'];

      if(empty($srcName1)){
        $dstName1 = "null";
      }else{
        $dstName1= "uploads/".date('Ymd_his').$srcName1;
        $result1=move_uploaded_file($tmpName1, $dstName1);
      }
       if(empty($srcName2)){
        $dstName2 = "null";
      }else{
        $dstName2= "uploads/".date('Ymd_his').$srcName2;
        $result2=move_uploaded_file($tmpName2, $dstName2);
      }
       if(empty($srcName3)){
        $dstName3 = "null";
      }else{
        $dstName3= "uploads/".date('Ymd_his').$srcName3;
        $result3=move_uploaded_file($tmpName3, $dstName3);
      }
       if(empty($srcName4)){
        $dstName4 = "null";
      }else{
        $dstName4= "uploads/".date('Ymd_his').$srcName4;
        $result4=move_uploaded_file($tmpName4, $dstName4);
      }
       if(empty($srcName5)){
        $dstName5 = "null";
      }else{
        $dstName5= "uploads/".date('Ymd_his').$srcName5;
        $result5=move_uploaded_file($tmpName5, $dstName5);
      }
      
 
  
   //글 작성 시간 변수
    $now= date('Y-m-d H:i:s');


  //선택된 프로젝트에 제목, 내용, 이미지경로, 날짜 저장됨
    $sql="insert into ".$pname."_".$pkey." (id, imgPath, title, contents, img1,img2,img3,img4,img5, date) values('$id',
    '$profileImg', '$name','$contents','$dstName1','$dstName2','$dstName3','$dstName4','$dstName5','$now')";

   
    $result = mysqli_query($connect, $sql);

    
    
      if($result) echo "insert success \n";
    else echo "\ninsert fail \n";


   mysqli_close($connect);
    
?>