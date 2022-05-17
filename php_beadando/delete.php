<?php

    include('commentstorage.php');
    include('com.php');
    include('helper.php');

    session_start();
    $comment_storage = new CommentStorage();
    $com = new Com($comment_storage);

    $comments = $comment_storage->findAll();
    
    foreach ($comments as $key => $value)
    {
        if ($_GET['text'] == $value['text'] && $_GET['user'] == $value['author'])
        {
            $comment_storage->delete($key);
            
            
        }
    }
    

   
    $name = $_GET['name'];
    redirect('teamdetails.php?name='.$name);


?>