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
$sum = 0;
$eventCount = 0;

$sql = new mysqli( $db_desti, $db_uname, $db_pword, $db_dbase );

if ($sql->connect_errno) {
    array_push ( $error, "Failed to connect to MySQL: (" . $mysqli->connect_errno . ") " . $mysqli->connect_error );
}

  $sql_state = "SELECT
                    events.id,
                    payment_amount,
                    events.username AS username,
                    event_name,
                    description,
                    location,
                    hashtag,
                    event_date,
                    event_time,
                    vis_public,
                    image_name,
                    attendence.payment_email,
                    name,
                    end
                FROM
                    attendence
                LEFT OUTER JOIN
                    events
                  ON
                    events.id = attendence.event_id
                WHERE
                    attendence.username = ?";

if (!($prep = $sql->prepare($sql_state))) {
  array_push( $error, "MySQL Error - unable to prepare statement." );
}
$prep->bind_param("s", $_POST["username"]);

$result = array(
            'id' => null,
            'payment_amount' => null,
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
            $result['id'],
            $result['payment_amount'],
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

  $array_results = array();
  
  /* now we want to fetch the data from the database */
  while ($prep->fetch()) {
    $sum += $result['payment_amount'];
    array_push( $array_results,
            array(
              'event_id' => $result['id'],
              'payment_amount' => $result['payment_amount'],
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
    $eventCount = $eventCount + 1;
  }

  /* close the statement */
  $prep->close();
}

if ( sizeof($error) > 0 ) {
  if ( $invalid_type ) {
    $object = array(
        'status' => '400',
        'errors' => $error,
        'result' => $array_results,
        'sum' => $sum,
        'eventCount' => $eventCount
      );
    echo json_encode( $object );
  } else {
    $object = array(
        'status' => '500',
        'errors' => 'There was a problem preparing your statement',
        'result' => $array_results,
        'sum' => $sum,
        'eventCount' => $eventCount
      );
    echo json_encode( $object );
  }
} else {
  $object = array(
      'status' => '200',
      'errors' => null,
      'result' => $array_results,
      'sum' => $sum,
      'eventCount' => $eventCount
	  );
  echo json_encode( $object );
}


/* close up */
  
  

  
  
?>