package com.payment.common;

import com.payment.common.domain.Address;
import com.payment.common.domain.Item;
import com.payment.common.domain.Member;
import com.payment.common.domain.Stock;
import com.payment.common.repository.ItemRepository;
import com.payment.common.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class Config {

}
