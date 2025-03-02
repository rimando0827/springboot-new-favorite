package com.example.samuraitravel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;

@Controller
@RequestMapping("/houses")
public class HouseController {
	private final HouseRepository houseRepository;
	private final ReviewRepository reviewRepository;
	private final FavoriteRepository favoriteRepository;

	public HouseController(HouseRepository houseRepository, ReviewRepository reviewRepository,
			FavoriteRepository favoriteRepository) {
		this.houseRepository = houseRepository;
		this.reviewRepository = reviewRepository;
		this.favoriteRepository = favoriteRepository;
	}

	//一覧ページ

	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "area", required = false) String area,
			@RequestParam(name = "price", required = false) Integer price,
			@RequestParam(name = "order", required = false) String order,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		Page<House> housePage;

		if (keyword != null && !keyword.isEmpty()) {

			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%",
						"%" + keyword + "%", pageable);
			} else {
				housePage = houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%",
						"%" + keyword + "%", pageable);
			}
		} else if (area != null && !area.isEmpty()) {

			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
			} else {
				housePage = houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
			}
		} else if (price != null) {

			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
			} else {
				housePage = houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
			}
		} else {

			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findAllByOrderByPriceAsc(pageable);
			} else {
				housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);
			}
		}

		model.addAttribute("housePage", housePage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("area", area);
		model.addAttribute("price", price);
		model.addAttribute("order", order);

		return "houses/index";
	}

	//民宿詳細ページ

	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model) {
		House house = houseRepository.getReferenceById(id);
		boolean isFavorite = false; // 初期値として false をセット

		if (userDetailsImpl != null) {
			Integer userId = userDetailsImpl.getUser().getId();
			Optional<Favorite> optionalFavorite = favoriteRepository.findByHouseIdAndUserId(id, userId);

			if (optionalFavorite.isPresent()) {
				model.addAttribute("favorite", optionalFavorite.get());
				isFavorite = true; // Favorite が存在する場合は true
			}
		}

		List<Review> newReview = reviewRepository.findByHouseIdOrderByCreatedAtDesc(id);

		List<Review> displayedReviews = newReview.size() > 6 ? newReview.subList(0, 6) : newReview;
		
		model.addAttribute("isFavorite", isFavorite); // boolean 値を必ず渡す

		model.addAttribute("newReview", newReview);
		model.addAttribute("house", house);
		model.addAttribute("displayedReviews", displayedReviews);
		model.addAttribute("totalReviewCount", newReview.size());
		model.addAttribute("reservationInputForm", new ReservationInputForm());

		return "houses/show";
	}

}
