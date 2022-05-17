<?php

    include('matchstorage.php');
    include('teamstorage.php');
    include('auth.php');
    include('userstorage.php');
    include('commentstorage.php');
    include('com.php');

    session_start();
    $matchStorage = new MatchStorage();
    $teamStorage = new TeamStorage();
    $user_storage = new UserStorage();
    $auth = new Auth($user_storage);
    $comment_storage = new CommentStorage();
    $com = new Com($comment_storage);
    

    $matches = $matchStorage->findAll();
    $teams = $teamStorage->findAll();

    foreach($teams as $t){
        if($t['name'] === $_GET['name']){
            $id = $t['id'];
        }
    }
    $teamAway = array();
    $score = array();
   
    
    $comments = [];

?>
<!DOCTYPE html>
<html lang="hu">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="style.css">
    <title>Csapatrészletek</title>
</head>
<body onload="chcTa()" >
    <main id="homePage">
        <p><b>Csapat neve:</b> <?= $_GET['name'] ?> </p>
        <p><b>Meccsek és eredmények:</b></p>
        <ul>
        <?php foreach($matches as $m){ ?>
            <?php if($m['home']['id'] ===  $id ){ ?>
                <?php foreach($teams as $t){ 
                    if($t['id'] === $m['away']['id']){ ?>
                        <li><?= $_GET['name'] ?> - <?= $t['name'] ?>: mérkőzés ideje: <?= $m['date']?>, 
                        eredmény: 
                        <?php if($m['home']['score'] > $m['away']['score']){ ?>
                            <div id="green"> <?= $m['home']['score']?>  -  <?=$m['away']['score']  ?> </div>
                        <?php } ?>

                        <?php if($m['home']['score'] < $m['away']['score']){ ?>
                            <div id="red"> <?= $m['home']['score']?>  -  <?=$m['away']['score']  ?> </div>
                        <?php } ?>

                        <?php if($m['home']['score'] === $m['away']['score']){ ?>
                            <div id="yellow"> <?= $m['home']['score']?>  -  <?=$m['away']['score']  ?> </div>
                        <?php } ?>
                        </li><?php }
                } 
            }?>
            <?php if($m['away']['id'] ===  $id ){ ?>
                <?php foreach($teams as $t){ 
                    if($t['id'] === $m['home']['id']){ ?>
                        <li><?= $t['name'] ?> - <?= $_GET['name'] ?>:  mérkőzés ideje: <?= $m['date']?>,
                         eredmény: 
                        <?php if($m['home']['score'] < $m['away']['score']){ ?>
                            <div id="green"> <?= $m['home']['score']?>  -  <?=$m['away']['score']  ?> </div>
                        <?php } ?>

                        <?php if($m['home']['score'] > $m['away']['score']){ ?>
                            <div id="red"> <?= $m['home']['score']?>  -  <?=$m['away']['score']  ?> </div>
                        <?php } ?>

                        <?php if($m['home']['score'] === $m['away']['score']){ ?>
                            <div id="yellow"> <?= $m['home']['score']?>  -  <?=$m['away']['score']  ?> </div>
                        <?php } ?>
                     </li> <?php }
                } 
            }?>
        <?php } ?>
        </ul>
 
    </main>
    <main id="homePage">
        <form action="" method="post" id="form" novalidate>
            <p>Új hozzászólás írása:</p>
            <?php if($auth->is_authenticated()){ ?>
                <textarea name="textarea" id="textarea"> </textarea> <br> <span id="commentError" class="error"></span><br>
                
                <input type="submit" id="comment" value="Hozzászólok" name="comment"></button>
                
                
            <?php } else { ?>
                <textarea name="textarea" id="textarea" disabled> </textarea> <br> <span class="error">Ha hozzászólást szeretnél írni, előbb jelentkezz be!</span>
            <?php } ?>
            <br><br>
            <?php  
             if(isset($_POST['comment'])){
                $comments['text'] = $_POST['textarea'];
                $comments['teamid'] = $id;
                $comments['author'] = $auth->authenticated_user()['username'];
                date_default_timezone_set('Europe/Budapest');
                $comments['time'] = date("Y-m-d h:i:sa"); 
                $com->comment($comments);
                $comments = [];
                
            } ?>

            <?php $cm = $comment_storage->findAll();
            foreach($cm as $com){ 
                if($auth->is_authenticated()){
                if($auth->authenticated_user()['username'] != "admin"){
                    if($com['teamid'] == $id) {?>
                        <b id="commentinfo">A hozzászólást írta: </b> <?=$com['author'] ?><br>
                        <b id="commentinfo">A hozzászólás ideje: </b> <?=$com['time'] ?><br>
                        <b id="commentinfo">A hozzászólás: </b><?=$com['text'] ?><br><br><br>
                    <?php } ?>
                <?php }else{ ?>
                    
                    <div id="commentblock">
                        <?php if($com['teamid'] == $id) {?>
                        <b id="commentinfo">A hozzászólást írta: </b> <?=$com['author'] ?><br>
                        <b id="commentinfo">A hozzászólás ideje: </b> <?=$com['time'] ?><br>
                        <b id="commentinfo">A hozzászólás: </b><?=$com['text'] ?>
                    </div>
                    <a id="deletelink" href="delete.php?text=<?= $com['text']?>&user=<?=$com['author']?>&name=<?= $_GET['name'] ?>">Hozzászólás törlése</a><br><br>
                    <?php }?>
                    <?php }} ?>
            <?php } ?>

        </form>
        <a href="index.php" id="indexlink">Vissza a főoldalra</a>
    </main>

    <script>
                

        const txtarea = document.getElementById("textarea");
        txtarea.addEventListener('keyup', textTest);

        function textTest(e) {
            const btn = document.getElementById("comment");
            
            if(txtarea.value === ' '){
                btn.disabled = true;
                document.getElementById("commentError").innerHTML = "Üres szöveget nem küldhetsz hozzászólásként! Kérlek írj valamit!";
            } else {
                btn.disabled = false;
                document.getElementById("commentError").innerHTML = " ";
            }
        }
   
</script>

</body>
</html>