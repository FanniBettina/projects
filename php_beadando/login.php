<?php

include('auth.php');
include('helper.php');
include('userstorage.php');

// functions
function validate($post, &$data, &$errors) {
  if(!isset($post['username']) || trim($post['username']) === ''){
    $errors['username'] = 'Felhasználónév hiányzik!';
  }
  else{
    $data['username'] = $post['username'];
  }

  if(!isset($post['password']) || trim($post['password']) === ''){
    $errors['password'] = 'Jelszó hiányzik!';
  }
  else{
      $data['password'] = $post['password'];
  }

  return count($errors) === 0;
}

// main
session_start();
$user_storage = new UserStorage();
$auth = new Auth($user_storage);
$data = [];
$errors = [];
if ($_POST) {
  if (validate($_POST, $data, $errors)) {
    $auth_user = $auth->authenticate($data['username'], $data['password']);
    if (!$auth_user) {
      $errors['global'] = "Bejelentkezési hiba";
    } else {
      $auth->login($auth_user);
      redirect('index.php');
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
    <title>Bejelentkezés</title>
</head>
<body>
    <h1>Bejelentkezés</h1>

    
    <main id="homePage">
        <?php if (isset($errors['global'])) : ?>
            <p><span class="error"><?= $errors['global'] ?></span></p><br>
        <?php endif; ?>

        <?php if (isset($errors['username'])) : ?>
                <span class="error"><?= $errors['username'] ?></span><br>
        <?php endif; ?>

        <?php if (isset($errors['password'])) : ?>
                <span class="error"><?= $errors['password'] ?></span><br>
        <?php endif; ?>
        <br>
        <form action="" method="post" novalidate>
          <div id="registration">
              <label for="username"><b>Felhasználónév:</b> </label><br>
              <input type="text" name="username" id="username" value="<?= $_POST['username'] ?? "" ?>">
          </div>
          <div  id="registration">
              <label for="password"><b>Jelszó:</b> </label><br>
              <input type="password" name="password" id="password" value="<?= $_POST['password'] ?? "" ?>">  
          </div>
          <br>
          <div>
              <button type="submit" id="registrationButton">Bejelentkezés</button>
          </div>
        </form>
        <br>
        <a href="registration.php" id="registrationLink">Regisztráció</a>
    </main>

</body>
</html>