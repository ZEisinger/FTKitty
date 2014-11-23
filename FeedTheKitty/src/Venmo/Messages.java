package Venmo;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Messages {

    public static String safeJSON(JSONObject object, String key){
        try {
            return object.has(key) ? object.getString(key) : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class User{
        private String userName;
        private String firstName;
        private String lastName;
        private String displayName;
        private String about;
        private String pictureUrl;
        private String dateJoined;
        private String userId;

        public User(JSONObject object){

            this.userName = safeJSON(object, "username");
            this.firstName = safeJSON(object, "first_name");
            this.lastName = safeJSON(object, "last_name");
            this.displayName = safeJSON(object, "display_name");
            this.about = safeJSON(object, "about");
            this.pictureUrl = safeJSON(object, "profile_picture_url");
            this.dateJoined = safeJSON(object, "date_joined");
            this.userId = safeJSON(object, "id");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            User user = (User) o;

            if (!userId.equals(user.userId)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return userId.hashCode();
        }

        public String getUserName() {
            return userName;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getAbout() {
            return about;
        }

        public String getPictureUrl() {
            return pictureUrl;
        }

        public String getDateJoined() {
            return dateJoined;
        }

        @Override
        public String toString() {
            return "User{" +
                    "\n\t\tuserName='" + userName + '\'' +
                    ", \n\t\tfirstName='" + firstName + '\'' +
                    ", \n\t\tlastName='" + lastName + '\'' +
                    ", \n\t\tdisplayName='" + displayName + '\'' +
                    ", \n\t\tabout='" + about + '\'' +
                    ", \n\t\tpictureUrl='" + pictureUrl + '\'' +
                    ", \n\t\tdateJoined='" + dateJoined + '\'' +
                    "\n}";
        }

    }

    public static class ErrorResponse{
        private String originalMessage;
        private String message;
        private String code;

        public ErrorResponse(String originalMessage){
            this.originalMessage = originalMessage;

            JSONTokener tokener = new JSONTokener(originalMessage);

            try{
                JSONObject root = new JSONObject(tokener);
                if(root.has("error") && root.getJSONObject("error").has("message")){
                    JSONObject errMessage = root.getJSONObject("error");

                    this.message = safeJSON(errMessage, "message");
                    this.code = safeJSON(errMessage, "code");
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        public String getOriginalMessage() {
            return originalMessage;
        }

        public String getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return "ErrorResponse{" +
                    "\tmessage='" + message + '\'' +
                    ", \tcode='" + code + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ErrorResponse that = (ErrorResponse) o;

            if (!originalMessage.equals(that.originalMessage)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return originalMessage.hashCode();
        }
    }

    public static class PaymentResponse{
        private String originalMessage;
        private String status;
        private String refund;
        private String medium;
        private String fee;
        private String dateTimeCompleted;
        private User userReceiver;
        private User userSender;
        private String audience;
        private String note;
        private String amount;
        private String action;
        private String id;
        private ErrorResponse errorResponse;

        public PaymentResponse(String originalMessage){
            this.originalMessage = originalMessage;
            this.errorResponse = null;

            JSONTokener tokener = new JSONTokener(originalMessage);

            try {
                JSONObject root = new JSONObject(tokener);

                if(root.has("data") && root.getJSONObject("data").has("payment")) {
                    JSONObject payment = root.getJSONObject("data").getJSONObject("payment");
                    this.status = safeJSON(payment, "status");
                    this.refund = safeJSON(payment, "refund");
                    this.medium = safeJSON(payment, "medium");
                    this.fee = safeJSON(payment, "fee");
                    this.dateTimeCompleted = safeJSON(payment, "date_completed");
                    this.audience = safeJSON(payment, "audience");
                    this.note = safeJSON(payment, "note");
                    this.amount = safeJSON(payment, "amount");
                    this.action = safeJSON(payment, "action");
                    this.id = safeJSON(payment, "id");

                    if (payment.has("target")) {
                        JSONObject target = payment.getJSONObject("target");
                        if (target.has("user")) {
                            this.userReceiver = new User(target.getJSONObject("user"));
                        }

                    }

                    if (payment.has("actor")) {
                        JSONObject actor = payment.getJSONObject("actor");
                        if (actor.has("user")) {
                            this.userSender = new User(actor.getJSONObject("user"));
                        }

                    }
                }else if(root.has("error") && root.getJSONObject("error").has("message")){
                    this.errorResponse = new ErrorResponse(originalMessage);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PaymentResponse that = (PaymentResponse) o;

            if (!id.equals(that.id)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        public ErrorResponse getErrorResponse() {
            return errorResponse;
        }

        public String getOriginalMessage() {
            return originalMessage;
        }

        public String getStatus() {
            return status;
        }

        public String getRefund() {
            return refund;
        }

        public String getMedium() {
            return medium;
        }

        public String getFee() {
            return fee;
        }

        public String getDateTimeCompleted() {
            return dateTimeCompleted;
        }

        public User getUserReceiver() {
            return userReceiver;
        }

        public User getUserSender() {
            return userSender;
        }

        public String getAudience() {
            return audience;
        }

        public String getNote() {
            return note;
        }

        public String getAmount() {
            return amount;
        }

        public String getAction() {
            return action;
        }

        @Override
        public String toString() {
            return "PaymentResponse{" +
                    "\n\tstatus='" + status + '\'' +
                    ", \n\trefund='" + refund + '\'' +
                    ", \n\tmedium='" + medium + '\'' +
                    ", \n\tfee='" + fee + '\'' +
                    ", \n\tdateTimeCompleted='" + dateTimeCompleted + '\'' +
                    ", \n\tuserReceiver=" + userReceiver +
                    ", \n\tuserSender=" + userSender +
                    ", \n\taudience='" + audience + '\'' +
                    ", \n\tnote='" + note + '\'' +
                    ", \n\tamount='" + amount + '\'' +
                    ", \n\taction='" + action + '\'' +
                    "\n}";
        }
    }

}

