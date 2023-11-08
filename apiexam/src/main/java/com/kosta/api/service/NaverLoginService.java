package com.kosta.api.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.api.dao.UserDao;
import com.kosta.api.dto.Token;
import com.kosta.api.dto.UserInfo;

@Service
public class NaverLoginService {

	@Autowired
	private UserDao userDao;

	public UserInfo naverLogin(String code, String state) throws Exception {
		Token token = getAccessToken(code, state);
		UserInfo loginUser = getUserInfo(token);
		System.out.println(token);
		UserInfo userInfo = userDao.selectUser(loginUser.getId());
		if (userInfo == null) { // 회원가입 되어있지 않음
			userDao.insertUserByNaver(loginUser); // 회원가입함
			userInfo = loginUser;
		}
		return userInfo;

	}

	public Token getAccessToken(String code, String state) throws Exception {
		String clientId = "XbyjZbaHaEtGuMNV2DBv";
		String clientSecret = "HpXJu918L6";
		String redirectURI = URLEncoder.encode("http://localhost:8090/api/naverlogin", "UTF-8");
		StringBuilder apiURL = new StringBuilder("https://nid.naver.com/oauth2.0/token?"); // stringbuilder를 하는 이유가
		apiURL.append("grant_type=authorization_code");
		apiURL.append("&client_id=" + clientId);
		apiURL.append("&client_secret=" + clientSecret);
		apiURL.append("&redirect_uri=" + redirectURI);
		apiURL.append("&code=" + code);
		apiURL.append("&state=" + state);

		URL url = new URL(apiURL.toString());
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		BufferedReader br;
		System.out.print("responseCode=" + responseCode);
		if (responseCode == 200) { // 정상 호출
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} else { // 에러 발생
			br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}
		String inputLine;
		StringBuffer res = new StringBuffer();
		while ((inputLine = br.readLine()) != null) {
			res.append(inputLine);
		}
		br.close();
		System.out.println(res.toString());
		if (responseCode != 200) {
			throw new Exception(res.toString());
		}
		Token token = new Token();

		JSONParser parser = new JSONParser();
		JSONObject tokenObj = (JSONObject) parser.parse(res.toString());

		token.setAccessToken((String) tokenObj.get("access_token"));
		token.setTokenType((String) tokenObj.get("token_type"));
		return token;
	}

	public UserInfo getUserInfo(Token token) throws Exception { // naver로부터 token으로 사용자 정보 얻어오기
		URL url = new URL("https://openapi.naver.com/v1/nid/me");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET"); // 메소드 지정
		conn.setRequestProperty("Authorization", token.getTokenType() + " " + token.getAccessToken()); // 요청

		BufferedReader br; // scanner와 유사 입력받은 데이터가 String으로 고정
		int resultCode = conn.getResponseCode();

		if (resultCode >= 200 && resultCode <= 300) {// 코드가 200~300 사이면 정상
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else { // 에러
			br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

		}
		StringBuilder resBuilder = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			resBuilder.append(line);
		}
		br.close();
		conn.disconnect();
		System.out.println(resBuilder.toString());

		JSONParser parser = new JSONParser();
		JSONObject resObj = (JSONObject) parser.parse(resBuilder.toString());
		JSONObject user = (JSONObject) resObj.get("response");
		String id = (String) user.get("id");
		String nickname = (String) user.get("nickname");
		String age = (String) user.get("age");
		String gender = (String) user.get("gender");
		String email = (String) user.get("email");
		String mobile = (String) user.get("mobile");
		String name = (String) user.get("name");
		String birthday = (String) user.get("birthday");

		UserInfo userInfo = new UserInfo(id, nickname, email, gender, name, mobile, age, birthday);
		return userInfo;
	}
}
