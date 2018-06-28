
package cashfreeUser_java_integration;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class cashfreeUser {
    public static String token;
    public static Long expiry;
    public static String clientId;
    public static String clientSecret;
    public static  String stage;


    public String clientAuth(String clientId, String clientSecret, String stage) throws Exception {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.stage = stage.toUpperCase();
        HashMap<String,String> headers =new HashMap<String,String>();

        String postData = "";

        headers.put("X-Client-Id",clientId);
        headers.put("X-Client-Secret", clientSecret);
        String link = "";
        if (stage == "TEST") {
            link = "https://payout-gamma.cashfree.com//payout/v1/authorize";
        }
        else if (stage == "PROD"){

            link = "https://payout-api.cashfree.com//payout/v1/authorize";
        }

        String response = makePostCall(link,headers,postData);



        JSONParser parser = new JSONParser();
        JSONObject responseObj = (JSONObject) parser.parse(response);
        String status = (String) responseObj.get("status");
        String message = ((String)responseObj.get("message"));
        if (status.equals("ERROR")){
            return message;
        }
        else {
            String getToken = (String) ((JSONObject) responseObj.get("data")).get("token");
            String tempexpiry = (String) ((JSONObject) responseObj.get("data")).get("expiry");
            Long getExpiry = Long.parseLong(tempexpiry);


            this.token = getToken;
            this.expiry = getExpiry;

            return getToken;
        }
    }


    public String makePostCall(String link, Map<String, String> headers, String postData)
    {
        String response = "";
        try {
            URL myURL = new URL(link);

            HttpURLConnection conn = (HttpURLConnection)myURL.openConnection();

            for(Map.Entry m:headers.entrySet()){
                conn.setRequestProperty(m.getKey().toString(),m.getValue().toString());
            }

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", "" + postData.getBytes().length);

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                response += output;
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static String getParamsString(Map<String, String> params)  {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(entry.getKey()); // Using URLEncoder is recommended here
            result.append("=");
            result.append(entry.getValue()); // Using URLEncoder is recommended here too
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }

    public String makeGetCall(String endpoint, Map<String, String> headers, Map<String, String> getParametersMap)
    {
        String response = "";

        try {

            if(!getParametersMap.isEmpty()){
                String getData = getParamsString(getParametersMap);
                endpoint += getData;
            }

            URL myURL = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection)myURL.openConnection();
            for(Map.Entry m:headers.entrySet()){
                conn.setRequestProperty(m.getKey().toString(),m.getValue().toString());
            }

            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            while ((output = br.readLine()) != null) {
                response += output;
            }
            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }



    public String expiryCheck() throws Exception {
        //checks to see if the expiry time is < 1 min and regenerates token accordingly

        Long expirytime  = this.expiry;
        long currenttime;
        currenttime = System.currentTimeMillis();
        currenttime = (long) (currenttime/1000.0);

        if (expirytime - currenttime <=60){

            cashfreeUser temp = new cashfreeUser();
            return temp.clientAuth(this.clientId, this.clientSecret, this.stage);
        }

        else {
            return "";
        }
    }



    public String addBeneficiary(String beneId, String name, String email, String phone, String bankAccount, String ifsc, String address1, String address2, String vpa, String city, String state, String pincode) throws Exception {

        if ((beneId == null) || (name  == null) || (email == null ) || (phone == null) || (address1 ==null)){
            return  "Mandatory parameters missing";
        }
        else{
            HashMap<String,String> userParam =new HashMap<String,String>();
            //created a hashmap with the input received
            userParam.put("beneId",beneId);
            userParam.put("name", name);
            userParam.put("email",email);
            userParam.put("phone",phone);
            userParam.put("bankAccount", bankAccount);
            userParam.put("ifsc", ifsc);
            userParam.put("address1", address1);
            userParam.put("address2",address2);
            userParam.put("vpa",vpa);
            userParam.put("city",city);
            userParam.put("state",state);
            userParam.put("pincode",pincode);

            JSONObject userParamJson = new JSONObject();
            userParamJson.putAll(userParam);




            cashfreeUser temp = new cashfreeUser();
            temp.expiryCheck();
            String token = this.token;

            HashMap<String,String> headers =new HashMap<String,String>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + token);
            String link = "";
            if (this.stage == "TEST") {
                link = "https://payout-gamma.cashfree.com/payout/v1/addBeneficiary";
            }
            else if (this.stage == "PROD")
            {
                link = "https://payout-api.cashfree.com/v1/addBeneficiary";
            }

            String response = makePostCall(link,headers,userParamJson.toString());

            JSONParser parser = new JSONParser();
            JSONObject responseObj = (JSONObject) parser.parse(response);

            return responseObj.toJSONString();

        }

    }

    public String getBalance() throws  Exception{

        String token = this.token;

        HashMap<String,String> header =new HashMap<String,String>();
        header.put("Authorization", "Bearer " + token);

        cashfreeUser temp = new cashfreeUser();
        temp.expiryCheck();
        String link = "";

        if (this.stage == "TEST") {

            link = "https://payout-gamma.cashfree.com/payout/v1/getBalance";}
        else if (this.stage == "PROD") {
            link =  "https://payout-api.cashfree.com/payout/v1/getBalance";
        }


        HashMap<String,String> getData = new HashMap<String, String>();

        String response = makeGetCall(link,header,getData);

        JSONParser parser = new JSONParser();
        JSONObject responseObj = (JSONObject) parser.parse(response);

        return responseObj.toJSONString();

    }

    public String requestTransfer(String beneId, String amount, String transferId, String transferMode, String remarks) throws Exception {

        if ((beneId == null) || (amount  == null) || (transferId == null )){
            return  "Mandatory parameters missing";
        }
        else{

            HashMap<String,String> userRequestParam =new HashMap<String,String>();
            userRequestParam.put("beneId", beneId);
            userRequestParam.put("amount", amount);
            userRequestParam.put("transferId", transferId);
            userRequestParam.put("transferMode", transferMode);
            userRequestParam.put("remarks", remarks);

            JSONObject userRequestParamJson = new JSONObject();
            userRequestParamJson.putAll(userRequestParam);

            String token = this.token;

            cashfreeUser temp = new cashfreeUser();
            temp.expiryCheck();

            HashMap<String,String> headers =new HashMap<String,String>();
            headers.put("Authorization","Bearer " + token);

            String link = "https://payout-gamma.cashfree.com/payout/v1/requestTransfer";

            String response = makePostCall(link, headers, userRequestParamJson.toString());

            JSONParser parser = new JSONParser();
            JSONObject responseObj = (JSONObject) parser.parse(response);

            return responseObj.toJSONString();

        }
    }

    public String getTransferStatus(String transferId) throws Exception {

        String token = this.token;

        HashMap<String,String> header =new HashMap<String,String>();
        header.put("Authorization", "Bearer " + token);
        header.put("transferId", transferId);
        HashMap<String,String> getData =new HashMap<String,String>();


        cashfreeUser temp = new cashfreeUser();
        temp.expiryCheck();
        String link = "";
        if (this.stage == "TEST") {
            link = "https://payout-gamma.cashfree.com/payout/v1/requestTransfer";
        }
        else if (this.stage =="PROD")
        {
            link = "https://payout-api.cashfree.com//v1/requestTransfer";
        }


        String response = makeGetCall(link, header, getData);

        JSONParser parser = new JSONParser();
        JSONObject responseObj = (JSONObject) parser.parse(response);

        return responseObj.toJSONString();


    }

    public String bankDetailsValidation(String name, String phone, String bankAccount, String ifsc) throws ParseException {

        if ((name == null) || (phone  == null) || (bankAccount == null ) || (ifsc == null)){
            return  "Mandatory parameters missing";
        }
        else {
            String token = this.token;
            String link = "";
            if (this.stage == "TEST") {
                link = "https://payout-gamma.cashfree.com/payout/v1/validation/bankDetails" + "?name=" + name + "&phone=" + phone + "&bankAccount=" + bankAccount + "&ifsc=" + ifsc;
            }
            else if (this.stage =="PROD")
            {
                link = "https://payout-api.cashfree.com/payout/v1/validation/bankDetails" + "?name=" + name + "&phone=" + phone + "&bankAccount=" + bankAccount + "&ifsc=" + ifsc;

            }


            HashMap<String,String> header =new HashMap<String,String>();
            header.put("Authorization", "Bearer " + token);
            header.put("name", name );
            header.put("phone", phone);
            header.put("bankAccount", bankAccount);
            header.put("ifsc", ifsc);

            HashMap<String,String> getData =new HashMap<String,String>();

            String response = makeGetCall(link, header, getData);

            JSONParser parser = new JSONParser();
            JSONObject responseObj = (JSONObject) parser.parse(response);

            return responseObj.toJSONString();


        }

    }

    public static void main(String[] args) throws Exception{
        cashfreeUser newuser = new cashfreeUser();

        System.out.println(newuser.clientAuth("dummyClientId", "dummyClientSecret","TEST/PROD"));
        newuser.expiryCheck();
        System.out.println(newuser.addBeneficiary("JOHN180120","john doe", "johndoe@cashfree.com", "9876543210","00091181202233","HDFC0000001","ABC Street","add 2","vpa","Bangalore", "Karnataka","560001" ));
        System.out.println(newuser.getBalance());
        System.out.println(newuser.requestTransfer("JOHN18011","100","76723288672267867867","banktransfer","optional"));
        System.out.println(newuser.getTransferStatus("76723288672267867867"));
        System.out.println(newuser.bankDetailsValidation("Joh","9910115208", "00011020001772", "HDFC0000001"));


    }
}


