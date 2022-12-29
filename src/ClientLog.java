import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class ClientLog {
    private List<Integer> buyList = new ArrayList<>();
    private int[] userBuy;


    public void setUserBuy(int[] userBuy) {
        this.userBuy = userBuy;
    }

    public int[] getUserBuy() {
        return userBuy;
    }

    public void log(int productNum, int amount) {
        buyList.add(productNum);
        buyList.add(amount);
    }

    public void exportAsCSV(File txtFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(txtFile, false))) {
            writer.write("productNum,amount\n");
            for (int i = 0; i < buyList.size(); i = i + 2)
                writer.write(buyList.get(i) + " " + buyList.get(i + 1) + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportAsJSON(File file) {
        JSONObject obj = new JSONObject();
        JSONArray userBuyJson = new JSONArray();
        for (int x : userBuy) {
            userBuyJson.add(x);
        }
        obj.put("userBuy", userBuyJson);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(obj.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAsJSON(File file) {
        JSONParser parser = new JSONParser();
        ArrayList<Integer> buyFromJSON = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader((file)));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray userBuy = (JSONArray) jsonObject.get("userBuy");
            for (Object amount : userBuy) {
                buyFromJSON.add(Integer.parseInt(amount.toString()));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        userBuy = new int[buyFromJSON.size()];
        for (int i = 0; i < buyFromJSON.size(); i++) {
            userBuy[i] = buyFromJSON.get(i);
        }
    }
}
