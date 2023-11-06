package com.kosta.api.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import com.kosta.api.dto.AnimalClinic;
import com.kosta.api.dto.PageInfo;

@Service
public class SeoulApiService {
	public List<AnimalClinic> animalClinicList(PageInfo pageInfo) throws Exception {
		int startIdx = (pageInfo.getCurPage() - 1) * 10 + 1;
		StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
		urlBuilder.append("/" + URLEncoder.encode("6f426d644b6b696e383975536a5849", "UTF-8")); // 인증키 여기에 넣음
		urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8")); // 요청 파일 타입 type 문자단위 데이터 주고받는거
		urlBuilder.append("/" + URLEncoder.encode("LOCALDATA_020301", "UTF-8")); // 서비스 이름
		urlBuilder.append("/" + URLEncoder.encode(startIdx + "", "UTF-8"));
		urlBuilder.append("/" + URLEncoder.encode(startIdx + 10 - 1 + "", "UTF-8")); // 요청하기 위한 url만드는 작업
		// 원래 row때문에

		// 입출력은 무조건 stream
		// request
		URL url = new URL(urlBuilder.toString()); // java.neturl
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET"); // 메소드 지정
		conn.setRequestProperty("Content-type", "application/json"); // 요청

		// response
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

		List<AnimalClinic> acList = new ArrayList<>();
		JSONParser parser = new JSONParser(); // 파싱처리
		JSONObject mobj = (JSONObject) parser.parse(resBuilder.toString());
		JSONObject LOCALDATA_020301 = (JSONObject) mobj.get("LOCALDATA_020301"); // JSONOBJECT로 캐스팅
		Long list_total_count = (Long) LOCALDATA_020301.get("list_total_count");
		JSONArray row = (JSONArray) LOCALDATA_020301.get("row");

		for (int i = 0; i < row.size(); i++) {
			JSONObject acJson = (JSONObject) row.get(i);
			String trdStateNm = (String) acJson.get("TRDSTATENM");
			String siteTel = (String) acJson.get("SITETEL");
			String rdNwhlAddr = (String) acJson.get("RDNWHLADDR");
			String bplcNm = (String) acJson.get("BPLCNM");
			String x = (String) acJson.get("X");
			String y = (String) acJson.get("Y");
			acList.add(new AnimalClinic(trdStateNm, siteTel, rdNwhlAddr, bplcNm, x, y));

		}
		int allPage = (int) Math.ceil(list_total_count.doubleValue() / 10);
		int startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		int endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		if (pageInfo.getCurPage() > allPage)
			pageInfo.setCurPage(allPage);
		return acList;
	}
}
