package me.zeanzai.seatatccorder.service;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @author shawnwang
 */
@LocalTCC
public interface TccService {

    @TwoPhaseBusinessAction(name = "tccService", commitMethod = "commit", rollbackMethod = "rollback")
    boolean tryCreateOrder(BusinessActionContext businessActionContext,
                           @BusinessActionContextParameter(paramName = "userId") String userId,
                           @BusinessActionContextParameter(paramName = "orderId") String orderId,
                           @BusinessActionContextParameter(paramName = "productId") Long productId,
                           @BusinessActionContextParameter(paramName = "num") Long num);


    boolean commit(BusinessActionContext businessActionContext);

    boolean rollback(BusinessActionContext businessActionContext);

}
