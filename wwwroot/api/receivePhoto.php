<?php
error_reporting(E_ALL); 
ini_set( 'display_errors','1');
$target_dir = "../storage/pictures/";
$target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);
$imageFileType =  strtolower( pathinfo( $target_file,PATHINFO_EXTENSION ));
$error = array("Sorry, only JPG, JPEG, PNG & GIF files are allowed.");
$name = null;
$mime_type = null;
$invalid_type = false;

// Allow certain file formats (JPG, JPEG, PNG, and GIF)
$array_types = array("jpg","png","jpeg","gif");
foreach ($array_types as $type) {
  if( strcmp ( $imageFileType, $type ) == 0 ) {
    $error = array();
  }
}

$check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
if($check !== false) {
  $mime_type = $check["mime"];
} else {
  array_push ( $error, "File is not an image." );
}

// Check if file already exists
if (file_exists($target_file)) {
  array_push ( $error, "Sorry, file already exists." );
}

// Check file size (currently 50 kb)
if ($_FILES["fileToUpload"]["size"] > 10000000) {
  array_push ( $error, "Sorry, your file is too large." );
}

// Check if $uploadOk is set to 0 by an error
if ( count($error) > 0 ) {
  $invalid_type = true;
// if everything is ok, try to upload file
} else {
  if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target_file)) {
    $name = basename( $_FILES["fileToUpload"]["name"] );
  }
}

if ( sizeof($error) > 0 ) {
  if ( $invalid_type ) {
    $object = array(
        'status' => '400',
        'errors' => $error,
        'name' => null,
        'mime_type' => null
      );
    echo json_encode( $object );
  } else {
    $object = array(
        'status' => '500',
        'errors' => 'Sorry, there was an error uploading your file.',
        'name' => null,
        'mime_type' => null
      );
    echo json_encode( $object );
  }
} else {
  $object = array(
      'status' => '200',
      'errors' => null,
      'name' => $name,
      'mime_type' => $mime_type
	  );
  echo json_encode( $object );
}
?>