package com.sporthenon.utils;


public class Test {

	public static void main(String[] args) throws Exception {
//		DatabaseHelper.setFactory(null, "standalone");
//		ArrayList<Object> lFuncParams = new ArrayList<Object>();
//		lFuncParams.add(5);
//		lFuncParams.add(9);
//		lFuncParams.add(193);
//		lFuncParams.add(0);
//		lFuncParams.add("0");
//		Championship oCp = (Championship) DatabaseHelper.loadEntity(Championship.class, new Integer(String.valueOf(lFuncParams.get(1))));
//		Event oEv = (Event) DatabaseHelper.loadEntity(Event.class, new Integer(String.valueOf(lFuncParams.get(3)).equals("0") ? String.valueOf(lFuncParams.get(2)) : String.valueOf(lFuncParams.get(3))));
//		RenderOptions opts = ServletHelper.buildOptions(new HashMap());
//		StringBuffer html = new StringBuffer();
//		html.append(HtmlConverter.getHeader(HtmlConverter.HEADER_RESULTS, lFuncParams, opts));
//		html.append(HtmlConverter.convertResults(DatabaseHelper.call("GetResults", lFuncParams), oCp, oEv, opts));
//		
//		Document doc = Jsoup.parse(html.toString().replaceAll("&nbsp;", " "));
//		System.out.println(doc.toString());
//		Element shortTitle = doc.getElementsByAttributeValue("class", "shorttitle").first();
//		Element url = doc.getElementsByAttributeValue("class", "url").first();
		
		//http://jsoup.org/cookbook/extracting-data/dom-navigation
		
		//System.out.println(shortTitle.html());
//		Elements links = content.getElementsByTag("a");
//		for (Element link : links) {
//		  String linkHref = link.attr("href");
//		  String linkText = link.text();
//		}

/**
<table class='header'>
<tr><th colspan=2><img src='img/render/collapse.gif' class='toggleimg' onclick='toggleContent(this)'/><span class='toggletext' onclick='toggleContent(this)'>RESULTS</span></th></tr>
<tr><td class='logos' rowspan=4><a href='http://www.fia.com' target='_blank'><img alt='' src='http://localhost:8082/img/0-5-L.png?1380629230162'/></a><img alt='' src='http://localhost:8082/img/1-9-L.png?1380629230162'/></td>
<td><a href='javascript:info("SP-5")'>Auto Racing</a></td></tr><tr><td><a href='javascript:info("CP-9")'>World Rally Championship (WRC)</a></td></tr>
<tr><td><a href='javascript:info("EV-193")'>Standings - Manufacturers</a></td></tr><tr><td>Years: All</td></tr>
</table>

<table class='tsort'><thead><tr class='rsort'><th onclick='sort("1380629230271", this, 0);'>Year</th><th colspan='3' onclick='sort("1380629230271", this, 1);'>1st</th><th colspan='3' onclick='sort("1380629230271", this, 2);'>2nd</th><th colspan='3' onclick='sort("1380629230271", this, 3);'>3rd</th></tr></thead><tbody id='tb-1380629230271'><tr><td class='srt'><a href='javascript:info("YR-160")'>2010</a></td><td class='srt' style='font-weight:bold;'><table><tr><th><img alt='' src='http://localhost:8082/img/5-1300-S.png?1380629230287'/></th><td><a href='javascript:info("TM-1300")'>Citroën Total WRT</a></td></tr></table></td><td><table><tr><th><img alt='' src='http://localhost:8082/img/4-155-S.gif?1380629230287'/></th><td><a href='javascript:info("CN-155")'>FRA</a></td></tr></table></td><td>456</td><td class='srt'><table><tr><th><img alt='' src='http://localhost:8082/img/5-1299-S.png?1380629230287'/></th><td><a href='javascript:info("TM-1299")'>BP-Ford WRT</a></td></tr></table></td><td><table><tr><th><img alt='' src='http://localhost:8082/img/4-92-S.gif?1380629230287'/></th><td><a href='javascript:info("CN-92")'>USA</a></td></tr></table></td><td>337</td><td class='srt'><a href='javascript:info("TM-1415")'>Citroën Junior Team</a></td><td><table><tr><th><img alt='' src='http://localhost:8082/img/4-155-S.gif?1380629230287'/></th><td><a href='javascript:info("CN-155")'>FRA</a></td></tr></table></td><td>217</td></tr><tr><td class='srt'><a href='javascript:info("YR-161")'>2011</a></td><td class='srt' style='font-weight:bold;'><table><tr><th><img alt='' src='http://localhost:8082/img/5-1300-S.png?1380629230287'/></th><td><a href='javascript:info("TM-1300")'>Citroën Total WRT</a></td></tr></table></td><td><table><tr><th><img alt='' src='http://localhost:8082/img/4-155-S.gif?1380629230287'/></th><td><a href='javascript:info("CN-155")'>FRA</a></td></tr></table></td><td>403</td><td class='srt'><a href='javascript:info("TM-1416")'>Ford Abu Dhabi WRT</a></td><td><table><tr><th><img alt='' src='http://localhost:8082/img/4-158-S.gif?1380629230287'/></th><td><a href='javascript:info("CN-158")'>GBR</a></td></tr></table></td><td>376</td><td class='srt'><a href='javascript:info("TM-1417")'>M-Sport Stobart Ford WRT</a></td><td><table><tr><th><img alt='' src='http://localhost:8082/img/4-158-S.gif?1380629230287'/></th><td><a href='javascript:info("CN-158")'>GBR</a></td></tr></table></td><td>178</td></tr><tr><td class='srt'><a href='javascript:info("YR-162")'>2012</a></td><td class='srt' style='font-weight:bold;'><table><tr><th><img alt='' src='http://localhost:8082/img/5-1300-S.png?1380629230287'/></th><td><a href='javascript:info("TM-1300")'>Citroën Total WRT</a></td></tr></table></td><td><table><tr><th><img alt='' src='http://localhost:8082/img/4-155-S.gif?1380629230287'/></th><td><a href='javascript:info("CN-155")'>FRA</a></td></tr></table></td><td>453</td><td class='srt'><a href='javascript:info("TM-1418")'>Ford WRT</a></td><td><table><tr><th><img alt='' src='http://localhost:8082/img/4-158-S.gif?1380629230287'/></th><td><a href='javascript:info("CN-158")'>GBR</a></td></tr></table></td><td>309</td><td class='srt'><a href='javascript:info("TM-1419")'>M-Sport Ford WRT</a></td><td><table><tr><th><img alt='' src='http://localhost:8082/img/4-158-S.gif?1380629230287'/></th><td><a href='javascript:info("CN-158")'>GBR</a></td></tr></table></td><td>170</td></tr><table class='winrec'><tr><th colspan=3><img src='img/render/collapse.gif' class='toggleimg' onclick='toggleContent(this)'/><span class='toggletext' onclick='toggleContent(this)'>WIN RECORDS</span></th></tr><tr><td class='caption'><a href='javascript:info("TM-1300")'>Citroën Total WRT</a></td><td><table><tr><td class='bar1'>&nbsp;</td><td class='bar2' style='width:100px;'>&nbsp;</td><td class='bar3'>&nbsp;</td></tr></table></td><td class='count'>3</td></tr></tbody></table>
 */
	}
	
}