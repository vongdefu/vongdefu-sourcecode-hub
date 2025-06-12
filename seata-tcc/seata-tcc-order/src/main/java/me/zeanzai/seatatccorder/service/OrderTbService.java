package me.zeanzai.seatatccorder.service;

import me.zeanzai.seatatccorder.model.OrderTb;

/**
 * @author shawnwang
 */
public interface OrderTbService {

    OrderTb create(Long productId, String userId, Long num);

    Void createTcc(String userId, Long productId, Long num);

}
