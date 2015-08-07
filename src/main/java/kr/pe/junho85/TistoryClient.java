package kr.pe.junho85;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

/**
 * @see "http://www.tistory.com/guide/api/post"
 */
public class TistoryClient {
    private static final String ACCESS_TOKEN = "";

    private static final String CLIENT_ID = "";
    private static final String SECRET_KEY = "";
    private static final String REDIRECT_URI = "";

    private static final String TARGET_URL = "junho85.pe.kr";
    private static final String WRITE_API_URL = "https://www.tistory.com/apis/post/write";
    private static final String MODIFY_API_URL = "https://www.tistory.com/apis/post/modify";
    private static final String CATEGORY_LIST_API_URL = "https://www.tistory.com/apis/category/list";

    // 커피 밀어주기
    private static final String DONATE = "<iframe src=\"http://gift.blog.daum.net/widget?entryId=0&amp;setNo=2043\" width=\"100%\" height=\"250\" frameborder=\"0\" border=\"0\" scrolling=\"no\" allowtransparency=\"true\" ;=\"\"></iframe>";

    private static final String OUTPUT = "json";

    private static final String GAME_CATEGORY = "584194";

    private static final String USER_AGENT = "Mozilla/5.0";

    public void getAccessToken() {
        String clientId = "{발급받은 client_id를 입력하세요}";
        String clientSecret = "{발급받은 client_secret 을 입력하세요}";
        String redirectUri = "{등록시 입력한 redirect uri 를 입력하세요}";
        String grantType = "authorization_code";

//        String requestUrl = "https://www.tistory.com/oauth/access_token/?code=" + authorization_code +
//                "&client_id=" + CLIENT_ID +
//                "&client_secret=" + SECRET_KEY +
//                "&redirect_uri=" + REDIRECT_URI +
//                "&grant_type=" + grantType;
    }
    /**
     * write tistory article
     * @param tistoryBrainDotsArticle
     */
    public void write(TistoryBrainDotsArticle tistoryBrainDotsArticle) throws IOException {
        // common validation check
        checkCommonValidation(tistoryBrainDotsArticle);

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(WRITE_API_URL);

        // add header
        post.setHeader("User-Agent", USER_AGENT);

        String title = "브레인 도트 스테이지 " + tistoryBrainDotsArticle.getStage() + " 클리어 동영상";

        String content =
                "<h2>브레인 도트 스테이지 " + tistoryBrainDotsArticle.getStage() + " 클리어</h2>" +
                        "<div>" + tistoryBrainDotsArticle.getYoutube() + "</div>\n" +
                        "<div>" + tistoryBrainDotsArticle.getStrategy() + "</div>\n" +
                        "<div>" + DONATE + "</div>\n";

        Set<String> tagSets = getBrainDotsTags();

        tagSets.add(tistoryBrainDotsArticle.getStage());

        String tags = Joiner.on(",").join(tagSets);

        List urlParameters = Lists.newArrayList();
        urlParameters.add(new BasicNameValuePair("access_token", ACCESS_TOKEN));
        urlParameters.add(new BasicNameValuePair("targetUrl", TARGET_URL)); // 티스토리 주소. http://xxx.tistory.com 일경우 xxx 만 입력, 2차도메인일 경우 http://제거한 url 입력
        urlParameters.add(new BasicNameValuePair("output", OUTPUT));    // output type

        urlParameters.add(new BasicNameValuePair("title", title));  // 제목
        urlParameters.add(new BasicNameValuePair("content", content));  // 내용
        urlParameters.add(new BasicNameValuePair("category", GAME_CATEGORY));   // 카테고리
        urlParameters.add(new BasicNameValuePair("tag", tags)); // 태그


        if (tistoryBrainDotsArticle.getVisibility() != null) {
            urlParameters.add(new BasicNameValuePair("visibility", tistoryBrainDotsArticle.getVisibility()));
        }

        post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));

        HttpResponse response = client.execute(post);

        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
    }

    public void modify(TistoryBrainDotsArticle tistoryBrainDotsArticle) throws Exception {
        // validation check for modify
        if (tistoryBrainDotsArticle.getPostId() == null) {
            throw new RuntimeException("postId needed");
        }

        // common validation check
        checkCommonValidation(tistoryBrainDotsArticle);


        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(MODIFY_API_URL);

        // add header
        post.setHeader("User-Agent", USER_AGENT);

        String title = "브레인 도트 스테이지 " + tistoryBrainDotsArticle.getStage() + " 클리어 동영상";

        String content =
                "<h2>브레인 도트 스테이지 " + tistoryBrainDotsArticle.getStage() + " 클리어</h2>" +
                "<div>" + tistoryBrainDotsArticle.getYoutube() + "</div>" +
                "<div>" + tistoryBrainDotsArticle.getStrategy() + "</div>" +
                "<div>" + DONATE + "</div>\n";

        Set<String> tagSets = getBrainDotsTags();

        tagSets.add(tistoryBrainDotsArticle.getStage());

        String tags = Joiner.on(",").join(tagSets);

        List urlParameters = Lists.newArrayList();
        urlParameters.add(new BasicNameValuePair("access_token", ACCESS_TOKEN));
        urlParameters.add(new BasicNameValuePair("targetUrl", TARGET_URL)); //
        urlParameters.add(new BasicNameValuePair("title", title));  // 제목
        urlParameters.add(new BasicNameValuePair("content", content));  // 내용
        urlParameters.add(new BasicNameValuePair("category", GAME_CATEGORY));   // 카테고리
        urlParameters.add(new BasicNameValuePair("tag", tags)); // 태그
        urlParameters.add(new BasicNameValuePair("output", OUTPUT));    // output type

        urlParameters.add(new BasicNameValuePair("postId", tistoryBrainDotsArticle.getPostId()));   // only modify

        post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));

        HttpResponse response = client.execute(post);

        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
    }

    protected void checkCommonValidation(TistoryBrainDotsArticle tistoryBrainDotsArticle) {
        if (tistoryBrainDotsArticle.getYoutube() == null) {
            throw new RuntimeException("youtube needed");
        }

        if (tistoryBrainDotsArticle.getStage() == null) {
            throw new RuntimeException("stage needed");
        }

        if (tistoryBrainDotsArticle.getStrategy() == null) {
            throw new RuntimeException("strategy needed");
        }
    }


    /**
     * brain dots tags
     * @return tags set
     */
    private Set<String> getBrainDotsTags() {
        Set<String> tagSets = Sets.newHashSet();
        tagSets.add("브레인도트");
        tagSets.add("braindots");
        tagSets.add("brain dots");
        tagSets.add("게임");
        tagSets.add("game");
        tagSets.add("퍼즐");
        tagSets.add("puzzle");
        tagSets.add("공략");
        tagSets.add("strategy");
        tagSets.add("클리어");
        tagSets.add("clear");
        tagSets.add("머리");
        tagSets.add("brain");

        return tagSets;
    }

    /**
     * get category list
     * @throws IOException
     */
    public void categoryList() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        String output = "json";
        HttpGet request = new HttpGet(CATEGORY_LIST_API_URL + "?access_token=" + ACCESS_TOKEN + "&targetUrl=" + TARGET_URL + "&output=" + output);

        // add header
        request.setHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);

        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
    }
}
