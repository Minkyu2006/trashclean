$(document).ready(function() {
	$('.nav-icon').on('click', function(e) {
		$('.wrap').toggleClass('nav--small');
		
		e.preventDefault();
	})
	
	
	// nav
	$('.nav__link.toggle').on('click', function(e) {
		var $this = $(this);
		
		if ($this.next().hasClass('active')) {
        	$this.removeClass('active');
            $this.next().removeClass('active');
        } else {
            $this.parent().parent().find('li .nav__child').removeClass('active');
            $this.toggleClass('active');
            $this.next().toggleClass('active');
        }
		
		e.preventDefault();
	})
	
	// panel fold
	$('.panel__button.fold').on('click', function(e) {
		var foldArea = $(this).parents('.panel__header').next('.panel__body')
		
		if(foldArea.hasClass('fold')) {
			foldArea.removeClass('fold');
		} else {
			foldArea.addClass('fold');
		}
		

		e.preventDefault();
	})
	
	// btn-group
	$('.c-btn-group').on('click', '.c-btn-group__button', function(e) {
		var $this = $(this);
		var _index = $this.index();
		var $content = $('.c-btn-group__button').eq(_index);

		$this.add($content).addClass('active').siblings().removeClass('active');

		e.preventDefault();
	}).find('.c-btn-group__button').eq(0).trigger('click');
	
	// contents-profile
	$('.contents-profile__btn').on('click', function(e) {
		var context = $(this).next('.profile-context');
		
		if(context.hasClass('active')) {
			$(this).removeClass('active');
			context.removeClass('active');
		} else {
			$(this).addClass('active');
			context.addClass('active');
		}
		
		e.preventDefault();
		return false;
	})
	
	// 다른 영역 클릭시 팝업 닫기
	$('body').on('click', function() {
		$('.contents-profile__btn').removeClass('active');
		$('.profile-context').removeClass('active');
	})	
	$('.contents-profile').on('click', function(e) {
		e.stopPropagation();
	})
})