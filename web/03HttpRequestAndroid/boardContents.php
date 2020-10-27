<?php

  header('content-type: text/html; charset=utf-8');   
  $connect= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
  mysqli_query($connect, "SET NAMES UTF8");

  
  //title, contents, img, pname, pkey를 param으로 받음
  $name= $_POST['name'];
  $contents= $_POST['contents'];
  $pname = $_POST['pname'];
  $pkey = $_POST['pkey'];
  (int)$count = $_POST['count'];

  // $file = Array();

  $file1= $_FILES['img1'];
  $file2= $_FILES['img2'];
  $file3= $_FILES['img3'];
  $file4= $_FILES['img4'];
  $file5= $_FILES['img5'];

  echo $file1;
  

  // for($i = 1; $i <= $count; $i++){
  //     $file[$i] = $_FILES['img['$i']'];
  // }


  // $srcName = Array();
  // $tmpName = Array();
  // $dstName = Array();
  // $result = Array();
  
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

      $dstName1= "uploads/".date('Ymd_his').$srcName1;
      $dstName2= "uploads/".date('Ymd_his').$srcName2;
      $dstName3= "uploads/".date('Ymd_his').$srcName3;
      $dstName4= "uploads/".date('Ymd_his').$srcName4;
      $dstName5= "uploads/".date('Ymd_his').$srcName5;  



      $result1=move_uploaded_file($tmpName1, $dstName1);
      $result2=move_uploaded_file($tmpName2, $dstName2);
      $result3=move_uploaded_file($tmpName3, $dstName3);
      $result4=move_uploaded_file($tmpName4, $dstName4);
      $result5=move_uploaded_file($tmpName5, $dstName5);            


  //   for($i = 1; $i <= $count; $i++){
  //     $srcName[$i] = $file[$i]['name'];
  //     $tmpName[$i] = $file[$i]['tmp_name']; //php 파일을 받으면 임시저장소에 넣는다. 그곳이 tmp
  //     // echo $srcName[$i];

  //     //임시 저장소 이미지를 원하는 폴더로 이동
  //    //ftp://jwab.dothome.co.kr/html/Android/uploads/  여기에 저장됨
  //     $dstName[$i]= "uploads/".date('Ymd_his').$srcName[$i];

  //     echo $dstName[$i];

  //      $result[$i]=move_uploaded_file($tmpName[$i], $dstName[$i]);
  //   if($result[$i]){
  //       echo "\nupload success\n";
  //   }else{
  //       echo "\nupload fail\n";
  //   }
  // }
    
   
 
  
   //글 작성 시간 변수
    $now= date('Y-m-d H:i:s');


  //선택된 프로젝트에 제목, 내용, 이미지경로, 날짜 저장됨
    $sql="insert into ".$pname."_".$pkey."(title, contents, img1,img2,img3,img4,img5, date) values('$name','$contents','$dstName1','$dstName2','$dstName3','$dstName4','$dstName5','$now')";
    // echo $sql;
    $result = mysqli_query($connect, $sql);
    
      if($result) echo "insert success \n";
    else echo "\ninsert fail \n";
 
   mysqli_close($connect);
    
?>