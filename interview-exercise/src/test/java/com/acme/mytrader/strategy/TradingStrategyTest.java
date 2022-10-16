package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class TradingStrategyTest {

    @Mock
    ExecutionService executionService;
    @Mock
    PriceSource priceSource;

    TradingStrategy tradingStrategy;

    @Before
    public void  init(){
        tradingStrategy = new TradingStrategy(10, executionService);
        tradingStrategy.addSellTrigger("BlackRock", 25.0d);
        tradingStrategy.addBuyTrigger("BlackRock", 20.0d);
    }

    @Test
    public void testTradingStrategy_buy(){
        //price for BlackRock exact as the buy trigger point so buy() should NOT be executed
        tradingStrategy.priceUpdate("BlackRock", 20.0d);
        Mockito.verify(executionService, Mockito.times(0)).buy(anyString(), anyDouble(), anyInt());

        //price for BlackRock below the buy trigger point so buy() should be executed
        tradingStrategy.priceUpdate("BlackRock", 19.0d);
        Mockito.verify(executionService, Mockito.times(1)).buy(anyString(), anyDouble(), anyInt());

    }

    @Test
    public void testTradingStrategy_sell(){
        //price for BlackRock exact as the sell trigger point so sell() should NOT be executed
        tradingStrategy.priceUpdate("BlackRock", 25.0d);
        Mockito.verify(executionService, Mockito.times(0)).sell(anyString(), anyDouble(), anyInt());

        //price for BlackRock below the buy trigger point so sell should be executed
        tradingStrategy.priceUpdate("BlackRock", 27.0d);
        Mockito.verify(executionService, Mockito.times(1)).sell(anyString(), anyDouble(), anyInt());

    }
}
