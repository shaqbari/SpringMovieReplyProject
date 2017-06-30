package com.sist.movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.net.*;
import java.io.*;
@Component("mm")
public class MovieManager {
	//@Autowired
	//private MovieDAO dao;
    private List<String> list=
    		new ArrayList<String>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext app=
				new ClassPathXmlApplicationContext("applicationContext.xml");
		MovieManager mm=(MovieManager)app.getBean("mm");
		mm.list.clear();
		for(int i=1;i<=20;i++)
		{
			mm.getData(i);
		}
		int j=1;
		for(String code:mm.list)
		{
			mm.movieInfoData(j, code);
			j++;
			System.out.println("j="+j);
		}
		System.out.println("end");
	}
	
	public void getData(int page)
	{
		try
        {
        	Document doc=Jsoup.connect("http://movie.naver.com/movie/sdb/rank/rmovie.nhn?sel=pnt&date=20170630&page="+page).get();
            Elements codes=doc.select("div.tit5 a");
            for(int i=0;i<codes.size();i++)
            {
            	Element atag=codes.get(i);
            	String href=atag.attr("href");
            	//System.out.println("http://movie.naver.com"+href);
            	list.add(href.substring(href.lastIndexOf("=")+1));
            }
        }catch(Exception ex)
        {
        	System.out.println(ex.getMessage());
        }
	}
	// 17421
	public void movieInfoData(int r,String code)
	{
		
		try
		{
			
			Document doc=Jsoup.connect("http://movie.naver.com/movie/bi/mi/basic.nhn?code="+code).get();
		    Element titleElem=doc.select("div.mv_info h3.h_movie a").first();
		    Element genreElem=doc.select("p.info_spec span a").first();
		    Element directorElem=doc.select("dl.step1 dd a").first();
		    Elements actorElems=doc.select("dl.step2 dd a");
		    String actor="";
		    for(int i=0;i<actorElems.size();i++)
		    {
		    	Element elem=actorElems.get(i);
		    	actor+=elem.text()+",";
		    }
		    actor=actor.substring(0, actor.lastIndexOf(","));
		    Element posterElem=doc.select("div.poster a img").first();
		    String img=posterElem.attr("src");
		    Elements spanElem=doc.select("p.info_spec span a");
		    String grade="";
		    for(int i=0;i<spanElem.size();i++)
		    {
		    	Element a=spanElem.get(i);
		    	String str=a.attr("href");
		    	str=str.substring(str.lastIndexOf("?")+1,
		    			str.lastIndexOf("="));
		    	if(str.equals("grade"))
		    	{
		    		grade=a.text();
		    		break;
		    	}
		    }
		    Element starElem=doc.select("div.star_score span.st_on").first();
		    String star="";
		    if(starElem.text().equals(""))
		    {
		    	star="0";
		    }
		    else
		    {
		    	star=starElem.text().replaceAll("[]", "");
		    }
		    Element storyElem=doc.select("div.story_area p.con_tx").first();
		    String story=storyElem.text();
		    story=story.replaceAll("[A-Za-z]", "");
		    story=story.replace("&", "");
		    
		    if(titleElem.text()!=null)
		    {
		      System.out.println(titleElem.text()+":"
		    		+genreElem.text()+":"
		    		+directorElem.text()+":"
		    		+actor+":"+img+":"+grade
		    		+":"+star+":"+story);
		       MovieVO vo=new MovieVO();
		       vo.setTitle(titleElem.text());
		       vo.setActor(actor);
		       vo.setDirector(directorElem.text());
		       vo.setGenre(genreElem.text());
		       vo.setPoster(img);
		       vo.setGrade(grade);
		       vo.setStar(star);
		       vo.setStory(story);
		       vo.setRank(String.valueOf(r));
		       //dao.movieInsert(vo);
		      
		    }
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}
	// 7b429affa32c43e1adf62ad1eebb6928
	/*
	 *  https://apis.daum.net/search/blog?apikey={apikey}&q=īī\BF\C0\C5\E5&output=xml
	 */
	/*public void movieReviewData(int page,String title)
	{
		try
		{
			URL url=new URL("https://apis.daum.net/search/blog?apikey=7b429affa32c43e1adf62ad1eebb6928&output=xml&result=20&pageno="+page+"&q="+URLEncoder.encode(title, "UTF-8"));
			JAXBContext jc=JAXBContext.newInstance(Channel.class);
			Unmarshaller um=jc.createUnmarshaller();
			Channel ch=(Channel)um.unmarshal(url);
			List<Item> list=ch.getItem();
			FileWriter fw=new FileWriter("/home/sist/data/movie_review.txt",true);
			for(Item item:list)
			{
				fw.write(item.getDescription()+"\n");
			}
			fw.close();
		}catch(Exception ex)
		{
			System.out.println(ex.getMessage());
		}
	}*/
	// \BF\B9\B8\C5\C0\B2 , \B9ڽ\BA\BF\C0\C7ǽ\BA , \BF\B5ȭ \BC\F8\C0\A7
	/*public List<MovieInfoVO> getRankData()
	   {
		   List<MovieInfoVO> mrv=new ArrayList<MovieInfoVO>();
		   try
		   {
			   // $("div#title").hide()
			   Document doc=Jsoup.connect("http://movie.naver.com/movie/sdb/rank/rmovie.nhn").get();
			   Elements title=doc.select("td.title div.tit3");
			   Elements id=doc.select("td.range");
			   Elements type=doc.select("td.ac img");
			   int j=1;
			   for(int i=0;i<10;i++)
			   {
				   Element telem=title.get(i);
				   Element ielem=id.get(i);
				   Element typeElem=type.get(j);
				   String img=typeElem.attr("src");
				   String temp=img.substring(
						   img.lastIndexOf("/")+1);
				   System.out.println(temp);
						   //+ielem.text());
				   MovieInfoVO vo=new MovieInfoVO();
				   vo.setImage("http://imgmovie.naver.net/2007/img/common/"+temp);
				   vo.setTitle(telem.text());
				   vo.setIdcrement(Integer.parseInt(ielem.text()));
				   if(temp.equals("icon_up_1.gif"))
				   {
					   vo.setType(0);
				   }
				   else if(temp.equals("icon_down_1.gif"))
				   {
					   vo.setType(1);
				   }
				   else
				   {
					   vo.setType(2);
				   }
				   mrv.add(vo);
				   j+=2;
			   }
		   }catch(Exception ex)
		   {
			   System.out.println(ex.getMessage());
		   }
		   return mrv;
	   }
	   public List<MovieInfoVO> getReserveRankData()
	   {
		   List<MovieInfoVO> mrv=new ArrayList<MovieInfoVO>();
		   try
		   {
			   // $("div#title").hide()
			   Document doc=Jsoup.connect("http://movie.naver.com/movie/sdb/rank/rreserve.nhn").get();
			   Elements title=doc.select("td.title div.tit4");
			   Elements reserve=doc.select("td.reserve_per");
			   
			   for(int i=0;i<10;i++)
			   {
				   Element telem=title.get(i);
				   Element relem=reserve.get(i);
				   
				   MovieInfoVO vo=new MovieInfoVO();
				   vo.setTitle(telem.text());
				   String temp=relem.text();// 61.05%
				   temp=temp.substring(0,temp.lastIndexOf("%"));
				   vo.setReserve(Double.parseDouble(temp));
				   mrv.add(vo);
			   }
		   }catch(Exception ex)
		   {
			   System.out.println(ex.getMessage());
		   }
		   return mrv;
	   }
	   public List<MovieInfoVO> getBoxRankData()
	   {
		   List<MovieInfoVO> mrv=new ArrayList<MovieInfoVO>();
		   try
		   {
			   // $("div#title").hide()
			   Document doc=Jsoup.connect("http://movie.naver.com/movie/sdb/rank/rboxoffice.nhn").get();
			   Elements title=doc.select("td.title div.tit1");
			   Elements showUser=doc.select("td.reserve_cnt em");
			   Elements marketShare=doc.select("td.reserve_per");
			   for(int i=0;i<10;i++)
			   {
				   Element telem=title.get(i);
				   Element selem=showUser.get(i);
				   Element melem=marketShare.get(i);
				   MovieInfoVO vo=new MovieInfoVO();
				   vo.setTitle(telem.text());
				   String su=selem.text();// 53529
				   su=su.replace(",", "");
				   vo.setShowUser(Integer.parseInt(su));
				   String ms=melem.text();
				   ms=ms.substring(0,ms.lastIndexOf("%"));// 3.70%
				   vo.setMarketShare(Double.parseDouble(ms));
				   mrv.add(vo);
			   }
		   }catch(Exception ex)
		   {
			   System.out.println(ex.getMessage());
		   }
		   return mrv;
	   }
	   public void movieReviewData1(int page,String title)
		{
			try
			{
				URL url=new URL("https://apis.daum.net/search/blog?apikey=7b429affa32c43e1adf62ad1eebb6928&output=xml&result=20&pageno="+page+"&q="+URLEncoder.encode(title, "UTF-8"));
				JAXBContext jc=JAXBContext.newInstance(Channel.class);
				Unmarshaller um=jc.createUnmarshaller();
				Channel ch=(Channel)um.unmarshal(url);
				List<Item> list=ch.getItem();
				FileWriter fw=new FileWriter("/home/sist/total/movie_daum",true);
				for(Item item:list)
				{
					fw.write(item.getDescription()+"\n");
				}
				fw.close();
			}catch(Exception ex)
			{
				System.out.println(ex.getMessage());
			}
		}
*/
}




