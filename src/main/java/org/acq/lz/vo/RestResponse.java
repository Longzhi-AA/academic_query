package org.acq.lz.vo;

public class RestResponse<T> {

    private String code;

    private String message;

    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public RestResponse<T> buildSuccessResponse(T data){
        RestResponse<T> response = new RestResponse<T>();
        response.setCode("0");
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    public RestResponse<String> buildFailResponse(String code, String message){
        RestResponse<String> response = new RestResponse<String>();
        response.setCode(code);
        response.setMessage(message);
        response.setData( "request fail");
        return response;
    }
}
