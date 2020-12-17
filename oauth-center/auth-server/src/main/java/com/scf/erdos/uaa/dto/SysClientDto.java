package com.scf.erdos.uaa.dto;

import java.util.List;
import java.util.Set;

import com.scf.erdos.uaa.model.SysClient;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class SysClientDto extends SysClient {

    private static final long serialVersionUID = 1475637288060027265L;

    private List<Long> permissionIds;

    private Set<Long> serviceIds;

}
