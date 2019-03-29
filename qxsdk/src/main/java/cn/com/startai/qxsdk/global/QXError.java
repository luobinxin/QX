package cn.com.startai.qxsdk.global;

/**
 * Created by Robin on 2019/3/21.
 * 419109715@qq.com 彬影
 */
public class QXError extends QXErrorCode {
    protected String errcode;
    protected String errmsg;

    public QXError() {
    }

    public QXError(String errcode) {
        this.errcode = errcode;
        this.errmsg = QXErrorCode.getErrorMsgByCode(errcode);
    }

    public QXError(String errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "QXError{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }


}
