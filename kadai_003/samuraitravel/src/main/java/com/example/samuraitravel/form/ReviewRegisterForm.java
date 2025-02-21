package com.example.samuraitravel.form;

import lombok.Data;

@Data
public class ReviewRegisterForm {
	
	private int houseId;
	
	private int userId;
	
    //@NotBlank(message = "評価を５段階で再選択してください")
    private int score;
    
   // @NotBlank(message = "レビュー内容を再入力してください")
    private String content;

    
}