package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReviewEditForm;
import com.example.samuraitravel.form.ReviewRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReviewRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReviewService;

@Controller
@RequestMapping("/houses/{houseId}/reviews")
public class ReviewController {

	private ReviewService reviewService;
	private HouseRepository houseRepository;
	private ReviewRepository reviewRepository;

	public ReviewController(
			ReviewService reviewService,
			HouseRepository houseRepository,
			ReviewRepository reviewRepository) {
		this.reviewService = reviewService;
		this.houseRepository = houseRepository;
		this.reviewRepository = reviewRepository;
	}

	@GetMapping
	public String index(@PathVariable(name = "houseId") Integer houseId,
			@PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {
		House house = houseRepository.getReferenceById(houseId);
		Page<Review> reviewPage = reviewRepository.findByHouseIdOrderByCreatedAtDesc(houseId, pageable);
		model.addAttribute("house",house);
		model.addAttribute("reviewPage",reviewPage);

		return "reviews/index";
	}

	@GetMapping("/register")
	public String register(@PathVariable(name = "houseId") Integer houseId, Model model) {
		House house = houseRepository.getReferenceById(houseId);

		model.addAttribute("house", house);
		model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());

		return "reviews/register";

	}

	@PostMapping("/create")
	public String create(@PathVariable(name = "houseId") Integer houseId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model) {
		if (bindingResult.hasErrors()) {
			
			return "reviews/register";
		}
		
		reviewRegisterForm.setHouseId(houseId);
		
		User user = userDetailsImpl.getUser();
		Integer userId = user.getId();
		reviewRegisterForm.setUserId(userId);
		
		reviewService.create(reviewRegisterForm);
		
	    // houseId をリダイレクトの際に URL 変数として追加
	    redirectAttributes.addAttribute("houseId", houseId);
		
		redirectAttributes.addFlashAttribute("successMessage","レビュー内容を投稿しました。");
		
		return "redirect:/houses/{houseId}/reviews";
	}

	@GetMapping("/{reviewId}/edit")
	public String edit(@PathVariable(name = "houseId") Integer houseId,
			@PathVariable(name = "reviewId") Integer reviewId,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model) {
		House house = houseRepository.getReferenceById(houseId);
		Review review = reviewRepository.getReferenceById(reviewId);
		User user = userDetailsImpl.getUser();
		ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), house.getId(),user.getId(),  review.getScore(), review.getContent());

		model.addAttribute("house", house);
		model.addAttribute("review", review);
		model.addAttribute("reviewEditForm", reviewEditForm);

		return "reviews/edit";
	}

	@PostMapping("/{reviewId}/update")
	public String update(@PathVariable(name = "houseId") Integer houseId,
			@PathVariable(name = "reviewId") Integer reviewId,
			@ModelAttribute @Validated ReviewEditForm reviewEditForm,
			BindingResult bindingResult,RedirectAttributes redirectAttributes ,
			Model model) {
		if (bindingResult.hasErrors()) {

			return "reviews/edit";
		}
		

		reviewEditForm.setHouseId(houseId);
		reviewEditForm.setId(reviewId);
		
		model.addAttribute("house", houseId);
		model.addAttribute("review", reviewId);
		
		reviewService.update(reviewEditForm);
		
		// houseId をリダイレクトの際に URL 変数として追加
		redirectAttributes.addAttribute("houseId" , houseId);
		redirectAttributes.addAttribute("reviewId" , reviewId);
		redirectAttributes.addFlashAttribute("successMessage","レビュー内容を編集しました。");
		

		return "redirect:/houses/{houseId}/reviews";

	}

	@PostMapping("/{reviewId}/delete")
	public String delete(@PathVariable(name = "reviewId") Integer reviewId,@PathVariable(name = "houseId") Integer houseId, 
			RedirectAttributes redirectAttributes,Model model) {
		House house = houseRepository.getReferenceById(houseId);
		Review review = reviewRepository.getReferenceById(reviewId);
		reviewRepository.deleteById(reviewId);
		
		model.addAttribute("house", house);
		model.addAttribute("review", review);
		
		redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました");

		return "redirect:/houses/{houseId}/reviews";
	}

}