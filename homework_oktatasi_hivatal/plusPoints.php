<?php
class PlusPoints{
  private $data;
  private $examResults;
  private $plusPoints;

  function __construct($array) {
    $this->data = $array;
    $this->examResults = $this->data["erettsegi-eredmenyek"];
    $this->plusPoints = $this->data["tobbletpontok"];
  }

  function languageExam(){
    $points = 0;
    $found = false;
    foreach($this->plusPoints as $i => $l){
      if($this->plusPoints[$i]['kategoria'] === 'Nyelvvizsga' && $this->plusPoints[$i]['tipus'] === 'B2'){
        if($points + 28 <= 100){
          foreach($this->plusPoints as $j => $k){
            if($this->plusPoints[$j]['tipus'] === 'C1' && $this->plusPoints[$j]['nyelv'] === $this->plusPoints[$i]['nyelv']){
              $points += 0;
              $found = true;
            }
          }
          if($found === false){
            $points += 28;
          }
        }else{
          $points = 100;
        }
      }else if($this->plusPoints[$i]['kategoria'] === 'Nyelvvizsga' && $this->plusPoints[$i]['tipus'] === 'C1'){
        if($points + 40 <= 100){
          $points += 40;
        }else{
          $points = 100;
        }
      }
    }

    return $points;
  }

  function advancedFinalExam(){
    $points = $this->languageExam();
    foreach($this->examResults as $i => $exam){
      if($this->examResults[$i]['tipus'] === 'emelt'){
        if($points + 50 <= 100){
          $points += 50;
        }else{
          $points = 100;
        }
      }
    }
    return $points;
  }
}
?>