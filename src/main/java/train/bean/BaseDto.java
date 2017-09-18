package train.bean;

import java.util.List;

/**
 * Created by xie on 17/9/17.
 */
public class BaseDto {

    private String validateMessagesShowId;
    private boolean status;
    private int httpstatus;
    private ValidateMessages validateMessages;
    private List<?> messages;


    public String getValidateMessagesShowId() {
        return validateMessagesShowId;
    }

    public void setValidateMessagesShowId(String validateMessagesShowId) {
        this.validateMessagesShowId = validateMessagesShowId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getHttpstatus() {
        return httpstatus;
    }

    public void setHttpstatus(int httpstatus) {
        this.httpstatus = httpstatus;
    }

    public ValidateMessages getValidateMessages() {
        return validateMessages;
    }

    public void setValidateMessages(ValidateMessages validateMessages) {
        this.validateMessages = validateMessages;
    }

    public List<?> getMessages() {
        return messages;
    }

    public void setMessages(List<?> messages) {
        this.messages = messages;
    }

    public static class ValidateMessages {
    }
}
