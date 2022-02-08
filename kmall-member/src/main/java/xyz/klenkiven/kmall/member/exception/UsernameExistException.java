package xyz.klenkiven.kmall.member.exception;

/**
 * Username is Exist
 * @author KlenKiven
 */
public class UsernameExistException extends RuntimeException {
    public UsernameExistException() {
        super("username");
    }
}
