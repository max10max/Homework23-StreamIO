import org.w3c.dom.*;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
   private static List<String> loadConfigXML = new ArrayList<>();
   private static List<String> saveConfigXML = new ArrayList<>();
   private static List<String> logConfigXML = new ArrayList<>();

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        settingsFromXML();
        File fileForBasketLoad = new File(loadConfigXML.get(1));
        File fileForBasketSave = new File(saveConfigXML.get(1));
        File fileForLog = new File(logConfigXML.get(1));
        String[] products = {"Молоко", "Хлеб", "Гречневая крупа"};
        int[] prices = {50, 14, 80};
        int[] userBuy = {0, 0, 0}; //Массив содержит количество купленных товаров относительно массива products
        Scanner scanner = new Scanner(System.in);
        ClientLog clientLog = new ClientLog();
        Basket basket;

        if(loadConfigXML.get(0).equals("true")) {
                if (loadConfigXML.get(2).equals("json")){
                    clientLog.loadAsJSON(fileForBasketLoad);
                    basket = new Basket(products, prices);
                    basket.setUserChoose(clientLog.getUserBuy());
                    basket.printCard();
                    userBuy = basket.getUserChoose();
                } else {
                    basket = Basket.loadFromTxtFile(fileForBasketLoad);
                    basket.setPrices(prices);
                    basket.printCard();
                    userBuy = basket.getUserChoose();
                }
        } else {
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
            clientLog.log(productNumber, productCount);
            switch (productNumber) {
                case 1: {
                    userBuy[0] += productCount;
                    basket.addToCart(0, userBuy[0]);
                    basket.printCard();
                    continue;
                }
                case 2: {
                    userBuy[1] += productCount;
                    basket.addToCart(1, userBuy[1]);
                    basket.printCard();
                    continue;
                }
                case 3: {
                    userBuy[2] += productCount;
                    basket.addToCart(2, userBuy[2]);
                    basket.printCard();
                    continue;
                }
            }
        }

        clientLog.setUserBuy(userBuy);


        if(saveConfigXML.get(0).equals("true")){
            if(saveConfigXML.get(2).equals("json")){
                clientLog.exportAsJSON(fileForBasketSave);
            } else{
                basket.saveTxt(fileForBasketSave);
            }
        }

        if(logConfigXML.get(0).equals("true")){
            clientLog.exportAsCSV(fileForLog);
        }
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

    public static void settingsFromXML() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("shop.xml"));
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();

        Element loadElement = (Element) nodeList.item(1);
        loadConfigXML.add(0,loadElement.getElementsByTagName("enabled").item(0).getTextContent());
        loadConfigXML.add(1,loadElement.getElementsByTagName("fileName").item(0).getTextContent());
        loadConfigXML.add(2,loadElement.getElementsByTagName("format").item(0).getTextContent());

        Element saveElement = (Element) nodeList.item(3);
        saveConfigXML.add(0,saveElement.getElementsByTagName("enabled").item(0).getTextContent());
        saveConfigXML.add(1,saveElement.getElementsByTagName("fileName").item(0).getTextContent());
        saveConfigXML.add(2,saveElement.getElementsByTagName("format").item(0).getTextContent());

        Element logElement = (Element) nodeList.item(5);
        logConfigXML.add(0,logElement.getElementsByTagName("enabled").item(0).getTextContent());
        logConfigXML.add(1,logElement.getElementsByTagName("fileName").item(0).getTextContent());

    }
}
