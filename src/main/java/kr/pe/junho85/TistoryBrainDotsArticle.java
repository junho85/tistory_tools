package kr.pe.junho85;

import lombok.Data;

@Data
public class TistoryBrainDotsArticle {
    private String stage;
    private String youtube;
    private String strategy;
    private String visibility;    // 0: 비공개, 1: 보호, 2: 공개, 3: 발행, 생략시 비공개

    private String postId; // only modify
}
