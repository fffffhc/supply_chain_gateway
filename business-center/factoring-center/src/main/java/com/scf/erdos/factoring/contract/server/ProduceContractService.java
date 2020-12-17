package com.scf.erdos.factoring.contract.server;

public interface ProduceContractService {
    /**
     * 生成融资项目相关的合同
     * @param id
     * @return
     */
    void generateFinancingProjectContract(Integer id);
}
