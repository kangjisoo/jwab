<?php
 
    header("Content-Type: text/html; charset=UTF-8");
 
    $name= $_POST['name'];
    $message= $_POST['msg'];
 
    echo " 이름  : $name \n";
    echo "메시지 : $message \n";
?>