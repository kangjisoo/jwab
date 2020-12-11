<?php
 
    header('Content-Type:text/html; charset=utf-8');
    $conn= mysqli_connect("localhost","jwab","wltn2002569!","jwab"); 
    mysqli_query($conn, "set names utf8");    //한글 깨짐 방지

     $id = $_POST['id'];
     $member = $_POST['member'];

     $members = Array();
  
    //"@"를 기준으로 문자열 나눔
  $pattern = "@";
  $members = explode($pattern, $member);
  $num = count($members)-1;
 

  
  //나눈 값 배열에 저장
  for($i = 0; $i<$num; $i++){
     $members[$i];
    }

    for($i = 0; $i<$num; $i++){
        $sql = "select * from profile_img where id = '" .$members[$i]. "'";
        $result=mysqli_query($conn, $sql);

         while ($row = mysqli_fetch_array($result)) {   
            echo "@$row[id]!$row[imgpath]";
        }

    }

   
 mysqli_close($conn);





 
    //쿼리문 작성
    // $sql="select imgpath from profile_img where id = '" .$id. "'";
    // $result=mysqli_query($conn, $sql);

  
    //    // $row= mysqli_query($conn, $sql);
    //    //    echo "$row[contents]&$row[img1]&$row[img2]&$row[img3]&$row[img4]&$row[img5];"; 

    //  //결과의 총 레코드 수(줄 수, 행의 개수)
   
    //     //데이터 한줄을 연관배열(키값으로 구분)로 받아오기
    //     $row= mysqli_fetch_array($result, MYSQLI_ASSOC);
    //     echo "$row[imgpath]";
   
 
?>