<!DOCTYPE html>
<html>
<body>

<form action="api/getEventList.php" method="post" enctype="multipart/form-data">
    Create Event:<br />
    visiblity (private or public) <input type="text" name="visibility"><br />
    if private : <br />
    friend_list (511 chars) <input type="text" name="friend_list"><br />
    <input type="submit" value="Get event" name="submit">
</form>

</body>
</html>