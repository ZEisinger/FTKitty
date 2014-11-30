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
$event_id = null;

$sql = new mysqli( $db_desti, $db_uname, $db_pword, $db_dbase );

if ($sql->connect_errno) {
    array_push ( $error, "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error );
}
if ( array_key_exists( "id", $_POST ) && strtolower ( $_POST["id"] ) != "" )
{
  $event_id = $_POST['id'];
} else {
  $sql_state = "SELECT
                      id
                  FROM
                      events
                  WHERE
                      username = ?
                    AND
                      event_name = ?";
  if (!($prep = $sql->prepare($sql_state))) {
    array_push( $error, "MySQL Error - unable to prepare statement." );
  }
  $prep->bind_param("ss", $_POST["event_username"], $_POST["event_name"]);
  $prep->bind_result($event_id);

  /* execute the statement */
  $prep->execute();

  /* now we want to fetch the data from the database */
  $prep->fetch();

  /* close the statement */
  $prep->close();
}

$sql_state = "INSERT INTO attendence (username, event_id, payment_amount, payment_email) VALUES (?,?,?,?)";


/* make sure we can prepare the statement */
if ( $prep = $sql->prepare($sql_state) ) {
  /* first we want to bind $findname to the first ? in our statement
  since it is a string, we'll use 's' */
  $prep->bind_param("siss", $_POST["username"], $event_id, $_POST["payment_amount"], 
                    $_POST["receive_payment_email"]);

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