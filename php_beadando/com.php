<?php

    include_once('storage.php');
    class Com{
        private $user_storage;
        private $user = NULL;

        public function __construct(IStorage $user_storage) {
            $this->user_storage = $user_storage;
        
            if (isset($_SESSION["user"])) {
              $this->user = $_SESSION["user"];
            }
        }

        public function comment($comment){
            $comments = [
                'author'  => $comment['author'],
                'text'  => $comment['text'],
                'teamid'  => $comment['teamid'],
                'time' => $comment['time'],
                
            
            ];
            return $this->user_storage->add($comment);
            
        }

        public function delete($comment){
            return $this->user_storage->unset($comment);
        }
    }


?>