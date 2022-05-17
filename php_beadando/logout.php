<?php

    include('auth.php');
    include('userstorage.php');
    include('helper.php');

    session_start();
    $user_storage = new UserStorage();
    $auth = new Auth($user_storage);

    $auth->logout();
    redirect('index.php');
?>