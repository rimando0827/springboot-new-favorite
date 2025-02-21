package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service

//@Transactionalを書くところ
public class ReviewService {
	//reviewテーブルの値をを利用・登録する
	//HouseテーブルのIDを利用する
	//userテーブルのIDを利用する
	
	
	private final ReviewRepository reviewRepository;
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;
	


	public ReviewService(ReviewRepository reviewRepository,
			HouseRepository houseRepository,
			UserRepository userRepository) {
		this.reviewRepository = reviewRepository;
		this.houseRepository = houseRepository;
		this.userRepository = userRepository;
	}

	//新規レビューをDBに保存 //ReviewInputForm ←新規投稿のForm
	@Transactional
	//レビューの登録処理を行うcreate()メソッドを定義　※コントローラから呼び出して使う
	public void create(ReviewRegisterForm reviewRegisterForm) {
	    Review review = new Review();
	    
	    //reviewRegisterFormから受け取った民宿ID,ユーザーIDを各エンティティデータと照らし合わせる
	    House house = houseRepository.getReferenceById(reviewRegisterForm.getHouseId());
	    User user = userRepository.getReferenceById(reviewRegisterForm.getUserId());
	    
	    //フォームの入力内容を受け取る
	    review.setHouse(house);
	    review.setUser(user);
	    review.setScore(reviewRegisterForm.getScore());
	    review.setContent(reviewRegisterForm.getContent());

	    reviewRepository.save(review);
	}
	
	@Transactional
	//レビューの編集処理を行うupdate()メソッドを定義　※コントローラから呼び出して使う
	public void update(ReviewEditForm reviewEditForm) {
		Review review = reviewRepository.getReferenceById(reviewEditForm.getId());
		House house = houseRepository.getReferenceById(reviewEditForm.getHouseId());
	    User user = userRepository.getReferenceById(reviewEditForm.getUserId());
	    
	    //フォームの入力内容を受け取る
	    review.setHouse(house);
	    review.setUser(user);
	    review.setScore(reviewEditForm.getScore());
	    review.setContent(reviewEditForm.getContent());

	    reviewRepository.save(review);
	}
	
	/*
	//レビュー一覧ページに表示するデータを取得する
    public List<ReviewListForm> findReviewsByHouseId(int houseId) {
        return convertReviewList(reviewRepository.findByHouseIdOrderByCreatedAtDesc(houseId));
    }
    
    private List<ReviewListForm> convertReviewList(List<Review> reviewList){
    	
    	List<ReviewListForm> convReviewList = new ArrayList<ReviewListForm>();
    	
    	for(Review review : reviewList) {
    		
    		ReviewListForm reviewListForm = new ReviewListForm();
    		reviewListForm.setId(review.getId());
    		reviewListForm.setHouse(review.getHouse());
    		reviewListForm.setUser(review.getUser());
    		reviewListForm.setContent(review.getContent());
    		reviewListForm.setScore(convertScore(review.getScore()));
    		reviewListForm.setUpdatedAt(review.getUpdatedAt());
    		
    		convReviewList.add(reviewListForm);
    	}
    	
    	return convReviewList;
    }
    
    */

    
    
    
	
}