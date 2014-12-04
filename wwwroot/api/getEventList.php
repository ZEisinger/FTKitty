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

if ( array_key_exists( "username", $_GET ) && array_key_exists( "event_name", $_GET ) )
{
  $_POST['username'] = $_GET['username'];
  $_POST['event_name'] = $_GET['event_name'];
}

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
                    name,
                    end
                FROM
                    events
                WHERE
                    END = 0
                  AND
                    ";

if ( strtolower ( $_POST["visibility"] ) == "public" ) {
  $sql_state = $sql_state . "vis_public = 1";
  if (!($prep = $sql->prepare($sql_state))) {
    array_push( $error, "MySQL Error - unable to prepare statement." );
  }
} else {
  $ids = explode(",", $_POST["friend_list"]);
  $in = join(',', array_fill(0, count($ids), '?'));
  $sql_state = $sql_state . "vis_public = 0
                  AND
                    username IN ($in)";
  if (!($prep = $sql->prepare($sql_state))) {
    array_push( $error, "MySQL Error - unable to prepare statement." );
  }
  $prep->bind_param(str_repeat('s', count($ids)), ...$ids);
}

$result = array(
            'event_id' => null,
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
            'name' => null,
            'end' => null
          );

if ( sizeof($error) == 0)
{
  $prep->bind_result(
            $result['event_id'],
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
            $result['name'],
            $result['end']
         );

  /* execute the statement */
  $prep->execute();
}

$array_results = array();

if ( sizeof($error) == 0)
{

  /* now we want to fetch the data from the database */
  while ($prep->fetch()) {
    array_push( $array_results,
            array(
              'event_id' => $result['event_id'],
              'username' => $result['username'],
              'event_name' => $result['event_name'],
              'description' => $result['description'],
              'location' => $result['location'],
              'hashtag' => $result['hashtag'],
              'event_date' => $result['event_date'],
              'event_time' => $result['event_time'],
              'vis_public' => $result['vis_public'] ? 'public' : 'private',
              'image_name' => $result['image_name'],
              'payment_email' => $result['payment_email'],
              'name' => $result['name'],
              'end' => $result['end'] ? 'history' : 'active'
            )
          );
  }
}

/* close the statement */
$prep->close();

if ( sizeof($error) > 0 ) {
  if ( $invalid_type ) {
    $object = array(
        'status' => '400',
        'errors' => $error,
        'result' => $array_results
      );
    echo json_encode( $object );
  } else {
    $object = array(
        'status' => '500',
        'errors' => 'There was a problem preparing your statement',
        'result' => $array_results
      );
    echo json_encode( $object );
  }
} else {
  $object = array(
      'status' => '200',
      'errors' => null,
      'result' => $array_results
	  );
  echo json_encode( $object );
}


/* close up */
  
  

  
  
?>