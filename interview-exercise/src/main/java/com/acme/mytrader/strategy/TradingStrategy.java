package com.acme.mytrader.strategy;

import com.acme.mytrader.execution.ExecutionService;
import com.acme.mytrader.price.PriceListener;

import java.util.HashMap;
import java.util.Optional;

/**
 * <pre>
 * User Story: As a trader I want to be able to monitor stock prices such
 * that when they breach a trigger level orders can be executed automatically
 * </pre>
 */
public class TradingStrategy implements PriceListener {

    private HashMap<String,Double> buyTriggers;
    private HashMap<String,Double> sellTriggers;
    private Integer amount;
    private ExecutionService executionService;

    public TradingStrategy(Integer amount, ExecutionService executionService) {
        this.amount = amount;
        this.executionService = executionService;

        sellTriggers = new HashMap<>();
        buyTriggers = new HashMap<>();
    }


    @Override
    public void priceUpdate(String security, double price) {
        Optional<Double> buyTrigger = Optional.ofNullable(buyTriggers.get(security));
        Optional<Double> sellTrigger = Optional.ofNullable(sellTriggers.get(security));

        if(buyTrigger.isPresent() && buyTrigger.get() > price){
            executionService.buy(security, buyTrigger.get(), amount);
            buyTriggers.remove(security, buyTrigger);
        }
        if (sellTrigger.isPresent() && sellTrigger.get() < price){
            executionService.sell(security, price, amount);
            sellTriggers.remove(security, sellTrigger);
        }
    }

    public void addSellTrigger(String security, double price){
        sellTriggers.put(security, price);
    }

    public void addBuyTrigger(String security, double price){
        buyTriggers.put(security, price);
    }

    public void clearTriggers(){
        sellTriggers.clear();
        buyTriggers.clear();
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}