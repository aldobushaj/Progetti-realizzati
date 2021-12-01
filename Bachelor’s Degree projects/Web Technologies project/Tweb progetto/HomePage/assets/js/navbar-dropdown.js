    jQuery(function($){

    var DATA_KEY = 'bs.navbar-dropdown';
    var EVENT_KEY = '.' + DATA_KEY;
    var DATA_API_KEY = '.data-api';
    
    var Event = {
        COLLAPSE: 'collapse' + EVENT_KEY,
        CLICK_DATA_API: 'click' + EVENT_KEY + DATA_API_KEY,
        SCROLL_DATA_API: 'scroll' + EVENT_KEY + DATA_API_KEY,
        RESIZE_DATA_API: 'resize' + EVENT_KEY + DATA_API_KEY,
        COLLAPSE_SHOW: 'show.bs.collapse',
        COLLAPSE_HIDE: 'hide.bs.collapse',
        DROPDOWN_COLLAPSE: 'collapse.bs.nav-dropdown'
    };

    var ClassName = {
        IN: 'in',
        OPENED: 'opened',
        BG_COLOR: 'bg-color',
        DROPDOWN_OPEN: 'navbar-dropdown-open',
        SHORT: 'navbar-short'
    };

    var Selector = {
        BODY: 'body',
        BASE: '.navbar-dropdown',
        TOGGLER: '.navbar-toggler[aria-expanded="true"]',
        TRANSPARENT: '.transparent',
        FIXED_TOP: '.navbar-fixed-top'
    };

    function _dataApiHandler(event) {

        if (event.type === 'resize') {

            $(Selector.BODY).removeClass(ClassName.DROPDOWN_OPEN);
            $(Selector.BASE).find(".navbar-collapse").removeClass("show");
            $(Selector.BASE)
                .removeClass(ClassName.OPENED)
                .find(Selector.TOGGLER).each(function(){
                    
                    $( $(this).attr('data-target') )
                        .removeClass(ClassName.IN)
                        .add(this)
                        .attr('aria-expanded', 'false');

                });

        }

        var scrollTop = $(this).scrollTop();
        $(Selector.BASE).each(function(){

            if (!$(this).is(Selector.FIXED_TOP)) return;

            if ($(this).is(Selector.TRANSPARENT) && !$(this).hasClass(ClassName.OPENED)) {

                if (scrollTop > 0) {
                    $(this).removeClass(ClassName.BG_COLOR);
                } else {
                    $(this).addClass(ClassName.BG_COLOR);
                }

            }
        
            if (scrollTop > 0) {
                $(this).addClass(ClassName.SHORT);
            } else {
                $(this).removeClass(ClassName.SHORT);
            }

        });

    }

    var _timeout;
    $(window)
        .on(Event.SCROLL_DATA_API + ' ' + Event.RESIZE_DATA_API, function(event){
            clearTimeout(_timeout);
            _timeout = setTimeout(function(){
                _dataApiHandler(event);
            }, 10);
        })
        .trigger(Event.SCROLL_DATA_API);

    $(document)
        .on(Event.CLICK_DATA_API, Selector.BASE, function(event){
            event.targetWrapper = this;
        })
        .on(Event.COLLAPSE_SHOW + ' ' + Event.COLLAPSE_HIDE, function(event){

            $(event.target).closest(Selector.BASE).each(function(){

                if (event.type == 'show') {

                    $(Selector.BODY).addClass(ClassName.DROPDOWN_OPEN);

                    $(this).addClass(ClassName.OPENED);

                } else {

                    $(Selector.BODY).removeClass(ClassName.DROPDOWN_OPEN);

                    $(this).removeClass(ClassName.OPENED);

                    $(window).trigger(Event.SCROLL_DATA_API);

                    $(this).trigger(Event.COLLAPSE);

                }

            });

        })
        .on(Event.DROPDOWN_COLLAPSE, function(event){

            $(event.relatedTarget)
                .closest(Selector.BASE)
                .find(Selector.TOGGLER)
                .trigger('click');

        });

});



// Bottone scroll to Top  che fa tornare al top della pagina
$(document).ready(function() {
    if ($('.mbr-arrow-up').length) {
        var $scroller = $('#scrollToTop'),
            $main = $('body,html'),
            $window = $(window);
        $scroller.css('display', 'none');
        $window.scroll(function() {
            if ($(this).scrollTop() > 0) {
                $scroller.fadeIn();
            } else {
                $scroller.fadeOut();
            }
        });
        $scroller.click(function() {
            $main.animate({
                scrollTop: 0
            }, 400);
            return false;
        });
    }
    



    //Animazione corpo delle pagine
    if ($('input[name=animation]').length) {
        $('input[name=animation]').remove();

        var $animatedElements = $('p, h1, h2, h3, h4, h5, a, button, small, img, li, blockquote, .mbr-author-name, em, label, input, textarea, .input-group, .iconbox, .btn-social, .mbr-figure, .mbr-map, .mbr-testimonial .card-block, .mbr-price-value, .mbr-price-figure, .dataTable, .dataTables_info').not(function() {
            return $(this).parents().is('.navbar, .mbr-arrow, footer, .iconbox, .mbr-slider, .mbr-gallery, .mbr-testimonial .card-block, #cookiesdirective, .mbr-wowslider, .accordion, .tab-content, .engine, #scrollToTop');
        }).addClass('hidden animated');

        function getElementOffset(element) {
            var top = 0;
            do {
                top += element.offsetTop || 0;
                element = element.offsetParent;
            } while (element);

            return top;
        }

        function elCarouselItem(element) {
            if (element.parents('.carousel-item').css('display') !== 'none') return false;
            var parentEl = element.parents('.carousel-item').parent();
            if (parentEl.find('.carousel-item.active .hidden.animated').lenght){
                return false;
            }
            else if (parentEl.attr('data-visible') > 1){
                var visibleSlides = parentEl.attr('data-visible');
                if (element.parents().is('.cloneditem-' + (visibleSlides - 1)) && element.parents('.cloneditem-' + (visibleSlides - 1)).attr('data-cloned-index') >= visibleSlides){
                    return true;
                }
                else{
                    element.removeClass('animated hidden');
                    return false;
                }
            }
            else return true;
        }

        function checkIfInView() {
            var window_height = window.innerHeight;
            var window_top_position = document.documentElement.scrollTop || document.body.scrollTop;
            var window_bottom_position = window_top_position + window_height - 50;

            $.each($animatedElements, function() {
                var $element = $(this);
                var element = $element[0];
                var element_height = element.offsetHeight;
                var element_top_position = getElementOffset(element);
                var element_bottom_position = (element_top_position + element_height);

                // check to see if this current element is within viewport
                if ((((element_bottom_position >= window_top_position) &&
                    (element_top_position <= window_bottom_position)) || elCarouselItem($element)) &&
                    ($element.hasClass('hidden'))) {
                    $element.removeClass('hidden').addClass('fadeInUp')
                        .one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function() {
                            $element.removeClass('animated fadeInUp');
                        });
                }
            });
        }

        var $window = $(window);
        $window.on('scroll resize', checkIfInView);
        $window.trigger('scroll');
    }

});