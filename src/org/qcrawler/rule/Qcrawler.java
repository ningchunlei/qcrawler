package org.qcrawler.rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Qcrawler {
	
	public Map<Url,List<Rule>> spider = new HashMap();

	public Map<Url, List<Rule>> getSpider() {
		return spider;
	}

	public void setSpider(Map<Url, List<Rule>> spider) {
		this.spider = spider;
	}

	
	
	
	
}
