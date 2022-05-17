<?php

    include('teamstorage.php');
    include('matchstorage.php');
    include('auth.php');
    include('userstorage.php');
    session_start();


    //beolvasas
    $teamStorage = new TeamStorage();
    $matchStorage = new MatchStorage();
    $user_storage = new UserStorage();
    $auth = new Auth($user_storage);
    

    $teams = $teamStorage->findAll();
    $matches = $matchStorage->findAll();
    
    /*function cmp($a, $b){
        $a = date('Y-m-d', strtotime($a));
        $b = date('Y-m-d', strtotime($b));
    }*/

    $array = [];
    foreach( $matches as $m){
        array_push($array, $m['date']);
    }
    function sortFunction( $a, $b ) {
        return strtotime($a) - strtotime($b);
    }
    usort($array, "sortFunction");
    
    
    

?>

<!DOCTYPE html>
<html lang="hu">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="style.css">
    <title>Eötvös Loránd Stadion</title>
</head>
<body>
    <h1 >Üdvözöljük az Eötvös Loránd Stadion <br> hivatalos honlapján</h1>
    <div id="logins" >
        <?php if(!($auth->is_authenticated())){ ?>
        <a id="login" href="login.php" >Bejelentkezés </a> 
        <?php } else { ?> <a id="login" href="logout.php" >Kijelentkezés <?php }?></a> 
        <a id="login" href="registration.php">Regisztráció </a>
    </div>
    <main id="homePage">
        <div id="informations">
            <p>Az Eötvös Loránd Stadion szeretne megjelenni az interneten is, ehhez szeretnének egy weboldalt, ahol megjelennek a náluk játszott meccsek, illetve szeretnék, hogy a rajongók tudják követni kedvenceik eredményeit. Az ELTE hallgatóinak köszönhetően mostmár bárki olvashat a stadionról, a benne zajló meccsekről, a résztvevő csapatokról és minden más hozzá kapcsolódó eseményről. <br><br>
            A Stadion az 1900-as évek végefelé épült, hogy otthont adjon hazai és külföldi meccseknek egyaránt. Nagy befogadóképességű, sport- illetve zenei rendezvények lebonyolítására egyaránt alkalmas. Külön kiemelendő, hogy amikor éppen üres a stadion, az ELTE hallgatói ingyen bemehetnek focizni, futni vagy éppen csak kicsit elvonulni a nagy világ elől.

            </p>
        </div>
    </main>

    <main id="homePage">
        <h2>Csapatok</h2>
        <ul>
           <?php foreach($teams as $t){ ?>
                <li><a id="link" href='teamdetails.php?name=<?= $t['name'] ?>' target="_blank"><?= $t['name'] ?></a></li> 
           <?php } ?>
        </ul>

        <h2>Legutóbbi 5 lejátszott meccs</h2>
        <ul>
            <?php $size = count($array);
            $k = 5;
            ?>
            <?php for( $i = $size - 1;  $k > 0 && $i > 0; $i--){ 
                for($j = 1; $j <= count($matches); $j++){
                        if($array[$i] == $matches[$j]['date']){
                            if($matches[$j]['home']['score'] != "") {
                                foreach($teams as $t){ 
                                    $id1 = $matches[$j]['home']['id']; 
                                    $id2 = $matches[$j]['away']['id'];
                                    if($t['id'] === $id1){
                                        $name1 = $t['name']; }
                                    if($t['id'] === $id2){
                                        $name2 = $t['name']; }
                                }?>
                                <li><?= $name1?> - <?= $name2?></li> 
                                
                                <?php $k--;
                            } 

                        } ?>
                        
                    <?php } 
            } ?>
        </ul>


        
    </main>
</body>
</html>