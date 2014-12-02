<!DOCTYPE html>
<html>
<body>

<form action="api/findEvent.php" method="post" enctype="multipart/form-data">
    Create Event:<br />
    id (int) <input type="text" name="id"><br />
    OR<br />
    username (511 chars) <input type="text" name="username"><br />
    event_name (255 chars) <input type="text" name="event_name"><br />
    <input type="submit" value="Get event" name="submit">
</form>

</body>
</html>