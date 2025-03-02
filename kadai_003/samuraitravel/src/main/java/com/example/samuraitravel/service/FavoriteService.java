package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.FavoriteForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.UserRepository;

@Service
public class FavoriteService {
	
	private final FavoriteRepository favoriteRepository;
	private final HouseRepository houseRepository;
	private final UserRepository userRepository;
	
	public FavoriteService(FavoriteRepository favoriteRepository,
			HouseRepository houseRepository,
			UserRepository userRepository) {
		
		this.favoriteRepository = favoriteRepository;
		this.houseRepository = houseRepository;
		this.userRepository = userRepository;
	}
	
		@Transactional
		public void newFavorite(FavoriteForm favoriteForm) {
			Favorite favorite = new Favorite();
			
			House house = houseRepository.getReferenceById(favoriteForm.getHouseId());
		    User user = userRepository.getReferenceById(favoriteForm.getUserId());
			
		    favorite.setHouse(house);
		    favorite.setUser(user);
			
			favoriteRepository.save(favorite);
			
		}
	}

