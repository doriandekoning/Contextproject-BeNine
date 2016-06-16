$(document).ready(function() {
    // Load the carousel from the html.
    var carousel = $('#carousel');


    // Register swipe listeners
    var swipemanager = new Hammer.Manager(carousel[0]);
    var Swipe = new Hammer.Swipe({
        threshold: 1,
        velocity: 0.1
    });

    swipemanager.add(Swipe);

    // On left swipe, next slide.
    swipemanager.on('swipeleft', function (e) {
        carousel.carousel("next");
    });

    // On right swipe, previous slide.
    swipemanager.on('swiperight', function (e) {
        carousel.carousel("prev");
    });
});