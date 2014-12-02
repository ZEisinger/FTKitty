<!DOCTYPE html>
<html>
<body>

<form action="api/confirmPayment.php" method="post" enctype="multipart/form-data">
    Create Event:<br />
    (<br />
    id (int) <input type="text" name="id"><br />
    OR<br />
    event_username (511 chars) <input type="text" name="event_username"><br />
    event_name (255 chars) <input type="text" name="event_name"><br />)<br />
    username (511 chars) <input type="text" name="username"><br />
    receive_payment_email (63 chars) <input type="text" name="receive_payment_email"><br />
    payment_amount (numeric(15,2)) <input type="text" name="payment_amount"><br />
    
    <input type="submit" value="Get event" name="submit">
</form>

</body>
</html>