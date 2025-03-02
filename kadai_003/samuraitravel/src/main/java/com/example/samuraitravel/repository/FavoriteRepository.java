package com.example.samuraitravel.repository;





import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer>{
	public Page<Favorite> findByUserIdOrderByCreatedAtDesc(Integer useId, Pageable pageable );
	Optional<Favorite> findByHouseIdAndUserId(Integer houseId, Integer userId);
		
	}


