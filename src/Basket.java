import com.sun.source.tree.NewArrayTree;

import java.io.*;
import java.util.Arrays;

public class Basket {
    private String[] products;
    private int[] prices;
    private int[] userChoose; // Кол-во выбранного товара покупателем

    private File textFile;

    public Basket(String[] prod, int[] cost) {
        this.products = prod;
        this.prices = cost;
        userChoose = new int[products.length];
    }

    public void setUserChoose(int[] amount){
        this.userChoose = amount;
    }
    public int[] getUserChoose(){
        return userChoose;
    }

    public void setTextFile(File textFile) {
        this.textFile = textFile;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }

    public void addToCart(int productNumber, int amount) {
        userChoose[productNumber] =  amount;
//        System.out.printf("Продукт \"%s\" добавлен в корзину в количестве %d штук!\n",
//                products[productNumber], amount);
    }

    public void printCard() {
        System.out.println("Ваша корзина покупок:");
        for (int i = 0; i < products.length; i++) {
            if (userChoose[i] != 0) {
                System.out.println(products[i] + " в количестве - " + userChoose[i] + " шт");
            }
        }
    }

    public void saveTxt(File textFile) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(textFile, false))) {
            for (String product : products) {
                out.write(product + "  ");
            }
            out.append("\n");
            for (int amount : userChoose) {
                out.write(amount + "  ");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static Basket loadFromTxtFile(File textFile) {
        String[] prodFromFile = null;
        int[] amount = null;
        try (BufferedReader in = new BufferedReader(new FileReader(textFile))) {
            String s;
            while ((s = in.readLine()) != null) {
                String[] basketFromFile = s.split("  ");
                try {
                    amount = new int[basketFromFile.length];
                    for (int i = 0; i < amount.length; i++) {
                        amount[i] = Integer.parseInt(basketFromFile[i]);
                    }
                } catch (NumberFormatException e) {
                    prodFromFile = basketFromFile;
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Basket basket = new Basket(prodFromFile, new int[prodFromFile.length]);
        basket.setUserChoose(amount);
        return basket;
    }
}
