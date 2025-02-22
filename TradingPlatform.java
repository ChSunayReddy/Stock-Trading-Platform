import java.util.*;

// Stock class to represent a stock
class Stock {
    String symbol;
    String name;
    double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }
}

// Market class to manage stocks
class Market {
    private Map<String, Stock> stocks = new HashMap<>();
    private Random random = new Random();

    public void addStock(String symbol, String name, double price) {
        stocks.put(symbol, new Stock(symbol, name, price));
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public void updateStockPrices() {
        for (Stock stock : stocks.values()) {
            double change = (random.nextDouble() * 10 - 5);
            stock.price = Math.max(1, stock.price + change);
        }
    }

    public void displayMarket() {
        System.out.println("Stock Market:");
        for (Stock stock : stocks.values()) {
            System.out.println(stock.symbol + " - " + stock.name + " : $" + stock.price);
        }
    }
}

// Portfolio class to manage user stocks
class Portfolio {
    private Map<String, Integer> holdings = new HashMap<>();
    private double balance;
    private Market market;

    public Portfolio(double balance, Market market) {
        this.balance = balance;
        this.market = market;
    }

    public void buyStock(String symbol, int quantity) {
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        double cost = stock.price * quantity;
        if (cost > balance) {
            System.out.println("Insufficient balance.");
            return;
        }

        balance -= cost;
        holdings.put(symbol, holdings.getOrDefault(symbol, 0) + quantity);
        System.out.println("Bought " + quantity + " shares of " + symbol);
    }

    public void sellStock(String symbol, int quantity) {
        if (!holdings.containsKey(symbol) || holdings.get(symbol) < quantity) {
            System.out.println("Not enough shares to sell.");
            return;
        }

        Stock stock = market.getStock(symbol);
        double revenue = stock.price * quantity;
        balance += revenue;
        holdings.put(symbol, holdings.get(symbol) - quantity);
        if (holdings.get(symbol) == 0) holdings.remove(symbol);
        System.out.println("Sold " + quantity + " shares of " + symbol);
    }

    public void displayPortfolio() {
        System.out.println("Portfolio:");
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            System.out.println(entry.getKey() + " - Shares: " + entry.getValue());
        }
        System.out.println("Balance: $" + balance);
    }
}

// TradingPlatform class to simulate user interaction
public class TradingPlatform {
    public static void main(String[] args) {
        Market market = new Market();
        market.addStock("AAPL", "Apple Inc.", 150);
        market.addStock("GOOGL", "Alphabet Inc.", 2800);
        market.addStock("TSLA", "Tesla Inc.", 700);

        Portfolio portfolio = new Portfolio(5000, market);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. View Market\n2. Buy Stock\n3. Sell Stock\n4. View Portfolio\n5. Update Market\n6. Exit");
            int choice = scanner.nextInt();

            switch(choice) {
                case 1:
                    market.displayMarket();
                    break;
                case 2:
                    System.out.print("Enter stock symbol: ");
                    String buySymbol = scanner.next();
                    System.out.print("Enter quantity: ");
                    int buyQty = scanner.nextInt();
                    portfolio.buyStock(buySymbol, buyQty);
                    break;
                case 3:
                    System.out.print("Enter stock symbol: ");
                    String sellSymbol = scanner.next();
                    System.out.print("Enter quantity: ");
                    int sellQty = scanner.nextInt();
                    portfolio.sellStock(sellSymbol, sellQty);
                    break;
                case 4:
                    portfolio.displayPortfolio();
                    break;
                case 5:
                    market.updateStockPrices();
                    System.out.println("Market prices updated.");
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
