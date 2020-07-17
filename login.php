<?php

  header('content-type: text/html; charset=utf-8'); 
  // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
  $connect=mysqli_connect("10.210.14.164", "root", "wltn2002569") or 
	die( "SQL server에 연결할 수 없습니다.");
 
  mysqli_query($connect, "SET NAMES UTF8");

  // 데이터베이스 선택
  mysqli_select_db($connect,"signdb");
 
  // 세션 시작
  session_start();

 //id와 pw입력받아서 $id, $password1에 저장
  $id = $_POST['u_id'];
  $password1 = $_POST['u_pw'];

 //db에 등록된 password1과 입력받은 $password1비교해서 같으면1, 다르면0을 pw_chk에 저장, db에 등록된id와 입력받은 id 같은거중에서만 검색	
   $sql = "SELECT IF(strcmp(password1,'$password1'),0,1) pw_chk
	 FROM signtb  WHERE id = '$id'";

 //결과값 저장
  $result = mysqli_query($connect, $sql);
 
  // 예외처리
  if($result)
  {
    $row = mysqli_fetch_array($result);
    if(is_null($row['pw_chk']))
    {
      echo "Can not find ID";
    }
    else
    {
      echo "$row[pw_chk]";
    }
  }
  else
  {
   echo mysqli_errno($connect);

  }
?>
