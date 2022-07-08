<?php
class BasicPoints{
  private $data;
  private $selectedDepartment;
  private $examResults;
  private $allExamsPassed = true;

  function __construct($array) {
    $this->data = $array;
    $this->selectedDepartment = $this->data["valasztott-szak"];
    $this->examResults = $this->data["erettsegi-eredmenyek"];
  }

  function get_data() {
    return $this->data;
  }

  function get_selectedDepartment(){
    return $this->selectedDepartment;
  }

  function studentPassedAllExams(){
    foreach($this->examResults as $i => $exam){
      if($this->examResults[$i]['eredmeny'] < 20){
        return $this->examResults[$i]['nev'];
      }
    }
    return "ok";
  }

  function studentHadTheMandatoryExams(){
    $b1 = false;
    $b2 = false;
    $b3 = false;
    foreach($this->examResults as $i => $exam){
      if($this->examResults[$i]['nev'] === "magyar nyelv és irodalom"){
        $b1 = true;
      }else if($this->examResults[$i]['nev'] === "történelem"){
        $b2 = true;
      } else if( $this->examResults[$i]['nev'] === "matematika"){
        $b3 = true;
      }
    }
    return $b1 && $b2 && $b3;
  }


  function subjectsForPPKE($subjectName){
    return $subjectName === "francia" || $subjectName === "német" || $subjectName === "olasz" || $subjectName === "orosz" || $subjectName === "spanyol" ||  $subjectName === "történelem";
  }

  function subjectsForELTE($subjectName){
    return $subjectName === "biológia" || $subjectName === "fizika" || $subjectName === "informatika" || $subjectName === "kémia";
  }

  function pointCanBeCalculated(){
    $b1 = false;
    $b2 = false;
    if($this->selectedDepartment['egyetem'] === "ELTE"){
      foreach($this->examResults as $i => $exam){
        if($this->examResults[$i]['nev'] === "matematika"){
          $b1 = true;
        }
        if($this->subjectsForELTE($this->examResults[$i]['nev']) === true){
          $b2 = true;
        }
      }
    }else if($this->selectedDepartment['egyetem'] === "PPKE"){
      foreach($this->examResults as $i => $exam){
        if($this->examResults[$i]['nev'] === "angol" && $this->examResults[$i]['tipus'] === "emelt"){
          $b1 = true;
        }
        if($this->subjectsForPPKE($this->examResults[$i]['nev']) === true){
          $b2 = true;
        }
      }
    }
    return $b1 && $b2;
  }

  function basicPointCalculator(){
    $points = 0;
    $max = 0;
    if($this->pointCanBeCalculated() && $this->studentHadTheMandatoryExams() && $this->studentPassedAllExams() === "ok"){
      if($this->selectedDepartment['egyetem'] === "ELTE"){
        foreach($this->examResults as $i => $exam){
          if($this->examResults[$i]['nev'] === "matematika"){
            $points += floatval($this->examResults[$i]['eredmeny']);
          }
          if($this->subjectsForELTE($this->examResults[$i]['nev']) === true){
            if($this->examResults[$i]['eredmeny'] > $max ){
              $max = floatval($this->examResults[$i]['eredmeny']); 
            }
          }
        }
        $points += $max;
      }else if($this->selectedDepartment['egyetem'] === "PPKE"){
        foreach($this->examResults as $i => $exam){
          if($this->examResults[$i]['nev'] === "angol" && $this->examResults[$i]['tipus'] === "emelt"){
            $points += floatval($this->examResults[$i]['eredmeny']);
          }
          if($this->subjectsForPPKE($this->examResults[$i]['nev']) === true){
            if($this->examResults[$i]['eredmeny'] > $max ){
              $max = floatval($this->examResults[$i]['eredmeny']); 
            }
          }
        }
        $points += $max;
      }
    }else{
      return 0;
    }
    return 2*$points;
  }



}
?>