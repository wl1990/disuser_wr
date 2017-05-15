package disuser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.test.utils.HttpClientUtil;


public class UserControllerTest {
	@Test
	public void userTest(){
		/*HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userName", "123");
		map.put("email", "123");
		HttpEntity<String> requestEntity = new HttpEntity<String>(JSONObject.toJSONString(map), headers);
		RestTemplate rest=new RestTemplate();
		ResponseEntity<String> exchange =rest.exchange("http://localhost:8081/san/userupdate", HttpMethod.POST, requestEntity, String.class,map);
		JSONObject json=JSONObject.parseObject(exchange.getBody());*/
		try {
			// http://localhost:8081/disuser/swagger-ui.html
		/*	String s1=HttpClientUtil.httpGetRequest("http://localhost:8081/disuser/user/1234");
			System.out.println("--"+s1);*/
			/*Map<String,Object> map=new HashMap<String,Object>();
			map.put("userName", "123");
			map.put("email", "123");
			JSONObject jsonObject=new JSONObject();
			jsonObject.putAll(map);
			String	s = HttpClientUtil.httpPostRequest("http://localhost:8081/san/userupdate",jsonObject);
			System.out.println("----#$%---"+s);*/
			
			Date date=new Date(1463270400000L);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			System.out.println(sdf.format(date));
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
