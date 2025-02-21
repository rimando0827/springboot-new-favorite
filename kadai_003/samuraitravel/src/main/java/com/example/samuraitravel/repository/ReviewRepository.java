package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	public Page<Review> findByHouseIdOrderByCreatedAtDesc(Integer houseId, Pageable pageable );
	public Page<Review> findByHouseOrderByCreatedAtDesc(User review, Pageable pageable );
	
	List<Review> findByHouseIdOrderByCreatedAtDesc(Integer houseId);

}
