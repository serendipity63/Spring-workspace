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

import com.kosta.api.dto.ElecCharge;
import com.kosta.api.dto.PageInfo;

@Service
public class ElecChargeService {
	public List<ElecCharge> elecChargeList(PageInfo pageInfo) throws Exception {
		// 1. URL을 만들기 위한 StringBuilder
		StringBuilder urlBuilder = new StringBuilder("https://api.odcloud.kr/api/EvInfoServiceV2/v1/getEvSearchList?");
		urlBuilder.append("page=" + URLEncoder.encode(pageInfo.getCurPage() + "", "UTF-8")); // pageinfo에 curpage를 해줘야
		urlBuilder.append("&perPage=" + URLEncoder.encode(10 + "", "UTF-8")); // 요청하기 위한 url만드는 작업
		urlBuilder.append("&returnType=" + URLEncoder.encode("JSON", "UTF-8")); /* JSON */
		urlBuilder.append("&serviceKey=" + URLEncoder.encode(
				"AoQZRma0VSNRDcyVaxfzeGC5h4q8a18l9dJwO2m0uPKLuZmRpyld3mxoZyHBe0dS1BmjTgc2cXF/bu4rrLohtA==", "UTF-8"));
		System.out.println(urlBuilder.toString());
		// REQUEST
		URL url = new URL(urlBuilder.toString()); // java.neturl
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET"); // 메소드 지정
		conn.setRequestProperty("Content-type", "application/json; chatset=UTF-8"); // 요청

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

		List<ElecCharge> elecChargeList = new ArrayList<>();
		JSONParser parser = new JSONParser(); // 파싱처리
		JSONObject mobj = (JSONObject) parser.parse(resBuilder.toString());
		Long totalcount = (Long) mobj.get("totalCount");
		JSONArray data = (JSONArray) mobj.get("data");

		for (int i = 0; i < data.size(); i++) {
			JSONObject ec = (JSONObject) data.get(i);
			Long csId = (Long) ec.get("csId");
			String csNm = (String) ec.get("csNm");
			String addr = (String) ec.get("addr");
			String lat = (String) ec.get("lat");
			String longi = (String) ec.get("longi");
			Long cpId = (Long) ec.get("cpId");
			String cpNm = (String) ec.get("cpNm");
			String chargeTp = (String) ec.get("chargeTp");
			String cpTp = (String) ec.get("cpTp");
			String statUpdatetime = (String) ec.get("statUpdatetime");
			String cpStat = (String) ec.get("cpStat");

			elecChargeList.add(
					new ElecCharge(csId, csNm, addr, lat, longi, cpId, cpNm, chargeTp, cpTp, statUpdatetime, cpStat));

		}
		int allPage = (int) Math.ceil(totalcount.doubleValue() / 10);
		int startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		int endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		if (pageInfo.getCurPage() > allPage)
			pageInfo.setCurPage(allPage);

		return elecChargeList;

	}

}
