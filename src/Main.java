import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File fileForBasket = new File("basket.txt");
        String[] products = {"Молоко", "Хлеб", "Гречневая крупа"};
        int[] prices = {50, 14, 80};
        int[] userBuy = {0, 0, 0}; //Массив содержит количество купленных товаров относительно массива products
        Scanner scanner = new Scanner(System.in);
        Basket basket;
        if (fileForBasket.exists()) {
            basket = Basket.loadFromTxtFile(fileForBasket);
            basket.setPrices(prices);
            basket.printCard();
            userBuy = basket.getUserChoose();
        } else {
          //  boolean fileCreate = fileForBasket.createNewFile();
            basket = new Basket(products, prices);
        }


        System.out.println("Список возможных товаров для покупки");
        for (int i = 0; i < products.length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт");
        }

        while (true) {
            System.out.println("Выберете товар и количество или введите end");
            String input = scanner.nextLine();
            if ("end".equals(input)) break;

            String[] userChoose = input.split(" ");
            int productNumber = Integer.parseInt(userChoose[0]);
            int productCount = Integer.parseInt(userChoose[1]);
            switch (productNumber) {
                case 1: {
                    userBuy[0] += productCount;
                    basket.addToCart(0, userBuy[0]);
                    basket.printCard();
                    continue;
                }
                case 2: {
                    userBuy[1] += productCount;
                    basket.addToCart(1, userBuy[0]);
                    basket.printCard();
                    continue;
                }
                case 3: {
                    userBuy[2] += productCount;
                    basket.addToCart(2, userBuy[0]);
                    basket.printCard();
                    continue;
                }
            }
        }
        basket.saveTxt(fileForBasket);
        shoping(products, prices, userBuy);

    }

    public static void shoping(String[] products, int[] prices, int[] userBuy) {
        System.out.println("Ваша корзина:");
        int totalSum = 0;
        for (int i = 0; i < products.length; i++)
            if (userBuy[i] > 0) {
                int sum = userBuy[i] * prices[i];
                totalSum += sum;
                System.out.println(products[i] + " " + userBuy[i] + " шт " + prices[i] + " руб/шт" +
                        " " + sum + " в сумме");
            }
        System.out.println("Итого " + totalSum + " руб");
    }
}
