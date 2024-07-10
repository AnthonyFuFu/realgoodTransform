
$(function(){
	var owl = $('.tabs');
	owl.owlCarousel({
		loop: false,
		margin: 0,
		nav: false,
		dots: false,
		responsive:{
			0:{
				items: 3
			},
			600:{
				items: 4
			},
			1000:{
				items: 4
			}
		}
	});

	var owlTeacherMenu = $('.owl-carousel-ttc');
	owlTeacherMenu.owlCarousel({
		loop: false,
		margin: 40,
		nav: false,
		dots: false,
		responsive:{
			0:{
				items: 3
			},
			600:{
				items: 4
			},
			1000:{
				items: 5
			}
		}
	});

	/* 20220218 start */
	var owlOverviewTeacher = $('.owl-overview-teacher');
	owlOverviewTeacher.owlCarousel({
		loop: false,
		margin: 20,
		nav: false,
		dots: true,
		responsive:{
			0:{
				items: 1
			},
			600:{
				items: 2
			},
			1000:{
				items: 3
			}
		}
	});
	/* 20220218 end */

	/* 20220511 start */
	var owlRelatedCourse = $('.owl-related-course');
	owlRelatedCourse.owlCarousel({
		loop: false,
		margin: 20,
		nav: false,
		dots: true,
		responsive:{
			0:{
				items: 1
			},
			600:{
				items: 3
			},
			1000:{
				items: 5
			}
		}
	});
	/* 20220511 end */

	/* 20230419 start */
	// var owlCourseCase = $('.owl-course-case');
	// owlCourseCase.owlCarousel({
	// 	loop: false,
	// 	margin: 20,
	// 	nav: false,
	// 	dots: true,
	// 	responsive:{
	// 		0:{
	// 			items: 1
	// 		},
	// 		600:{
	// 			items: 3
	// 		},
	// 		1000:{
	// 			items: 3
	// 		}
	// 	}
	// });
	/* 20230419 end */
});