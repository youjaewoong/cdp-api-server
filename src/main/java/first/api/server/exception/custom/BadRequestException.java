package first.api.server.exception.custom;

import first.api.server.exception.error.ErrorCode;
import first.api.server.exception.error.PlatformErrorCode;

public class BadRequestException extends CmpException {

  /* serialVersionUID */
  private static final long serialVersionUID = 1L;

  public static String PARAM_KEY_FIELD = "field";

  public BadRequestException(ErrorCode errorCode) {
    super(errorCode, null);
  }

  public BadRequestException(String extMessage) {
    super(PlatformErrorCode.InvalidInput, extMessage);
  }
}
