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

$page = json_decode(file_get_contents("http://cmsc436.striveforthehighest.com/api/findEvent.php?username=" . $_POST["username"] . "&event_name=" . $_POST["event_name"]), true);

if ($page['result'] === null)
{

  $sql = new mysqli( $db_desti, $db_uname, $db_pword, $db_dbase );

  if ($sql->connect_errno) {
      array_push ( $error, "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error );
  }

  if ( strtolower ( $_POST["visibility"] ) == "public" )
    $sql_state = "INSERT INTO events (username, event_name, description, location, hashtag, event_date, event_time, vis_public, image_name, payment_email, end) VALUES (?,?,?,?,?,?,?,1,?,?";
  else
    $sql_state = "INSERT INTO events (username, event_name, description, location, hashtag, event_date, event_time, vis_public, image_name, payment_email, end) VALUES (?,?,?,?,?,?,?,0,?,?";

  if ( strtolower ( $_POST["end"] ) == "true" )
    $sql_state = $sql_state . ",1)";
  else
    $sql_state = $sql_state . ",0)";
    
  // TODO:  check duplicates username + event_name should be unique

  /* make sure we can prepare the statement */
  if ( $prep = $sql->prepare($sql_state) ) {
    /* first we want to bind $findname to the first ? in our statement
    since it is a string, we'll use 's' */
    $prep->bind_param("sssssssss", $_POST["username"], $_POST["event_name"], $_POST["description"], 
                      $_POST["location"], $_POST["hashtag"], $_POST["event_date"], 
                      $_POST["event_time"], $_POST["image_name"], $_POST["payment_email"]);

    /* execute the statement */
    $prep->execute();

    /* now we want to fetch the data from the database */
    $prep->fetch();
    
    /* close the statement */
    $prep->close();

    $id = mysqli_insert_id($sql);
   
  } else {
      array_push( $error, "MySQL Error - unable to prepare statement." );
  }
} else {
  array_push( $error, "event already exists" );
  $invalid_type = true;
}

if ( sizeof($error) > 0 ) {
  if ( $invalid_type ) {
    $object = array(
        'status' => '400',
        'errors' => $error,
        'id' => $id
      );
    echo json_encode( $object );
  } else {
    $object = array(
        'status' => '500',
        'errors' => 'There was a problem preparing your statement',
        'id' => $id
      );
    echo json_encode( $object );
  }
} else {
  $object = array(
      'status' => '200',
      'errors' => null,
      'id' => $id
	  );
  echo json_encode( $object );
}


/* close up */
  
?>