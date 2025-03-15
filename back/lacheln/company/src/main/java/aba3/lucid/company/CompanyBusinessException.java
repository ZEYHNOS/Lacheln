package aba3.lucid.company;

public class CompanyBusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public CompanyBusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
