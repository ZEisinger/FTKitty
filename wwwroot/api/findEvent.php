<?php
error_reporting(E_ALL); 
ini_set( 'display_errors','1');

$error = array();
$db_uname = "autobot436";
$db_pword = "FeedMe!";
$db_dbase = "cmsc436";
$db_desti = "localhost";

$sql_state = "";
$invalid_type = false;
$id = null;

$sql = new mysqli( $db_desti, $db_uname, $db_pword, $db_dbase );

if ($sql->connect_errno) {
    array_push ( $error, "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error );
}

  $sql_state = "SELECT
                    id,
                    username,
                    event_name,
                    description,
                    location,
                    hashtag,
                    event_date,
                    event_time,
                    vis_public,
                    image_name,
                    payment_email,
                    end
                FROM
                    events
                WHERE
                    ";

if ( array_key_exists( "id", $_POST ) && strtolower ( $_POST["id"] ) != "" ) {
  $sql_state = $sql_state . "id = ?";
  if (!($prep = $sql->prepare($sql_state))) {
    array_push( $error, "MySQL Error - unable to prepare statement." );
  }
  $prep->bind_param("s", $_POST["id"]);
} else {
  $sql_state = $sql_state . "username = ?
                  AND
                    event_name = ?
                  AND
                    end = 0";
  if (!($prep = $sql->prepare($sql_state))) {
    array_push( $error, "MySQL Error - unable to prepare statement." );
  }
  $prep->bind_param("ss", $_POST["username"], $_POST["event_name"]);
}

$result = array(
            'id' => null,
            'username' => null,
            'event_name' => null,
            'description' => null,
            'location' => null,
            'hashtag' => null,
            'event_date' => null,
            'event_time' => null,
            'vis_public' => null,
            'image_name' => null,
            'payment_email' => null,
            'end' => null
          );

if ( sizeof($error) == 0)
{
  $prep->bind_result(
            $result['id'],
            $result['username'],
            $result['event_name'],
            $result['description'],
            $result['location'],
            $result['hashtag'],
            $result['event_date'],
            $result['event_time'],
            $result['vis_public'],
            $result['image_name'],
            $result['payment_email'],
            $result['end']
         );

  /* execute the statement */
  $prep->execute();

  /* now we want to fetch the data from the database */
  $prep->fetch();

  /* close the statement */
  $prep->close();
}

if ( sizeof($error) > 0 ) {
  if ( $invalid_type ) {
    $object = array(
        'status' => '400',
        'errors' => $error,
        'result' => $result
      );
    echo json_encode( $object );
  } else {
    $object = array(
        'status' => '500',
        'errors' => 'There was a problem preparing your statement',
        'id' => $result
      );
    echo json_encode( $object );
  }
} else {
  $object = array(
      'status' => '200',
      'errors' => null,
      'id' => $result
	  );
  echo json_encode( $object );
}


/* close up */
  
  

  
  
?>