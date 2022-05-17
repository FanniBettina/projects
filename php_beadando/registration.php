<?php

include('auth.php');
include('userstorage.php');
include('helper.php');

// functions
function validate($post, &$data, &$errors) {
    if(!isset($post['username']) || trim($post['username']) === ''){
        $errors['username'] = 'Felhasználónév hiányzik!';
    }
    else{
        $data['username'] = $post['username'];
    }

    if(!isset($post['email']) || trim($post['email']) === ''){
        $errors['email'] = 'E-mail cím hiányzik!';
    }else if(!filter_var($post['email'], FILTER_VALIDATE_EMAIL)){ 
        $errors['email'] = "E-mail cím formátuma nem megfelelő!";
    }else{
        $data['email'] = $post['email'];
    }

    if(!isset($post['password']) || trim($post['password']) === ''){
        $errors['password'] = 'Jelszó hiányzik!';
    }
    else{
        $data['password'] = $post['password'];
    }

    if($post['passwordAgain'] != $post['password']){
        $errors['passwordAgain'] = 'A jelszavak nem egyeznek meg!';
    }

  return count($errors) === 0;
}

// main
$user_storage = new UserStorage();
$auth = new Auth($user_storage);
$errors = [];
$data = [];
if (count($_POST) > 0) {
  if (validate($_POST, $data, $errors)) {
    if ($auth->user_exists($data['username'])) {
      $errors['global'] = "A felhasználó már létezik";
    } else {
      $auth->register($data);
      redirect('login.php');
    } 
  }
}

?>


<!DOCTYPE html>
<html lang="hu">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="style.css">
    <title>Regisztráció</title>
</head>
<body>
    <h1>Regisztráció</h1>

    <main id="homePage">
        <?php if (isset($errors['global'])) : ?>
            <p><span class="error"><?= $errors['global'] ?></span></p><br>
        <?php endif; ?>

    
        <br>
        <form action="" method="post" novalidate>
            <div id="registration">
                <label for="username"><b>Felhasználónév:</b> </label><br>
                <input type="text" name="username" id="username" value="<?= $_POST['username'] ?? "" ?>"><span class="error"><?= $errors['username'] ?? '' ?></span><br>
                
            </div>
            <div id="registration">
                <label for="email"><b>E-mail:</b> </label><br>
                <input type="email" name="email" id="email" value="<?= $_POST['email'] ?? "" ?>"><span class="error"><?= $errors['email'] ?? ''?></span><br>
                
            </div>
            <div id="registration">
                <label for="password"><b>Jelszó:</b> </label><br>
                <input type="password" name="password" id="password" value="<?= $_POST['password'] ?? "" ?>"><span class="error"><?= $errors['password'] ?? ''?></span><br>
                
            </div>
            <div id="registration">
                <label for="password"><b>Jelszó újra:</b> </label><br>
                <input type="password" name="passwordAgain" id="passwordAgain" value="<?= $_POST['passwordAgain'] ?? "" ?>"><span class="error"><?= $errors['passwordAgain'] ?? ''?></span><br>
                
            </div>
            <div>
                <br>
                <button type="submit" id="registrationButton">Regisztráció</button><br><br>
                <a href="index.php" id="indexlink">Vissza a főoldalra</a>
            </div>
        </form>
    </main>


</body>
</html>