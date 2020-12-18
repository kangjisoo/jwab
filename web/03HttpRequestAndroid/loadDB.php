<?php
 
    header('Content-Type:text/html; charset=utf-8');
 
    $conn= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
    //$conn= mysqli_connect("localhost","umul","aa142536!","umul");
 
    mysqli_query($conn, "set names utf8");    //한글 깨짐 방지

     $pname = $_POST['pname'];
     $pkey = $_POST['pkey'];
     $id = $_POST['id'];
     $imgPath = $_POST['imgPath'];
 
    //쿼리문 작성
    $sql="select * from ".$pname."_".$pkey;
    $result=mysqli_query($conn, $sql);
 
    //$result : 결과 표
 
    //결과의 총 레코드 수(줄 수, 행의 개수)
    $rowCnt= mysqli_num_rows($result);

    
    //레코드 수 만큼 반복하여 한줄씩 데이터 읽어오기
    for($i=0; $i<$rowCnt; $i++){
        //데이터 한줄을 연관배열(키값으로 구분)로 받아오기
        $row= mysqli_fetch_array($result, MYSQLI_ASSOC);
        echo "$row[title]&$row[id]&$row[date]&$row[imgPath];";
    }
 
    mysqli_close($conn);
 
?>
