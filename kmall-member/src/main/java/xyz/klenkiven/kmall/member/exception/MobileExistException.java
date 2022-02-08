package xyz.klenkiven.kmall.member.exception;

/**
 * Mobile phone number is exist
 * @author klenkiven
 */
public class MobileExistException extends RuntimeException{
    public MobileExistException() {
        super("phone");
    }
}
