import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class callapi {

    public static void main(String[] args) throws  IOException {
        System.out.println("Welcome to Stanza API Tester");
        callAnalyze();
    }

    public static void callAnalyze() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL url = new URL("http://localhost:8000/analyze");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setDoOutput(true);

        RequestData requestData = new RequestData();
        requestData.text = "Hello my Name is Lukas";
        requestData.lang = "en";
        String tests = objectMapper.writeValueAsString(requestData);

        // Send Request to Stanza API
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = tests.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Read response from Stanza API
        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String inputLine = "";
        String inputLineList = "";
        while ((inputLine = in.readLine()) != null) {
            inputLineList = inputLine.substring(1, inputLine.length() -1);
            System.out.println(inputLineList);
        }
        List<ResponseData> ListOfResponseData = objectMapper.readValue(inputLineList,  new TypeReference<>(){});

        for (ResponseData response: ListOfResponseData) {
            System.out.println("Response id: " + response.id + " Text: " + response.text + " Upos: " + response.upos);
        }
        in.close();
        con.disconnect();
    }
    
}
