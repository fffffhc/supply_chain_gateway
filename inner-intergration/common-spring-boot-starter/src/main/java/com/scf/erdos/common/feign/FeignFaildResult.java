package com.scf.erdos.common.feign;

import lombok.Data;

@Data
public class FeignFaildResult {
	
	 private int status;
	 private String resp_msg;
}
