package com.kosta.api.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.api.dao.UserDao;
import com.kosta.api.dto.UserInfo;

@Service
public class KakaoLoginService {
	@Autowired
	private UserDao userDao;

	public UserInfo kakaoLogin(String code) throws Exception {
		String token = getAccessToken(code);
		System.out.println(token);
		UserInfo kakaoInfo = getUserInfo(token);// 사용자 정보 받기
		UserInfo userInfo = userDao.selectUser(kakaoInfo.getId());
		if (userInfo == null) {
			userDao.insertUserByKakao(kakaoInfo);
			userInfo = kakaoInfo;
		}
		return userInfo;
	}

	// request
	public String getAccessToken(String code) throws Exception { // kakao로부터 토큰 요청하여 받기
		StringBuilder urlBuilder = new StringBuilder("https://kauth.kakao.com/oauth/token");
		URL url = new URL(urlBuilder.toString()); // java.neturl
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST"); // 메소드 지정
		conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 요청
		conn.setDoOutput(true); // 출력 스트림 활성화(파라미터 body 영역에 넣기위해)

		// 파라미터 생성
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream())); // 스트림에다 꽂아 넣는다
		StringBuilder param = new StringBuilder();
		param.append("grant_type=authorization_code"); // 요청
		param.append("&client_id=25026a8d2b03657741ee2c7b317c8e4d"); // 요청
		param.append("&redirect_uri=http://localhost:8090/api/kakaologin"); // 요청
		param.append("&code=" + code);

		// 생성한 파라미터를 출력스트림에 쓰기(body에 넣기)
		bw.write(param.toString());
		bw.flush();

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
		System.out.println(resBuilder.toString());// 같음

		JSONParser parser = new JSONParser(); // 토큰 파싱처리 제이슨 아래에서 파싱 처리하는거
		JSONObject tokenObj = (JSONObject) parser.parse(resBuilder.toString());
		String token = (String) tokenObj.get("access_token");
		return token;
	}

	public UserInfo getUserInfo(String token) throws Exception { // kakao로부터 token으로 사용자 정보 얻어오기
		StringBuilder urlBuilder = new StringBuilder("https://kapi.kakao.com/v2/user/me");
		URL url = new URL(urlBuilder.toString()); // java.neturl
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST"); // 메소드 지정
		conn.setRequestProperty("Authorization", "Bearer " + token); // 요청

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

		JSONParser parser = new JSONParser(); // 데이터 주고받는거는 json이다 파싱처리하기
		JSONObject user = (JSONObject) parser.parse(resBuilder.toString());
		String id = (Long) user.get("id") + "";
		JSONObject properties = (JSONObject) user.get("properties");
		String nickname = (String) properties.get("nickname");
		String profileImage = (String) properties.get("profile_image");
		String thumbnailImage = (String) properties.get("thumbnail_image");

		JSONObject kakaoAccount = (JSONObject) user.get("kakao_account");
		String email = (String) kakaoAccount.get("email");
		String gender = (String) kakaoAccount.get("gender");

		UserInfo userInfo = new UserInfo(id, nickname, profileImage, thumbnailImage, email, gender);

		return userInfo;

	}
}
