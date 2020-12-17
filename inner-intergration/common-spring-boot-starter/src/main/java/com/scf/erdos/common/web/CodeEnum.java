package com.scf.erdos.common.web;

/**
 * @Description :
 * @author：bao-clm
 * @date: 2019/2/4
 * @version：1.0
 */
public enum CodeEnum {
    SUCCESS(200),
    ERROR(-1);

    private Integer code;
    CodeEnum(Integer code){
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
