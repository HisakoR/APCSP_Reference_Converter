package scripts;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class updater {

    private static final String GITHUB_API_URL = "https://api.github.com/repos/HisakoR/Nans_APCSP_Pseudocode_Converter/releases/latest";

    public int getVersionNumber(){
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(GITHUB_API_URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 解析 JSON 响应
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray assets = jsonResponse.getJSONArray("assets");

            // 查找名为 version.vsn 的文件
            String versionVsnUrl = null;
            for (int i = 0; i < assets.length(); i++) {
                JSONObject asset = assets.getJSONObject(i);
                if ("version.vsn".equals(asset.getString("name"))) {
                    versionVsnUrl = asset.getString("url");
                    break;
                }
            }

            if (versionVsnUrl != null) {
                // 获取 version.vsn 文件内容
                connection = (HttpURLConnection) new URL(versionVsnUrl).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/octet-stream");
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                // 读取第一行并返回数字
                String firstLine = reader.readLine();
                reader.close();
                return Integer.parseInt(firstLine.trim());
            }
            else {
                System.out.println("File version.vsn not found in the latest release.");
                return -1; // 文件未找到，返回-1或其他适当的值
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            return -1; // 出现异常，返回-1或其他适当的值
        }
    }
}
