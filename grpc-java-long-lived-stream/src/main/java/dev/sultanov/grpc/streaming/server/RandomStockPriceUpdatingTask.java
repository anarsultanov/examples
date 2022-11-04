package dev.sultanov.grpc.streaming.server;

import java.util.concurrent.ThreadLocalRandom;

public class RandomStockPriceUpdatingTask implements Runnable {

    private final StockRepository repository = StockRepository.INSTANCE;

    @Override
    public void run() {
        while (true) {
            updateRandomStock();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void updateRandomStock() {
        var random = ThreadLocalRandom.current();
        var stocks = repository.getStocks();
        var randomStock = stocks.stream().skip(random.nextInt(stocks.size())).findFirst().orElseThrow();
        var newPrice = randomStock.getPrice() + random.nextDouble(-5.0, 5.0);
        randomStock.updatePrice(newPrice);
    }
}
