package com.kh.spring.react;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/test")
	public List<String> Hello() {
		return Arrays.asList("서버의 포트번호 : 8081", "리액트 포트번호 : 3000");
	}
}
