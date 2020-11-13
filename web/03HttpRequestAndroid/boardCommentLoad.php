<?php
 
    header('Content-Type:text/html; charset=utf-8');
 
    $conn= mysqli_connect("localhost","jwab","wltn2002569!","jwab");
    //$conn= mysqli_connect("localhost","umul","aa142536!","umul");
 
    mysqli_query($conn, "set names utf8");    //한글 깨짐 방지

     $pname = $_POST['pname'];
     $boardTitle = $_POST['boardTitle'];
     $boardDate = $_POST['boardDate'];
     
 
    //쿼리문 작성
    $sql="select id, contents, date from comment where projectName = '" .$pname."' and boardTitle = '" .$boardTitle. "' and boardDate = '" .$boardDate. "'";
    $result=mysqli_query($conn, $sql);

         //결과의 총 레코드 수(줄 수, 행의 개수)
    $rowCnt= mysqli_num_rows($result);
 
    // //레코드 수 만큼 반복하여 한줄씩 데이터 읽어오기
    for($i=0; $i<$rowCnt; $i++){
        //데이터 한줄을 연관배열(키값으로 구분)로 받아오기
        $row= mysqli_fetch_array($result, MYSQLI_ASSOC);
        echo "$row[contents]&$row[id]&$row[date];";
    }
    

        
    
 
    mysqli_close($conn);
 
?>
