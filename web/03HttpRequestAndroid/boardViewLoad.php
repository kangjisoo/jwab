<?php
 
    header('Content-Type:text/html; charset=utf-8');
 
    $conn= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
    //$conn= mysqli_connect("localhost","umul","aa142536!","umul");
 
    mysqli_query($conn, "set names utf8");    //한글 깨짐 방지

     $pname = $_POST['pname'];
     $pkey = $_POST['pkey'];
     $date = $_POST['date'];

 
    //쿼리문 작성
    $sql="select contents, img1, img2, img3, img4, img5 from ".$pname."_".$pkey." where date = '" .$date."'";
    $result=mysqli_query($conn, $sql);

     //결과의 총 레코드 수(줄 수, 행의 개수)
   
        //데이터 한줄을 연관배열(키값으로 구분)로 받아오기
        $row= mysqli_fetch_array($result, MYSQLI_ASSOC);
        echo "$row[contents]&$row[img1]&$row[img2]&$row[img3]&$row[img4]&$row[img5];";
    

        
    
 
    mysqli_close($conn);
 
?>
