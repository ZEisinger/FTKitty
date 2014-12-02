<!DOCTYPE html>
<html>
<body>

<form action="api/insertEvent.php" method="post" enctype="multipart/form-data">
    Create Event:<br />
    username (511 chars) <input type="text" name="username"><br />
    event_name (255 chars) <input type="text" name="event_name"><br />
    description (1023 chars) <input type="text" name="description"><br />
    location (127 chars) <input type="text" name="location"><br />
    hashtag (31 chars) <input type="text" name="hashtag"><br />
    event_date (31 chars) <input type="text" name="event_date"><br />
    event_time (31 chars) <input type="text" name="event_time"><br />
    visibility (public or other) <input type="text" name="visibility"><br />
    image_name (127 chars) <input type="text" name="image_name"><br />
    payment_email (63 chars) <input type="text" name="payment_email"><br />
    end (true or false, default false) <input type="text" name="end"><br />
    <input type="submit" value="Create event" name="submit">
</form>

</body>
</html>